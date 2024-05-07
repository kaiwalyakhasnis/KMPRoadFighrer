package store.state

import androidx.compose.ui.geometry.Rect
import store.type.ReduxState

data class RoadState(
    val leftSectionBounds: Rect = Rect.Zero,
    val rightSectionBounds: Rect = Rect.Zero
) : ReduxState {
    fun isCollidingWith(rect: Rect): Boolean {
        return leftSectionBounds.overlaps(rect) || rightSectionBounds.overlaps(rect)
    }
}