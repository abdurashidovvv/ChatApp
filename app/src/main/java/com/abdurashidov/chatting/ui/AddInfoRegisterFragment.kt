package com.abdurashidov.chatting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.databinding.FragmentAddInfoRegisterBinding
import com.abdurashidov.chatting.models.User
import com.abdurashidov.chatting.utils.MyData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddInfoRegisterFragment : Fragment() {

    lateinit var binding: FragmentAddInfoRegisterBinding
    lateinit var auth: FirebaseAuth
    var imgUri = ""
    lateinit var firebaseStorage: FirebaseStorage
    lateinit var reference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddInfoRegisterBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()


        firebaseStorage = FirebaseStorage.getInstance()
        reference = firebaseStorage.getReference("images/")

        binding.profileImage.setOnClickListener {
            getImgContent.launch("image/*")
        }

        binding.next.setOnClickListener {

            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val userName = binding.username.text.toString()
            val imgUrl = imgUri
            if (firstName != "" && lastName != "" && userName != "" && imgUrl != "") {
                MyData.user = User(lastName, firstName, null, imgUrl, null)
                findNavController().navigate(R.id.usersFragment)
            } else {
                Toast.makeText(
                    binding.root.context,
                    "Iltimos hamma maydonlarni toldiring!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root
    }

    val getImgContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        binding.profileImage.setImageURI(it)
        val m = System.currentTimeMillis()
        val uploadTask = reference.child(m.toString()).putFile(it)
        uploadTask.addOnSuccessListener {
            if (it.task.isSuccessful) {
                val downloadUrl = it.metadata?.reference?.downloadUrl
                downloadUrl?.addOnSuccessListener { imgUrl ->
                    imgUri = imgUrl.toString()
                }
                Toast.makeText(binding.root.context, "Save image", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {

        }
    }
}