package store.middleware

import store.GameState
import store.reducer.GameControlActions
import store.reducer.GameStatusActions
import store.reducer.ScoreActions
import redux.Action
import redux.Middleware
import redux.MiddlewareContext
import store.type.GameStatus

class GameRestartMiddleware : Middleware<GameState, Action> {
    override fun invoke(
        context: MiddlewareContext<GameState, Action>,
        next: (Action) -> Unit,
        action: Action
    ) {
        when (action) {
            is GameStatusActions.Update -> {
                if (action.status is GameStatus.Running) {
                    context.store.dispatch(
                        ScoreActions.Reset
                    )
                    context.store.dispatch(
                        GameControlActions.Reset
                    )
                }
            }
        }
        next(action)
    }
}