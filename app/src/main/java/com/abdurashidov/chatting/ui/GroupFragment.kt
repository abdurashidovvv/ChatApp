package com.abdurashidov.chatting.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.abdurashidov.chatting.MyData
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.adapters.GroupAdapter
import com.abdurashidov.chatting.adapters.UserAdapter
import com.abdurashidov.chatting.databinding.DialogItemBinding
import com.abdurashidov.chatting.databinding.FragmentGroupBinding
import com.abdurashidov.chatting.models.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class GroupFragment : Fragment(), GroupAdapter.RvClick {

    lateinit var binding: FragmentGroupBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var list = ArrayList<String>()
    lateinit var groupAdapter: GroupAdapter
    private  val TAG = "GroupFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupBinding.inflate(layoutInflater)

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("groups")

        binding.addGroup.setOnClickListener {
            val alertDialog = AlertDialog.Builder(binding.root.context).create()
            val dialogItemBinding = DialogItemBinding.inflate(layoutInflater)
            alertDialog.setView(dialogItemBinding.root)

            dialogItemBinding.cancel.setOnClickListener {
                alertDialog.cancel()
            }
            dialogItemBinding.save.setOnClickListener {
                if (dialogItemBinding.groupName.text!!.isNotBlank()) {
                    val key = reference.push().key
                    reference.child(key!!).setValue(dialogItemBinding.groupName.text.toString())
                    alertDialog.cancel()
                } else {
                    Toast.makeText(
                        binding.root.context,
                        "Iltimos hamma maydonlarni toldiring.!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            alertDialog.show()
        }


        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val children = snapshot.children
//                Log.d(TAG, "onDataChange: $children")
                for (child in children) {
//                    Log.d(TAG, "onDataChange: $child")
                    val value=child.getValue(String::class.java)
                    Log.d(TAG, "onDataChange: $value")
                    if (value != null) {
                        list.add(value)
                    }
                }
                groupAdapter = GroupAdapter(list, this@GroupFragment)
                binding.rv.adapter = groupAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return binding.root
    }



    override fun onClick(label: String) {
        findNavController().navigate(R.id.groupMessageFragment, bundleOf("groupName" to label))
    }
}