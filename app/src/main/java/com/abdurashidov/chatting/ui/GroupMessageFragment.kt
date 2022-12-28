package com.abdurashidov.chatting.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.abdurashidov.chatting.MyData
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.adapters.GroupMessageAdapter
import com.abdurashidov.chatting.adapters.UserAdapter
import com.abdurashidov.chatting.databinding.FragmentGroupMessageBinding
import com.abdurashidov.chatting.models.GroupMessage
import com.abdurashidov.chatting.models.User
import com.google.android.material.shape.CornerFamily
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class GroupMessageFragment : Fragment() {

    lateinit var binding:FragmentGroupMessageBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var groupMessageAdapter: GroupMessageAdapter
    lateinit var auth: FirebaseAuth
    var list=ArrayList<User>()
    private  val TAG = "GroupMessageFragment"

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
        reference=firebaseDatabase.getReference("groupsMessage")
        auth= FirebaseAuth.getInstance()

        binding.sendBtn.setOnClickListener {
            Log.d(TAG, "onCreateView: ${MyData.list}")
            val list=MyData.list
            var message:GroupMessage?=null
            list.forEach {
                if (it.uid==auth.currentUser!!.uid){
                    val author=it.email
                    val image=it.photoUrl
                    val m=binding.messageEt.text.toString()
                    val date=SimpleDateFormat("yyyy.MM.dd HH:mm").format(Date())
                    val uid= auth.currentUser!!.uid
                    val groupMessage=GroupMessage(author, image, m, date, uid)
                    message=groupMessage
                }
            }
            if (message!=null && message?.image!=null && message?.author!=null){
                val key=reference.push().key
                reference.child("$name/$key").setValue(message)
                binding.messageEt.text.clear()
            }else{
//                    Log.d(TAG, "onCreateView: $message")
                Toast.makeText(context, "Not data found", Toast.LENGTH_SHORT).show()
            }
        }

        reference.child("$name")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messageList=ArrayList<GroupMessage>()
                    val children=snapshot.children
                    for (child in children) {
                        val value=child.getValue(GroupMessage::class.java)
                        messageList.add(value!!)
                    }
                    groupMessageAdapter= GroupMessageAdapter(messageList, auth.currentUser!!.uid)
                    binding.rv.adapter=groupMessageAdapter
                    binding.rv.scrollToPosition(list.size-1)
                }

                override fun onCancelled(error: DatabaseError) {
                    
                }
            })

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