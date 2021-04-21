package com.jordanro.buttontoaction.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "actions_state")
data class ActionState(@SerializedName("action_type") @PrimaryKey val actionType: Action.Type,
                       @SerializedName("last_used_timestamp")val lastUsedTimestamp: Long){
}