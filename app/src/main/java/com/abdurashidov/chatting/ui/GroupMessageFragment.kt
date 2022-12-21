package com.abdurashidov.chatting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.databinding.FragmentGroupMessageBinding
import com.google.android.material.shape.CornerFamily

class GroupMessageFragment : Fragment() {

    lateinit var binding:FragmentGroupMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentGroupMessageBinding.inflate(layoutInflater)
        val name = arguments?.getString("groupName") as String
        binding.text.text=name
        setup()


        return binding.root
    }

    private fun setup() {
        val radius = 20f
        binding.shapeableImage.shapeAppearanceModel = binding.shapeableImage.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, radius)
            .build()
    }

}