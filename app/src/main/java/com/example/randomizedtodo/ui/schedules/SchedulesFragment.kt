package com.example.randomizedtodo.ui.schedules

import android.app.Activity
import android.content.Context
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
import androidx.navigation.fragment.findNavController
import com.example.randomizedtodo.R
import com.example.randomizedtodo.databinding.FragmentSchedulesBinding

class SchedulesFragment : Fragment(), MenuProvider {

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

        schedulesViewModel.model.schedulesObservable.observe(viewLifecycleOwner) { _ ->
            binding.listTasks.adapter = ArrayAdapter(activity as Activity, android.R.layout.simple_list_item_1, schedulesViewModel.scheduleNames)
        }

        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.menu_add) {
            findNavController().navigate(R.id.nav_add_schedule)
            return true
        }
        return false
    }
}