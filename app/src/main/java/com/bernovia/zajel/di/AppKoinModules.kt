package com.bernovia.zajel.di

import org.koin.core.module.Module

class AppKoinModules {

    companion object {
        fun getModules(): List<Module> {
            return mutableListOf(
                appModule,
                viewModelModule,
                cacheModule,
                repositoryModule,
                serviceModuleV1
            )
        }
    }
}