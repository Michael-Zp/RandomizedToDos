package com.example.randomizedtodo.ui.tasks

import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.Model
import com.example.randomizedtodo.model.Task

class TasksViewModel() : ViewModel() {
    val taskNames: ArrayList<String> = ArrayList()

    lateinit var model: Model

    fun init(model: Model) {
        this.model = model
        refresh()
    }

    fun getByIdx(idx: Int): Task {
        return this.model.tasks[idx]
    }

    fun add(newTask: Task) {
        model.tasks.add(newTask)
        taskNames.add(newTask.name)
        model.publishTasksUpdate()
    }

    fun refresh() {
        taskNames.clear()
        taskNames.addAll(model.tasks.map { it.name })
    }
}