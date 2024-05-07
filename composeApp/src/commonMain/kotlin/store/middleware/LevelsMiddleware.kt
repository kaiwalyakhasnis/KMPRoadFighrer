package store.middleware

import store.GameState
import store.reducer.LevelActions
import store.reducer.ScoreActions
import redux.Action
import redux.Middleware
import redux.MiddlewareContext

class LevelsMiddleware : Middleware<GameState, Action> {
    override fun invoke(
        context: MiddlewareContext<GameState, Action>,
        next: (Action) -> Unit,
        action: Action
    ) {
        next(action)

        when (action) {
            is ScoreActions.Increment -> {
                val score = context.state.scoreState.score
                if (score.mod(25) == 0) {
                    context.store.dispatch(
                        LevelActions.Increment
                    )
                }
            }

            is ScoreActions.Reset -> {
                context.store.dispatch(
                    LevelActions.Reset
                )
            }
        }
    }
}