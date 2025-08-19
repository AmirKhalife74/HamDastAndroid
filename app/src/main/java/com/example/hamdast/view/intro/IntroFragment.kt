package com.example.hamdast.view.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hamdast.R
import com.example.hamdast.databinding.FragmentIntroBinding
import com.example.hamdast.view.intro.adapter.IntroPagerAdapter
import me.relex.circleindicator.CircleIndicator3
import kotlin.math.abs


class IntroFragment : Fragment() {


    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIntroBinding.inflate(layoutInflater,null,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        listen()
        observe()
    }

    private fun init(){
       binding.apply {
           introViewPager.adapter = IntroPagerAdapter(requireActivity())
           indicator.setViewPager(introViewPager)
           introViewPager.setPageTransformer { page, position ->
               page.alpha = 0.25f + (1 - abs(position))
               page.scaleY = 0.75f + (1 - abs(position)) * 0.25f
           }




       }


    }
    private fun listen(){}
    private fun observe(){}



}