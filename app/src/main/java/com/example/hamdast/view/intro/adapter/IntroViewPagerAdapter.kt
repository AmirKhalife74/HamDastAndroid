package com.example.hamdast.view.intro.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hamdast.view.intro.pages.IntroFragment1
import com.example.hamdast.view.intro.pages.IntroFragment2
import com.example.hamdast.view.intro.pages.IntroFragment3

class IntroPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        IntroFragment1(),
        IntroFragment2(),
        IntroFragment3()
    )



    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}