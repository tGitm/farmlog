package com.example.farmlog.archive

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.farmlog.R
import com.example.farmlog.landsmap.LandsMapActivity
import com.facebook.shimmer.ShimmerFrameLayout

class ArchiveActivity : AppCompatActivity() {

    private lateinit var swiperRefres: SwipeRefreshLayout
    private lateinit var shrimmerView: ShimmerFrameLayout
    private var adapter: ArchiveAdapter? = null
    private lateinit var backIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)

        // hooks
        shrimmerView = findViewById(R.id.shimmer_view_container)
        swiperRefres = findViewById(R.id.swipeRefresh)
        backIcon = findViewById(R.id.backOnMain)

        backIcon.setOnClickListener() {
            startActivity(Intent(this, LandsMapActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            // need to stop when data will be loaded
            shrimmerView.stopShimmer()
            shrimmerView.visibility = View.GONE
        }, 2000)

        swiperRefres.setOnRefreshListener {
            // get data from db/API
        }

        getChores()
    }

    private fun getChores() {

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