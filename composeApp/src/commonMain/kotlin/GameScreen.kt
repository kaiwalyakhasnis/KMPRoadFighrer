import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import components.BlueCar
import components.BusyRoad
import components.DashBoard
import components.GameControls
import components.GameOver
import components.PlayerCar
import redux.ext.observeAsComposableState
import store.state.LevelsState
import store.type.GameStatus

@Composable
fun GameScreen(
    gameViewModel: GameViewModel
) {
    val status = gameViewModel.gameStore.observeAsComposableState(
        map = { state -> state.gameStatusState }
    )
    val score = gameViewModel.gameStore.observeAsComposableState(
        map = { state -> state.scoreState }
    ).value?.score ?: 0

    val levelState = gameViewModel.gameStore.observeAsComposableState(
        map = { state -> state.levelsState }
    ).value ?: LevelsState()

    when (status.value?.status) {
        is GameStatus.Running -> {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ) {
                val transition = rememberInfiniteTransition()
                // this acts as game loop
                val animation = transition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = levelState.getCurrentLevelData().speed,
                            easing = LinearEasing
                        )
                    )
                )

                // road along with traffic
                BusyRoad(
                    height = maxHeight,
                    width = maxWidth,
                    storeDispatcher = gameViewModel.storeDispatcher,
                    animation = animation.value,
                    roadColorFilter = levelState.getCurrentLevelData().roadColorFilter
                )

                // blue car
                BlueCar(
                    animation = animation.value,
                    height = maxHeight,
                    storeDispatcher = gameViewModel.storeDispatcher
                )

                // red car
                PlayerCar(
                    modifier = Modifier.align(
                        alignment = Alignment.BottomCenter
                    ),
                    storeDispatcher = gameViewModel.storeDispatcher,
                    blueCarState = gameViewModel.gameStore.observeAsComposableState(
                        map = { state -> state.blueCarState }
                    ).value,
                    roadState = gameViewModel.gameStore.observeAsComposableState(
                        map = { state -> state.roadState }
                    ).value,
                    playerOffset = gameViewModel.gameStore.observeAsComposableState(
                        map = { state -> state.gameControllerState }
                    ).value?.playerOffset
                )

                GameControls(
                    modifier = Modifier.align(Alignment.BottomStart),
                    storeDispatcher = gameViewModel.storeDispatcher
                )

                // score and level
                DashBoard(
                    modifier = Modifier.align(Alignment.TopStart),
                    score = score,
                    level = levelState.getCurrentLevelData().level
                )
            }
        }

        is GameStatus.Finish -> {
            GameOver(
                score = score,
                storeDispatcher = gameViewModel.storeDispatcher
            )
        }

        else -> {
            // do nothing
        }
    }
}