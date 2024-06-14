package com.example.todo_app.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todo_app.R
import com.example.todo_app.SharedViewModel
import com.example.todo_app.databinding.FragmentAddTaskBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).visibility=
            View.GONE
        binding=
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_task, container, false)

        //backbutton navigation
        binding.appbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        //autocomplete List
        (binding.menu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(sharedViewModel.getprojectsname().toTypedArray() )

        //handling editText listeners and date Pickers
        editTextListeners()
        return binding.root
    }

    private fun editTextListeners() {
        binding.startDate.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = this.requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                bindDataPicker()
                binding.startDate.editText?.clearFocus()
            }
        }
        binding.endDate.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = this.requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                bindDataPicker()
                binding.startDate.editText?.clearFocus()
            }
        }
    }

    private fun bindDataPicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.show(this.requireActivity().supportFragmentManager, "datePicker")
        datePicker.addOnPositiveButtonClickListener {
            binding.startDate.editText?.setText(datePicker.headerText)
        }
    }


}