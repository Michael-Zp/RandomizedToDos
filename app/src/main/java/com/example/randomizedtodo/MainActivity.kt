package com.example.randomizedtodo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.randomizedtodo.databinding.ActivityMainBinding
import com.example.randomizedtodo.model.ModelLoader
import com.example.randomizedtodo.model.version_3.Model
import com.example.randomizedtodo.ui.groups.GroupsViewModel
import com.example.randomizedtodo.ui.schedules.ScheduleEditViewModel
import com.example.randomizedtodo.ui.schedules.SchedulesViewModel
import com.example.randomizedtodo.ui.taskList.TaskListViewModel
import com.example.randomizedtodo.ui.tasks.TaskEditViewModel
import com.example.randomizedtodo.ui.tasks.TasksViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private lateinit var model: Model

    private val taskListViewModel: TaskListViewModel by viewModels()
    private val tasksViewModel: TasksViewModel by viewModels()
    private val taskEditViewModel: TaskEditViewModel by viewModels()
    private val groupsViewModel: GroupsViewModel by viewModels()
    private val schedulesViewModel: SchedulesViewModel by viewModels()
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val modelLoader = ModelLoader(filesDir)

        model = modelLoader.load()
        taskListViewModel.init(model)
        tasksViewModel.init(model)
        taskEditViewModel.init(model)
        groupsViewModel.init(model)
        schedulesViewModel.init(model)
        scheduleEditViewModel.init(model)

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

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        model.checkPeriodOverflow()
        model.save()
    }

    override fun onResume() {
        super.onResume()
        model.load()
        model.checkPeriodOverflow()

        tasksViewModel.refresh()
        taskListViewModel.refresh()
    }
}