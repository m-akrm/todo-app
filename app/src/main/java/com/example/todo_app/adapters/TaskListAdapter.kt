package com.example.todo_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.SharedViewModel
import com.example.todo_app.dataclasses.Task
import com.example.todo_app.databinding.EvenProgressLayoutBinding
import com.example.todo_app.databinding.OddProgressLayoutBinding
import com.example.todo_app.dataclasses.Project


class TaskListAdapter(private val oneditClicked: (Task) -> Unit, private val ondeleteClicked: (Task) -> Unit,private val onProgressChange: (Task) -> Unit) : ListAdapter<Task, RecyclerView.ViewHolder>(
    TaskDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==0) {
            val binding = EvenProgressLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            EvenTaskViewHolder(binding)
        }else{
            val binding = OddProgressLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            OddTaskViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = getItem(position)
        if (position%2==0) (holder as EvenTaskViewHolder).bind(task)
        else (holder as OddTaskViewHolder).bind(task)

    }

    override fun getItemViewType(position: Int): Int {
        return position%2
    }


    inner class EvenTaskViewHolder(private val binding: EvenProgressLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task=task
            binding.editCardview.setOnClickListener {
                oneditClicked(task)
            }
            binding.deleteCardview.setOnClickListener {
                ondeleteClicked(task)
            }
            binding.parentCardView.setOnClickListener {
                onProgressChange(task)
            }
        }
    }

    inner class OddTaskViewHolder(private val binding: OddProgressLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task=task
            binding.editCardview.setOnClickListener {
                oneditClicked(task)
            }
            binding.deleteCardview.setOnClickListener {
                ondeleteClicked(task)
            }
            binding.parentCardView.setOnClickListener {
                onProgressChange(task)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskName == newItem.taskName
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}