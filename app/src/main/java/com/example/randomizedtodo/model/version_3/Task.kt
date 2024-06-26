package com.example.randomizedtodo.model.version_3

import com.example.randomizedtodo.model.ID
import com.example.randomizedtodo.model.IdAble

class Task(var enabled: Boolean, var name: String, var groupId: ID?, var scheduleId: ID?) : IdAble() {

    var absoluteCompletions: Int = 0
    val taskCompletionsInPeriod: HashMap<Period, Int> = HashMap()

    init {
        Period.entries.forEach {
            taskCompletionsInPeriod[it] = 0
        }
    }

    fun addCompletion() {
        taskCompletionsInPeriod.forEach {
            taskCompletionsInPeriod[it.key] = it.value + 1
        }
        absoluteCompletions++
    }
}