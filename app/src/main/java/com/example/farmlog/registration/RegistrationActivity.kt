package com.example.farmlog.registration

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlog.R
import com.example.farmlog.login.LoginActivity


class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // hidding status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val goLogin: TextView = findViewById(R.id.goLogin)
        goLogin.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val next: Button = findViewById(R.id.registrationButton1)
        next.setOnClickListener() {
            val intent = Intent(this, SecondRegistrationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}