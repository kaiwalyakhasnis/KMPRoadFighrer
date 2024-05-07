package store.reducer

import redux.Action
import store.type.GameStatus


sealed class GameStatusActions : Action("GameStatusActions") {
    data class Update(
        val status: GameStatus
    ) : GameStatusActions()
}

val gameStatusReducer = reducerFor<GameStatusActions> { state, action ->
    when(action){
        is GameStatusActions.Update -> {
            val gameStatusState = state.gameStatusState.copy(status = action.status)
            state.copy(gameStatusState = gameStatusState)
        }
    }
}