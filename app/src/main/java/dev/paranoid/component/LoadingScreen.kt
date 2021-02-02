package dev.paranoid.component

import android.view.View
import android.view.WindowManager
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.runtime.onDispose
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.navigate
import dev.paranoid.R
import dev.paranoid.android.MainActivity

@Composable
fun LoadingScreen(onLoaded: () -> Unit) {
    val offset = animatedFloat(0f)

    val context = AmbientContext.current

    onActive {
        (context as? MainActivity)?.apply {
            // cancel windowBackground@styles.xml
            window.decorView.setBackgroundColor(android.graphics.Color.BLACK)
        }
        offset.animateTo(1f, tween(2000, easing = FastOutLinearInEasing)) { r, v ->
            onLoaded()
        }
    }

    onDispose {
        (context as? MainActivity)?.apply {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            vectorResource(R.drawable.ic_pre_release),
            contentScale = ContentScale.None
        )

        Column(Modifier.height((offset.value * 200).coerceAtMost(100f).dp)) {
            Spacer(Modifier.weight(1f))
            LinearProgressIndicator(
                offset.value,
                color = Color.White
            )
        }
    }

}