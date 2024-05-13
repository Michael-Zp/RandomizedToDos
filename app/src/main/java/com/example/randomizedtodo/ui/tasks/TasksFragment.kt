package com.example.randomizedtodo.ui.tasks

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.randomizedtodo.databinding.FragmentTasksBinding
import kotlin.random.Random

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tasksViewModel: TasksViewModel by activityViewModels()

        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView: ListView = binding.listTasks

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(activity as Activity, android.R.layout.simple_list_item_1, tasksViewModel.taskNames)
        listView.adapter = arrayAdapter


        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, tasksViewModel.taskNames[position], Toast.LENGTH_SHORT).show()
        }

        tasksViewModel.updater.observe(viewLifecycleOwner) { _ ->
            listView.adapter = ArrayAdapter(activity as Activity, android.R.layout.simple_list_item_1, tasksViewModel.taskNames)
        }

        val btnGenerate: Button = binding.button

        val rng: Random = Random

        btnGenerate.setOnClickListener {
            if (tasksViewModel.taskNames.isNotEmpty())
            {
                val idx = rng.nextInt(0, tasksViewModel.taskNames.count())
                Toast.makeText(activity, tasksViewModel.taskNames[idx], Toast.LENGTH_SHORT).show()
                tasksViewModel.removeAt(idx)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}