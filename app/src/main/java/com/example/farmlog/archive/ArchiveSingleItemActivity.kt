package com.example.farmlog.archive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmlog.R
import com.example.farmlog.registration.RegistrationActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArchiveSingleItemActivity : AppCompatActivity() {
    private lateinit var editChore: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive_single_item)

        editChore = findViewById(R.id.editChore)
        editChore.setOnClickListener() {
            startActivity(Intent(this, RegistrationActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
    }
}