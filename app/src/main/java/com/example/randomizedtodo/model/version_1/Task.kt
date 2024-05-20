package com.example.randomizedtodo.model.version_1

@Suppress("DEPRECATION")
@Deprecated("Model version is deprecated, use new version instead.")
class Task(var name: String, var group: Group?, var schedule: Schedule?) {

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