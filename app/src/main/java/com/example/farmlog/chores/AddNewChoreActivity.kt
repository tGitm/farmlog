package com.example.farmlog.chores

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlog.R
import com.example.farmlog.chores.models.LandNameSpinnerModel
import com.example.farmlog.landsmap.LandsMapActivity
import com.example.farmlog.landsmap.api.RetrofitClientLands
import com.example.farmlog.landsmap.models.GeojsonResponse
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddNewChoreActivity : AppCompatActivity() {
    private lateinit var chooseLand: Spinner
    private lateinit var pickDate: EditText
    private lateinit var addImage: Button
    private lateinit var importedImageView: ImageView
    private lateinit var goBack: ImageView
    private lateinit var addChore: FloatingActionButton

    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_chore)

        pickDate = findViewById(R.id.chore_setDate)
        chooseLand = findViewById(R.id.lands_spinner)

        pickDate.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year)
                    pickDate.setText(dat)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        addImage = findViewById(R.id.addImage)
        importedImageView = findViewById(R.id.choreImage)
        goBack = findViewById(R.id.backOnMain)
        addChore = findViewById(R.id.addNewChore)

        goBack.setOnClickListener() {
            startActivity(Intent(this, LandsMapActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        // get and set data from retrofit to spinner
        getLands()

        /*
        addImage.isEnabled = false

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 111)
        } else {
            addImage.isEnabled = true
        }

        addImage.setOnClickListener() {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)
        }

        override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addImage.isEnabled = true
            }
        }

        addImage.setOnClickListener() {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }*/

        addImage.setOnClickListener() {
            var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, 101)
        }

        window.decorView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)
            val screenHeight = window.decorView.rootView.height
            val keypadHeight: Int = screenHeight - r.bottom

            //Log.d(TAG, "keypadHeight = " + keypadHeight);
            if (keypadHeight > screenHeight * 0.15) {
                //Keyboard is opened
                onWindowFocusChanged()
            } else {
                // keyboard is closed
                onWindowFocusChanged()
            }
        }
    }

    private fun getLands() {
        val userGerkId: String? = SharedPrefManager.getInstance(applicationContext).user.gerkMID

        RetrofitClientLands.instance.getLand(userGerkId).enqueue(object :
            Callback<GeojsonResponse> {
            override fun onResponse(
                call: Call<GeojsonResponse>,
                response: Response<GeojsonResponse>
            ) {
                if (response.code() == 200) {
                    val body = response.body()

                    if (body != null) {
                        val data = body.lands.toString()
                        //val jobj = JSONObject(data)
                        //val jarray = jobj.getJSONArray("data")

                        val namesOfLands: MutableList<String> = mutableListOf()

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

                        chooseLand.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                Toast.makeText(applicationContext, "Prosim izberite parcelo", Toast.LENGTH_LONG).show()
                            }

                        }


                        /*
                        for (i in 0 until body.lands.size) {
                            val geo = body.lands[i]

                            val geos = geo.get("geometry")
                            val properties = geo.get("properties")
                        }
                        */
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            var pic = data?.getParcelableExtra<Bitmap>("data")
            importedImageView.setImageBitmap(pic)
        }
    }

    private fun onWindowFocusChanged() {
        val v = window.decorView
        v.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}