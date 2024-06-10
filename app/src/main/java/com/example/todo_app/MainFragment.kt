package com.example.todo_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.todo_app.adapters.ProjectListAdapter
import com.example.todo_app.adapters.TaskListAdapter
import com.example.todo_app.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_main,container,false)

        // adjusting recyclerview adapters
        val adapter = TaskListAdapter(sharedViewModel)
        val projectListAdapter= ProjectListAdapter()

        adapter.submitList(sharedViewModel.tasks.value)
        projectListAdapter.submitList(sharedViewModel.projects.value)

        binding.progressRecyclerview.adapter = adapter
        binding.groupsRecyclerview.adapter=projectListAdapter

        //setting counters
        binding.tasksCounter.text=sharedViewModel.tasks.value?.size.toString()
        binding.projectsCounter.text=sharedViewModel.projects.value?.size.toString()

        return binding.root
    }

}