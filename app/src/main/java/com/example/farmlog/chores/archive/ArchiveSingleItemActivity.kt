package com.example.farmlog.chores.archive

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlog.R
import com.example.farmlog.archive.EditArchiveActivity
import com.example.farmlog.auth.registration.RegistrationActivity
import com.example.farmlog.chores.models.Chores
import com.example.farmlog.chores.models.DeleteResponse
import com.example.farmlog.chores.api.RetrofitClientChores
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArchiveSingleItemActivity : AppCompatActivity() {
    private lateinit var editChore: FloatingActionButton
    private lateinit var goBackButton: ImageView
    private lateinit var choreName: TextView
    private lateinit var choreDesc: TextView
    private lateinit var choreAccessories: TextView
    private lateinit var choreDate: TextView
    private lateinit var removeChore: ImageView
    private lateinit var choreId: String
    private lateinit var dialog: Dialog
    private lateinit var dialogYes: Button
    private lateinit var dialogNo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_single_item)

        init()

        // delete popup dialog
        dialog.setContentView(R.layout.delete_dialog)
        dialog.window?.attributes?.windowAnimations = R.style.dialogStyle;

        editChore.setOnClickListener {
            startActivity(Intent(this, EditArchiveActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        removeChore.setOnClickListener {
            dialog.show()
        }

        dialogYes = dialog.findViewById(R.id.dialog_ok)
        dialogNo = dialog.findViewById(R.id.dialog_cancel)

        dialogYes.setOnClickListener {
            removeItem()
            startActivity(Intent(this, ArchiveActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        val userId: String? = SharedPrefManager.getInstance(applicationContext).user._id
        val choreId: String? = intent.getStringExtra("choreId")

        // send data to editChore acitvity
        editChore.setOnClickListener {
            val editChoreActivity = Intent(this, EditArchiveActivity::class.java)
            editChoreActivity.putExtra("choreId", choreId)
            editChoreActivity.putExtra("choreName", choreName.text)
            editChoreActivity.putExtra("choreDesc", choreDesc.text)
            editChoreActivity.putExtra("choreAcc", choreAccessories.text)
            editChoreActivity.putExtra("choreDate", choreDate.text)

            startActivity(editChoreActivity)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        /*
        deleteChore.setOnClickListener {
            val afterDelete = Intent(this, ArchiveActivity::class.java)

            RetrofitClientChores.instance.deleteChore(choreId).enqueue(object : Callback<DeleteResponse> {
                override fun onResponse(
                    call: Call<DeleteResponse>,
                    response: Response<DeleteResponse>
                ) {
                    if (response.code() == 200) {
                        Log.i("Api_ok", "OK")
                    } else {
                        Log.i("Api_error", response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                    Log.i("Api-chores-error", t.message.toString())
                    Toast.makeText(
                        applicationContext,
                        "Prišlo je do napake, na novo zaženite aplikacijo",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

            startActivity(afterDelete)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
        */

        dialogNo.setOnClickListener {
            dialog.dismiss()
        }

        goBackButton.setOnClickListener() {
            startActivity(Intent(this, ArchiveActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }

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
                    Log.i("Api_success", response.body().toString())
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
        removeChore = findViewById(R.id.removeChore)
        dialog = Dialog(this)

        getData()
    }

    // get data from recycler view
    private fun getData() {
        val intent = intent.extras
        val workId = intent?.get("choreId")
        val workTitle = intent?.get("choreName")
        val choreDes = intent?.get("choreDesc")
        val choreAccs = intent?.get("choreAcc")
        val choreD = intent?.get("choreDate")

        choreId = workId.toString()
        choreName.text = workTitle.toString()
        choreDesc.text = choreDes.toString()
        choreAccessories.text = choreAccs.toString()
        choreDate.text = choreD.toString()
    }

    // remove chore from user
    private fun removeItem() {
        RetrofitClientChores.instance.deleteChore(choreId).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                if (response.code() == 200) {
                    Log.i("DeletedChore", choreId)
                } else {
                    Log.i("Api_error", response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.i("Api-chores-error", t.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Prišlo je do napake, na novo zaženite aplikacijo",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

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