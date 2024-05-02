package com.example.randomizedtodo

class TasksData(val tasks: ArrayList<String>) {


    override fun toString(): String {
        return "Count ${tasks.count()}"
    }


}