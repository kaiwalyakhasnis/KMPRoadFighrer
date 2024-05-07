package store.reducer

import store.state.ScoreState
import redux.Action

sealed class ScoreActions : Action("ScoreActions") {
    object Increment : ScoreActions()
    object Reset : ScoreActions()
}

val scoreReducer = reducerFor<ScoreActions> { state, action ->
    when (action) {
        is ScoreActions.Increment -> {
            state.copy(scoreState = ScoreState(state.scoreState.score + 1))
        }

        is ScoreActions.Reset -> {
            state.copy(scoreState = ScoreState(0))
        }
    }
}