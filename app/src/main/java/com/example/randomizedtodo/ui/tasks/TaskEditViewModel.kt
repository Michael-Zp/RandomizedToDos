package com.example.randomizedtodo.ui.tasks

import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.Model
import com.example.randomizedtodo.model.Task

class TaskEditViewModel() : ViewModel() {
    var selectedTask: Task? = null

    lateinit var model: Model

    fun init(model: Model) {
        this.model = model
    }

    fun deleteSelectedTask() {
        this.model.tasks.remove(selectedTask)
        selectedTask = null
        model.publishTasksUpdate()
    }
}