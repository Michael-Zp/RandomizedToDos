package com.example.randomizedtodo.model

import java.util.Date

public class Task(public val name: String, public val group: Group?, public val schedule: Schedule?, public val taskCompletionsOn: ArrayList<Date>) {

}