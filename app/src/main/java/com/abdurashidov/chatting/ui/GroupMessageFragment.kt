package com.abdurashidov.chatting.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdurashidov.chatting.MyData
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.databinding.FragmentGroupMessageBinding
import com.abdurashidov.chatting.models.GroupMessage
import com.google.android.material.shape.CornerFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class GroupMessageFragment : Fragment() {

    lateinit var binding:FragmentGroupMessageBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var auth: FirebaseAuth

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentGroupMessageBinding.inflate(layoutInflater)
        val name = arguments?.getString("groupName") as String
        binding.text.text=name
        setup()

        firebaseDatabase= FirebaseDatabase.getInstance()
        reference=firebaseDatabase.getReference("groups")
        auth= FirebaseAuth.getInstance()

        binding.sendBtn.setOnClickListener {
            val me=binding.messageEt.text.toString()
            val author= com.abdurashidov.chatting.utils.MyData.user!!.displayName
            val image= com.abdurashidov.chatting.utils.MyData.user!!.photoUrl
            val message=binding.messageEt.text.toString()
            val simpleDateFormat=SimpleDateFormat("dd.MM.yyyy_HH:mm:ss")
            val date=simpleDateFormat.format(Date())
            val uid=auth.uid
            val groupMessage=GroupMessage(author, image, message, date, uid)
            val key=reference.push().key
            reference.child(name).child(key!!).setValue(groupMessage)

        }

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