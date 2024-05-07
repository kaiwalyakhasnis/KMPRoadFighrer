package store.state

import androidx.compose.ui.geometry.Rect
import components.BlueCarPositionState
import store.type.ReduxState

data class BlueCarState(
    val bounds: Rect = Rect.Zero
) : ReduxState {
    fun isCollidingWith(rect: Rect): Boolean {
        return bounds.overlaps(rect)
    }

    fun hasPassedPlayerCar(
        blueCarPosState: BlueCarPositionState,
        playerCarTop: Float
    ): Boolean {
        return !blueCarPosState.hasCounted && bounds.top >= playerCarTop
    }
}