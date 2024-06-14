package com.example.todo_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.example.todo_app.R
import com.example.todo_app.databinding.FragmentAddProjectBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView


class AddProjectFragment : Fragment() {

    private lateinit var binding: FragmentAddProjectBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_add_project,container,false)
        this.requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).visibility= View.VISIBLE
        //priority list
        val priority= arrayOf("Low","Medium","High")
        (binding.priority.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(priority)

        return binding.root
    }

}