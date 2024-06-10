package com.example.todo_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.dataclasses.Project
import com.example.todo_app.R
import com.example.todo_app.databinding.GroupsLayoutBinding

class ProjectListAdapter : ListAdapter<Project, ProjectListAdapter.ProjectViewHolder>(
    ProjectDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectViewHolder {
        val binding=GroupsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = getItem(position)
        holder.bind(project)
    }

    inner class ProjectViewHolder(private val binding:GroupsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(project: Project) {

            if (project.icon == null) {
                binding.iconImageView.setImageResource(R.drawable.office_icon)
            } else {
                binding.iconImageView.setImageDrawable(project.icon)
            }

            binding.project=project
            binding.taskcount.text= buildString {
                append(project.taskscount.toString())
                append("Tasks")
            }
            binding.progressTextview.text=project.progressPercentage.toString()
        }
    }
}

class ProjectDiffCallback : DiffUtil.ItemCallback<Project>() {
    override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem.projectName == newItem.projectName
    }

    override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem == newItem
    }
}