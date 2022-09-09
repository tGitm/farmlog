package com.example.farmlog.profile

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.farmlog.R
import com.example.farmlog.auth.usermodels.NewPasswordResponse
import com.example.farmlog.auth.usermodels.PasswordEditBody
import com.example.farmlog.auth.usermodels.UserEditBody
import com.example.farmlog.auth.usermodels.UserEditResponse
import com.example.farmlog.chores.api.RetrofitClient
import com.example.farmlog.landsmap.LandsMapActivity
import com.example.farmlog.storage.SharedPrefManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var backIcon: ImageView
    private lateinit var saveButton: FloatingActionButton
    private lateinit var newPass: TextInputEditText
    private lateinit var repeatNewPass: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        saveButton = findViewById(R.id.save_edited_password)
        backIcon = findViewById(R.id.backOnMain)
        newPass= findViewById(R.id.new_password)
        repeatNewPass= findViewById(R.id.repeat_new_password)

        val mainActivity = Intent(this, LandsMapActivity::class.java)
        val id: String? = SharedPrefManager.getInstance(applicationContext).user._id

        val email = SharedPrefManager.getInstance(applicationContext).user.email //getUserData.getString("email", null)
        val firstName = SharedPrefManager.getInstance(applicationContext).user.first_name
        val lastName = SharedPrefManager.getInstance(applicationContext).user.last_name
        val address = SharedPrefManager.getInstance(applicationContext).user.address
        val post = SharedPrefManager.getInstance(applicationContext).user.post
        val postalCode = SharedPrefManager.getInstance(applicationContext).user.postal_code
        val gerkMID = SharedPrefManager.getInstance(applicationContext).user.gerkMID


        saveButton.setOnClickListener() {
            val passwordNew = newPass.text.toString().trim()
            val editPassword = UserEditBody(firstName,lastName,email,passwordNew,address,post,postalCode,gerkMID)

            RetrofitClient.instance.editUser(
                        id, editPassword
            ).enqueue(object: Callback<UserEditResponse>{
                override fun onResponse(
                    call: Call<UserEditResponse>,
                    response: Response<UserEditResponse>
                ) {

                    if (response.code() == 200) {
                        SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user)
                        Toast.makeText(applicationContext, "Geslo je bilo spremenjeno", Toast.LENGTH_LONG).show()

                        TimeUnit.SECONDS.sleep(1)

                        mainActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(mainActivity)
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
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