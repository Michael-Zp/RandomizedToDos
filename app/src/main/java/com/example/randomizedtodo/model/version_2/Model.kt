package com.example.randomizedtodo.model.version_2

import androidx.lifecycle.MutableLiveData
import com.example.randomizedtodo.model.BaseModel
import com.example.randomizedtodo.model.ModelLoader
import java.util.Calendar

class Model(
    val tasks: ArrayList<Task>,
    val groups: ArrayList<Group>,
    val schedules: ArrayList<Schedule>,
    modelLoader: ModelLoader) : BaseModel<Model>(saveFilePath, modelLoader) {

    companion object{
        @JvmStatic
        val saveFilePath: String = "version_2/model.json"
    }

    var lastUpdateByPeriod: HashMap<Period, Int> = HashMap()

    init {
        Period.entries.forEach { lastUpdateByPeriod[it] = -1 }
    }

    constructor(tasks: ArrayList<Task>,
                groups: ArrayList<Group>,
                schedules: ArrayList<Schedule>,
                lastUpdateByPeriod: HashMap<Period, Int>?,
                modelLoader: ModelLoader) : this(tasks, groups, schedules, modelLoader) {
        if (lastUpdateByPeriod == null) {
            this.lastUpdateByPeriod = HashMap()
            Period.entries.forEach { this.lastUpdateByPeriod[it] = -1 }
        } else {
            lastUpdateByPeriod.forEach { this.lastUpdateByPeriod[it.key] = it.value }
        }
        checkPeriodOverflow()
    }

    @Transient var tasksObservable: MutableLiveData<Int> = MutableLiveData(0)

    @Transient var groupsObservable: MutableLiveData<Int> = MutableLiveData(0)

    @Transient var schedulesObservable: MutableLiveData<Int> = MutableLiveData(0)

    fun publishTasksUpdate() {
        tasksObservable.value = tasksObservable.value!! + 1
        checkPeriodOverflow()
        modelLoader.save(this)
    }

    fun publishGroupsUpdate() {
        groupsObservable.value = groupsObservable.value!! + 1
        checkPeriodOverflow()
        modelLoader.save(this)
    }

    fun publishSchedulesUpdate() {
        schedulesObservable.value = schedulesObservable.value!! + 1
        checkPeriodOverflow()
        modelLoader.save(this)
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

    override fun convertToNextModel(newModelLoader: ModelLoader): Model {
        throw NotImplementedError("Conversion to next model not implemented yet.")
    }

    override fun load() {
        val loadedModel: Model = modelLoader.load()

        publishTasksUpdate()
        publishSchedulesUpdate()
        publishGroupsUpdate()
    }
}