package com.example.todo_app.adapters

import com.example.todo_app.databinding.CalenderLayoutBinding



import android.view.LayoutInflater

import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.dataclasses.Project
import com.example.todo_app.dataclasses.Task


class CalenderListAdapter(private val oneditClicked: (Task) -> Unit, private val ondeleteClicked: (Task) -> Unit)  : ListAdapter<Task, CalenderListAdapter.CalenderViewHolder>(
    CalenderDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalenderViewHolder {
        val binding=CalenderLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CalenderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalenderViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class CalenderViewHolder(private val binding:CalenderLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.data=task
            binding.editcardview.setOnClickListener{
                oneditClicked(task)
            }
            binding.deletecardview.setOnClickListener{
                ondeleteClicked(task)
            }
        }
    }
}

class CalenderDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.projectName == newItem.projectName
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}




