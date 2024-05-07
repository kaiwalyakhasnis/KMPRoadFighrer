package store.reducer

import store.GameState
import redux.Action
import redux.Reducer

open class ReducerWrapper<T : Action>(
    val actionType: String,
    reducer: Reducer<GameState, T>
) : Reducer<GameState, T> by reducer

inline fun <reified T : Action> reducerFor(
    crossinline reducer: (state: GameState, action: T) -> GameState
): ReducerWrapper<Action> {
    return ReducerWrapper(T::class.simpleName!!, object : Reducer<GameState, Action> {
        override fun invoke(state: GameState, action: Action): GameState {
            return reducer(state, action as T)
        }
    })
}

internal fun <T : Action> combineReducers(
    vararg reducers: ReducerWrapper<in T>
): Reducer<GameState, Action> {
    // group reducers by action type
    val map = reducers.groupBy { it.actionType }

    // merge reducers for an action
    return object : Reducer<GameState, Action> {
        @Suppress("UNCHECKED_CAST")
        override fun invoke(state: GameState, action: Action): GameState {
            return map[action.actionType]?.fold(state) { next, reducer ->
                reducer(next, action as T)
            } ?: state
        }
    }
}