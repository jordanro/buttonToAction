package com.jordanro.buttontoaction.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jordanro.buttontoaction.data.entities.Action
import com.jordanro.buttontoaction.data.entities.ActionState

@Dao
interface ActionsDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveActions(actions: List<Action>)

    @Query("SELECT * FROM actions")
    fun getActions() : List<Action>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateActionState(actionState: ActionState)

    @Query("SELECT * FROM actions_state")
    suspend fun getActionsState(): List<ActionState>

}