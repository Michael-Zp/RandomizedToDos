package com.example.randomizedtodo.ui.taskList

import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.Model
import com.example.randomizedtodo.model.Task

class TaskListViewModel() : ViewModel() {
    val taskNames: ArrayList<String> = ArrayList()
    private val idxToTaskMap: ArrayList<Task> = ArrayList()

    lateinit var model: Model

    fun init(model: Model) {
        this.model = model
        refresh()
    }

    fun finishedIdx(idx: Int) {
        idxToTaskMap[idx].addCompletion()
        refresh()
    }

    fun refresh() {
        taskNames.clear()
        idxToTaskMap.clear()

        model.tasks.forEach {
            val timesAdded: Int

            if (it.schedule == null)
            {
                timesAdded = 1 - it.absoluteCompletions
            }
            else
            {
                val hasRepeatEveryPart = it.schedule.repeatEvery != null
                val hasMaxRepeatInPeriodPart = it.schedule.maximumRepetitionsPerPeriod != null && it.schedule.maximumRepetitionsPerPeriodPeriod != null

                if (!hasRepeatEveryPart && !hasMaxRepeatInPeriodPart)
                {
                    timesAdded = it.schedule.numberTimes - it.absoluteCompletions
                }
                else if(hasRepeatEveryPart && !hasMaxRepeatInPeriodPart)
                {
                    timesAdded = it.schedule.numberTimes - (it.taskCompletionsInPeriod.getOrPut(it.schedule.repeatEvery!!) { -> 0 })
                }
                else if(!hasRepeatEveryPart) // && hasMaxRepeatInPeriodPart
                {
                    val completionsThisPeriod = (it.taskCompletionsInPeriod.getOrPut(it.schedule.maximumRepetitionsPerPeriodPeriod!!) { -> 0 })
                    val maxLeftCompletionsThisPeriod = it.schedule.maximumRepetitionsPerPeriod!! - completionsThisPeriod
                    val absoluteCompletionsLeft = it.schedule.numberTimes - it.absoluteCompletions
                    timesAdded = minOf(maxLeftCompletionsThisPeriod, absoluteCompletionsLeft)
                }
                else // hasRepeatEveryPart && hasMaxRepeatInPeriodPart
                {
                    val completionsThisPeriod = (it.taskCompletionsInPeriod.getOrPut(it.schedule.maximumRepetitionsPerPeriodPeriod!!) { -> 0 })
                    val maxLeftCompletionsThisPeriod = it.schedule.maximumRepetitionsPerPeriod!! - completionsThisPeriod
                    val completionsLeft = it.schedule.numberTimes - (it.taskCompletionsInPeriod.getOrPut(it.schedule.repeatEvery!!) { -> 0 })
                    timesAdded = minOf(maxLeftCompletionsThisPeriod, completionsLeft)
                }
            }

            for (i in 1..timesAdded)
            {
                taskNames.add(it.name)
                idxToTaskMap.add(it)
            }
        }

        model.publishTasksUpdate()
    }
}