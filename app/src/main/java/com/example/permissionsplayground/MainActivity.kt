package com.example.permissionsplayground

import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView : TextView = findViewById(R.id.txtView) as TextView
        val textView2 : TextView = findViewById(R.id.txtView2) as TextView
        val button : Button = findViewById(R.id.refresh) as Button

        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                checkLocationPermissionAPI28(99)
                checkBackgroundLocationPermissionAPI30(
                    99)
                textView.setText("Foreground location perm granted: " + checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION))
                textView2.setText("Background location perm granted: " + checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION))
            }

        })

    }

    @TargetApi(28)
    fun Context.checkLocationPermissionAPI28(locationRequestCode: Int) {
        if (!checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION) || !checkSinglePermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            val permList = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            requestPermissions(permList, locationRequestCode)
        }
    }

    @TargetApi(29)
    private fun Context.checkLocationPermissionAPI29(locationRequestCode: Int) {
        if (checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkSinglePermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        ) return
        val permList = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        requestPermissions(permList, locationRequestCode)

    }

    @TargetApi(30)
    private fun Context.checkBackgroundLocationPermissionAPI30(backgroundLocationRequestCode: Int) {
        if (checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) return
        AlertDialog.Builder(this)
            .setTitle("Allow BG location")
            .setMessage("Allow BG location permission to this app?")
            .setPositiveButton("Yes") { _, _ ->
                // this request will take user to Application's Setting page
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    backgroundLocationRequestCode
                )
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

    private fun Context.checkSinglePermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

}