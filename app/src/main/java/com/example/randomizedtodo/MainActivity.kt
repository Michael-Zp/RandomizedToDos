package com.example.randomizedtodo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.randomizedtodo.databinding.ActivityMainBinding
import com.example.randomizedtodo.model.Model
import com.example.randomizedtodo.ui.groups.GroupsViewModel
import com.example.randomizedtodo.ui.schedules.SchedulesViewModel
import com.example.randomizedtodo.ui.taskList.TaskListViewModel
import com.example.randomizedtodo.ui.tasks.TasksViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var model: Model

    private val taskListViewModel: TaskListViewModel by viewModels()
    private val tasksViewModel: TasksViewModel by viewModels()
    private val groupsViewModel: GroupsViewModel by viewModels()
    private val schedulesViewModel: SchedulesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_task_list, R.id.nav_tasks, R.id.nav_schedules, R.id.nav_groups
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        model = Model(ArrayList(), ArrayList(), ArrayList(), filesDir)
        taskListViewModel.init(model)
        tasksViewModel.init(model)
        groupsViewModel.init(model)
        schedulesViewModel.init(model)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        val displayName = navController.currentDestination!!.displayName

        if (displayName.contains("nav_task_list"))
        {
            menuInflater.inflate(R.menu.main_no_plus, menu)
        }
        else
        {
            menuInflater.inflate(R.menu.main, menu)

            menu[0].setOnMenuItemClickListener {
                if (navController.currentDestination != null)
                {

                    if (displayName.contains("nav_tasks"))
                    {
                        navController.navigate(R.id.nav_add_task)
                    }
                    else if (displayName.contains("nav_schedules"))
                    {
                        navController.navigate(R.id.nav_add_schedule)
                    }
                    else if (displayName.contains("nav_groups"))
                    {
                        navController.navigate(R.id.nav_add_group)
                    }
                }
                else
                {
                    Toast.makeText(this, "none", Toast.LENGTH_LONG).show()
                }
                true
            }
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        model.save()
    }

    override fun onResume() {
        super.onResume()
        model.load()

        tasksViewModel.refresh()
        taskListViewModel.refresh()
    }
}