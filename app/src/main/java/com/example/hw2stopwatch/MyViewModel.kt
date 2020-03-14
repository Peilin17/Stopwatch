package com.example.hw2stopwatch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel() : ViewModel() {

    //equivalent of 'public static float ...'
    companion object{
        val recordsList = ArrayList<String>()
        var isRun = false
        var stopTime = MutableLiveData<Long>()
        var base = MutableLiveData<Long>()

    }
    fun addRecord(s: String)
    {
        recordsList.add(s)
    }
    fun getlist(): ArrayList<String> {
        return recordsList
    }
    fun saveStatus(r: Boolean, t: Long)
    {
        isRun = r
        stopTime.value = t
    }
    fun getRun(): Boolean {
        return isRun
    }
    fun getST(): MutableLiveData<Long> {
        return stopTime
    }
    fun getBase(): MutableLiveData<Long> {
        return base
    }
    fun setBase(b : Long)
    {
        base.value = b
    }
    fun clear()
    {
        recordsList.clear()
    }





}