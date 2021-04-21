package com.jordanro.buttontoaction.repositories

import com.jordanro.buttontoaction.data.entities.Action
import com.jordanro.buttontoaction.data.entities.ActionState
import com.jordanro.buttontoaction.data.local.ActionsDao
import com.jordanro.buttontoaction.data.remote.ActionsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActionsRepository @Inject constructor(private val remoteDataSource:ActionsService,private val localDataSource: ActionsDao,private val actionsManager :ActionsManager){

    private suspend fun loadActions() : List<Action>{
        val remoteActions = remoteDataSource.loadActions()
        localDataSource.saveActions(remoteActions)
        return remoteActions
    }

    private suspend fun loadActionStates() : List<ActionState>{
        return localDataSource.getActionsState()
    }

    suspend fun updateActionState(actionState: ActionState){
        withContext(Dispatchers.IO) {
            localDataSource.updateActionState(actionState)
            actionsManager.updateActionState(loadActionStates())
        }
    }

    suspend fun getCurrentAction() : Flow<Action> {
        return withContext(Dispatchers.IO) {
            val actions = loadActions()
            val actionStates = loadActionStates()

            actionsManager.actions = actions
            actionsManager.updateActionState(actionStates)

            val actionFlow = actionsManager.getCurrentAction()

            actionFlow
        }


    }

}

