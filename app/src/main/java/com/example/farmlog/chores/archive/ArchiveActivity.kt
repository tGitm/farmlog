package com.example.farmlog.archive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.farmlog.R
import com.example.farmlog.chores.archive.ArchiveAdapter
import com.example.farmlog.chores.models.Chore
import com.example.farmlog.chores.models.ChoresResponse
import com.example.farmlog.landsmap.LandsMapActivity
import com.example.farmlog.landsmap.api.RetrofitClientChores
import com.example.farmlog.storage.SharedPrefManager
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArchiveActivity : AppCompatActivity() {

    private lateinit var swiperRefres: SwipeRefreshLayout
    private lateinit var shrimmerView: ShimmerFrameLayout
    private var adapter: ArchiveAdapter? = null
    private lateinit var backIcon: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager

    var choresList: ArrayList<Chore?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        // hooks
        shrimmerView = findViewById(R.id.shimmer_view_container)
        swiperRefres = findViewById(R.id.swipeRefresh)
        backIcon = findViewById(R.id.backOnMain)
        recyclerView = findViewById(R.id.archive_recycler)

        backIcon.setOnClickListener() {
            startActivity(Intent(this, LandsMapActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            // need to stop when data will be loaded
            shrimmerView.stopShimmer()
            shrimmerView.visibility = View.GONE
        }, 2000)

        swiperRefres.setOnRefreshListener {
            // get data from db/API
        }

        initRecycleView()
        fetchChores()
    }

    // get chores for user from api response
    private fun fetchChores() {
        val userId: String? = SharedPrefManager.getInstance(applicationContext).user._id

        RetrofitClientChores.instance.getUserChores(userId).enqueue(object : Callback<Chore> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<Chore>,
                response: Response<Chore>
            ) {
                if (response.code() == 200) {
                    choresList.addAll(listOf(response.body()))
                    adapter?.addItems(choresList)

                    Log.i("ChoresList", choresList.toString())

                    } else {
                        Log.i("Map-error", response.errorBody().toString())
                    }
                }

            override fun onFailure(call: Call<Chore>, t: Throwable) {
                Log.i("Api-chores-error", t.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Prišlo je do napake, na novo zaženite aplikacijo",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun initRecycleView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ArchiveAdapter()
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
}