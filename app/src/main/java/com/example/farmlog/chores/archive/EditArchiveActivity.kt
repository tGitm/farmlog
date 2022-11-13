package com.example.farmlog.archive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.farmlog.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditArchiveActivity : AppCompatActivity() {
    private lateinit var editChore: FloatingActionButton
    private lateinit var goBackButton: ImageView
    private lateinit var choreName: TextView
    private lateinit var choreDesc: TextView
    private lateinit var choreAccessories: TextView
    private lateinit var choreDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_archive)

        init()
    }

    private fun init() {
        choreName = findViewById(R.id.chore_name)
        choreDesc = findViewById(R.id.chore_description)
        choreAccessories = findViewById(R.id.chore_accessories)
        choreDate = findViewById(R.id.chore_setDate)
        editChore = findViewById(R.id.editChore_button)
        goBackButton = findViewById(R.id.backOnArchive)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val decorView: View = window.decorView
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }
}