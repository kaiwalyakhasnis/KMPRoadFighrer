package store

import store.state.BlueCarState
import store.state.GameControllerState
import store.state.GameStatusState
import store.state.LevelsState
import store.state.RoadState
import store.state.ScoreState
import store.type.ReduxState

data class GameState(
    val blueCarState: BlueCarState = BlueCarState(),
    val roadState: RoadState = RoadState(),
    val gameStatusState: GameStatusState = GameStatusState(),
    val scoreState: ScoreState = ScoreState(),
    val gameControllerState: GameControllerState = GameControllerState(),
    val levelsState: LevelsState = LevelsState()
): ReduxState