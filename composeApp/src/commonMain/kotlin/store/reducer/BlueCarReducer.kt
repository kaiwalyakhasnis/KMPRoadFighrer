package store.reducer

import androidx.compose.ui.geometry.Rect
import redux.Action

sealed class BlueCarActions : Action("BlueCarActions") {
    data class UpdateBounds(
        val rect: Rect
    ) : BlueCarActions()
}

val blueCarReducer = reducerFor<BlueCarActions> { state, action ->
    when (action) {
        is BlueCarActions.UpdateBounds -> {
            val blueCarState = state.blueCarState.copy(bounds = action.rect)
            state.copy(blueCarState = blueCarState)
        }
    }
}