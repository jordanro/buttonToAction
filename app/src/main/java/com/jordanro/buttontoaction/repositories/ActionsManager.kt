package com.jordanro.buttontoaction.repositories

import com.jordanro.buttontoaction.data.entities.Action
import com.jordanro.buttontoaction.data.entities.ActionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface ActionsManager {

    var currentAction : MutableStateFlow<Action>

    var actions :List<Action>?

    fun updateActionState(actionStates_: List<ActionState>)
    fun getCurrentAction() : Flow<Action>
}