package com.hafiztaruligani.test

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_location.*

class GetLocation : AppCompatActivity() {
    lateinit var locationRequest:LocationRequest
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient


    companion object{
        var instance:GetLocation?=null

        fun getMainInstance():GetLocation{
            return instance!!
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        instance = this
        updateLocation()
    }


    fun updateTextView(s : String){
        this@GetLocation.runOnUiThread{
            textView.setText(s)
        }
    }




    fun updateLocation(){
        buildLocationRequest()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            textView.setText("PermissionDenied")
            return
        }else{
            textView.setText("PermissionGranted")
        }

        btnStart.setOnClickListener {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest  ,getPendingIntent())
        }
        btnStop.setOnClickListener {
            textView.setText("STOPPED")
            fusedLocationProviderClient.removeLocationUpdates(getPendingIntent())
        }
    }
    fun getPendingIntent() : PendingIntent? {
        val intent = Intent(this@GetLocation, BroadcastReceiver::class.java)
        intent.setAction(BroadcastReceiver.ACTION_PROCESS_UPDATE)

        return PendingIntent.getBroadcast(this@GetLocation,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }



    fun buildLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 50
        locationRequest.fastestInterval = 30
        locationRequest.smallestDisplacement = 10F

    }

}