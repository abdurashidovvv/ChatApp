package com.abdurashidov.chatting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.abdurashidov.chatting.MyData
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.databinding.FragmentWritePhoneBinding

class WritePhoneFragment : Fragment() {

    lateinit var binding: FragmentWritePhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentWritePhoneBinding.inflate(layoutInflater)

        binding.next.setOnClickListener {
            MyData.phoneNumber=binding.edtNumber.text.toString()
            findNavController().navigate(R.id.phoneAuthFragment)
        }

        return binding.root
    }
}