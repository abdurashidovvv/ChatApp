package com.abdurashidov.chatting.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.adapters.UserAdapter
import com.abdurashidov.chatting.databinding.FragmentUsersBinding
import com.abdurashidov.chatting.databinding.HeaderItemBinding
import com.abdurashidov.chatting.models.User
import com.abdurashidov.chatting.utils.MyData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UsersFragment : Fragment(), UserAdapter.RvClick {

    lateinit var binding: FragmentUsersBinding
    lateinit var firebaseAuth: FirebaseAuth
    private val TAG = "UsersFragment"
    private lateinit var userAdapter: UserAdapter
    var list=ArrayList<User>()
    var index=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentUsersBinding.inflate(layoutInflater)

        firebaseAuth=FirebaseAuth.getInstance()
        val currentUser=firebaseAuth.currentUser

//        val header=HeaderItemBinding.inflate(layoutInflater)
//        header.image.setImageResource(R.drawable.password)
//        binding.navView.addHeaderView(header.root)

        val email = MyData.user?.email
        val displayName = MyData.user?.displayName
        val phoneNumber = currentUser?.phoneNumber
        val photoUrl = MyData.user?.photoUrl
        val uid = currentUser!!.uid


        val database = Firebase.database
        val myRef = database.getReference("users")

        val user=User(email, displayName, phoneNumber, photoUrl.toString(), uid)



        Toast.makeText(binding.root.context, "${currentUser.email}", Toast.LENGTH_SHORT).show()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val filterList= arrayListOf<User>()
                var children = snapshot.children
                for (child in children) {
                    val value=child.getValue(User::class.java)
                    if (value != null && uid!=value.uid) {
                        list.add(value)
                    }
                    if (value!=null && value.uid==uid){
                        filterList.add(value)
                    }
                }
                if (filterList.isEmpty()){
                    myRef.child(uid).setValue(user)
                }
                userAdapter= UserAdapter(list, this@UsersFragment)
                Log.d(TAG, "onDataChange: $list")
                binding.rv.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return binding.root
    }

    override fun onClick(label: User) {
        MyData.user=label
        findNavController().navigate(R.id.messageFragment)
    }

}