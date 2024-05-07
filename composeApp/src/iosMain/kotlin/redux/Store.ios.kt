package redux

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import redux.internal.ReducerChainBuilder

actual open class Store<S : State, A : Action> actual constructor(
    initialState: S,
    reducer: Reducer<S, A>,
    middleware: List<Middleware<S, A>>
) {

    private val dispatcher = Dispatchers.Default
    private val reducerChainBuilder = ReducerChainBuilder(reducer = reducer, middleware = middleware)
    private val scope = CoroutineScope(dispatcher)

    internal val subscriptions = mutableSetOf<Subscription<S, A>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // Once an exception happened we do not want to accept any further actions. So let's cancel the scope which
        // will cancel all jobs and not accept any new ones.
        scope.cancel()
    }
    private val dispatcherWithExceptionHandler = dispatcher + exceptionHandler

    private var currentState = initialState

    /**
     * The current [State].
     */
    actual val state: S
        get() = currentState

    /**
     * Registers an [Observer] function that will be invoked whenever the [State] changes.
     *
     * It's the responsibility of the caller to keep track of the returned [Subscription] and call
     * [Subscription.unsubscribe] to stop observing and avoid potentially leaking memory by keeping an unused [Observer]
     * registered. It's is recommend to use one of the `observe` extension methods that unsubscribe automatically.
     *
     * The created [Subscription] is in paused state until explicitly resumed by calling [Subscription.resume].
     * While paused the [Subscription] will not receive any state updates. Once resumed the [observer]
     * will get invoked immediately with the latest state.
     *
     * @return A [Subscription] object that can be used to unsubscribe from further state changes.
     */
    actual fun observeManually(observer: Observer<S>): Subscription<S, A> {
        val subscription = Subscription(observer, store = this)
        subscriptions.add(subscription)

        return subscription
    }

    /**
     * Dispatch an [Action] to the store in order to trigger a [State] change.
     */
    actual fun dispatch(action: A) = scope.launch(dispatcherWithExceptionHandler) {
        reducerChainBuilder.get(this@Store).invoke(action)
    }

    /**
     * Transitions from the current [State] to the passed in [state] and notifies all observers.
     */
    internal actual fun transitionTo(state: S) {
        if (state == currentState) {
            // Nothing has changed.
            return
        }

        currentState = state
        subscriptions.forEach { subscription -> subscription.dispatch(state) }
    }

    private fun removeSubscription(subscription: Subscription<S, A>) {
        subscriptions.remove(subscription)
    }

    /**
     * A [Subscription] is returned whenever an observer is registered via the [observeManually] method. Calling
     * [unsubscribe] on the [Subscription] will unregister the observer.
     */
    actual class Subscription<S : State, A : Action> internal constructor(
        internal val observer: Observer<S>,
        store: Store<S, A>,
    ) {
        private val storeReference = store
        internal var binding: Binding? = null
        private var active = false

        /**
         * Resumes the [Subscription]. The [Observer] will get notified for every state change.
         * Additionally it will get invoked immediately with the latest state.
         */
        actual fun resume() {
            active = true

            storeReference.state?.let(observer)
        }

        /**
         * Pauses the [Subscription]. The [Observer] will not get notified when the state changes
         * until [resume] is called.
         */
        actual fun pause() {
            active = false
        }

        /**
         * Notifies this subscription's observer of a state change.
         *
         * @param state the updated state.
         */
        actual fun dispatch(state: S) {
            if (active) {
                observer.invoke(state)
            }
        }

        /**
         * Unsubscribe from the [Store].
         *
         * Calling this method will clear all references and the subscription will not longer be
         * active.
         */
        actual fun unsubscribe() {
            active = false

            storeReference?.removeSubscription(this)
            // storeReference.clear()

            binding?.unbind()
        }

        interface Binding {
            fun unbind()
        }
    }
}