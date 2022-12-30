package com.example.farmlog.archive

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlog.R
import com.example.farmlog.chores.api.RetrofitClientChores
import com.example.farmlog.chores.archive.ArchiveActivity
import com.example.farmlog.chores.models.ChoreEditBody
import com.example.farmlog.chores.models.EditChoreResponse
import com.example.farmlog.landsmap.api.RetrofitClientLands
import com.example.farmlog.landsmap.models.GeojsonResponse
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EditArchiveActivity : AppCompatActivity() {
    private lateinit var saveEdited: FloatingActionButton
    private lateinit var goBackButton: ImageView
    private lateinit var choreName: EditText
    private lateinit var choreDesc: EditText
    private lateinit var choreAccessories: EditText
    private lateinit var choreDate: EditText
    private lateinit var chooseLand: Spinner

    // temp variables for editing
    lateinit var choreTitleTemp: String
    lateinit var choreDescTemp: String
    lateinit var choreAccessoriesTemp: String
    lateinit var choreDateTemp: String

    val namesOfLands: MutableList<String> = mutableListOf()
    var landSelected: String = ""

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_archive)

        init()

        addLandsToSpinner()

        choreDate.setOnTouchListener(OnTouchListener { _, event ->
            val inType: Int = choreDate.inputType // backup the input type
            choreDate.inputType = InputType.TYPE_NULL // disable soft input
            choreDate.onTouchEvent(event) // call native handler
            choreDate.inputType = inType // restore input type
            true // consume touch even
        })

        choreDate.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year)
                    choreDate.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // get data from single archive activity
        val choreId: String? = intent.getStringExtra("choreId")
        val choreTitle: String? = intent.getStringExtra("choreName")
        val choreDes: String? = intent.getStringExtra("choreDesc")
        val choreAcc: String? = intent.getStringExtra("choreAcc")
        val choreD: String? = intent.getStringExtra("choreDate")

        choreName.hint = choreTitle
        choreDesc.hint = choreDes
        choreAccessories.hint = choreAcc
        choreDate.hint = choreD

        // get and set data from retrofit to spinner
        chooseLand.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                landSelected = namesOfLands[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(applicationContext, "Prosim izberite parcelo", Toast.LENGTH_LONG).show()
            }
        }

        saveEdited.setOnClickListener {

            choreTitleTemp = choreName.text.toString().ifEmpty {
                intent.getStringExtra("choreName").toString()
            }

            choreDescTemp = choreDesc.text.toString().ifEmpty {
                intent.getStringExtra("choreDesc").toString()
            }

            choreAccessoriesTemp = choreAccessories.text.toString().ifEmpty {
                intent.getStringExtra("choreAcc").toString()
            }

            choreDateTemp = choreDate.text.toString().ifEmpty {
                choreDate.hint.toString()
            }

            val editChoreData = ChoreEditBody(
                landSelected,
                choreTitleTemp,
                choreDescTemp,
                choreAccessoriesTemp,
                choreDateTemp
            )

            Log.i("EditChore", "$editChoreData")

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

    private fun addLandsToSpinner() {
        val userGerkId: String? = SharedPrefManager.getInstance(applicationContext).user.gerkMID

        RetrofitClientLands.instance.getLandsForSpinner(userGerkId).enqueue(object :
            Callback<GeojsonResponse> {
            override fun onResponse(
                call: Call<GeojsonResponse>,
                response: Response<GeojsonResponse>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        val data = body.lands.toString()
                        for (i in 0 until body.lands.size) {
                            val lands = body.lands[i]
                            val properties = lands.get("properties")
                            val geoJsonData: JSONObject = JSONObject(properties.toString())
                            val name = geoJsonData.getString("DOMACE_IME")
                            namesOfLands.add(name)
                        }
                        Log.i("LandsList: ", "$namesOfLands")

                        val landsNameAdapter = ArrayAdapter(applicationContext, R.layout.spinner_row, R.id.land_name_spinner, namesOfLands)
                        chooseLand.adapter = landsNameAdapter
                    }
                }
            }

            override fun onFailure(call: Call<GeojsonResponse>, t: Throwable) {
                Log.i("AddNewChoreGetLand_error", t.message.toString())
                Toast.makeText(
                    applicationContext,
                    "Prišlo je do napake, na novo zaženite aplikacijo",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun init() {
        choreName = findViewById(R.id.chore_name_input)
        choreDesc = findViewById(R.id.chore_description_input)
        choreAccessories = findViewById(R.id.chore_accessories_input)
        choreDate = findViewById(R.id.chore_setDate)
        saveEdited = findViewById(R.id.save_chore_button)
        goBackButton = findViewById(R.id.backToChores)
        chooseLand = findViewById(R.id.lands_spinner)
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