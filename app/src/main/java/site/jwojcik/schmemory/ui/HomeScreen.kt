package site.jwojcik.schmemory.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

var easterEggClicks = 0
var logo = R.drawable.logo
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
        // Thought Bubble
        ThoughtBubble(
            text = "Time to Schmemorize!",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
        )

        AnimatedVisibility(
            visible = showSmallButtons,
            enter = fadeIn(animationSpec = tween(800)),
            exit = fadeOut(animationSpec = tween(400))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top Left Button
                MorphingButton(
                    morph = morph,
                    iconRes = R.drawable.podium,
                    contentDescription = "Speech Screen Button",
                    onClick = onSpeechesClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 220.dp, start = 32.dp)
                )


                // Top Right Button
                MorphingButton(
                    morph = morph,
                    iconRes = R.drawable.masks,
                    contentDescription = "Scenes Screen Button",
                    onClick = onScenesClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 220.dp, end = 32.dp)
                )


                // Bottom Settings Button
                MorphingButton(
                    morph = morph,
                    iconRes = R.drawable.gear,
                    contentDescription = "Settings Screen Button",
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 170.dp)
                )
            }
        }


        //Logo Button
        RotatingScallopedLogo(
            onClick = { showSmallButtons = !showSmallButtons },
            modifier = Modifier.size(240.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue
            )
        )
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
            .size(110.dp)
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
            modifier = Modifier.size(64.dp),
            tint = Color.Unspecified
        )
        RotatingScallopedButton(iconRes, contentDescription)
    }
}

class CustomRotatingMorphShape(
    private val morph: Morph,
    private val percentage: Float,
    private val rotation: Float
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
        matrix.rotateZ(rotation)

        val path = morph.toPath(progress = percentage).asComposePath()
        path.transform(matrix)

        return Outline.Generic(path)
    }
}
@Composable
private fun RotatingScallopedButton(
    iconRes: Int,
    contentDescription: String
    ) {
    val shapeA = remember {
        RoundedPolygon(
            12,
            rounding = CornerRounding(0.2f)
        )
    }
    val shapeB = remember {
        RoundedPolygon.star(
            12,
            rounding = CornerRounding(0.2f)
        )
    }
    val morph = remember {
        Morph(shapeA, shapeB)
    }
    val infiniteTransition = rememberInfiniteTransition("infinite outline movement")
    val animatedProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedMorphProgress"
    )
    val animatedRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedMorphProgress"
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(
                    CustomRotatingMorphShape(
                        morph,
                        animatedProgress.value,
                        animatedRotation.value
                    )
                )
                .size(90.dp)
        )
    }
}

@Composable
private fun RotatingScallopedLogo(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = Blue)
) {
    val shapeA = remember {
        RoundedPolygon(
            12,
            rounding = CornerRounding(0.2f)
        )
    }
    val shapeB = remember {
        RoundedPolygon.star(
            12,
            rounding = CornerRounding(0.2f)
        )
    }
    val morph = remember {
        Morph(shapeA, shapeB)
    }
    val infiniteTransition = rememberInfiniteTransition("infinite outline movement")
    val animatedProgress = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedMorphProgress"
    )
    val animatedRotation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "animatedRotation"
    )

    val morphShape = CustomRotatingMorphShape(
        morph,
        animatedProgress.value,
        animatedRotation.value
    )

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = morphShape,
        contentPadding = contentPadding,
        colors = colors
    ) {
        Image(
            painter = painterResource(logo),
            contentDescription = "Home Button",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
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


@Composable
fun ThoughtBubble(text: String, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "thought bubble floating")
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating",
    )

    Box(modifier = modifier.offset(y = floatingOffset.dp)) {
        // Small "thought" circles
        Box(
            modifier = Modifier
                .size(12.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 10.dp, y = 5.dp)
                .background(Blue, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 20.dp, y = 15.dp)
                .background(Blue, shape = CircleShape)
        )

        // The main bubble
        Box(
            modifier = Modifier
                .background(Blue, shape = RoundedCornerShape(24.dp))
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .clickable{easterEggClicks++; if (easterEggClicks == 15) {logo = R.drawable.anya} else {logo = R.drawable.logo} },

            ) {
            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
