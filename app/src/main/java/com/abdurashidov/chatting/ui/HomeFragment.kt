package com.abdurashidov.chatting.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.abdurashidov.chatting.R
import com.abdurashidov.chatting.adapters.StateAdapters
import com.abdurashidov.chatting.databinding.FragmentHomeBinding
import com.abdurashidov.chatting.databinding.TabItemViewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var stateAdapters: StateAdapters
    lateinit var list: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = ArrayList()
        list.add("Users")
        list.add("Groups")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        stateAdapters = StateAdapters(list, this)
        binding.myViewpager.adapter = stateAdapters

        setTab()

        TabLayoutMediator(binding.myTablyout, binding.myViewpager) { tab, position ->
            val tabItemView = TabItemViewBinding.inflate(layoutInflater)
            when (position) {
                0 -> tabItemView.name.setImageResource(R.drawable.ic_person)
                1 -> tabItemView.name.setImageResource(R.drawable.ic_group)
                else -> tabItemView.name.setImageResource(R.drawable.ic_person)
            }

            tab.customView = tabItemView.root
        }.attach()

        return binding.root
    }

    private fun setTab() {
        binding.myTablyout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.findViewById<View>(R.id.selected)?.visibility = View.VISIBLE
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.findViewById<View>(R.id.selected)?.visibility = View.GONE
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val customView = tab?.customView
                customView?.findViewById<View>(R.id.selected)?.visibility = View.VISIBLE
            }
        })

    }
    override fun onResume() {
        super.onResume()
        com.abdurashidov.chatting.MyData.isOnline("online")
    }

    override fun onPause() {
        super.onPause()
        com.abdurashidov.chatting.MyData.isOnline("offline")
    }
}