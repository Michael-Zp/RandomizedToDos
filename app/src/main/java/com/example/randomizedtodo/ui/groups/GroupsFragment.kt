package com.example.randomizedtodo.ui.groups

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.randomizedtodo.databinding.FragmentGroupsBinding

class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {val groupsViewModel: GroupsViewModel by activityViewModels()

        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView: ListView = binding.listGroups

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(activity as Activity, android.R.layout.simple_list_item_1,groupsViewModel.groupNames)
        listView.adapter = arrayAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, groupsViewModel.groupNames[position], Toast.LENGTH_SHORT).show()
        }

        groupsViewModel.updater.observe(viewLifecycleOwner) { _ ->
            listView.adapter = ArrayAdapter(activity as Activity, android.R.layout.simple_list_item_1, groupsViewModel.groupNames)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}