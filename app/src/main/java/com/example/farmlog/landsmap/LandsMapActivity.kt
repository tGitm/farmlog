package com.example.farmlog.landsmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmlog.R

class LandsMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lands_map)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}