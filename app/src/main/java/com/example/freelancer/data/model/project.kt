package com.example.freelancer.data.model

enum class ProjectStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED
}

data class Project(
    val id: String,
    val name: String,
    val client: String,
    val date: String,
    val status: ProjectStatus,
    val completedMilestones: Int = 0,
    val totalMilestones: Int = 0
) {
    val progressPercentage: Int
        get() {
            return if (totalMilestones > 0) {
                ((completedMilestones.toFloat() / totalMilestones) * 100).toInt()
            } else {
                0
            }
        }
}