package com.example.randomizedtodo.model.version_3

import com.example.randomizedtodo.model.IdAble

class Schedule(var name: String,
               var numberTimes: Int,
               var repeatEvery: Period?,
               var maximumRepetitionsPerPeriod: Int?,
               var maximumRepetitionsPerPeriodPeriod: Period?) : IdAble() {
}