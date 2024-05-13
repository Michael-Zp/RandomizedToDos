package com.example.randomizedtodo.ui.schedules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.Model
import com.example.randomizedtodo.model.Task

class SchedulesViewModel : ViewModel() {
    val scheduleNames: ArrayList<String> = ArrayList<String>()
    val updater: MutableLiveData<Int> = MutableLiveData(0)

    private lateinit var model: Model

    public fun init(model: Model) {
        this.model = model
        refresh()
    }

    public fun add(newTask: Task) {
        model.tasks.add(newTask)
        scheduleNames.add(newTask.name)
        publishEntryUpdate()
    }

    public fun removeAt(idx: Int) {
        model.tasks.removeAt(idx)
        scheduleNames.removeAt(idx)
        publishEntryUpdate()
    }

    public fun refresh() {
        scheduleNames.clear()
        scheduleNames.addAll(model.schedules.map { it.name })
        publishEntryUpdate()
    }

    private fun publishEntryUpdate() {
        updater.value = updater.value!! + 1
    }
}