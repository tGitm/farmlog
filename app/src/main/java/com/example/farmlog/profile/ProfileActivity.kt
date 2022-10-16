package com.example.farmlog.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlog.R
import com.example.farmlog.landsmap.LandsMapActivity
import com.example.farmlog.storage.SharedPrefManager


class ProfileActivity : AppCompatActivity() {

    private lateinit var email: TextView
    private lateinit var firstName: TextView
    private lateinit var lastName: TextView
    private lateinit var address: TextView
    private lateinit var post: TextView
    private lateinit var postalCode: TextView
    private lateinit var gerkMID: TextView
    private lateinit var editProfile: Button
    private lateinit var changePassword: TextView
    private lateinit var backIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        email = findViewById(R.id.value_user_email)
        firstName = findViewById(R.id.value_user_first_name)
        lastName = findViewById(R.id.value_user_last_name)
        address = findViewById(R.id.value_user_address)
        post = findViewById(R.id.value_user_post)
        postalCode = findViewById(R.id.value_user_zip)
        gerkMID = findViewById(R.id.value_user_MID)
        editProfile = findViewById(R.id.edit_profile)
        changePassword = findViewById(R.id.change_password)
        backIcon = findViewById(R.id.backOnMain)

        //val getUserData = getSharedPreferences("Login", MODE_PRIVATE)
        email.text = SharedPrefManager.getInstance(applicationContext).user.email //getUserData.getString("email", null)
        firstName.text = SharedPrefManager.getInstance(applicationContext).user.first_name
        lastName.text = SharedPrefManager.getInstance(applicationContext).user.last_name
        address.text = SharedPrefManager.getInstance(applicationContext).user.address
        post.text = SharedPrefManager.getInstance(applicationContext).user.post
        postalCode.text = SharedPrefManager.getInstance(applicationContext).user.postal_code
        gerkMID.text = SharedPrefManager.getInstance(applicationContext).user.gerkMID

        editProfile.setOnClickListener() {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        backIcon.setOnClickListener() {
            startActivity(Intent(this, LandsMapActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        changePassword.setOnClickListener() {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
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