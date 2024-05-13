package com.example.randomizedtodo.model

class Schedule(public val name: String,
               public val numberTimes: Int,
               public val repeatEvery: Period?,
               public val maximumRepetitionsPerPeriod: Int?,
               public val maximumRepetitionsPerPeriodPeriod: Period?) {
}