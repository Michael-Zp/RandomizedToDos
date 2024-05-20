package com.example.randomizedtodo.model

import com.google.gson.Gson
import java.io.File

@Suppress("DEPRECATION")
class ModelLoader(private val filesDir: File) {
    private val loadEmptyModel: Boolean = false

    private fun getSaveFile(modelPart: String): File {
        return File(filesDir, modelPart)
    }

    fun <TNext> save(model: BaseModel<TNext>) {
        val file = getSaveFile(model.filePath)
        if (loadEmptyModel)
        {
            return
        }

        if (!file.exists()) {
            val parent = File(file.parent!!)
            if (parent.exists()) {
                parent.delete()
            }
            File(file.parent!!).mkdirs()
            file.createNewFile()
        }

        val json = Gson().toJson(model)
        file.writeText(json)
    }

    fun load(): com.example.randomizedtodo.model.version_2.Model {
        val modelInstance = com.example.randomizedtodo.model.version_2.Model(ArrayList(), ArrayList(), ArrayList(), this)

        if (loadEmptyModel)
        {
            return modelInstance
        }

        var loadedModel: com.example.randomizedtodo.model.version_2.Model? = null

        val saveFileVersion1 = getSaveFile(com.example.randomizedtodo.model.version_1.Model.saveFilePath)
        val saveFileVersion2 = getSaveFile(com.example.randomizedtodo.model.version_2.Model.saveFilePath)

        if (saveFileVersion2.exists())
        {
            loadedModel = Gson().fromJson(saveFileVersion2.readText(), com.example.randomizedtodo.model.version_2.Model::class.java)
        }
        else if (saveFileVersion1.exists())
        {
            val content = saveFileVersion1.readText()
            val model1 = Gson().fromJson(content, com.example.randomizedtodo.model.version_1.Model::class.java)
            loadedModel = model1.convertToNextModel(this)
        }
        else
        {
            loadedModel = com.example.randomizedtodo.model.version_2.Model(ArrayList(), ArrayList(), ArrayList(), this)
        }

        modelInstance.tasks.clear()
        modelInstance.groups.clear()
        modelInstance.schedules.clear()
        modelInstance.lastUpdateByPeriod.clear()

        modelInstance.tasks.addAll(loadedModel!!.tasks)
        modelInstance.groups.addAll(loadedModel.groups)
        modelInstance.schedules.addAll(loadedModel.schedules)

        // Is only != null if it was present in the last save file. Otherwise it is actually null
        if (loadedModel!!.lastUpdateByPeriod != null || loadedModel.lastUpdateByPeriod.isEmpty()) {
            com.example.randomizedtodo.model.version_2.Period.entries.forEach {
                if (loadedModel.lastUpdateByPeriod.containsKey(it)) {
                    modelInstance.lastUpdateByPeriod[it] = loadedModel.lastUpdateByPeriod[it]!!
                } else {
                    modelInstance.lastUpdateByPeriod[it] = -1
                }
            }
        }

        save(modelInstance)
        return modelInstance
    }
}