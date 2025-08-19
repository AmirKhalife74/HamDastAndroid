package com.example.hamdast.view.intro.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hamdast.R
import com.example.hamdast.databinding.FragmentIntro2Binding


class IntroFragment2 : Fragment() {


    lateinit var binding: FragmentIntro2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntro2Binding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }
}