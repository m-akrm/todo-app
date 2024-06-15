package com.example.todo_app.fragments

import android.os.Bundle
import android.util.AttributeSet
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
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

        // adjusting recyclerview adapters
        val adapter = TaskListAdapter(
            ondeleteClicked = {
                sharedViewModel.deleteTask(it)
                Toast.makeText(this.requireContext(), "delete", Toast.LENGTH_SHORT).show()
            },
            oneditClicked = {
                this.findNavController().navigate(MainFragmentDirections.actionMainFragmentToAddTaskFragment().setTask(it))
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
            projectListAdapter.submitList(it)
            binding.groupsRecyclerview.adapter=projectListAdapter

        }

        sharedViewModel.tasks.observe(viewLifecycleOwner){
            adapter.submitList(it)
            binding.progressRecyclerview.adapter = adapter
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

}