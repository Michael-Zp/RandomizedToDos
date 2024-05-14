package com.example.randomizedtodo.model

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import java.io.File

class Model(val tasks: ArrayList<Task>, val groups: ArrayList<Group>, val schedules: ArrayList<Schedule>, private val filesDir: File) {

    val tasksObservable: MutableLiveData<Int> = MutableLiveData(0)
    val groupsObservable: MutableLiveData<Int> = MutableLiveData(0)
    val schedulesObservable: MutableLiveData<Int> = MutableLiveData(0)

    fun publishTasksUpdate() {
        tasksObservable.value = tasksObservable.value!! + 1
    }

    fun publishGroupsUpdate() {
        groupsObservable.value = groupsObservable.value!! + 1
    }

    fun publishSchedulesUpdate() {
        schedulesObservable.value = schedulesObservable.value!! + 1
    }


    private val yeetOldTasks: Boolean = true

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

            tasks.addAll(entries.tasks)
            groups.addAll(entries.groups)
            schedules.addAll(entries.schedules)
        }
    }
}