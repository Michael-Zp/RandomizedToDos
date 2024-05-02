package com.example.randomizedtodo.model

import com.google.gson.Gson
import java.io.File

class Model(public val tasks: ArrayList<Task>, public val groups: ArrayList<Group>, public val schedules: ArrayList<Schedule>, private val filesDir: File) {

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

    public fun save() {
        val file = getSaveFile()
        if (yeetSaveFileIfYeetSet(file))
        {
            return
        }

        val json = Gson().toJson(this)
        file.writeText(json)
    }

    public fun load() {
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