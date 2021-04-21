package com.jordanro.buttontoaction.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson

class BaseConverter{


    @TypeConverter
    fun intListToJson(value: List<Int>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToIntList(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()

}