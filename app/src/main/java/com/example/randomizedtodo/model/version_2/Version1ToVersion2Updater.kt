@file:Suppress("DEPRECATION")

package com.example.randomizedtodo.model.version_2

import com.example.randomizedtodo.model.ID
import com.example.randomizedtodo.model.VersionUpdater

class Version1ToVersion2Updater : VersionUpdater<com.example.randomizedtodo.model.version_1.Model, Model>() {
    override fun convert(old: com.example.randomizedtodo.model.version_1.Model): Model {
        val newGroupsList: ArrayList<Group> = ArrayList()
        old.groups.forEach {
            newGroupsList.add(Group(it.name))
        }

        val newSchedulesList: ArrayList<Schedule> = ArrayList()
        old.schedules.forEach {
            newSchedulesList.add(
                Schedule(
                    it.name,
                    it.numberTimes,
                    ConvertPeriod(it.repeatEvery),
                    it.maximumRepetitionsPerPeriod,
                    ConvertPeriod(it.maximumRepetitionsPerPeriodPeriod)
                )
            )
        }

        val newTaskList: ArrayList<Task> = ArrayList()
        old.tasks.forEach { task ->
            val newTask: Task = Task(task.name, null, null)

            var groupId: ID? = null
            if (task.group != null) {
                newGroupsList.forEach { group ->
                    if (task.group!!.name == group.name) {
                        groupId = group.ID
                    }
                }
            }

            newTask.groupId = groupId

            var scheduleId: ID? = null
            if (task.schedule != null) {
                newSchedulesList.forEach { schedule ->
                    if (task.schedule!!.name == schedule.name &&
                        task.schedule!!.numberTimes == schedule.numberTimes &&
                        task.schedule!!.repeatEvery == schedule.repeatEvery &&
                        task.schedule!!.maximumRepetitionsPerPeriod == schedule.maximumRepetitionsPerPeriod &&
                        task.schedule!!.maximumRepetitionsPerPeriodPeriod == schedule.maximumRepetitionsPerPeriodPeriod) {
                        scheduleId = schedule.ID
                    }
                }
            }

            newTask.scheduleID = scheduleId
        }

        val newLastUpdateMap: HashMap<Period, Int> = HashMap()
        old.lastUpdateByPeriod.forEach { (period, i) ->
            newLastUpdateMap[ConvertPeriod(period)!!] = i
        }

        return Model(newTaskList, newGroupsList, newSchedulesList, newLastUpdateMap, old.filesDir)
    }

    private fun ConvertPeriod(old: com.example.randomizedtodo.model.version_1.Period?) : Period? {
        var newPeriod: Period? = null

        if (old != null) {
            Period.entries.forEach {
                if (it.name == old.name) {
                    newPeriod = it
                }
            }
        }

        return newPeriod
    }
}