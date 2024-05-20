package com.example.randomizedtodo.model.version_1

@Suppress("DEPRECATION")
@Deprecated("Model version is deprecated, use new version instead.")
class Schedule(var name: String,
               var numberTimes: Int,
               var repeatEvery: Period?,
               var maximumRepetitionsPerPeriod: Int?,
               var maximumRepetitionsPerPeriodPeriod: Period?) {
}