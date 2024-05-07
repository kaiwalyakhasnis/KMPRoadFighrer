package store.reducer

import redux.Action
import store.state.GameControllerState
import store.type.ControllerButton

sealed class GameControlActions : Action("GameControlActions") {
    data class OnClicked(
        val button: ControllerButton
    ) : GameControlActions()

    object Reset: GameControlActions()
}

val gameControlReducer = reducerFor<GameControlActions> { state, action ->
    when (action) {
        is GameControlActions.OnClicked -> {
            state.copy(
                gameControllerState = GameControllerState(
                    playerOffset = state.gameControllerState.playerOffset + when (action.button) {
                        ControllerButton.Left -> -20f
                        ControllerButton.Right -> 20f
                        else -> 0f
                    }
                )
            )
        }
        is GameControlActions.Reset -> {
            state.copy(
                gameControllerState = GameControllerState(
                    playerOffset = 0f
                )
            )
        }
    }

}