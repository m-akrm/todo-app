package com.example.todo_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_app.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_main,container,false)

        val tasks = listOf(
            Task("Task 1", "Project A", "2023-04-01", "2023-04-15", "In Progress", 50),
            Task("Task 2", "Project B", "2023-04-16", "2023-04-30", "Completed", 100),
            Task("Task 3", "Project C", "2023-05-01", "2023-05-15", "To Do", 0)
        )
        val adapter = TaskListAdapter()
        adapter.submitList(tasks)
        binding.progressRecyclerview.adapter = adapter

        return binding.root
    }

}