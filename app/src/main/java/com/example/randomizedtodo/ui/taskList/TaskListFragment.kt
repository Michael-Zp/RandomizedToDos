package com.example.randomizedtodo.ui.taskList

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.example.randomizedtodo.R
import com.example.randomizedtodo.databinding.FragmentTaskListBinding
import kotlin.random.Random

class TaskListFragment : Fragment(), MenuProvider {

    private var _binding: FragmentTaskListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val taskListViewModel: TaskListViewModel by activityViewModels()

        val listView: ListView = binding.listTasks

        taskListViewModel.refresh()
        listView.adapter = ArrayAdapter(activity as Activity, android.R.layout.simple_list_item_1, taskListViewModel.taskNames)

        taskListViewModel.model.tasksObservable.observe(viewLifecycleOwner) { _ ->
            listView.adapter = ArrayAdapter(activity as Activity, android.R.layout.simple_list_item_1, taskListViewModel.taskNames)
        }

        taskListViewModel.model.schedulesObservable.observe(viewLifecycleOwner) { _ ->
            listView.adapter = ArrayAdapter(activity as Activity, android.R.layout.simple_list_item_1, taskListViewModel.taskNames)
        }

        val btnGenerate: Button = binding.button

        val rng: Random = Random

        btnGenerate.setOnClickListener {
            if (taskListViewModel.taskNames.isNotEmpty())
            {
                val idx = rng.nextInt(0, taskListViewModel.taskNames.count())
                Toast.makeText(activity, taskListViewModel.taskNames[idx], Toast.LENGTH_SHORT).show()
                taskListViewModel.finishedIdx(idx)
            }
        }

        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_no_plus, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}