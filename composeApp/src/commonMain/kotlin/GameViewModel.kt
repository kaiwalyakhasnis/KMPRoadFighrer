import store.GameStore
import store.StoreDispatcher
import store.middleware.GameRestartMiddleware
import store.middleware.LevelsMiddleware

class GameViewModel {
    val gameStore by lazy {
        GameStore(
            gameRestartMiddleware = GameRestartMiddleware(),
            levelsMiddleware = LevelsMiddleware()
        )
    }
    val storeDispatcher = StoreDispatcher(gameStore::dispatch)
}