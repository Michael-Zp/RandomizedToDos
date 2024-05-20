package com.example.randomizedtodo.model

import java.lang.reflect.Type
import kotlin.reflect.typeOf

abstract class BaseModel<TNext>(@Transient val filePath: String, @Transient val modelLoader: ModelLoader) {
    companion object {

        @JvmStatic
        private val knownPaths: HashMap<String, Type> = HashMap()
    }

    init {
        if (knownPaths.containsKey(filePath) && knownPaths[filePath] != this::class.java) {
            throw Exception(buildString {
                append("Two models should never have the same save path, as they will generate collisions. Colliding path: '")
                append(filePath)
                append("'")
            })
        }

        knownPaths[filePath] = this::class.java
    }

    abstract fun convertToNextModel(newModelLoader: ModelLoader): TNext

    fun save() {
        modelLoader.save(this)
    }

    abstract fun load()
}