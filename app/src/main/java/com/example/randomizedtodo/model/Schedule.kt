package com.example.randomizedtodo.model

class Schedule(public val name: String,
               public val numberTimes: Int,
               public val repeatEvery: Period?,
               public val maximumRepetitions: Int?,
               public val maximumRepetitionsPerPeriod: Period?) {
}