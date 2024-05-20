package com.example.randomizedtodo.ui.taskList

import androidx.lifecycle.ViewModel
import com.example.randomizedtodo.model.version_2.Model
import com.example.randomizedtodo.model.version_2.Task

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

        model.tasks.forEach { task ->
            val timesAdded: Int

            if (task.scheduleId == null)
            {
                timesAdded = 1 - task.absoluteCompletions
            }
            else
            {
                val schedule = model.schedules.find { task.scheduleId == it.ID }

                if (schedule != null)
                {
                    val hasRepeatEveryPart = schedule.repeatEvery != null
                    val hasMaxRepeatInPeriodPart = schedule.maximumRepetitionsPerPeriod != null && schedule.maximumRepetitionsPerPeriodPeriod != null

                    if (!hasRepeatEveryPart && !hasMaxRepeatInPeriodPart)
                    {
                        timesAdded = schedule.numberTimes - task.absoluteCompletions
                    }
                    else if(hasRepeatEveryPart && !hasMaxRepeatInPeriodPart)
                    {
                        timesAdded = schedule.numberTimes - (task.taskCompletionsInPeriod.getOrPut(schedule.repeatEvery!!) { -> 0 })
                    }
                    else if(!hasRepeatEveryPart) // && hasMaxRepeatInPeriodPart
                    {
                        val completionsThisPeriod = (task.taskCompletionsInPeriod.getOrPut(schedule.maximumRepetitionsPerPeriodPeriod!!) { -> 0 })
                        val maxLeftCompletionsThisPeriod = schedule.maximumRepetitionsPerPeriod!! - completionsThisPeriod
                        val absoluteCompletionsLeft = schedule.numberTimes - task.absoluteCompletions
                        timesAdded = minOf(maxLeftCompletionsThisPeriod, absoluteCompletionsLeft)
                    }
                    else // hasRepeatEveryPart && hasMaxRepeatInPeriodPart
                    {
                        val completionsThisPeriod = (task.taskCompletionsInPeriod.getOrPut(schedule.maximumRepetitionsPerPeriodPeriod!!) { -> 0 })
                        val maxLeftCompletionsThisPeriod = schedule.maximumRepetitionsPerPeriod!! - completionsThisPeriod
                        val completionsLeft = schedule.numberTimes - (task.taskCompletionsInPeriod.getOrPut(schedule.repeatEvery!!) { -> 0 })
                        timesAdded = minOf(maxLeftCompletionsThisPeriod, completionsLeft)
                    }
                }
                else
                {
                    timesAdded = 0
                }
            }

            for (i in 1..timesAdded)
            {
                taskNames.add(task.name)
                idxToTaskMap.add(task)
            }
        }

        model.publishTasksUpdate()
    }
}