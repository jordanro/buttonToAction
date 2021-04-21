package com.jordanro.buttontoaction.di

import android.content.Context
import com.jordanro.buttontoaction.data.local.AppDatabase
import com.jordanro.buttontoaction.data.remote.ActionServiceMock
import com.jordanro.buttontoaction.data.remote.ActionsService
import com.jordanro.buttontoaction.repositories.ActionsManager
import com.jordanro.buttontoaction.repositories.ActionsManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)

class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideActionsDao(db: AppDatabase) = db.actionsDao()

    @Singleton
    @Provides
    fun provideActionsService() :ActionsService{
        return ActionServiceMock()
    }

    @Singleton
    @Provides
    fun provideActionsManager() :ActionsManager{
        return ActionsManagerImpl()
    }
}