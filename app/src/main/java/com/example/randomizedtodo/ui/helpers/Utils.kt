package com.example.randomizedtodo.ui.helpers

import android.app.Activity
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

class Utils {
    companion object {
        @JvmStatic
        fun<T> LinkListToViewModel(
            activity: Activity,
            getBackList: () -> ArrayList<String>,
            setAdapter: (ArrayAdapter<String>) -> Unit,
            getUpdater: () -> MutableLiveData<Int>,
            viewLifecycleOwner: LifecycleOwner
        ) {
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(activity, android.R.layout.simple_spinner_item, getBackList())
            setAdapter(arrayAdapter)
            getUpdater().observe(viewLifecycleOwner) { _ ->
                setAdapter(ArrayAdapter(activity,android.R.layout.simple_spinner_item, getBackList()))
            }
        }

        @JvmStatic
        fun CheckPeriods() {

        }
    }
}