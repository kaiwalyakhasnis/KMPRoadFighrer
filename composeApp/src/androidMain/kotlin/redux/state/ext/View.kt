/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package redux.state.ext

import android.view.View
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import redux.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import redux.Action
import redux.State

/**
 * Helper extension method for consuming [State] from a [Store] sequentially in order scoped to the
 * lifetime of the [View]. The [block] function will get invoked for every [State] update.
 *
 * This helper will automatically stop observing the [Store] once the [View] gets detached. The
 * provided [LifecycleOwner] is used to determine when observing should be stopped or resumed.
 *
 * Inside a [Fragment] prefer to use [Fragment.consumeFrom].
 */
@ExperimentalCoroutinesApi // Channel
fun <S : State, A : Action> View.consumeFrom(
    store: Store<S, A>,
    owner: LifecycleOwner,
    block: (S) -> Unit,
) {
    val scope = toScope()
    val channel = store.channel(owner)

    scope.launch {
        channel.consumeEach { state -> block(state) }
    }
}

/**
 * Creates a [CoroutineScope] that is active as long as this [View] is attached. Once this [View]
 * gets detached this [CoroutineScope] gets cancelled automatically.
 *
 * By default coroutines dispatched on the created [CoroutineScope] run on the main dispatcher.
 *
 * Note: This scope gets only cancelled if the [View] gets detached. In cases where the [View] never
 * gets attached this may create a scope that never gets cancelled!
 */
@MainThread
fun View.toScope(): CoroutineScope {
    val scope = MainScope()

    addOnAttachStateChangeListener(
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) = Unit

            override fun onViewDetachedFromWindow(view: View) {
                scope.cancel()
                view.removeOnAttachStateChangeListener(this)
            }
        },
    )

    return scope
}