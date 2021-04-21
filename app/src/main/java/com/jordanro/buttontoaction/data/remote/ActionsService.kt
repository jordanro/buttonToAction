package com.jordanro.buttontoaction.data.remote

import com.jordanro.buttontoaction.data.entities.Action

interface ActionsService {

    suspend fun loadActions(): List<Action>
}