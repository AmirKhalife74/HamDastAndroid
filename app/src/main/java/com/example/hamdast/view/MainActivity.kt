package com.example.hamdast.view


import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.hamdast.R
import com.example.hamdast.databinding.ActivityMainBinding
import com.example.hamdast.utils.G2JFromMillis
import com.example.hamdast.utils.G2JTypes
import com.example.hamdast.utils.gregorianToPersian
import com.example.hamdast.utils.persianToGregorian
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission داده شد
            } else {
                // Permission رد شد
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater,null,false)
        init()
        checkPermissions()
        listeners()
        observe()
        setContentView(binding.root)
        val (gy, gm, gd) = persianToGregorian(1404, 6, 16)
        Log.d("PersianToGregorian", "1404/6/16 -> Gregorian: $gy/$gm/$gd") // باید 2025/9/7 باشه

    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun listeners(){
        binding.apply {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun init(){
        // window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        testTimestamp()
    }

    private fun observe(){

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun testTimestamp() {
        val localDateTime = LocalDateTime.of(2025, 9, 21, 12, 17)
        println("Test LocalDateTime: $localDateTime")
        val zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Tehran"))
        val timestamp = zonedDateTime.toInstant().toEpochMilli()
        println("Test Timestamp: $timestamp")
        val convertedBack = Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("Asia/Tehran")).toLocalDateTime()
        println("Converted back: $convertedBack")
    }


}
