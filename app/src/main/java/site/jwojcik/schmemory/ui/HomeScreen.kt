package site.jwojcik.schmemory.ui


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.jwojcik.schmemory.R
import site.jwojcik.schmemory.ui.theme.Blue
import site.jwojcik.schmemory.ui.theme.Green
import site.jwojcik.schmemory.ui.theme.Yellow


@Composable
fun HomeScreen(
    onScenesClick: () -> Unit,
    onSpeechesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    HomePageScreen(
        onScenesClick = onScenesClick,
        onSpeechesClick = onSpeechesClick,
        onSettingsClick = onSettingsClick
    )
}


@Composable
fun HomePageScreen(
    onScenesClick: () -> Unit,
    onSpeechesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var showSmallButtons by remember { mutableStateOf(false) }
    val shapeA = remember {
        RoundedPolygon(
            6,
            rounding = CornerRounding(0.2f)
        )
    }
    val shapeB = remember {
        RoundedPolygon.star(
            6,
            rounding = CornerRounding(0.1f)
        )
    }
    val morph = remember {
        Morph(shapeA, shapeB)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (showSmallButtons) {
            // Top Left Button
            MorphingButton(
                morph = morph,
                iconRes = R.drawable.podium,
                contentDescription = "Speech Screen Button",
                onClick = onSpeechesClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 230.dp, start = 32.dp)
            )


            // Top Right Button
            MorphingButton(
                morph = morph,
                iconRes = R.drawable.masks,
                contentDescription = "Scenes Screen Button",
                onClick = onScenesClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 230.dp, end = 32.dp)
            )


            // Bottom Settings Button
            MorphingButton(
                morph = morph,
                iconRes = R.drawable.gear,
                contentDescription = "Settings Screen Button",
                onClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 190.dp)
            )
        }


        // Logo Button
        Button(
            onClick = { showSmallButtons = !showSmallButtons },
            modifier = Modifier.size(240.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Home Button",
                modifier = Modifier.size(200.dp),
                tint = Color.Unspecified
            )
        }
    }
}


@Composable
fun MorphingButton(
    morph: Morph,
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedProgress by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )
    val scope = rememberCoroutineScope()


    Box(
        modifier = modifier
            .size(80.dp)
            .clip(MorphPolygonShape(morph, animatedProgress))
            .background(Green)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                scope.launch {
                    delay(200) // Delay navigation so user sees the morph animation
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            modifier = Modifier.size(48.dp), // Reduced size to center properly in morph shape
            tint = Color.Unspecified
        )
    }
}


class MorphPolygonShape(
    private val morph: Morph,
    private val percentage: Float
) : Shape {


    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        matrix.reset()
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)


        val path = morph.toPath(progress = percentage).asComposePath()
        path.transform(matrix)
        return Outline.Generic(path)
    }
}

