package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import store.StoreDispatcher
import store.reducer.GameStatusActions
import store.reducer.ScoreActions
import kmproadfighter.composeapp.generated.resources.Res
import kmproadfighter.composeapp.generated.resources.player_car
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import store.state.BlueCarState
import store.state.RoadState
import store.type.GameStatus

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PlayerCar(
    modifier: Modifier = Modifier,
    blueCarState: BlueCarState?,
    roadState: RoadState?,
    storeDispatcher: StoreDispatcher,
    playerOffset: Float?,
) {
    val (playerCarBounds, setPlayerCarBounds) = remember { mutableStateOf(Rect.Zero) }
    val (blueCarPosState, setBlueCarPosState) = remember { mutableStateOf(BlueCarPositionState()) }
    val hapticFeedback = LocalHapticFeedback.current

    Image(
        painter = painterResource(Res.drawable.player_car),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .paddingFromBaseline(bottom = 250.dp)
            .offset(
                x = playerOffset?.dp ?: 0.dp,
                y = 0.dp
            )
            .onGloballyPositioned { layoutCoordinates ->
                setPlayerCarBounds(layoutCoordinates.boundsInRoot())
            }
    )

    SideEffect {
        // todo: move all these handling to reducer
        // collision detection
        if (
            roadState?.isCollidingWith(playerCarBounds) == true || // left road barrier
            blueCarState?.isCollidingWith(playerCarBounds) == true
        ) {
            hapticFeedback.performHapticFeedback(
                hapticFeedbackType = HapticFeedbackType.LongPress
            )
            // game over
            storeDispatcher.dispatch(
                GameStatusActions.Update(
                    status = GameStatus.Finish
                )
            )
        }

        // score area
        val blueCarTop = blueCarState?.bounds?.top ?: 0f
        if (blueCarState?.hasPassedPlayerCar(
                blueCarPosState = blueCarPosState,
                playerCarTop = playerCarBounds.top
            ) == true
        ) {
            setBlueCarPosState(
                BlueCarPositionState(top = blueCarTop, hasCounted = true)
            )
            storeDispatcher.dispatch(ScoreActions.Increment)
        }

        if (blueCarTop < playerCarBounds.top) {
            setBlueCarPosState(
                blueCarPosState.copy(hasCounted = false)
            )
        }
    }
}

data class BlueCarPositionState(
    val top: Float = 0f,
    val hasCounted: Boolean = false
)
