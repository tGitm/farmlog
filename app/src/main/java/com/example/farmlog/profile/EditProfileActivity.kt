package com.example.farmlog.profile

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.farmlog.R
import com.example.farmlog.landsmap.LandsMapActivity
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity : AppCompatActivity() {
    private lateinit var email: TextInputEditText
    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var address: TextInputEditText
    private lateinit var post: TextInputEditText
    private lateinit var postalCode: TextInputEditText
    private lateinit var gerkMID: TextInputEditText
    private lateinit var backIcon: ImageView

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

        val getUserData = getSharedPreferences("Login", MODE_PRIVATE)
        email.hint = getUserData.getString("email", null)
        firstName.hint = getUserData.getString("first_name", null)
        lastName.hint = getUserData.getString("last_name", null)
        address.hint = getUserData.getString("address", null)
        post.hint = getUserData.getString("post", null)
        postalCode.hint = getUserData.getString("postalCode", null)
        gerkMID.hint = getUserData.getString("gerkMID", null)


        backIcon.setOnClickListener() {
            startActivity(Intent(this, ProfileActivity::class.java))
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