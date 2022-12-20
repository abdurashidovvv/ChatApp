package com.abdurashidov.chatting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    lateinit var binding:FragmentGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentGroupBinding.inflate(layoutInflater)


        return binding.root
    }
}