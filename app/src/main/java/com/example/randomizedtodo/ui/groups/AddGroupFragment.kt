package com.example.randomizedtodo.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.randomizedtodo.databinding.FragmentAddGroupBinding
import com.example.randomizedtodo.model.version_1.Group

class AddGroupFragment : Fragment() {
    private var _binding: FragmentAddGroupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val groupsViewModel: GroupsViewModel by activityViewModels()

        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnCloseGroup.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.btnCreateGroup.setOnClickListener {
            val name: String = binding.edtGroupName.text.toString()

            if (name != "")
            {
                groupsViewModel.add(Group(name))
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