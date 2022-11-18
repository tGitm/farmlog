package com.example.farmlog.archive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.farmlog.R
import com.example.farmlog.chores.api.RetrofitClientChores
import com.example.farmlog.chores.archive.ArchiveActivity
import com.example.farmlog.chores.archive.ArchiveSingleItemActivity
import com.example.farmlog.chores.models.ChoreEditBody
import com.example.farmlog.chores.models.EditChoreResponse
import com.example.farmlog.profile.ProfileActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditArchiveActivity : AppCompatActivity() {
    private lateinit var saveEdited: FloatingActionButton
    private lateinit var goBackButton: ImageView
    private lateinit var choreName: EditText
    private lateinit var choreDesc: EditText
    private lateinit var choreAccessories: EditText
    private lateinit var choreDate: EditText

    // temp variables for editing
    lateinit var choreTitleTemp: String
    lateinit var choreDescTemp: String
    lateinit var choreAccessoriesTemp: String
    lateinit var choreDateTemp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_archive)

        init()

        // get data from single archive activity
        val choreId: String? = intent.getStringExtra("choreId")
        val choreTitle: String? = intent.getStringExtra("choreName")
        val choreDes: String? = intent.getStringExtra("choreDesc")
        val choreAcc: String? = intent.getStringExtra("choreAcc")
        val choreD: String? = intent.getStringExtra("choreDate")

        saveEdited.setOnClickListener {

            choreTitleTemp = if (choreName.text.toString().isEmpty()) ({
                choreTitle
            }).toString() else {
                choreName.text.toString()
            }

            choreDescTemp = if (choreDesc.text.toString().isEmpty()) ({
                choreDes
            }).toString() else {
                choreDesc.text.toString()
            }

            choreAccessoriesTemp = if (choreAccessories.text.toString().isEmpty()) ({
                choreAcc
            }).toString() else {
                choreAccessories.text.toString()
            }

            choreDateTemp = if (choreDate.text.toString().isEmpty()) ({
                choreD
            }).toString() else {
                choreDate.text.toString()
            }

            val editChoreData = ChoreEditBody(
                choreTitleTemp,
                choreDescTemp,
                choreAccessoriesTemp
            )

            RetrofitClientChores.instance.editChore(choreId, editChoreData).enqueue(object : Callback<EditChoreResponse> {
                override fun onResponse(
                    call: Call<EditChoreResponse>,
                    response: Response<EditChoreResponse>
                ) {
                    if (response.code() == 200) {
                        Log.i("API_ok: ", "Chore updated")
                        Toast.makeText(
                            applicationContext,
                            "Opravilo je bilo uspešno posodobljeno",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<EditChoreResponse>, t: Throwable) {
                    t.message?.let { it1 -> Log.i("API_failure: ", it1) }
                    Toast.makeText(
                        applicationContext,
                        "Prišlo je do napake, poskusite ponovno",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

            Handler().postDelayed({
                val archiveActivity = Intent(this, ArchiveActivity::class.java)
                startActivity(archiveActivity)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
            }, 1500)
        }

        goBackButton.setOnClickListener() {
            val archiveActivity = Intent(this, ArchiveActivity::class.java)
            archiveActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(archiveActivity)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
    }

    private fun init() {
        choreName = findViewById(R.id.chore_name_input)
        choreDesc = findViewById(R.id.chore_description_input)
        choreAccessories = findViewById(R.id.chore_accessories_input)
        choreDate = findViewById(R.id.chore_setDate)
        saveEdited = findViewById(R.id.save_chore_button)
        goBackButton = findViewById(R.id.backToChores)
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