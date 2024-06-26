package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kmproadfighter.composeapp.generated.resources.Res
import kmproadfighter.composeapp.generated.resources.road_center
import kmproadfighter.composeapp.generated.resources.road_left
import kmproadfighter.composeapp.generated.resources.road_right
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import store.StoreDispatcher
import store.reducer.RoadActions
import store.type.RoadSectionType

@Composable
fun BusyRoad(
    height: Dp,
    width: Dp,
    storeDispatcher: StoreDispatcher,
    animation: Float,
    roadColorFilter: Color
) {
    val roadSectionModifier = Modifier.size(width, height)
    val scrollState = rememberScrollState()
    val currentPosY = height * animation

    Row {
        Column(
            modifier = Modifier
                .roadSection(scrollState)
                .weight(weight = 1f, fill = false)
                .onGloballyPositioned { layoutCoordinates ->
                    storeDispatcher.dispatch(
                        RoadActions.UpdateLeftBounds(layoutCoordinates.boundsInRoot())
                    )
                }
        ) {
            RoadSection(
                type = RoadSectionType.Left,
                modifier = roadSectionModifier,
                currentPosY = currentPosY,
                height = height * 2,
                roadColorFilter = roadColorFilter
            )
        }

        Column(
            modifier = Modifier
                .roadSection(scrollState)
                .weight(weight = 2.5f, fill = true)
        ) {
            RoadSection(
                type = RoadSectionType.Center,
                modifier = roadSectionModifier,
                currentPosY = currentPosY,
                height = height * 2,
                roadColorFilter = roadColorFilter
            )
        }

        Column(
            modifier = Modifier
                .roadSection(scrollState)
                .weight(weight = 0.5f, fill = true)
                .onGloballyPositioned { layoutCoordinates ->
                    storeDispatcher.dispatch(
                        RoadActions.UpdateRightBounds(layoutCoordinates.boundsInRoot())
                    )
                }
        ) {
            RoadSection(
                type = RoadSectionType.Right,
                modifier = roadSectionModifier,
                currentPosY = currentPosY,
                height = height * 2,
                roadColorFilter = roadColorFilter
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RoadSection(
    type: RoadSectionType,
    modifier: Modifier,
    currentPosY: Dp,
    height: Dp,
    roadColorFilter: Color
) {
    val image = painterResource(
        when (type) {
            RoadSectionType.Left -> Res.drawable.road_left
            RoadSectionType.Right -> Res.drawable.road_right
            RoadSectionType.Center -> Res.drawable.road_center
        }
    )

    val colorFilter = ColorFilter.tint(
        color = when (type) {
            RoadSectionType.Left, RoadSectionType.Right -> roadColorFilter
            RoadSectionType.Center -> Color.Unspecified
        }, blendMode = BlendMode.Color
    )
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier.offset(y = currentPosY),
        contentScale = ContentScale.FillBounds,
        colorFilter = colorFilter
    )
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier.offset(y = currentPosY - height + 2.dp),
        contentScale = ContentScale.FillBounds,
        colorFilter = colorFilter
    )
}

private fun Modifier.roadSection(
    scrollState: ScrollState
): Modifier = this
    .then(
        verticalScroll(scrollState)
    )
    .then(
        // absorb all vertical scrolls
        pointerInput(Unit) {
            detectVerticalDragGestures { change, _ ->
                change.consume()
            }
        }
    )
