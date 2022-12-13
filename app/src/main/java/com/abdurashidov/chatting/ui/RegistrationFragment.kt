package com.abdurashidov.chatting.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.databinding.FragmentRegistrationBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding
    lateinit var googleSignInClient:GoogleSignInClient
    var RC_SIGN_IN=1
    private val TAG = "RegistrationFragment"
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentRegistrationBinding.inflate(layoutInflater)

        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient=GoogleSignIn.getClient(binding.root.context, gso)
        auth= FirebaseAuth.getInstance()

        binding.signin.setOnClickListener {
            signIn()
        }
        binding.logout.setOnClickListener {
            googleSignInClient.signOut()
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RC_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account=task.getResult(ApiException::class.java)
                Log.d(TAG, "onActivityResult: ${account.email}")
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e:Exception){
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String){
        val credential=GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()){task->
                if (task.isSuccessful){
                    Log.d(TAG, "firebaseAuthWithGoogle: success")
                    val user=auth.currentUser
                    findNavController().navigate(R.id.usersFragment)
                }else{
                    Log.d(TAG, "firebaseAuthWithGoogle: failure")
//                    updateUI(null)
                    Toast.makeText(binding.root.context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn(){
        val signInIntent=googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}

