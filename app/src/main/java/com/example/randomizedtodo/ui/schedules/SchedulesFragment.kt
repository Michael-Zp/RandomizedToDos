package com.example.randomizedtodo.ui.schedules

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.randomizedtodo.databinding.FragmentSchedulesBinding
import com.example.randomizedtodo.ui.tasks.TasksViewModel

class SchedulesFragment : Fragment() {

    private var _binding: FragmentSchedulesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val schedulesViewModel: SchedulesViewModel by activityViewModels()
        _binding = FragmentSchedulesBinding.inflate(inflater, container, false)
        binding.listTasks.adapter = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, schedulesViewModel.scheduleNames)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}