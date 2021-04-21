package com.jordanro.buttontoaction.data.remote

import com.jordanro.buttontoaction.data.entities.Action

class ActionServiceMock :ActionsService {

    override suspend fun loadActions(): List<Action> {
        val result = ArrayList<Action>()
        result.add(Action(Action.Type.animation,true,10,listOf(0,1,3),86400000))
        result.add(Action(Action.Type.toast,true,14,listOf(0,1,2,3,4,5,6),3600000))
        result.add(Action(Action.Type.call,false,12,listOf(2,4,5),1000))
        result.add(Action(Action.Type.notification,true,7,listOf(1,2,6),60000))
        return result;
    }
}