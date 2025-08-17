package com.example.hamdast.view


import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

import com.example.hamdast.R
import com.example.hamdast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater,null,false)
        init()
        listeners()
        observe()
        setContentView(binding.root)


    }

    private fun listeners(){

    }

    private fun init(){

    }

    private fun observe(){

    }


}
