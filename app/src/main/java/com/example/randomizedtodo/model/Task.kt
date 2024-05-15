package com.example.randomizedtodo.model

import java.util.EnumMap

class Task(var name: String, var group: Group?, var schedule: Schedule?) {

    var absoluteCompletions: Int = 0
    val taskCompletionsInPeriod: EnumMap<Period, Int> = EnumMap(Period::class.java)

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