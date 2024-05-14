package com.example.randomizedtodo.model

class Schedule(var name: String,
               var numberTimes: Int,
               var repeatEvery: Period?,
               var maximumRepetitionsPerPeriod: Int?,
               var maximumRepetitionsPerPeriodPeriod: Period?) {
}