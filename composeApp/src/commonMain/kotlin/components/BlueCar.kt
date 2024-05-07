package components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import store.StoreDispatcher
import store.reducer.BlueCarActions
import kmproadfighter.composeapp.generated.resources.Res
import kmproadfighter.composeapp.generated.resources.blue_car
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BlueCar(
    animation: Float,
    storeDispatcher: StoreDispatcher,
    height: Dp
) {
    val currentPosY = height * animation
    val blueCarPositionRange = 120..250
    val (blueCarOffset, setBlueCarOffset) = remember {
        mutableStateOf((blueCarPositionRange).random())
    }
    if ((0.000f..0.020f).contains(animation)) {
        setBlueCarOffset((blueCarPositionRange).random())
    }
    val offsetAnimation: Dp by animateDpAsState(
        blueCarOffset.dp
    )
    Image(
        painter = painterResource(Res.drawable.blue_car),
        contentDescription = null,
        modifier = Modifier
            .absoluteOffset(
                x = offsetAnimation,
                y = currentPosY
            )
            .onGloballyPositioned { layoutCoordinates ->
                storeDispatcher.dispatch(
                    BlueCarActions.UpdateBounds(layoutCoordinates.boundsInRoot())
                )
            },
        contentScale = ContentScale.Fit
    )
}
