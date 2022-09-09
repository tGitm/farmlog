package com.example.farmlog.storage

import android.content.Context
import com.example.farmlog.auth.models.User

class SharedPrefManager private constructor(private val mCtx: Context) {

    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id", -1) != -1
        }

    val user: User
        get() {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences.getString("first_name", null),
                sharedPreferences.getString("last_name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getString("post", null),
                sharedPreferences.getString("postal_code", null),
                sharedPreferences.getString("gerkMID", null),
                sharedPreferences.getString("_id", null),
                sharedPreferences.getString("date", null),
                sharedPreferences.getInt("__v", 0)
            )
        }


    fun saveUser(user: User?) {

        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", user?.email)
        editor.putString("first_name", user?.first_name)
        editor.putString("last_name", user?.last_name)
        editor.putString("password", user?.password)
        editor.putString("address", user?.address)
        editor.putString("post", user?.post)
        editor.putString("postal_code", user?.postal_code)
        editor.putString("gerkMID", user?.gerkMID)
        editor.putString("_id", user?._id)
        editor.putString("date", user?.date)
        if (user != null) {
            editor.putInt("__v", user.__v)
        }

        editor.apply()

    }

    fun clear() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private val SHARED_PREF_NAME = "my_shared_preff"
        private var mInstance: SharedPrefManager? = null

        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

}