package com.example.randomizedtodo.model.version_2

import androidx.lifecycle.MutableLiveData
import com.example.randomizedtodo.model.BaseModel
import com.google.gson.Gson
import java.io.File
import java.util.Calendar

class Model(
    val tasks: ArrayList<Task>,
    val groups: ArrayList<Group>,
    val schedules: ArrayList<Schedule>,
    private val lastUpdateByPeriod: HashMap<Period, Int>) : BaseModel("version_2/model.json") {
    
    init {
        Period.entries.forEach { lastUpdateByPeriod[it] = -1 }
    }

    @Transient val tasksObservable: MutableLiveData<Int> = MutableLiveData(0)

    @Transient val groupsObservable: MutableLiveData<Int> = MutableLiveData(0)

    @Transient val schedulesObservable: MutableLiveData<Int> = MutableLiveData(0)

    fun publishTasksUpdate() {
        tasksObservable.value = tasksObservable.value!! + 1
        checkPeriodOverflow()
        save()
    }

    fun publishGroupsUpdate() {
        groupsObservable.value = groupsObservable.value!! + 1
        checkPeriodOverflow()
        save()
    }

    fun publishSchedulesUpdate() {
        schedulesObservable.value = schedulesObservable.value!! + 1
        checkPeriodOverflow()
        save()
    }

    fun checkPeriodOverflow() {
        val calendar: Calendar = Calendar.getInstance()
        val periodsToClear: HashSet<Period> = HashSet()

        val year = calendar.get(Calendar.YEAR)
        if (lastUpdateByPeriod[Period.Year] != year) {
            lastUpdateByPeriod[Period.Year] = year
            periodsToClear.add(Period.Day)
            periodsToClear.add(Period.Week)
            periodsToClear.add(Period.Month)
            periodsToClear.add(Period.Year)
        }

        val month = calendar.get(Calendar.MONTH)
        if (lastUpdateByPeriod[Period.Month] != month) {
            lastUpdateByPeriod[Period.Month] = month
            periodsToClear.add(Period.Day)
            periodsToClear.add(Period.Week)
            periodsToClear.add(Period.Month)
        }

        val week = calendar.get(Calendar.WEEK_OF_YEAR)
        if (lastUpdateByPeriod[Period.Week] != week) {
            lastUpdateByPeriod[Period.Week] = week
            periodsToClear.add(Period.Day)
            periodsToClear.add(Period.Week)
        }

        val day = calendar.get(Calendar.DAY_OF_YEAR)
        if (lastUpdateByPeriod[Period.Day] != day) {
            lastUpdateByPeriod[Period.Day] = day
            periodsToClear.add(Period.Day)
        }

        for (period in periodsToClear) {
            tasks.forEach { it.taskCompletionsInPeriod[period] = 0 }
        }
    }


    private val yeetOldTasks: Boolean = false

    private fun yeetSaveFileIfYeetSet(saveFile: File): Boolean {
        if (yeetOldTasks)
        {
            if (saveFile.exists())
            {
                saveFile.delete()
            }

            return true
        }

        return false
    }

    private fun getSaveFile(): File {
        return File(filesDir, "model.json")
    }

    fun save() {
        val file = getSaveFile()
        if (yeetSaveFileIfYeetSet(file))
        {
            return
        }

        val json = Gson().toJson(this)
        file.writeText(json)
    }

    fun load() {
        val file = getSaveFile()
        if (yeetSaveFileIfYeetSet(file))
        {
            return
        }

        if (file.exists())
        {
            val entries = Gson().fromJson(file.readText(), Model::class.java)

            tasks.clear()
            groups.clear()
            schedules.clear()
            lastUpdateByPeriod.clear()

            tasks.addAll(entries.tasks)
            groups.addAll(entries.groups)
            schedules.addAll(entries.schedules)

            // Is only != null if it was present in the last save file. Otherwise it is actually null
            if (entries.lastUpdateByPeriod == null || entries.lastUpdateByPeriod.isEmpty()) {
                Period.entries.forEach { lastUpdateByPeriod[it] = -1 }
            } else {
                entries.lastUpdateByPeriod.forEach { lastUpdateByPeriod[it.key] = it.value }
            }

            publishTasksUpdate()
            publishSchedulesUpdate()
            publishGroupsUpdate()
        }
    }
}