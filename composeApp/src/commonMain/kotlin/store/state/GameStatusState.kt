package store.state

import store.type.GameStatus

data class GameStatusState(
    val status: GameStatus = GameStatus.Running
)