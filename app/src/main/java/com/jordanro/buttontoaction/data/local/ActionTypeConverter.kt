package com.jordanro.buttontoaction.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jordanro.buttontoaction.data.entities.Action
import java.lang.reflect.Type


class ActionTypeConverter {

    @TypeConverter
    fun toString(type: Action.Type): String{
        return type.name
    }

    @TypeConverter
    fun fromString(type: String): Action.Type{
        return Action.Type.valueOf(type)
    }

}