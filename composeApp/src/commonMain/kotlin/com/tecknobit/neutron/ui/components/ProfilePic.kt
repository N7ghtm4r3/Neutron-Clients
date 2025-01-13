package com.tecknobit.neutron.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.logo
import org.jetbrains.compose.resources.painterResource

@Composable
@NonRestartableComposable
fun ProfilePic(
    profilePic: String?,
    size: Dp,
    onClick: () -> Unit
) {
    AsyncImage(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(profilePic)
            .crossfade(enable = true)
            .crossfade(durationMillis = 500)
            .build(),
        contentDescription = "User profile picture",
        error = painterResource(Res.drawable.logo),
        contentScale = ContentScale.Crop
    )
}