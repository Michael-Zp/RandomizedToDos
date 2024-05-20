package com.example.randomizedtodo.model

import com.example.randomizedtodo.model.version_1.Model
import com.example.randomizedtodo.model.version_1.Period
import com.google.gson.Gson
import java.io.File

class ModelLoader(private val filesDir: File) {

    companion object {

        @JvmStatic
        private val loadEmptyModel: Boolean = false

        @JvmStatic
        private fun getSaveFile(): File {

            return File(filesDir, "model.json")
        }

        @JvmStatic
        fun save() {
            val file = getSaveFile()
            if (loadEmptyModel)
            {
                return
            }

            val json = Gson().toJson(this)
            file.writeText(json)
        }

        @JvmStatic
        fun load() {
            val file = getSaveFile()
            if (loadEmptyModel)
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

}