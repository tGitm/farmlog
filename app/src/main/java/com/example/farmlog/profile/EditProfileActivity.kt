package com.example.farmlog.profile

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlog.R
import com.example.farmlog.auth.api.RetrofitClient
import com.example.farmlog.auth.usermodels.UserEditBody
import com.example.farmlog.auth.usermodels.UserEditResponse
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var address: EditText
    private lateinit var post: EditText
    private lateinit var postalCode: EditText
    private lateinit var gerkMID: EditText
    private lateinit var saveButton: FloatingActionButton
    private lateinit var backIcon: ImageView

    // temp variables for editing
    lateinit var emailTemp: String
    lateinit var firstNameTemp: String
    lateinit var lastNameTemp: String
    lateinit var addressTemp: String
    lateinit var postTemp: String
    lateinit var postalCodeTemp: String
    lateinit var gerkMIDTemp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        backIcon = findViewById(R.id.backOnMain)

        email = findViewById(R.id.edited_email)
        firstName = findViewById(R.id.edited_first_name)
        lastName = findViewById(R.id.edited_last_name)
        address = findViewById(R.id.edited_address)
        post = findViewById(R.id.edited_post)
        postalCode = findViewById(R.id.edited_zip)
        gerkMID = findViewById(R.id.edited_MID)

        saveButton = findViewById(R.id.save_edited_profile)

        //val getUserData = getSharedPreferences("Login", MODE_PRIVATE)
        email.hint = SharedPrefManager.getInstance(applicationContext).user.email //getUserData.getString("email", null)
        firstName.hint = SharedPrefManager.getInstance(applicationContext).user.first_name
        lastName.hint = SharedPrefManager.getInstance(applicationContext).user.last_name
        address.hint = SharedPrefManager.getInstance(applicationContext).user.address
        post.hint = SharedPrefManager.getInstance(applicationContext).user.post
        postalCode.hint = SharedPrefManager.getInstance(applicationContext).user.postal_code
        gerkMID.hint = SharedPrefManager.getInstance(applicationContext).user.gerkMID
        val id: String? = SharedPrefManager.getInstance(applicationContext).user._id
        //val id: String? = getUserData.getString("id", null)

        saveButton.setOnClickListener() {

            emailTemp = if (email.text.toString().isEmpty()) {
                SharedPrefManager.getInstance(applicationContext).user.email.toString()
            } else {
                email.text.toString()
            }

            firstNameTemp = if (firstName.text.toString().isEmpty()) {
                SharedPrefManager.getInstance(applicationContext).user.first_name.toString()
            } else {
                firstName.text.toString()
            }

            lastNameTemp = if (lastName.text.toString().isEmpty()) {
                SharedPrefManager.getInstance(applicationContext).user.last_name.toString()
            } else {
                lastName.text.toString()
            }

            addressTemp = if (address.text.toString().isEmpty()) {
                SharedPrefManager.getInstance(applicationContext).user.address.toString()
            } else {
                address.text.toString()
            }

            postTemp = if (post.text.toString().isEmpty()) {
                SharedPrefManager.getInstance(applicationContext).user.post.toString()
            } else {
                post.text.toString()
            }

            postalCodeTemp = if (postalCode.text.toString().isEmpty()) {
                SharedPrefManager.getInstance(applicationContext).user.postal_code.toString()
            } else {
                postalCode.text.toString()
            }

            gerkMIDTemp = if (gerkMID.text.toString().isEmpty()) {
                SharedPrefManager.getInstance(applicationContext).user.gerkMID.toString()
            } else {
                gerkMID.text.toString()
            }

            val editUserData = UserEditBody(
                firstNameTemp,
                lastNameTemp,
                emailTemp,
                addressTemp,
                postTemp,
                postalCodeTemp,
                gerkMIDTemp
            )
            RetrofitClient.instance.editUser(
                id, editUserData
            ).enqueue(object: Callback<UserEditResponse> {
                override fun onResponse(
                    call: Call<UserEditResponse>,
                    response: Response<UserEditResponse>
                ) {
                    if (response.code() == 200) {
                        SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user)
                        Log.i("updated user", SharedPrefManager.getInstance(applicationContext).user.toString())
                        Toast.makeText(applicationContext, "Uporabnik je uspešno posodobljen", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<UserEditResponse>, t: Throwable) {
                    t.message?.let { it1 -> Log.i("API_failure: ", it1) }
                    Toast.makeText(
                        applicationContext,
                        "Prišlo je do napake, poskusite ponovno",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

            Handler(Looper.getMainLooper()).postDelayed({
                val profileActivity = Intent(applicationContext, ProfileActivity::class.java)

                startActivity(profileActivity)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                finish()
            }, 1000)
        }

        backIcon.setOnClickListener() {
            val profileActivity = Intent(this, ProfileActivity::class.java)

            startActivity(profileActivity)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
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