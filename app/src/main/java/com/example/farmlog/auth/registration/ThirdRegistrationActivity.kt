package com.example.farmlog.auth.registration

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.example.farmlog.R
import com.example.farmlog.auth.api.RetrofitClient
import com.example.farmlog.auth.usermodels.RegistrationBody
import com.example.farmlog.auth.usermodels.RegistrationResponse
import com.example.farmlog.auth.login.LoginActivity
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
    private lateinit var loadingIcon: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        // hidding status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_registration)

        address = findViewById(R.id.address)
        post = findViewById(R.id.post)
        zip = findViewById(R.id.postalCode)
        gerkMID = findViewById(R.id.gerkMID)
        loadingIcon = findViewById(R.id.loadingData)

        // get data from 2nd registration activity
        val firstName: String? = intent.getStringExtra("firstName")
        val lastName: String? = intent.getStringExtra("lastName")
        val email: String? = intent.getStringExtra("email")
        val password: String? = intent.getStringExtra("password")
        val passwordRepeat: String? = intent.getStringExtra("passwordRepeat")

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

        val completeRegister: Button = findViewById(R.id.completeRegister)
        val welcomeIntent = Intent(this, WelcomeActivity::class.java)

        completeRegister.setOnClickListener() {
            loadingIcon.visibility = View.VISIBLE

            val addressvalue: String = address?.text.toString()
            val postValue: String = post?.text.toString()
            val zipValue: String = zip?.text.toString()
            val gerkId = gerkMID?.text.toString()

            val registrationInfo = RegistrationBody(firstName.toString(), lastName.toString() ,email.toString(), password.toString(), addressvalue, postValue, zipValue, gerkId)

            if (validateText()) {
                RetrofitClient.instance.createUser(
                    registrationInfo
                ).enqueue(object: Callback<RegistrationResponse>{
                    override fun onResponse(
                        call: Call<RegistrationResponse>,
                        response: Response<RegistrationResponse>)
                    {
                        loadingIcon.visibility = View.INVISIBLE
                        Log.i("Register", response.code().toString())

                        if (response.code() == 200) {
                            Toast.makeText(baseContext, "Uporabnik je registriran", Toast.LENGTH_LONG).show()
                            startActivity(welcomeIntent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                        }
                    }

                    override fun onFailure(call: Call<RegistrationResponse>, t: Throwable) {
                        loadingIcon.visibility = View.INVISIBLE
                        t.message?.let { it1 -> Log.i("API_failure: ", it1) }
                        Toast.makeText(baseContext, "Prišlo je do napake, poskusite ponovno", Toast.LENGTH_LONG).show()
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