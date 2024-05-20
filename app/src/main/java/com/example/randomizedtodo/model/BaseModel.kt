package com.example.randomizedtodo.model

abstract class BaseModel<TNext>(val filePath: String) {
    companion object {

        @JvmStatic
        private val knownPaths: HashSet<String> = HashSet()
    }

    init {
        if (knownPaths.contains(filePath)) {
            throw Exception(buildString {
                append("Two models should never have the same save path, as they will generate collisions. Colliding path: '")
                append(filePath)
                append("'")
            })
        }

        knownPaths.add(filePath)
    }

    abstract fun convertToNextModel(): TNext
}