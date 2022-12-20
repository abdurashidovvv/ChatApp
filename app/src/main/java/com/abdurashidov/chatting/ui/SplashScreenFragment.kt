package com.abdurashidov.chatting.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.databinding.FragmentSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    lateinit var binding: FragmentSplashScreenBinding
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSplashScreenBinding.inflate(layoutInflater)

        firebaseAuth=FirebaseAuth.getInstance()

        findNavController().popBackStack()
        if (isOnline(binding.root.context)){
            binding.lottieAnimation.setAnimation(R.raw.chat)
            binding.lottieAnimation.playAnimation()
            val handler=Handler(Looper.getMainLooper())
            val runnable=object : Runnable{
                override fun run() {
                    if (firebaseAuth.currentUser==null){
                        findNavController().navigate(R.id.writePhoneFragment)
                    }else{
                        findNavController().navigate(R.id.usersFragment)
                    }
                }
            }
            handler.postDelayed(runnable, 3000)
        }else{
            binding.lottieAnimation.setAnimation(R.raw.no_internet)
            binding.lottieAnimation.playAnimation()
        }

        return binding.root
    }

    fun isOnline(context: Context):Boolean{
        val connectivityManager=
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager!=null){
            val capabilities=
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities!=null){
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    return true
                }
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                    return true
                }
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    return true
                }
            }
        }
        return false
    }
}