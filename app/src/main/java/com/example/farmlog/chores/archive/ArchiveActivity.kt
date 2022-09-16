package com.example.farmlog.chores.archive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlog.R
import com.example.farmlog.chores.models.Chores
import com.example.farmlog.landsmap.LandsMapActivity
import com.example.farmlog.landsmap.api.RetrofitClientChores
import com.example.farmlog.storage.SharedPrefManager
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class ArchiveActivity : AppCompatActivity(), ArchiveAdapter.ClickListener {

    //private lateinit var swiperRefres: SwipeRefreshLayout
    private lateinit var shrimmerView: ShimmerFrameLayout
    private var adapter: ArchiveAdapter? = null
    private lateinit var backIcon: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var search: SearchView
    private lateinit var editChore: ImageView

    var choresList: MutableList<Chores> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        initView()
        initRecycleView()
        fetchChores()

        editChore.setOnClickListener() {
            startActivity(Intent(this, LandsMapActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        backIcon.setOnClickListener() {
            startActivity(Intent(this, LandsMapActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        // search bar logic
        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return true
            }

        })

        recyclerView.visibility = View.INVISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            // need to stop when data will be loaded
            shrimmerView.stopShimmer()
            shrimmerView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }, 1000)

        /*
        swiperRefres.setOnRefreshListener {
            // get data from db/API
            fetchChores()
        }*/
    }

    // get chores for user from api response
    private fun fetchChores() {
        val userId: String? = SharedPrefManager.getInstance(applicationContext).user._id

        RetrofitClientChores.instance.getUserChores(userId).enqueue(object : Callback<MutableList<Chores>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<MutableList<Chores>>,
                response: Response<MutableList<Chores>>
            ) {
                if (response.code() == 200) {
                    response.body()?.let { choresList.addAll(it) }
                    adapter?.addItems(choresList)
                    adapter?.notifyDataSetChanged()

                    Log.i("ChoresList", choresList.toString())

                    } else {
                        Log.i("Map-error", response.errorBody().toString())
                    }
                }

            override fun onFailure(call: Call<MutableList<Chores>>, t: Throwable) {
                Log.i("Api-chores-error", t.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Prišlo je do napake, na novo zaženite aplikacijo",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun initView() {
        recyclerView = findViewById(R.id.archive_list)
        editChore = findViewById(R.id.editChoreElement)
        shrimmerView = findViewById(R.id.shimmer_view_container)
        search = findViewById(R.id.search_view)
        //swiperRefres = findViewById(R.id.swipeRefresh)
        backIcon = findViewById(R.id.backOnMain)
    }

    private fun initRecycleView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ArchiveAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val decorView: View = window.decorView
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }

    override fun clickedItem(choreModel: Chores) {
        val secondRegistrationIntent = Intent(this, ArchiveSingleItemActivity::class.java)
        secondRegistrationIntent.putExtra("choreName", choreModel.work_title)
        secondRegistrationIntent.putExtra("choreDesc", choreModel.work_description)
        secondRegistrationIntent.putExtra("choreAcc", choreModel.accessories_used)
        secondRegistrationIntent.putExtra("choreDate", choreModel.createdAt)
        startActivity(secondRegistrationIntent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.farmlog_archive_menu, menu)
        var menuItem = menu!!.findItem(R.id.searchView)
        var searchView: SearchView = menuItem.actionView as SearchView

        // search bar logic
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    displayList.clear()
                    var searchT = newText.toLowerCase(Locale.getDefault())

                    Log.i("Search", searchT)

                    for (chore in choresList) {
                        if (chore.work_title.toLowerCase(Locale.getDefault())
                                .contains(searchT) || chore.accessories_used.toLowerCase(Locale.getDefault())
                                .contains(searchT) || chore.createdAt.toLowerCase(Locale.getDefault())
                                .contains(searchT)
                        ) {
                            displayList.add(chore)
                        }
                        recyclerView.adapter!!.notifyDataSetChanged()
                    }
                } else {
                    displayList.clear()
                    displayList.addAll(choresList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }*/
}
