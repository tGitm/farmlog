package com.example.farmlog.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.farmlog.R
import com.example.farmlog.landsmap.LandsMapActivity
import com.example.farmlog.registration.RegistrationActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val goRegister: TextView = findViewById(R.id.goRegister)
        goRegister.setOnClickListener() {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


        val login: Button = findViewById(R.id.loginButton)
        login.setOnClickListener() {
            val intent = Intent(this, LandsMapActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

}