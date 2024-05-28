package com.example.randomizedtodo.model.version_1


import androidx.lifecycle.MutableLiveData
import com.example.randomizedtodo.model.BaseModel
import com.example.randomizedtodo.model.ID
import com.example.randomizedtodo.model.ModelLoader
import com.example.randomizedtodo.model.version_2.Model
import java.util.Calendar

@Suppress("DEPRECATION")
@Deprecated("Model version is deprecated, use new version instead.")
class Model(
    val tasks: ArrayList<Task>,
    val groups: ArrayList<Group>,
    val schedules: ArrayList<Schedule>,
    modelLoader: ModelLoader) : BaseModel<Model>(saveFilePath, modelLoader) {

    companion object{
        @JvmStatic
        val saveFilePath: String = "model.json"
    }

    val lastUpdateByPeriod: HashMap<Period, Int> = HashMap()

    init {
        Period.entries.forEach { lastUpdateByPeriod[it] = -1 }
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
        val newGroupsList: ArrayList<com.example.randomizedtodo.model.version_2.Group> = ArrayList()
        groups.forEach {
            newGroupsList.add(com.example.randomizedtodo.model.version_2.Group(it.name))
        }

        val newSchedulesList: ArrayList<com.example.randomizedtodo.model.version_2.Schedule> = ArrayList()
        schedules.forEach {
            newSchedulesList.add(
                com.example.randomizedtodo.model.version_2.Schedule(
                    it.name,
                    it.numberTimes,
                    ConvertPeriod(it.repeatEvery),
                    it.maximumRepetitionsPerPeriod,
                    ConvertPeriod(it.maximumRepetitionsPerPeriodPeriod)
                )
            )
        }

        val newTaskList: ArrayList<com.example.randomizedtodo.model.version_2.Task> = ArrayList()
        tasks.forEach { task ->
            val newTask: com.example.randomizedtodo.model.version_2.Task =
                com.example.randomizedtodo.model.version_2.Task(task.name, null, null)

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

            newTask.scheduleId = scheduleId
            newTaskList.add(newTask)
        }

        val newLastUpdateMap: HashMap<com.example.randomizedtodo.model.version_2.Period, Int> = HashMap()
        // No idea how this can be null, but GSON is doing stuff when saving it wrong
        if (lastUpdateByPeriod != null) {
            lastUpdateByPeriod.forEach { (period, i) ->
                newLastUpdateMap[ConvertPeriod(period)!!] = i
            }
        }
        else {
            com.example.randomizedtodo.model.version_2.Period.entries.forEach {
                newLastUpdateMap[it] = -1
            }
        }

        return Model(newTaskList, newGroupsList, newSchedulesList, newLastUpdateMap, newModelLoader)
    }

    @Deprecated("Removed implementation, because model loader can't load this model anymore.",
        ReplaceWith("version_2.Model")
    )
    override fun load() {
        throw NotImplementedError("Removed implementation, because model loader can't load this model anymore.")
    }

    private fun ConvertPeriod(old: com.example.randomizedtodo.model.version_1.Period?) : com.example.randomizedtodo.model.version_2.Period? {
        var newPeriod: com.example.randomizedtodo.model.version_2.Period? = null

        if (old != null) {
            com.example.randomizedtodo.model.version_2.Period.entries.forEach {
                if (it.name == old.name) {
                    newPeriod = it
                }
            }
        }

        return newPeriod
    }
}