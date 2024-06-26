package store.reducer

import redux.Action

sealed class LevelActions : Action("LevelActions") {
    object Increment : LevelActions()
    object Reset : LevelActions()
}

val levelsReducer = reducerFor<LevelActions> { state, action ->
    when (action) {
        is LevelActions.Increment -> {
            val nextLevel = state.levelsState.currentLevel + 1
            if (nextLevel <= 5) {
                state.copy(levelsState = state.levelsState.copy(currentLevel = nextLevel))
            } else {
                state
            }
        }

        is LevelActions.Reset -> {
            state.copy(levelsState = state.levelsState.copy(currentLevel = 1))
        }
    }
}