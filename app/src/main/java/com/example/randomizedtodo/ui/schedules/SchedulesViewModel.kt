package com.example.randomizedtodo.ui.schedules

import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.version_1.Model
import com.example.randomizedtodo.model.version_1.Schedule

class SchedulesViewModel : ViewModel() {
    val scheduleNames: ArrayList<String> = ArrayList()

    lateinit var model: Model

    fun init(model: Model) {
        this.model = model
        refresh()
    }

    fun getByIdx(idx: Int): Schedule {
        return this.model.schedules[idx]
    }

    fun add(newSchedule: Schedule) {
        model.schedules.add(newSchedule)
        scheduleNames.add(newSchedule.name)
        model.publishSchedulesUpdate()
    }

    fun refresh() {
        scheduleNames.clear()
        scheduleNames.addAll(model.schedules.map { it.name })
    }
}