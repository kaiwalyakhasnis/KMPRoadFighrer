package store

import kotlinx.coroutines.Job
import redux.Action
import redux.Store
import store.middleware.GameRestartMiddleware
import store.middleware.LevelsMiddleware
import store.reducer.gameReducer

class GameStore(
    gameRestartMiddleware: GameRestartMiddleware,
    levelsMiddleware: LevelsMiddleware,
) : Store<GameState, Action>(
    initialState = GameState(),
    reducer = gameReducer,
    middleware = listOf(
        gameRestartMiddleware,
        levelsMiddleware
    )
)

fun interface StoreDispatcher {
    fun dispatch(action: Action): Job
}