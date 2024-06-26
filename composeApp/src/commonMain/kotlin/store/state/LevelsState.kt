package store.state

import androidx.compose.ui.graphics.Color

data class LevelsState(
    val currentLevel: Int = 1,
    val levelsData: List<LevelData> = listOf(
        LevelData(level = 1, speed = 1500, roadColorFilter = Color.Transparent),
        LevelData(level = 2, speed = 1400, roadColorFilter = Color.Blue),
        LevelData(level = 3, speed = 1300, roadColorFilter = Color.Magenta),
        LevelData(level = 4, speed = 1200, roadColorFilter = Color.Transparent),
        LevelData(level = 5, speed = 1000, roadColorFilter = Color.Blue)
    )
) {
    fun getCurrentLevelData(): LevelData {
        return levelsData.getOrNull(currentLevel - 1) ?: levelsData.first()
    }
}

data class LevelData(
    val level: Int,
    val speed: Int,
    val roadColorFilter: Color
)