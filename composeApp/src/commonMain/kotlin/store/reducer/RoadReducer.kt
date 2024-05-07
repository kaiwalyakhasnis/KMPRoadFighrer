package store.reducer

import androidx.compose.ui.geometry.Rect
import redux.Action

sealed class RoadActions : Action("RoadActions") {
    data class UpdateLeftBounds(
        val rect: Rect
    ) : RoadActions()

    data class UpdateRightBounds(
        val rect: Rect
    ) : RoadActions()
}

val roadReducer = reducerFor<RoadActions> { state, action ->
    when (action) {
        is RoadActions.UpdateLeftBounds -> {
            val roadState = state.roadState.copy(leftSectionBounds = action.rect)
            state.copy(roadState = roadState)
        }

        is RoadActions.UpdateRightBounds -> {
            val roadState = state.roadState.copy(rightSectionBounds = action.rect)
            state.copy(roadState = roadState)
        }
    }
}