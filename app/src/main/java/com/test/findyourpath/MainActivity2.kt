package com.test.findyourpath

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MainActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var search: SearchView

    private lateinit var googleMap : GoogleMap

    private val permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.READ_CONTACTS
    )

    private val permissionCode = 38

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        askForPermission()

        search = findViewById(R.id.search_loc)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment

        search.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                val location = search.query.toString()
                var addressList: List<Address>? = null

                if (location != null) {
                    val geoCoder = Geocoder(this@MainActivity2)
                    try {
                        addressList =
                            geoCoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        Toast.makeText(
                            this@MainActivity2,
                            "${e.printStackTrace()}",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        Log.d("loc", "${e.printStackTrace()}")
                    }

                    val address: Address = addressList!![0]

                    val latLng = LatLng(address.latitude, address.longitude)

                    googleMap.addMarker(
                        MarkerOptions().position(latLng)
                            .title(location)
                    )

                    googleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, 10F)
                    )
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        mapFragment.getMapAsync(this)
    }

    private fun askForPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionCode)
    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        // to show current location
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        }

        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflate = menuInflater
        inflate.inflate(R.menu.manu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.map_non -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_NONE
            }

            R.id.map_normal -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            }

            R.id.map_satellite -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }

            R.id.map_Hybrid -> {
                googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

























//    private fun inflateFragment(fragment: Fragment) {
////        val transaction = supportFragmentManager.beginTransaction()
////        transaction.replace(R.id.container , fragment)
////        transaction.commit()
//    }