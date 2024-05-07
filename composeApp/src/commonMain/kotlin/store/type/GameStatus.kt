package store.type

sealed class GameStatus {
    data object Running: GameStatus()
    data object Finish: GameStatus()
}