package com.abdurashidov.chatting.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.abdurashidov.chatting.adapters.MessageAdapter
import com.abdurashidov.chatting.databinding.FragmentMessageBinding
import com.abdurashidov.chatting.models.Message
import com.abdurashidov.chatting.utils.MyData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MessageFragment : Fragment() {

    lateinit var binding: FragmentMessageBinding
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var messageAdapter: MessageAdapter
    var exit=0

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(layoutInflater)

        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        reference = firebaseDatabase.getReference("users")

        firebaseAuth = FirebaseAuth.getInstance()

        val user = MyData.user


        binding.sendBtn.setOnClickListener {

            val m = binding.messageEt.text.toString()
            val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val date = simpleDateFormat.format(Date())
            val message = Message(m, date, firebaseAuth.currentUser!!.uid, user!!.uid)
            val key = reference.push().key

            reference.child("${firebaseAuth.currentUser!!.uid}/messages/${user.uid}/$key")
                .setValue(message)

            reference.child("${user.uid}/messages/${firebaseAuth.currentUser!!.uid}/$key")
                .setValue(message)

            binding.messageEt.text.clear()
        }

        reference.child("${firebaseAuth.currentUser?.uid}/messages/${user!!.uid}")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    val list=ArrayList<Message>()
                    for (child in children) {
                        val value=child.getValue(Message::class.java)
                        if (value != null) {
                            list.add(value)
                        }
                    }
                    messageAdapter= MessageAdapter(list, firebaseAuth.currentUser!!.uid)
                    binding.rv.adapter=messageAdapter//
                }

                override fun onCancelled(error: DatabaseError) {
                    
                }
            })

        val callback=object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
    }

}