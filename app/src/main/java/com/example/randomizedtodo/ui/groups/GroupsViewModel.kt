package com.example.randomizedtodo.ui.groups

import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.version_3.Group
import com.example.randomizedtodo.model.version_3.Model

class GroupsViewModel : ViewModel() {
    val groupNames: ArrayList<String> = ArrayList()

    lateinit var model: Model

    fun init(model: Model) {
        this.model = model
        refresh()
    }

    fun getByIdx(idx: Int): Group {
        return this.model.groups[idx]
    }

    fun add(newGroup: Group) {
        model.groups.add(newGroup)
        groupNames.add(newGroup.name)
        model.publishGroupsUpdate()
    }

    fun refresh() {
        groupNames.clear()
        groupNames.addAll(model.groups.map { it.name })
        model.publishGroupsUpdate()
    }
}