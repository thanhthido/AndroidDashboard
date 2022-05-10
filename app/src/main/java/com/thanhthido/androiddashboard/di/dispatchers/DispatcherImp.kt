package com.thanhthido.androiddashboard.di.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Dispatchers.Unconfined

class DispatcherImp : DispatcherProvider {

    override val main: CoroutineDispatcher
        get() = Main

    override val default: CoroutineDispatcher
        get() = Default

    override val io: CoroutineDispatcher
        get() = IO

    override val unconfined: CoroutineDispatcher
        get() = Unconfined

}