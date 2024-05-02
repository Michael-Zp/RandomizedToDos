package com.example.randomizedtodo.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.Model
import com.example.randomizedtodo.model.Task

class TasksViewModel() : ViewModel() {
    val taskNames: ArrayList<String> = ArrayList()
    val updater: MutableLiveData<Int> = MutableLiveData(0)

    private lateinit var model: Model

    public fun init(model: Model) {
        this.model = model
        refresh()
    }

    public fun add(newTask: Task) {
        model.tasks.add(newTask)
        taskNames.add(newTask.name)
        publishEntryUpdate()
    }

    public fun removeAt(idx: Int) {
        model.tasks.removeAt(idx)
        taskNames.removeAt(idx)
        publishEntryUpdate()
    }

    public fun refresh() {
        taskNames.clear()
        taskNames.addAll(model.tasks.map { it.name })

        publishEntryUpdate()
    }

    private fun publishEntryUpdate() {
        updater.value = updater.value!! + 1
    }
}