package com.example.randomizedtodo.ui.schedules

import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.Model
import com.example.randomizedtodo.model.Schedule

class ScheduleEditViewModel : ViewModel() {
    var selectedSchedule: Schedule? = null

    lateinit var model: Model

    fun init(model: Model) {
        this.model = model
    }

    fun deleteSelectedSchedule() {
        this.model.schedules.remove(selectedSchedule)
        selectedSchedule = null
        model.publishSchedulesUpdate()
    }
}