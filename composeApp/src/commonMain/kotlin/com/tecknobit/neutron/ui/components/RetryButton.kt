package com.tecknobit.neutron.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.resources.Res
import com.tecknobit.equinoxcompose.resources.retry
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@FutureEquinoxApi(
    releaseVersion = "1.1.4",
    additionalNotes = """
        - Will be released due its utility in different applications
    """
)
@Composable
fun RetryButton(
    onRetry: () -> Unit,
    text: StringResource = Res.string.retry,
) {
    TextButton(
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onRetry
    ) {
        Text(
            text = stringResource(text)
        )
    }
}