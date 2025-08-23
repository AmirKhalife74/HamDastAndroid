package com.example.hamdast.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hamdast.R
import com.example.hamdast.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var  binding: FragmentMainBinding
    private var navHost: NavHostFragment? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listen()
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMainBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    private fun init(){
        binding.apply {
            navHost =
                childFragmentManager.findFragmentById(R.id.nav_host_main_fragment) as NavHostFragment
            val navController = navHost!!.navController
            setupWithNavController(binding.bottomNavigationView, navController)

        }
    }
    private fun listen(){}
    private fun observe(){}


}