package com.example.todo_app.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.todo_app.R
import com.example.todo_app.SharedViewModel
import com.example.todo_app.databinding.FragmentAddProjectBinding
import com.example.todo_app.dataclasses.Project
import com.example.todo_app.dataclasses.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat


class AddProjectFragment : Fragment() {

    private lateinit var binding: FragmentAddProjectBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_add_project,container,false)
        this.requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).let {
            it.visibility=View.VISIBLE
            it.findViewById<FloatingActionButton>(R.id.fab).visibility=View.GONE
        }

        //handling editText listeners and date Pickers
        editTextListeners()
        binding.addProjectButton.setOnClickListener {

            //recieve data from views
            val projectname=binding.projectName.editText?.text.toString()
            val taskname=binding.taskName.editText?.text.toString()
            val duetime=binding.duetime.editText?.text.toString()
            val startdateedittext= binding.startDate.editText?.text.toString()
            val enddateedittext= binding.endDate.editText?.text.toString()
            val description=binding.projectDescription.editText?.text.toString()
            val startdate= SimpleDateFormat("yyyy-MM-dd").parse(startdateedittext)
            val enddate= SimpleDateFormat("yyyy-MM-dd").parse(enddateedittext)
            //check empty data
            if(projectname.isEmpty() || duetime.isEmpty() || startdateedittext.isEmpty() || enddateedittext.isEmpty() || taskname.isEmpty()){
                Toast.makeText(this.requireContext(), "some information is missing", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            if(sharedViewModel.isTaskExist(Task( taskname,projectname,startdate,enddate,"To-Do",0,duetime,description))){
                Toast.makeText(this.requireActivity(), "task already exists", Toast.LENGTH_SHORT).show()
            }
            else{
                sharedViewModel.addProject(Project( projectname,0,0),Task( taskname,projectname,startdate,enddate,"To-Do",0,duetime,description))


                Toast.makeText(this.requireActivity(), "project added successfully", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressedDispatcher.onBackPressed()


            }

        }

        return binding.root
    }


    private fun editTextListeners() {
        binding.startDate.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = this.requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
                datePicker.show(this.requireActivity().supportFragmentManager, "datePicker")
                datePicker.addOnPositiveButtonClickListener {
                    binding.startDate.editText?.setText(sharedViewModel.convertLongToTime(it))
                }
                binding.startDate.editText?.clearFocus()
            }
        }
        binding.endDate.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val imm = this.requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
                datePicker.show(this.requireActivity().supportFragmentManager, "datePicker")
                datePicker.addOnPositiveButtonClickListener {
                    binding.endDate.editText?.setText(sharedViewModel.convertLongToTime(it))
                }
                binding.endDate.editText?.clearFocus()
            }
        }
        binding.duetime.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                val imm = this.requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                MaterialTimePicker.Builder()
                    .setInputMode(INPUT_MODE_CLOCK)
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select due-time")

                    .build()

                    .let {
                        it.show(this.requireActivity().supportFragmentManager, "timePicker")
                        it.addOnPositiveButtonClickListener {v->
                            binding.duetime.editText?.setText(String.format("%02d", it.hour)+":"+String.format("%02d", it.minute))
                        }
                    }



                binding.duetime.editText?.clearFocus()
            }

        }
    }




}