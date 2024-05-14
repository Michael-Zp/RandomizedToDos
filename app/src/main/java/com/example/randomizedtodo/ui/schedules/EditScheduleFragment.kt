package com.example.randomizedtodo.ui.schedules

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
import com.example.randomizedtodo.databinding.FragmentEditScheduleBinding
import com.example.randomizedtodo.model.Period

class EditScheduleFragment : Fragment(), MenuProvider {
    private var _binding: FragmentEditScheduleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val scheduleEditViewModel: ScheduleEditViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditScheduleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnCancelScheduleEdit.setOnClickListener {
            activity?.onBackPressed()
        }

        val spinnerIdxToPeriodTable: HashMap<Int, Period> = HashMap()
        val periodList: ArrayList<String> = ArrayList()

        periodList.add("-Not Selected-")

        for (i in 0..<Period.entries.size)
        {
            val period: Period = Period.entries[i]
            periodList.add(period.name)
            spinnerIdxToPeriodTable[i + 1] = period
        }

        val arrayAdapterPeriod = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1, periodList )
        binding.spinRepeatingEvery.adapter = arrayAdapterPeriod
        binding.spinMaxTimesEvery.adapter = arrayAdapterPeriod

        binding.btnSaveScheduleEdit.setOnClickListener {
            val name: String = binding.edtScheduleName.text.toString()
            var success = true
            var repeatPeriod: Period? = null
            var maxRepeatPeriod: Period? = null

            if (name.isEmpty())
            {
                binding.lblScheduleName.setTextColor(Color.RED)
                success = false
            }

            val numberTimes: Int? = binding.edtNumberTimes.text.toString().toIntOrNull()
            if (numberTimes == null)
            {
                binding.txtNumberTimes.setTextColor(Color.RED)
                success = false
            }

            if (binding.spinRepeatingEvery.selectedItemPosition != 0)
            {
                repeatPeriod = spinnerIdxToPeriodTable[binding.spinRepeatingEvery.selectedItemPosition]
            }

            val maxNumberPerPeriod: Int? = binding.edtMaxNumberTimes.text.toString().toIntOrNull()
            if (binding.spinMaxTimesEvery.selectedItemPosition != 0)
            {
                maxRepeatPeriod = spinnerIdxToPeriodTable[binding.spinMaxTimesEvery.selectedItemPosition]
            }

            if ((maxNumberPerPeriod == null) != (maxRepeatPeriod == null))
            {
                binding.txtMaxPerPeriod.setTextColor(Color.RED)
                binding.txtMaxNumberTimes.setTextColor(Color.RED)
                binding.txtMaxNumberEvery.setTextColor(Color.RED)
                success = false
            }

            if (success)
            {
                scheduleEditViewModel.selectedSchedule!!.name = name
                scheduleEditViewModel.selectedSchedule!!.numberTimes = numberTimes!!
                scheduleEditViewModel.selectedSchedule!!.repeatEvery = repeatPeriod
                scheduleEditViewModel.selectedSchedule!!.maximumRepetitionsPerPeriod = maxNumberPerPeriod
                scheduleEditViewModel.selectedSchedule!!.maximumRepetitionsPerPeriodPeriod = maxRepeatPeriod

                activity?.onBackPressed()
            }
        }

        val lblScheduleNameOriginalColor = binding.lblScheduleName.currentTextColor
        val txtNumberTimesOriginalColor = binding.txtNumberTimes.currentTextColor
        val txtMaxPerPeriodOriginalColor = binding.txtMaxPerPeriod.currentTextColor
        val txtMaxNumberTimesOriginalColor = binding.txtMaxNumberTimes.currentTextColor
        val txtMaxNumberEveryOriginalColor = binding.txtMaxNumberEvery.currentTextColor
        val resetTextColors: () -> Unit = {
            binding.lblScheduleName.setTextColor(lblScheduleNameOriginalColor)
            binding.txtNumberTimes.setTextColor(txtNumberTimesOriginalColor)
            binding.txtMaxPerPeriod.setTextColor(txtMaxPerPeriodOriginalColor)
            binding.txtMaxNumberTimes.setTextColor(txtMaxNumberTimesOriginalColor)
            binding.txtMaxNumberEvery.setTextColor(txtMaxNumberEveryOriginalColor)
        }

        binding.edtNumberTimes.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { resetTextColors() } }
        binding.spinRepeatingEvery.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { resetTextColors() } }
        binding.edtMaxNumberTimes.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { resetTextColors() } }
        binding.spinMaxTimesEvery.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { resetTextColors() } }

        binding.edtScheduleName.setText(scheduleEditViewModel.selectedSchedule!!.name)
        binding.edtNumberTimes.setText(scheduleEditViewModel.selectedSchedule!!.numberTimes.toString())
        if (scheduleEditViewModel.selectedSchedule!!.repeatEvery != null)
        {
            binding.spinRepeatingEvery.setSelection(periodList.indexOf(scheduleEditViewModel.selectedSchedule!!.repeatEvery!!.name))
        }
        if (scheduleEditViewModel.selectedSchedule!!.maximumRepetitionsPerPeriod != null && scheduleEditViewModel.selectedSchedule!!.maximumRepetitionsPerPeriodPeriod != null)
        {
            binding.edtMaxNumberTimes.setText(scheduleEditViewModel.selectedSchedule!!.maximumRepetitionsPerPeriod!!.toString())
            binding.spinMaxTimesEvery.setSelection(periodList.indexOf(scheduleEditViewModel.selectedSchedule!!.maximumRepetitionsPerPeriodPeriod!!.name))
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
            scheduleEditViewModel.deleteSelectedSchedule()
            activity?.onBackPressed()
            return true
        }
        return false
    }
}