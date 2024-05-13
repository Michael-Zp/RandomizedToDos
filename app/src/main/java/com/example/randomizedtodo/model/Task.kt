package com.example.randomizedtodo.model

import java.util.EnumMap

public class Task(public val name: String, public val group: Group?, public val schedule: Schedule?) {

    public var absoluteCompletions: Int = 0
    public val taskCompletionsInPeriod: EnumMap<Period, Int> = EnumMap(com.example.randomizedtodo.model.Period::class.java)

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