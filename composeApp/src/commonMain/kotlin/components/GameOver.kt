package components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import store.StoreDispatcher
import store.reducer.GameStatusActions
import org.jetbrains.compose.ui.tooling.preview.Preview
import store.type.GameStatus

@Composable
fun GameOver(
    score: Int,
    storeDispatcher: StoreDispatcher? = null
) {
    Box(
        modifier = Modifier
            .background(color = Color.Black)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    storeDispatcher?.dispatch(
                        GameStatusActions.Update(
                            status = GameStatus.Running
                        )
                    )
                }
            }
    ) {
        Text(
            text = "Game Over \n\n Score $score \n\n\n Tap anywhere to restart",
            color = Color.White,
            fontSize = 32.sp,
            modifier = Modifier
                .align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun GameOverPreview() {
    GameOver(score = 100)
}
