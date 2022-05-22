package com.example.pokemon.di

import com.example.pokemon.Repository.*
import com.example.pokemon.datastore.DataStoreManager
import com.example.pokemon.local.FavoriteDao
import com.example.pokemon.local.UserDao
import com.example.pokemon.service.ApiHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object RepositoryModule {
    @ViewModelScoped
    @Provides
    fun providesUserRepository(userDao: UserDao, pref:DataStoreManager) = UserRepository(userDao,pref)

    @ViewModelScoped
    @Provides
    fun providesFavoriteRepository(favoriteDao: FavoriteDao) = FavoriteRepository(favoriteDao)

    @ViewModelScoped
    @Provides
    fun providesApiRepository(apiHelper: ApiHelper) = ApiRepository(apiHelper)

    @ViewModelScoped
    @Provides
    fun providesDetailRepository(favoriteDao: FavoriteDao, apiRepository: ApiRepository) = DetailRepository(apiRepository,favoriteDao)

    @ViewModelScoped
    @Provides
    fun providesHomeRepository(apiRepository: ApiRepository,pref: DataStoreManager) = ListRepository(apiRepository,pref)

}