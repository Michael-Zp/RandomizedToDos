package com.example.randomizedtodo.ui.groups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.Model
import com.example.randomizedtodo.model.Task

class GroupsViewModel : ViewModel() {
    val groupNames: ArrayList<String> = ArrayList<String>()
    val updater: MutableLiveData<Int> = MutableLiveData(0)

    private lateinit var model: Model

    public fun init(model: Model) {
        this.model = model
        refresh()
    }

    public fun add(newTask: Task) {
        model.tasks.add(newTask)
        groupNames.add(newTask.name)
        publishEntryUpdate()
    }

    public fun removeAt(idx: Int) {
        model.tasks.removeAt(idx)
        groupNames.removeAt(idx)
        publishEntryUpdate()
    }

    public fun refresh() {
        groupNames.clear()
        groupNames.addAll(model.schedules.map { it.name })
        publishEntryUpdate()
    }

    private fun publishEntryUpdate() {
        updater.value = updater.value!! + 1
    }
}