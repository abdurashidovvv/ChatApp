package com.abdurashidov.chatting.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.abdurashidov.chatting.MyData.phoneNumber
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.databinding.FragmentPhoneAuthBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class PhoneAuthFragment : Fragment() {

    lateinit var binding: FragmentPhoneAuthBinding
    lateinit var auth: FirebaseAuth
    private val TAG = "PhoneAuthFragment"
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    var stringBuilder: StringBuilder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneAuthBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("uz")

        sentVerificationCode(phoneNumber!!)

        binding.next.setOnClickListener {
            val text =
                "${binding.edt1.text}${binding.edt2.text}${binding.edt3.text}${binding.edt4.text}${binding.edt5.text}${binding.edt6.text}"
            stringBuilder?.append(binding.edt1.text.toString())
            stringBuilder?.append(binding.edt2.text.toString())
            stringBuilder?.append(binding.edt3.text.toString())
            stringBuilder?.append(binding.edt4.text.toString())
            stringBuilder?.append(binding.edt5.text.toString())
            stringBuilder?.append(binding.edt6.text.toString())
            try {
                verifyCode(text)
            } catch (e: Exception) {
                Log.d(TAG, "onCreateView: ${e.message}")
            }
        }

        return binding.root
    }

    private fun verifyCode(code: String) {
        if (code.length == 6) {
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)
        }
    }


    //send verification code
    fun sentVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS).setActivity(requireActivity())
            .setCallbacks(callbacks).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    //callback
    private var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String, token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    //Signin with credential
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithCredential:success")

                val user = task.result?.user
                Toast.makeText(binding.root.context, "Successful", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "signInWithPhoneAuthCredential: ${user!!.phoneNumber}")
                findNavController().navigate(R.id.addInfoRegisterFragment)
            } else {
                // Sign in failed, display a message and update the UI
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(
                        binding.root.context, "Password is invalid", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    //true
                    Log.d(TAG, "signInWithPhoneAuthCredential: Yeah")
                    Toast.makeText(binding.root.context, "Correct!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}