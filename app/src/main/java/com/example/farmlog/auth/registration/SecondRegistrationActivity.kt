package com.example.farmlog.auth.registration

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlog.R
import com.example.farmlog.auth.login.LoginActivity
import com.google.android.material.textfield.TextInputEditText

class SecondRegistrationActivity : AppCompatActivity() {
    var password: TextInputEditText? = null
    var passwordRepeat: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // hidding status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_registration)

        password = findViewById(R.id.password)
        passwordRepeat = findViewById(R.id.passwordRepeat)

        // get data from 1st registration activity
        val firstName: String? = intent.getStringExtra("firstName")
        val lastName: String? = intent.getStringExtra("lastName")
        val email: String? = intent.getStringExtra("email")

        Log.i("lastName", firstName.toString())

        val goLogin: TextView = findViewById(R.id.goLogin)
        goLogin.setOnClickListener() {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val goBack: ImageView = findViewById(R.id.goBack)
        goBack.setOnClickListener() {
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(registrationIntent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val lastRegistrationIntent = Intent(this, ThirdRegistrationActivity::class.java)
        val goToThird: Button = findViewById(R.id.registrationButton2)
        goToThird.setOnClickListener() {

            if (validateText()) {
                lastRegistrationIntent.putExtra("firstName", firstName.toString())
                lastRegistrationIntent.putExtra("lastName", lastName.toString())
                lastRegistrationIntent.putExtra("email", email.toString())
                lastRegistrationIntent.putExtra("password", password?.text.toString())
                lastRegistrationIntent.putExtra("passwordRepeat", passwordRepeat?.text.toString())

                startActivity(lastRegistrationIntent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
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

    private fun validateText(): Boolean {
        if (password?.text?.isEmpty() == true) {
            Toast.makeText(this, "Geslo je obvezno", Toast.LENGTH_LONG).show()
            return false
        }
        if (passwordRepeat?.text?.isEmpty() == true) {
            Toast.makeText(this, "Ponovitev gesla je obvezna", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        if (password?.text.toString().trim() != passwordRepeat?.text.toString().trim()) {
            Toast.makeText(this, "Gesli se ne ujemata", Toast.LENGTH_LONG).show()
            return false
        }
        return true
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

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}