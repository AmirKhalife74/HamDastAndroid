package com.example.hamdast.view.intro.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hamdast.databinding.FragmentIntro1Binding
class IntroFragment1 : Fragment() {

    private lateinit var binding: FragmentIntro1Binding
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
       binding = FragmentIntro1Binding.inflate(layoutInflater,container,false)
        return binding.root
    }

    private fun init(){}
    private fun listen(){}
    private fun observe(){}
}