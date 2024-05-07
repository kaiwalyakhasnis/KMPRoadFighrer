package store.reducer

val gameReducer = combineReducers(
    roadReducer,
    blueCarReducer,
    gameStatusReducer,
    scoreReducer,
    gameControlReducer,
    levelsReducer
)