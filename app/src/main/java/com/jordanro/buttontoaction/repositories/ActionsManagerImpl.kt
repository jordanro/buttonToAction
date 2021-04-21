package com.jordanro.buttontoaction.repositories

import com.jordanro.buttontoaction.data.entities.Action
import com.jordanro.buttontoaction.data.entities.ActionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ActionsManagerImpl() : ActionsManager{

    private val actionStatesMap = HashMap<Action.Type,ActionState>()

    private val disabledAction = Action(Action.Type.disabled)

    override var actions: List<Action>? = null
    var actionStates: List<ActionState>? = null

    override var currentAction: MutableStateFlow<Action> = MutableStateFlow<Action>(disabledAction)

    val handler = android.os.Handler()

    override fun getCurrentAction(): Flow<Action> {
        return currentAction
    }



    override fun updateActionState(actionStates_: List<ActionState>){
        actionStates = actionStates_
        processActionStates()
        processData()
        setNextStatusRefresh()
    }

    private fun processData(){
        val enabledActions = ArrayList<Action>()
//        val disabledActions = ArrayList<Action>() // TODO remove unused
        val weekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        actions?.forEach {
            if(isActionEnabled(it,weekDay - 1)){
                enabledActions.add(it)
            }
            else{
//                disabledActions.add(it)
            }
        }

        if(enabledActions.isEmpty()){
            currentAction.value = disabledAction
        }
        else {
            enabledActions.sortBy { it.priority }
            currentAction.value = enabledActions[0]
        }
    }

    private fun processActionStates(){

        actionStates?.forEach {
            actionStatesMap[it.actionType] = it
        }
    }

    private fun setNextStatusRefresh() {

        var nextUpdate : Long = -1
        val currentTimeMillis = System.currentTimeMillis()
        var nextUpdate1 = nextUpdate
        actions?.forEach {
            if (actionStatesMap.containsKey(it.type)) { // Action was clicked
                val coolDownExpiration =
                    actionStatesMap[it.type]!!.lastUsedTimestamp + it.coolDownPeriod
                if (coolDownExpiration > currentTimeMillis && (nextUpdate1 < 0 || coolDownExpiration < nextUpdate1)) {
                    nextUpdate1 = coolDownExpiration
                }
            }
        }

        if (nextUpdate1 > 0) {
            handler.removeCallbacks(refreshTask)
            handler.postDelayed(refreshTask,nextUpdate1 - currentTimeMillis)
        }
    }

    private fun isActionEnabled(action :Action,weekDay : Int): Boolean{
        var result = false
        if(action.enabled  && action.validDays.contains(weekDay)){
            result = if(actionStatesMap.containsKey(action.type)){ // Action was clicked
                actionStatesMap[action.type]!!.lastUsedTimestamp + action.coolDownPeriod < System.currentTimeMillis()
            } else{
                true
            }
        }
        return result
    }


    private val refreshTask = Runnable {
        processData()
        setNextStatusRefresh()
    }

}