package com.example.farmlog.registration

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.farmlog.R
import com.example.farmlog.api.RetrofitClient
import com.example.farmlog.auth.models.RegistrationResponse
import com.example.farmlog.login.LoginActivity
import com.example.farmlog.welcome.WelcomeActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdRegistrationActivity : AppCompatActivity() {
    var address: TextInputEditText? = null
    var post: TextInputEditText? = null
    var zip: TextInputEditText? = null
    var gerkMID: TextInputEditText? = null
    var gerkId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // hidding status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_registration)

        address = findViewById(R.id.address)
        post = findViewById(R.id.post)
        zip = findViewById(R.id.postalCode)
        gerkMID = findViewById(R.id.gerkMID)

        // get data from 2nd registration activity
        val intent: Intent = getIntent()
        val firstName: String = intent.getStringExtra("firstName")!!
        val lastName: String = intent.getStringExtra("lastName")!!
        val email: String = intent.getStringExtra("email")!!
        val password: String = intent.getStringExtra("password")!!
        val passwordRepeat: String = intent.getStringExtra("passwordRepeat")!!

        gerkId = gerkMID?.text.toString().toInt()

        val goLogin: TextView = findViewById(R.id.goLogin)
        goLogin.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val goBack: ImageView = findViewById(R.id.goBack)
        goBack.setOnClickListener() {
            val intent = Intent(this, SecondRegistrationActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val loginIntent = Intent(this, LoginActivity::class.java)
        val completeRegister: Button = findViewById(R.id.registrationButton3)
        completeRegister.setOnClickListener() {
            if (validateText()) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                RetrofitClient.instance.createUser(
                    firstName,
                    lastName,
                    email,
                    password,
                    gerkId!!
                ).enqueue(object: Callback<RegistrationResponse>{
                    override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        t.message?.let { it1 -> Log.i("API_failure: ", it1) }
                        Toast.makeText(baseContext, "Prišlo je do napake, poskusite ponovno", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<RegistrationResponse>, response: Response<RegistrationResponse>) {
                        Toast.makeText(baseContext, "Uporabnik je registriran", Toast.LENGTH_LONG).show()
                        startActivity(loginIntent)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                    }
                })
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
        if (address?.text?.isEmpty() == true) {
            Toast.makeText(this, "Naslov je obvezen", Toast.LENGTH_LONG).show()
            return false
        }
        if (post?.text?.isEmpty() == true) {
            Toast.makeText(this, "Pošta je obvezna", Toast.LENGTH_LONG).show()
            return false
        }
        if (zip?.text?.isEmpty() == true) {
            Toast.makeText(this, "Poštna številka je obvezna", Toast.LENGTH_LONG).show()
            return false
        }
        if (gerkMID?.text?.isEmpty() == true) {
            Toast.makeText(this, "GERK MID je obvezen", Toast.LENGTH_LONG).show()
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