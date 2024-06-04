package com.example.todo_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.databinding.EvenProgressLayoutBinding
import com.example.todo_app.databinding.OddProgressLayoutBinding


class TaskListAdapter(val viewModel: SharedViewModel) : ListAdapter<Task, RecyclerView.ViewHolder>(TaskDiffCallback()) {

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
            var icon=viewModel.projects.value?.find {
                it.projectName==task.projectName
            }?.icon
            if (icon == null) {
                binding.iconImageView.setImageResource(R.drawable.office_icon)
            } else {
                binding.iconImageView.setImageDrawable(icon)
            }
        }
    }

    inner class OddTaskViewHolder(private val binding: OddProgressLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task=task
            var icon=viewModel.projects.value?.find {
                it.projectName==task.projectName
            }?.icon
            if (icon == null) {
                binding.iconImageView.setImageResource(R.drawable.office_icon)
            } else {
                binding.iconImageView.setImageDrawable(icon)
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