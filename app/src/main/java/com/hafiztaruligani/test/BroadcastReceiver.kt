package com.hafiztaruligani.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BroadcastReceiver : BroadcastReceiver(){
    companion object{
        val ACTION_PROCESS_UPDATE = "com.hafiztaruligani.test.GetLocation.UPDATE_LOCATION"

    }
    override fun onReceive(context: Context?, intent: Intent?) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("message")


        if (intent!=null){
            val action = intent!!.action!!
            if (action.equals(ACTION_PROCESS_UPDATE)){
                var result = LocationResult.extractResult(intent!!)
                if (result!=null){
                    val location = result.lastLocation
                    val latLong = LatLng(location.latitude,location.longitude)

                    try {
                        myRef.setValue(latLong.toString())
                        Log.d("LOCATION FOREGROUND", latLong.toString())
                        GetLocation.getMainInstance().updateTextView(latLong.toString())
                    }catch (e:Exception){
                        //APP ON KILLED MODE
                        myRef.setValue(latLong.toString())
                        Log.d("LOCATION BACKGROUND", latLong.toString())
                        Toast.makeText(context,latLong.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}