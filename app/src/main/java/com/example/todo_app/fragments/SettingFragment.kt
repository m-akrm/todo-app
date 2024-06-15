package com.example.todo_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.example.todo_app.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).let {
            it.visibility=View.VISIBLE
            it.findViewById<FloatingActionButton>(R.id.fab).visibility=View.VISIBLE
        }

        return inflater.inflate(R.layout.fragment_setting, container, false)
    }


}