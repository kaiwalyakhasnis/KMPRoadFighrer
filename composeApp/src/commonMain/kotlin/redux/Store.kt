/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package redux

import kotlinx.coroutines.Job

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect open class Store<S : State, A : Action>(
    initialState: S,
    reducer: Reducer<S, A>,
    middleware: List<Middleware<S, A>> = emptyList()
) {
    val state: S
    internal fun transitionTo(state: S)
    fun observeManually(observer: Observer<S>): Subscription<S, A>
    fun dispatch(action: A): Job

    class Subscription<S : State, A : Action> {
        fun resume()
        fun pause()
        fun dispatch(state: S)
        fun unsubscribe()
    }
}