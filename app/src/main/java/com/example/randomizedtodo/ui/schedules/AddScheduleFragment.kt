package com.example.randomizedtodo.ui.schedules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.randomizedtodo.databinding.FragmentAddGroupBinding
import com.example.randomizedtodo.databinding.FragmentAddScheduleBinding
import com.example.randomizedtodo.model.Task
import com.example.randomizedtodo.ui.tasks.TasksViewModel
import java.util.Date

class AddScheduleFragment : Fragment() {
    private var _binding: FragmentAddScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val tasksViewModel: TasksViewModel by activityViewModels()

        _binding = FragmentAddScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnCloseSchedule.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnCreateSchedule.setOnClickListener {
            val newEntry: String = binding.edtScheduleName.text.toString()

            if (newEntry != "")
            {
                tasksViewModel.add(Task(newEntry, null, null, ArrayList()))
            }
            activity?.onBackPressed()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}