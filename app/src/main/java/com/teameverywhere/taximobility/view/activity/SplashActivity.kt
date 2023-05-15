package com.teameverywhere.taximobility.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.teameverywhere.taximobility.MainActivity
import com.teameverywhere.taximobility.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    companion object {
        const val TAG = "SplashActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()
    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val permission = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
            requestPermissions(permission, 100)
        }else {
            val handler = Handler()
            handler.postDelayed(Runnable {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            val count = grantResults.count() {
                it == PackageManager.PERMISSION_GRANTED
            }

            Log.d(TAG, "onRequestPermissionsResult: $count")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                val handler = Handler()
                handler.postDelayed(Runnable {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
            }else {
                if(count >= 1){
                    val handler = Handler()
                    handler.postDelayed(Runnable {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 3000)
                }
            }
        }
    }
}