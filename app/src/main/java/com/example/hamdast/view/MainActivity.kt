package com.example.hamdast.view


import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.hamdast.R
import com.example.hamdast.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        binding.apply {

        }
    }

    private fun init(){
        //window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
    }

    private fun observe(){

    }


}
