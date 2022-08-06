package com.example.farmlog.landsmap

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import com.example.farmlog.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView

class LandsMapActivity : AppCompatActivity(), OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var menuIcon: ImageView
    private lateinit var drawerMenu: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var content: ConstraintLayout

    private var END_SCALE: Float = 0.7f

    override fun onCreate(savedInstanceState: Bundle?) {
        // hidding status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lands_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // hooks
        menuIcon = findViewById(R.id.open_menu)
        drawerMenu = findViewById(R.id.drawer_menu)
        navigationView = findViewById(R.id.navigation)
        content = findViewById(R.id.content)

        navigationDrawer()
    }

    // Navigation Drawer
    private fun navigationDrawer() {
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.to_map)

        menuIcon.setOnClickListener() {
            if (drawerMenu.isDrawerVisible(GravityCompat.START)) {
                drawerMenu.closeDrawer(GravityCompat.START)
            } else {
                drawerMenu.openDrawer(GravityCompat.START)
            }
        }

        animateNavigationDrawer()
    }

    private fun animateNavigationDrawer() {
        // bottom line is for changin the color
        //drawerMenu.setScrimColor(getResources().getColor(R.color.primaryTextColor));
        drawerMenu.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                // Scale the View based on current slide offset
                val diffScaledOffset: Float = slideOffset * (1 - END_SCALE)
                val offsetScale = 1 - diffScaledOffset
                content.setScaleX(offsetScale)
                content.setScaleY(offsetScale)

                // Translate the View, accounting for the scaled width
                val xOffset = drawerView.width * slideOffset
                val xOffsetDiff: Float = content.getWidth() * diffScaledOffset / 2
                val xTranslation = xOffset - xOffsetDiff
                content.setTranslationX(xTranslation)
            }
        })
    }

    override fun onBackPressed() {
        if (drawerMenu.isDrawerVisible(GravityCompat.START)) {
            drawerMenu.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // onClick event for navigation item
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // adding marker
        val globoko = LatLng(45.95481200216156, 15.63454882337749)
        mMap.addMarker(MarkerOptions().position(globoko).title("Globoko"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(globoko))
    }


}