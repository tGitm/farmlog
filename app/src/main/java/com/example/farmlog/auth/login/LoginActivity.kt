package com.example.farmlog.auth.login

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlog.R
import com.example.farmlog.auth.usermodels.LoginResponse
import com.example.farmlog.auth.usermodels.SignInBody
import com.example.farmlog.auth.registration.RegistrationActivity
import com.example.farmlog.chores.api.RetrofitClient
import com.example.farmlog.landsmap.LandsMapActivity
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var login: Button
    private lateinit var register: TextView
    var email: TextInputEditText? = null
    var password: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.emailLogin)
        password = findViewById(R.id.passwordLogin)
        register = findViewById(R.id.goRegister)
        login = findViewById(R.id.loginButton)

        val mainActivity = Intent(this, LandsMapActivity::class.java)

        login.setOnClickListener() {
            val email = email?.text.toString().trim()
            val password = password?.text.toString().trim()
            val signInInfo = SignInBody(email, password)

            if (validateText()) {
                RetrofitClient.instance.loginUser(
                    signInInfo
                ).enqueue(object: Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        if (response.code() == 200) {
                            SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)

                            mainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            // store data in sharedPreferences
                            val sp = getSharedPreferences("Login", MODE_PRIVATE)
                            val userData = sp.edit()
                            userData.putString("user_token", response.body()?.user_token)
                            userData.putString("id", response.body()?.user?._id.toString())
                            userData.putString("first_name", response.body()?.user?.first_name.toString())
                            userData.putString("last_name", response.body()?.user?.last_name.toString())
                            userData.putString("email", response.body()?.user?.email.toString())
                            userData.putString("post", response.body()?.user?.post.toString())
                            userData.putString("postal_code", response.body()?.user?.postal_code.toString())
                            userData.putString("gerkMID", response.body()?.user?.gerkMID.toString())
                            userData.apply()

                            startActivity(mainActivity)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            Toast.makeText(applicationContext, "Prišlo je do napake, poskusite ponovno", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        t.message?.let { it1 -> Log.i("API_failure: ", it1) }
                        Toast.makeText(
                            applicationContext,
                            "Prišlo je do napake, poskusite ponovno",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })
            }
        }

        register.setOnClickListener() {
            startActivity(Intent(this, RegistrationActivity::class.java))
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

    private fun validateText(): Boolean {
        if (email?.text?.isEmpty() == true) {
            Toast.makeText(this, "Email je obvezen", Toast.LENGTH_LONG).show()
            return false
        }
        if (password?.text?.isEmpty() == true) {
            Toast.makeText(this, "Geslo je obvezno", Toast.LENGTH_LONG).show()
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

    override fun onStart() {
        super.onStart()

        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            val intent = Intent(applicationContext, LandsMapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}