package com.example.freelancer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.freelancer.R
import com.example.freelancer.ui.fragments.ContractsFragment
import com.example.freelancer.ui.fragments.DashboardFragment
import com.example.freelancer.ui.fragments.ProfileFragment
import com.example.freelancer.ui.fragments.ProjectsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Open Dashboard first
        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }

        bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.navigation_dashboard -> {
                    loadFragment(DashboardFragment())
                    true
                }

                R.id.navigation_projects -> {
                    loadFragment(ProjectsFragment())
                    true
                }

                R.id.navigation_contracts -> {
                    loadFragment(ContractsFragment())
                    true
                }

                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}