package com.example.freelancer.ui.projects

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.freelancer.databinding.ItemProjectBinding
import com.example.freelancer.data.model.Project
import com.example.freelancer.data.model.ProjectStatus

class ProjectAdapter(
    private val onCardClick: (Project) -> Unit,
    private val onStartProjectClick: (Project) -> Unit
) : ListAdapter<Project, ProjectAdapter.ProjectViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProjectViewHolder {

        val binding = ItemProjectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProjectViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    inner class ProjectViewHolder(
        private val binding: ItemProjectBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(project: Project) {

            // -----------------------------
            // Basic project information
            // -----------------------------

            binding.tvProjectName.text = project.name
            binding.tvClientName.text = project.client
            binding.tvProjectDate.text = project.date

            // -----------------------------
            // Status
            // -----------------------------

            when (project.status) {

                ProjectStatus.NOT_STARTED -> {

                    binding.tvProjectStatus.text = "NOT STARTED"

                    binding.tvProjectStatus.setTextColor(
                        Color.parseColor("#B08222")
                    )

                    // Show Start Project button
                    binding.btnStartProject.visibility = View.VISIBLE

                    // Hide milestone progress
                    binding.projectProgressContainer.visibility =
                        View.GONE
                }

                ProjectStatus.IN_PROGRESS -> {

                    binding.tvProjectStatus.text = "IN PROGRESS"

                    binding.tvProjectStatus.setTextColor(
                        Color.parseColor("#4A0E1C")
                    )

                    // Project already started
                    binding.btnStartProject.visibility = View.GONE

                    // Show milestone progress
                    binding.projectProgressContainer.visibility =
                        View.VISIBLE

                    binding.tvMilestoneProgress.text =
                        "${project.completedMilestones} of " +
                                "${project.totalMilestones} milestones"

                    binding.tvProgressPercent.text =
                        "${project.progressPercentage}%"

                    binding.projectProgress.progress =
                        project.progressPercentage
                }

                ProjectStatus.COMPLETED -> {

                    binding.tvProjectStatus.text = "COMPLETED"

                    binding.tvProjectStatus.setTextColor(
                        Color.parseColor("#2E7D32")
                    )

                    binding.btnStartProject.visibility =
                        View.GONE

                    binding.projectProgressContainer.visibility =
                        View.VISIBLE

                    binding.tvMilestoneProgress.text =
                        "All milestones completed"

                    binding.tvProgressPercent.text =
                        "100%"

                    binding.projectProgress.progress = 100
                }
            }

            // -----------------------------
            // Open project details
            // -----------------------------

            binding.projectCard.setOnClickListener {
                onCardClick(project)
            }

            // -----------------------------
            // Start project
            // -----------------------------

            binding.btnStartProject.setOnClickListener {
                onStartProjectClick(project)
            }
        }
    }

    // -----------------------------
    // DiffUtil
    // -----------------------------

    private class ProjectDiffCallback :
        DiffUtil.ItemCallback<Project>() {

        override fun areItemsTheSame(
            oldItem: Project,
            newItem: Project
        ): Boolean {

            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Project,
            newItem: Project
        ): Boolean {

            return oldItem == newItem
        }
    }
}