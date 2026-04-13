package site.jwojcik.schmemory.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import site.jwojcik.schmemory.R

@Composable
fun HomeScreen(
    onScenesClick: () -> Unit,
    onSpeechesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    HomePageScreen()
}

@Preview
@Composable
fun HomePreview() {
    HomeScreen(
        onScenesClick = {},
        onSpeechesClick = {},
        onSettingsClick = {}
    )
}

@Composable
fun HomePageScreen() {
    var showSmallButtons by remember {mutableStateOf(false)}

    Box(
        // Box Holding All The Buttons
        // logo is placeholder for our desired icons
        // buttons are hard coded to positions, will need to change if we plan to animate
        // positions may be incorrect for different sized screens
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
            contentAlignment = Alignment.Center
    ) {
        //AnimatedVisibility(visible = showSmallButtons) to animate, breaks code though
        if(showSmallButtons) {
            // Top Left Button
            SmallButton(
                modifier = Modifier.align(Alignment.TopStart).padding(top = 230.dp, start = 22.dp),
                iconRes = R.drawable.logo // SPEECH
            )

            // Top Right Button
            SmallButton(
                modifier = Modifier.align(Alignment.TopEnd).padding(top = 230.dp, end = 22.dp),
                iconRes = R.drawable.logo // SCRIPT
            )
        }

        // (Logo Button + Bottom Button)
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Button
            Button(
                onClick = {
                    showSmallButtons = !showSmallButtons
                },
                modifier = Modifier.size(240.dp),
                shape = CircleShape,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF659157)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo), // LOGO
                    contentDescription = "Home Button",
                    modifier = Modifier.size(200.dp),
                    tint = Color.Unspecified
                )
            }
        }

        // Bottom Button
        if(showSmallButtons) {
            SmallButton(
                modifier = Modifier.padding(top = 400.dp),
                iconRes = R.drawable.logo // SETTINGS
            )
        }
    }
}

@Composable
fun SmallButton(
    modifier: Modifier = Modifier,
    iconRes: Int
) {
    Button(
        onClick = { },
        modifier = modifier.size(80.dp),
        shape = CircleShape,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF0C987)
        )
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            tint = Color.Unspecified
        )
    }
}
