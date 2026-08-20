package com.example.freelancer.ui.projects

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freelancer.R
import com.example.freelancer.data.model.Project
import com.example.freelancer.data.model.ProjectStatus
import com.example.freelancer.databinding.FragmentProjectsBinding
import com.google.android.material.tabs.TabLayout

class ProjectsFragment : Fragment() {

    private var _binding: FragmentProjectsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var projectAdapter: ProjectAdapter

    private var allProjects = listOf<Project>()

    private var currentQuery = ""

    private var currentStatusFilter: ProjectStatus? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProjectsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupTabs()
        loadDummyData()
    }

    // ============================================================
    // RecyclerView
    // ============================================================

    private fun setupRecyclerView() {

        projectAdapter = ProjectAdapter(

            onCardClick = { project ->

                Toast.makeText(
                    requireContext(),
                    "Opening ${project.name}",
                    Toast.LENGTH_SHORT
                ).show()

                // TODO:
                // Later navigate to ProjectDetailsFragment
            },

            onStartProjectClick = { project ->

                startProject(project)
            }
        )

        binding.rvProjects.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = projectAdapter

            setHasFixedSize(true)
        }
    }

    // ============================================================
    // Search
    // ============================================================

    private fun setupSearch() {

        binding.etProjectSearch.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                    currentQuery =
                        s?.toString()
                            ?.trim()
                            ?: ""

                    filterProjects()
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )
    }

    // ============================================================
    // Tabs
    // ============================================================

    private fun setupTabs() {

        binding.projectTabs.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(
                    tab: TabLayout.Tab
                ) {

                    currentStatusFilter =
                        when (tab.position) {

                            1 -> ProjectStatus.NOT_STARTED

                            2 -> ProjectStatus.IN_PROGRESS

                            3 -> ProjectStatus.COMPLETED

                            else -> null
                        }

                    filterProjects()
                }

                override fun onTabUnselected(
                    tab: TabLayout.Tab
                ) {
                }

                override fun onTabReselected(
                    tab: TabLayout.Tab
                ) {
                }
            }
        )

        // Start with "All"
        binding.projectTabs.getTabAt(0)?.select()
    }

    // ============================================================
    // Dummy data for now
    // ============================================================

    private fun loadDummyData() {

        allProjects = listOf(

            Project(
                id = "1",
                name = "E-Commerce Android App",
                client = "Nexus Retail",
                date = "Oct 12, 2023",
                status = ProjectStatus.NOT_STARTED,
                completedMilestones = 0,
                totalMilestones = 6
            ),

            Project(
                id = "2",
                name = "Food Delivery App",
                client = "Zesty Bites",
                date = "Nov 05, 2023",
                status = ProjectStatus.IN_PROGRESS,
                completedMilestones = 3,
                totalMilestones = 6
            ),

            Project(
                id = "3",
                name = "Fitness Tracker UI",
                client = "FitLife Inc.",
                date = "Dec 15, 2023",
                status = ProjectStatus.COMPLETED,
                completedMilestones = 5,
                totalMilestones = 5
            ),

            Project(
                id = "4",
                name = "Chat Messenger App",
                client = "Talkative",
                date = "Jan 10, 2024",
                status = ProjectStatus.NOT_STARTED,
                completedMilestones = 0,
                totalMilestones = 4
            ),

            Project(
                id = "5",
                name = "Portfolio Website",
                client = "Personal",
                date = "Feb 20, 2024",
                status = ProjectStatus.IN_PROGRESS,
                completedMilestones = 2,
                totalMilestones = 4
            )
        )

        filterProjects()
    }

    // ============================================================
    // Filter
    // ============================================================

    private fun filterProjects() {

        val filteredProjects =
            allProjects.filter { project ->

                val matchesQuery =
                    currentQuery.isBlank() ||
                            project.name.contains(
                                currentQuery,
                                ignoreCase = true
                            ) ||
                            project.client.contains(
                                currentQuery,
                                ignoreCase = true
                            )

                val matchesStatus =
                    currentStatusFilter == null ||
                            project.status == currentStatusFilter

                matchesQuery && matchesStatus
            }

        projectAdapter.submitList(filteredProjects)

        // Empty state
        if (filteredProjects.isEmpty()) {

            binding.rvProjects.visibility =
                View.GONE

            binding.emptyState.visibility =
                View.VISIBLE

        } else {

            binding.rvProjects.visibility =
                View.VISIBLE

            binding.emptyState.visibility =
                View.GONE
        }
    }

    // ============================================================
    // Start Project
    // ============================================================

    private fun startProject(project: Project) {

        /*
         * CURRENTLY:
         * This updates only the local dummy list.
         *
         * LATER:
         * This will update Supabase:
         *
         * status = IN_PROGRESS
         * actual_start_date = current timestamp
         *
         * and milestone tracking begins.
         */

        if (project.status != ProjectStatus.NOT_STARTED) {
            return
        }

        val updatedProject =
            project.copy(
                status = ProjectStatus.IN_PROGRESS
            )

        allProjects =
            allProjects.map { existingProject ->

                if (existingProject.id == project.id) {
                    updatedProject
                } else {
                    existingProject
                }
            }

        filterProjects()

        Toast.makeText(
            requireContext(),
            "${project.name} started",
            Toast.LENGTH_SHORT
        ).show()
    }

    // ============================================================
    // Cleanup
    // ============================================================

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}