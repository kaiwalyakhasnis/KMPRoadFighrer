/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package redux.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import redux.Action
import redux.Observer
import redux.State
import redux.Store
import androidx.compose.runtime.State as ComposeState

// TODO this implementation needs to be revised based on
//  https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-lifecycle.html
@Composable
fun <S : State, A : Action, R> Store<S, A>.observeAsComposableState(map: (S) -> R): ComposeState<R?> {
    val state = remember { mutableStateOf<R?>(map(state)) }

    DisposableEffect(this) {
        val subscription = observeForever { observedState ->
            state.value = map(observedState)
        }
        onDispose { subscription.unsubscribe() }
    }

    return state
}

fun <S : State, A : Action> Store<S, A>.observeForever(
    observer: Observer<S>,
) = observeManually(observer).apply { resume() }
