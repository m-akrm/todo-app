package com.example.todo_app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todo_app.R
import com.example.todo_app.SharedViewModel
import com.example.todo_app.adapters.ProjectListAdapter
import com.example.todo_app.adapters.TaskListAdapter
import com.example.todo_app.databinding.FragmentMainBinding
import com.example.todo_app.dataclasses.Project
import com.example.todo_app.dataclasses.Task
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import java.util.Calendar

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    val calender=Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner=viewLifecycleOwner
        this.requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).let {
            it.visibility=View.VISIBLE
            it.findViewById<FloatingActionButton>(R.id.fab).visibility=View.VISIBLE
        }

        binding.button.setOnClickListener {
            this.requireActivity().findViewById<CoordinatorLayout>(R.id.coordinator).findViewById<BottomAppBar>(R.id.bottom_app_bar).findViewById<BottomNavigationView>(R.id.bottom_nav).selectedItemId=R.id.calenderFragment
        }

        // adjusting recyclerview adapters
        val adapter = TaskListAdapter(
            ondeleteClicked = {

                sharedViewModel.deleteTask(it)
                Toast.makeText(this.requireContext(), "delete", Toast.LENGTH_SHORT).show()
            },
            oneditClicked = {
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddTaskFragment().setTask(it))
            },
            onProgressChange = {
                progressChange(it)
            }
        )

        val projectListAdapter= ProjectListAdapter(
            ondeleteClicked = {
                sharedViewModel.deleteProject(it)
                Toast.makeText(this.requireContext(), "delete", Toast.LENGTH_SHORT).show() 
            },
            oneditClicked = {
                editProject(it)
            }
        )
        sharedViewModel.projects.observe(viewLifecycleOwner){
            Log.i("test","project observer")
            projectListAdapter.submitList(it)
            binding.groupsRecyclerview.adapter=projectListAdapter
            binding.projectsCounter.text=it.size.toString()

        }
        sharedViewModel.tasks.observe(viewLifecycleOwner){
            Log.i("test","task observer")
            adapter.submitList(it)
            binding.progressRecyclerview.adapter = adapter
            binding.tasksCounter.text=it.size.toString()
            sharedViewModel.changeCurrentTask(calender.time)
        }
        sharedViewModel.currenttasks.observe(viewLifecycleOwner){
            Log.i("tested",it.toString())
            binding.textView3.text="You completed your\ntasks today"
            if(it.isNotEmpty()){
                var progress=0
                for(task in it){
                    progress+=task.progressPercentage
                }
                progress/=it.size
                binding.circularProgressIndicator.progress=progress
                binding.textView4.text=progress.toString()+"%"
                if (progress!=100)
                    binding.textView3.text="Your today's task \nalmost done"
            }
            else{
                binding.circularProgressIndicator.progress=100
                binding.textView4.text=100.toString()+"%"
            }
        }






        //setting counters
        binding.tasksCounter.text=sharedViewModel.tasks.value?.size.toString()
        binding.projectsCounter.text=sharedViewModel.projects.value?.size.toString()

        return binding.root
    }

    private fun editProject(it: Project) {
        val editText = EditText(this.requireContext()).apply {
            setText(it.projectName)
        }
        MaterialAlertDialogBuilder(this.requireContext())
            .setTitle("Edit Project Name")

            .setIcon(R.drawable.edit_icon)
            .setView(editText)
            .setNegativeButton("dismiss") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("save") { dialog, which ->
                val enteredText = editText.text.toString() // Get text here
                sharedViewModel.editProject(it, enteredText)
                Toast.makeText(this.requireActivity(), "project edited", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
    private fun progressChange(task: Task) {
        val slider = Slider(this.requireContext()).apply {
            value = task.progressPercentage.toFloat()
            valueFrom = 0f
            valueTo = 100f
            stepSize = 5f
            labelBehavior = LabelFormatter.LABEL_VISIBLE
            setLabelFormatter {
                it.toInt().toString()+"%"
            }
        }

        MaterialAlertDialogBuilder(this.requireContext())
            .setView(slider)
            .setTitle("change task progress")


            .setNegativeButton("dismiss") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("save") { dialog, which ->
                val newtask = task.copy()
                newtask.progressPercentage = slider.value.toInt()
                if(newtask.progressPercentage==100) newtask.status="Completed"
                else if(newtask.progressPercentage==0) newtask.status="To Do"
                else newtask.status="In Progress"
                sharedViewModel.editTask(task, newtask)
            }
            .show()
    }

}