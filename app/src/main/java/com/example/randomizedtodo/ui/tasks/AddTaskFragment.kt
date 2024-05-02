package com.example.randomizedtodo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.randomizedtodo.databinding.FragmentAddTaskBinding
import com.example.randomizedtodo.model.Task

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}