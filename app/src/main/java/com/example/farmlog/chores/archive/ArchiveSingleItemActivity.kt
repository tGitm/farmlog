package com.example.farmlog.chores.archive

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.farmlog.R
import com.example.farmlog.auth.registration.RegistrationActivity
import com.example.farmlog.chores.models.Chores
import com.example.farmlog.landsmap.LandsMapActivity
import com.example.farmlog.landsmap.api.RetrofitClientChores
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.HashMap

class ArchiveSingleItemActivity : AppCompatActivity() {
    private lateinit var editChore: FloatingActionButton
    private lateinit var goBackButton: ImageView
    private lateinit var choreName: TextView
    private lateinit var choreDesc: TextView
    private lateinit var choreAccessories: TextView
    private lateinit var choreDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_single_item)

        init()

        editChore.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        goBackButton.setOnClickListener() {
            startActivity(Intent(this, ArchiveActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val userId: String? = SharedPrefManager.getInstance(applicationContext).user._id
        val choreId: String? = "6318d8c7c5bc0bacaf067c98"

        // hashMap fro querying the database
        val queryMap: HashMap<String, Any> = HashMap()
        if (choreId != null) queryMap["_id"] = choreId
        if (userId != null) queryMap["user_id"] = userId

        RetrofitClientChores.instance.getSingleChore(
            userId,
            queryMap["_id"].toString(),
            queryMap["user_id"].toString()
        ).enqueue(object : Callback<Chores> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<Chores>,
                response: Response<Chores>
            ) {
                if (response.code() == 200) {


                } else {
                    Log.i("Api_error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<Chores>, t: Throwable) {
                Log.i("Api-chores-error", t.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Prišlo je do napake, na novo zaženite aplikacijo",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun init() {
        choreName = findViewById(R.id.chore_review_name)
        choreDesc = findViewById(R.id.chore_review_description)
        choreAccessories = findViewById(R.id.chore_review_accessories)
        choreDate = findViewById(R.id.chore_review_date)
        editChore = findViewById(R.id.editChore)
        goBackButton = findViewById(R.id.backOnArchive)

        getData()
    }

    // get data from recycler view
    private fun getData() {
        val intent = intent.extras
        var workTitle = intent?.get("choreName")
        var choreDes = intent?.get("choreDesc")
        var choreAccs = intent?.get("choreAcc")
        var choreD = intent?.get("choreDate")

        choreName.text = workTitle.toString()
        choreDesc.text = choreDes.toString()
        choreAccessories.text = choreAccs.toString()
        choreDate.text = choreD.toString()

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