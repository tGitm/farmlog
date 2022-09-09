package com.example.farmlog.profile

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.farmlog.R
import com.example.farmlog.auth.models.UserEditBody
import com.example.farmlog.auth.models.UserEditResponse
import com.example.farmlog.chores.api.RetrofitClient
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    private lateinit var email: TextInputEditText
    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var address: TextInputEditText
    private lateinit var post: TextInputEditText
    private lateinit var postalCode: TextInputEditText
    private lateinit var gerkMID: TextInputEditText
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




        Log.i("old user", SharedPrefManager.getInstance(applicationContext).user.toString())

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
                emailTemp,
                firstNameTemp,
                lastNameTemp,
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
        }

        backIcon.setOnClickListener() {
            val profileActivity = Intent(this, ProfileActivity::class.java)
            profileActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(profileActivity)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
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