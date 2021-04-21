package com.jordanro.buttontoaction.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "actions")
data class Action(@PrimaryKey val type: Type,
                  val enabled: Boolean = true,
                  val priority: Int = 0,
                  @SerializedName("valid_days") val validDays : List<Int> = listOf(),
                  @SerializedName("cool_down")val coolDownPeriod: Long = 0){

    enum class Type{
        disabled,animation,toast,call,notification
    }
}