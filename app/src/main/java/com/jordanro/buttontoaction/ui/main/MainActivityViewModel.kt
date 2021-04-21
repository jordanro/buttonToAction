package com.jordanro.buttontoaction.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.jordanro.buttontoaction.data.entities.Action
import com.jordanro.buttontoaction.data.entities.ActionState
import com.jordanro.buttontoaction.repositories.ActionsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel  @ViewModelInject constructor(
    private val repository: ActionsRepository
) : ViewModel() {

    val currentAction : LiveData<Action> = liveData {
       emitSource(repository.getCurrentAction().asLiveData())
    }

    fun onActionDone(type: Action.Type) {
        CoroutineScope(Dispatchers.Main).launch {
            repository.updateActionState(ActionState(type, System.currentTimeMillis()))
        }
    }
}