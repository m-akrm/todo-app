package com.example.todo_app.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.todo_app.R
import com.example.todo_app.SharedViewModel
import com.example.todo_app.databinding.FragmentAddTaskBinding
import com.example.todo_app.dataclasses.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    val args:AddTaskFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).visibility= View.GONE
        binding= DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_task, container, false)
        if(args.task == null){
            binding.addTaskButton.setOnClickListener {
                val projectname=binding.projectName.editText?.text.toString()
                val taskname=binding.taskName.editText?.text.toString()
                val duetime=binding.duetime.editText?.text.toString()
                val startdate=binding.startDate.editText?.text.toString()
                val enddate=binding.endDate.editText?.text.toString()
                val description=binding.projectDescription.editText?.text.toString()
                if(projectname.isEmpty() || duetime.isEmpty() || startdate.isEmpty() || enddate.isEmpty() ||  taskname.isEmpty()){
                    Toast.makeText(this.requireContext(), "some information is missing", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(sharedViewModel.addTask(Task( taskname,projectname,startdate,enddate,"To-Do",0,duetime,description))){
                    Toast.makeText(this.requireActivity(), "task added successfully", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }else{
                    Toast.makeText(this.requireActivity(), "a task with same name exists", Toast.LENGTH_SHORT).show()
                }

            }
        }
        else{
            val task = args.task!!
            binding.projectName.editText?.setText(task.projectName)
            binding.taskName.editText?.setText(task.taskName)
            binding.duetime.editText?.setText(task.dueTime)
            binding.startDate.editText?.setText(task.startDate)
            binding.endDate.editText?.setText(task.endDate)
            binding.projectDescription.editText?.setText(task.description)

            binding.addTaskButton.setOnClickListener {
                val projectname=binding.projectName.editText?.text.toString()
                val taskname=binding.taskName.editText?.text.toString()
                val duetime=binding.duetime.editText?.text.toString()
                val startdate=binding.startDate.editText?.text.toString()
                val enddate=binding.endDate.editText?.text.toString()
                val description=binding.projectDescription.editText?.text.toString()
                if(projectname.isEmpty() || duetime.isEmpty() || startdate.isEmpty() || enddate.isEmpty()  || taskname.isEmpty()){
                    Toast.makeText(this.requireContext(), "some information is missing", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                sharedViewModel.editTask(task,Task( taskname,projectname,startdate,enddate,"To-Do",0,duetime,description))

                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        //backbutton navigation
        binding.appbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        //autocomplete List
        (binding.projectName.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(sharedViewModel.getprojectsname().toTypedArray() )
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
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
                datePicker.show(this.requireActivity().supportFragmentManager, "datePicker")
                datePicker.addOnPositiveButtonClickListener {
                    binding.startDate.editText?.setText(datePicker.headerText)
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
                    binding.endDate.editText?.setText(datePicker.headerText)
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
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Select due-time")
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)

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