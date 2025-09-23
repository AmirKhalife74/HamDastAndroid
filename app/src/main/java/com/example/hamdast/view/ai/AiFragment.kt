package com.example.hamdast.view.ai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hamdast.R
import com.example.hamdast.databinding.FragmentAiBinding


class AiFragment : Fragment() {

    private lateinit var binding: FragmentAiBinding

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
        binding = FragmentAiBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    private fun init(){}

    private fun listen(){}

    private fun observe(){}


}