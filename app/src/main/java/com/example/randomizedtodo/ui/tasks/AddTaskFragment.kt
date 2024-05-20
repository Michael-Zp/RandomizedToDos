package com.example.randomizedtodo.ui.tasks

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.randomizedtodo.databinding.FragmentAddTaskBinding
import com.example.randomizedtodo.model.version_2.Group
import com.example.randomizedtodo.model.version_2.Schedule
import com.example.randomizedtodo.model.version_2.Task
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
            val name: String = binding.edtTaskName.text.toString()

            val schedule: Schedule? = if (binding.spinSchedule.selectedItemPosition != 0) {
                schedulesViewModel.getByIdx(binding.spinSchedule.selectedItemPosition - 1)
            } else {
                null
            }

            val group: Group? = if (binding.spinGroup.selectedItemPosition != 0) {
                groupsViewModel.getByIdx(binding.spinGroup.selectedItemPosition - 1)
            } else {
                null
            }

            if (name.isNotEmpty())
            {
                tasksViewModel.add(Task(name, group?.ID, schedule?.ID))
                activity?.onBackPressed()
            }
            else
            {
                binding.lblTaskName.setTextColor(Color.RED)
            }
        }

        val lblTaskNameOriginalColor = binding.lblTaskName.currentTextColor
        val resetTextColors: () -> Unit = {
            binding.lblTaskName.setTextColor(lblTaskNameOriginalColor)
        }

        binding.edtTaskName.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { resetTextColors() } }

        Utils.LinkListToViewModel<SchedulesViewModel>(
            activity as Activity,
            {
                val list: ArrayList<String> = ArrayList()
                list.add("-Not selected-")
                list.addAll(schedulesViewModel.scheduleNames)
                list
            },
            { it -> binding.spinSchedule.adapter = it },
            { schedulesViewModel.model.schedulesObservable },
            viewLifecycleOwner)
        Utils.LinkListToViewModel<GroupsViewModel>(
            activity as Activity,
            {
                val list: ArrayList<String> = ArrayList()
                list.add("-Not selected-")
                list.addAll(groupsViewModel.groupNames)
                list
            },
            { it -> binding.spinGroup.adapter = it },
            { tasksViewModel.model.tasksObservable },
            viewLifecycleOwner)

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}