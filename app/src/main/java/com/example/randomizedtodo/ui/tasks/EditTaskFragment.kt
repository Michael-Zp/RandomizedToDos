package com.example.randomizedtodo.ui.tasks

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import com.example.randomizedtodo.R
import com.example.randomizedtodo.databinding.FragmentEditTaskBinding
import com.example.randomizedtodo.model.Group
import com.example.randomizedtodo.model.Schedule
import com.example.randomizedtodo.ui.groups.GroupsViewModel
import com.example.randomizedtodo.ui.schedules.SchedulesViewModel

class EditTaskFragment : Fragment(), MenuProvider {
    private var _binding: FragmentEditTaskBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val editTasksViewModel: TaskEditViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val schedulesViewModel: SchedulesViewModel by activityViewModels()
        val groupsViewModel: GroupsViewModel by activityViewModels()

        _binding = FragmentEditTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnCancelTaskEdit.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnSaveTask.setOnClickListener {
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
                editTasksViewModel.selectedTask!!.name = name
                editTasksViewModel.selectedTask!!.schedule = schedule
                editTasksViewModel.selectedTask!!.group = group
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

        val schedulesList: ArrayList<String> = ArrayList()
        schedulesList.add("-Not Selected-")
        schedulesList.addAll(schedulesViewModel.scheduleNames)
        binding.spinSchedule.adapter = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, schedulesList)

        val groupsList: ArrayList<String> = ArrayList()
        groupsList.add("-Not Selected-")
        groupsList.addAll(groupsViewModel.groupNames)
        binding.spinGroup.adapter = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, groupsList)

        binding.edtTaskName.setText(editTasksViewModel.selectedTask!!.name)
        if (editTasksViewModel.selectedTask?.schedule != null)
        {
            binding.spinSchedule.setSelection(1 + editTasksViewModel.model.schedules.indexOf(editTasksViewModel.selectedTask?.schedule!!))
        }
        if (editTasksViewModel.selectedTask?.group != null)
        {
            binding.spinGroup.setSelection(1 + editTasksViewModel.model.groups.indexOf(editTasksViewModel.selectedTask?.group!!))
        }

        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_delete, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.menu_delete) {
            editTasksViewModel.deleteSelectedTask()
            activity?.onBackPressed()
            return true
        }
        return false
    }
}