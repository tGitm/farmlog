package com.example.farmlog.landsmap

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.lifecycle.lifecycleScope
import com.example.farmlog.R
import com.example.farmlog.auth.login.LoginActivity
import com.example.farmlog.chores.AddNewChoreActivity
import com.example.farmlog.chores.archive.ArchiveActivity
import com.example.farmlog.landsmap.api.RetrofitClientLands
import com.example.farmlog.landsmap.models.GeojsonResponse
import com.example.farmlog.profile.ProfileActivity
import com.example.farmlog.storage.SharedPrefManager
import com.example.farmlog.welcome.AboutActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.maps.android.collections.PolygonManager
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class LandsMapActivity : AppCompatActivity(), OnMapReadyCallback,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var menuIcon: ImageView
    private lateinit var archiveIcon: ImageView
    private lateinit var profileIcon: ImageView
    private lateinit var drawerMenu: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var content: CoordinatorLayout
    private lateinit var addChore: FloatingActionButton
    private var geo: String = ""


    private var END_SCALE: Float = 0.7f

    override fun onCreate(savedInstanceState: Bundle?) {
        // hidding status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lands_map)

        val userGerkId: String? = SharedPrefManager.getInstance(applicationContext).user.gerkMID
        lifecycleScope.launch{
            val landsList = RetrofitClientLands.instance.getLandsForMap(userGerkId)
            geo = landsList.toString()
            with(Dispatchers.Main){
                Log.i("GeoLog", geo)
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // hooks
        menuIcon = findViewById(R.id.open_menu)
        drawerMenu = findViewById(R.id.drawer_menu)
        navigationView = findViewById(R.id.navigation)
        content = findViewById(R.id.content)
        archiveIcon = findViewById(R.id.archive)
        bottomAppBar = findViewById(R.id.bottomAppBar)
        addChore = findViewById(R.id.addNewChore)
        profileIcon = findViewById(R.id.profile)

        addChore.setOnClickListener() {
            startActivity(Intent(this, AddNewChoreActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        archiveIcon.setOnClickListener() {
            startActivity(Intent(this, ArchiveActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        profileIcon.setOnClickListener() {
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        navigationDrawer()
    }

    // Navigation Drawer
    private fun navigationDrawer() {
        navigationView.bringToFront()
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setCheckedItem(R.id.to_map)

        menuIcon.setOnClickListener {
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
        when (item.itemId) {
            R.id.to_map -> {
                this.startActivity(Intent(this,LandsMapActivity::class.java))
                return true
            }
            R.id.to_archive -> {
                this.startActivity(Intent(this, ArchiveActivity::class.java))
                return true
            }
            R.id.add_work -> {
                this.startActivity(Intent(this, AddNewChoreActivity::class.java))
                return true
            }
            R.id.my_profile -> {
                this.startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }
            R.id.to_about -> {
                this.startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            R.id.sign_out -> {
                val preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.clear()
                editor.apply()

                // set checkbox to false
                val rememberPreferences: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                val rememberEditor: SharedPreferences.Editor = rememberPreferences.edit()
                rememberEditor.putString("remember", "false")
                rememberEditor.apply()

                //SharedPrefManager.getInstance(applicationContext).clear()
                val loginActivity = Intent(this, LoginActivity::class.java)
                loginActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(loginActivity)

                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return false
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
/*
    override fun onMapReady(googleMap: GoogleMap) {
        val userGerkId: String? = SharedPrefManager.getInstance(applicationContext).user.gerkMID
        mMap = googleMap
        var geojson = ArrayList<GeojsonResponse>()
        val polygonManager = PolygonManager(mMap)

        //getGeojsonData() // tuki bi rabu zdej dobit ta response

        lifecycleScope.launch{
            val landsList = RetrofitClientLands.instance.getLandsForMap(userGerkId)
            var geojsonData: JSONArray = JSONArray()
            for (i in 0 until landsList.lands.size) {
                val geo = landsList.lands[i]
                geojsonData.put(geo)
                val geos = geo.get("geometry")
                val properties = geo.get("properties")

                val geometryJson: JSONObject = JSONObject(geos.toString())
                val geoJsonData: JSONObject = geometryJson
                Log.i("GeoTim", "$geoJsonData")

                val layer = GeoJsonLayer(mMap, geoJsonData)
                val style: GeoJsonPolygonStyle = layer.defaultPolygonStyle
                style.fillColor = resources.getColor(R.color.darkGray)
                style.strokeColor = resources.getColor(R.color.darkerGray)
                style.strokeWidth = 2f
                style.isClickable = true

                layer.addLayerToMap()

                layer.setOnFeatureClickListener(
                    GeoJsonLayer.GeoJsonOnFeatureClickListener {
                        Log.i("LayerClicked", "$properties")
                        Toast.makeText(
                            applicationContext,
                            "GeoJSON polygon clicked: $properties",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            /*
            val geojson = JSONObject()
            geojson.put("features", geojsonData)

            Log.i("GEO", geojson.get("geometry").toString())

            val layer = GeoJsonLayer(mMap, geojson)
            val style: GeoJsonPolygonStyle = layer.defaultPolygonStyle
            style.fillColor = resources.getColor(R.color.darkGray)
            style.strokeColor = resources.getColor(R.color.darkerGray)
            style.strokeWidth = 2f
            style.isClickable = true

            layer.addLayerToMap()

            layer.setOnFeatureClickListener(
                GeoJsonLayer.GeoJsonOnFeatureClickListener {
                    Log.i("LayerClicked", "kliknil si")

                }
            )
            */
        }

        // adding marker
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(45.92757404830929, 15.595209429220395)))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.5f ) );

    }
*/

    override fun onMapReady(googleMap: GoogleMap) {
        val userGerkId: String? = SharedPrefManager.getInstance(applicationContext).user.gerkMID
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(45.92757404830929, 15.595209429220395), 12.5f))
        mMap.uiSettings.isZoomControlsEnabled = true
        //mMap.animateCamera( CameraUpdateFactory.zoomTo( 12.5f ) )

        lifecycleScope.launch{
            val landsList = RetrofitClientLands.instance.
            getLandsForMap(userGerkId)
            val geojsonData: JSONArray = JSONArray()
            Log.i("geojsonVilja", "$landsList")
            for (i in 0 until landsList.lands.size) {
                val geo = landsList.lands[i]

                geojsonData.put(geo)
                val geos = geo.get("geometry")
                val properties = geo.get("properties")
                val geometryJson: JSONObject = JSONObject(geos.toString())
                val geoJsonData: JSONObject = geometryJson

                Log.i("geojsonViljaItem", "$geoJsonData")
                val layer = GeoJsonLayer(mMap, geoJsonData)
                val style: GeoJsonPolygonStyle = layer.defaultPolygonStyle
                style.fillColor = resources.getColor(R.color.darkGray)
                style.strokeColor = resources.getColor(R.color.darkerGray)
                style.strokeWidth = 2f
                style.isClickable = true
                layer.addLayerToMap()
            }
        }

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

    override fun onStart() {
        super.onStart()

        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            val intent = Intent(applicationContext, LandsMapActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}