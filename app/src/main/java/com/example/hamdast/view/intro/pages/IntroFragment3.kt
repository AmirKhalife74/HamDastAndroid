package com.example.hamdast.view.intro.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hamdast.R
import com.example.hamdast.databinding.FragmentIntro2Binding
import com.example.hamdast.databinding.FragmentIntro3Binding


class IntroFragment3 : Fragment() {

    lateinit var binding: FragmentIntro3Binding

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

        binding = FragmentIntro3Binding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    fun init(){}
    fun listen(){}
    fun observe(){}
}