package com.example.randomizedtodo.ui.tasks

import android.R
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.randomizedtodo.databinding.FragmentAddTaskBinding
import com.example.randomizedtodo.model.Task
import com.example.randomizedtodo.ui.groups.GroupsViewModel
import com.example.randomizedtodo.ui.helpers.Utils
import com.example.randomizedtodo.ui.schedules.SchedulesViewModel

class AddTaskFragment : Fragment() {
    private var _binding: FragmentAddTaskBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tasksViewModel: TasksViewModel by activityViewModels()
        val schedulesViewModel: SchedulesViewModel by activityViewModels()
        val groupsViewModel: GroupsViewModel by activityViewModels()

        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnCloseTask.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnFinalizeTask.setOnClickListener {
            val newEntry: String = binding.edtTaskName.text.toString()

            if (newEntry != "")
            {
                tasksViewModel.add(Task(newEntry, null, null, ArrayList()))
            }
            activity?.onBackPressed()
        }

//        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(activity as Activity, android.R.layout.simple_spinner_item, groupsViewModel.groupNames)
//        binding.spinGroup.adapter = arrayAdapter
//        tasksViewModel.updater.observe(viewLifecycleOwner) { _ ->
//            binding.spinGroup.adapter = ArrayAdapter(activity as Activity, android.R.layout.simple_spinner_item, groupsViewModel.groupNames)
//        }

        Utils.LinkListToViewModel<SchedulesViewModel>(activity as Activity, { schedulesViewModel.scheduleNames }, { it -> binding.spinSchedule.adapter = it }, { schedulesViewModel.updater }, viewLifecycleOwner)
        Utils.LinkListToViewModel<GroupsViewModel>(activity as Activity, { groupsViewModel.groupNames }, { it -> binding.spinGroup.adapter = it }, { tasksViewModel.updater }, viewLifecycleOwner)

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}