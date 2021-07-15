package com.jasvir.dogsapp.coroutines

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

/**
 * TODO
 *
 * @property uiContext context of Coroutine
 */
open class CouroutineViewModel(private val uiContext: CoroutineContext) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = uiContext + job

    private val job = SupervisorJob()

    /**
     * clear children if exit
     *
     */
    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancelChildren()
    }
}