package com.example.farmlog.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.URLSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.farmlog.R
import com.example.farmlog.auth.login.LoginActivity
import com.example.farmlog.landsmap.LandsMapActivity

class AboutActivity : AppCompatActivity() {
    private lateinit var backToApp: ImageView
    private lateinit var madeBy: TextView
    private lateinit var emailText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        init()

        backToApp.setOnClickListener {
            val intent = Intent(this, LandsMapActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }

        madeBy.removeLinksUnderline()
        emailText.removeLinksUnderline()

    }

    fun init() {
        backToApp = findViewById(R.id.back_to_app)
        madeBy = findViewById(R.id.made_by_text)
        emailText = findViewById(R.id.contact_text)
    }

    fun TextView.removeLinksUnderline() {
        val spannable = SpannableString(text)
        for (urlSpan in spannable.getSpans(0, spannable.length, URLSpan::class.java)) {
            spannable.setSpan(object : URLSpan(urlSpan.url) {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }, spannable.getSpanStart(urlSpan), spannable.getSpanEnd(urlSpan), 0)
        }
        text = spannable
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