package com.example.randomizedtodo.ui.groups

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.Group
import com.example.randomizedtodo.model.Model

class GroupsViewModel : ViewModel() {
    val groupNames: ArrayList<String> = ArrayList()
    val updater: MutableLiveData<Int> = MutableLiveData(0)

    private lateinit var model: Model

    fun init(model: Model) {
        this.model = model
        refresh()
    }

    fun add(newGroup: Group) {
        model.groups.add(newGroup)
        groupNames.add(newGroup.name)
        publishEntryUpdate()
    }

    fun removeAt(idx: Int) {
        model.tasks.removeAt(idx)
        groupNames.removeAt(idx)
        publishEntryUpdate()
    }

    fun refresh() {
        groupNames.clear()
        groupNames.addAll(model.schedules.map { it.name })
        publishEntryUpdate()
    }

    private fun publishEntryUpdate() {
        updater.value = updater.value!! + 1
    }
}