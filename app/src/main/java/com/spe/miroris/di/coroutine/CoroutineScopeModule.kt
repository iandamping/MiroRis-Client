package com.spe.miroris.di.coroutine

import com.spe.miroris.di.qualifier.CustomDefaultScope
import com.spe.miroris.di.qualifier.CustomIOScope
import com.spe.miroris.di.qualifier.CustomMainScope
import com.spe.miroris.di.qualifier.CustomNonDispatcherScope
import com.spe.miroris.di.qualifier.DefaultDispatcher
import com.spe.miroris.di.qualifier.IoDispatcher
import com.spe.miroris.di.qualifier.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @CustomNonDispatcherScope
    @Provides
    fun providesApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @CustomDefaultScope
    @Provides
    fun providesDefaultApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

    @CustomIOScope
    @Provides
    fun providesIoApplicationScope(
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)


    @CustomMainScope
    @Provides
    fun providesMainApplicationScope(
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + mainDispatcher)
}