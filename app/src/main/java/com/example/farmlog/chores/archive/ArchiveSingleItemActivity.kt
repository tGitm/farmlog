package com.example.farmlog.archive

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.farmlog.R
import com.example.farmlog.auth.registration.RegistrationActivity
import com.example.farmlog.chores.models.Chores
import com.example.farmlog.landsmap.api.RetrofitClientChores
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.HashMap

class ArchiveSingleItemActivity : AppCompatActivity() {
    private lateinit var editChore: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_single_item)

        editChore = findViewById(R.id.editChore)
        editChore.setOnClickListener() {
            startActivity(Intent(this, RegistrationActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
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
}