package dev.gustavo.countries.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.core.ui.util.shimmer

@Composable
fun FlagImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    backgroundColor: Color = Color.Transparent
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.CornerRadiusSmall))
            .background(backgroundColor)
    )
}

@Composable
fun SkeletonItem(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(Dimens.CornerRadiusSmall)
) {
    Box(
        modifier = modifier
            .clip(shape)
            .shimmer()
    )
}

@Composable
fun ErrorState(
    message: String,
    retryLabel: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    FullScreenMessage(
        message = message,
        icon = Icons.Default.ErrorOutline,
        iconTint = MaterialTheme.colorScheme.error,
        actionLabel = retryLabel,
        onAction = onRetry,
        messageTestTag = SharedTestTags.ERROR_MESSAGE,
        modifier = modifier.testTag(SharedTestTags.ERROR_STATE)
    )
}

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    FullScreenMessage(
        message = message,
        icon = Icons.Default.SearchOff,
        iconTint = MaterialTheme.colorScheme.primary,
        messageTestTag = SharedTestTags.EMPTY_STATE_MESSAGE,
        modifier = modifier.testTag(SharedTestTags.EMPTY_STATE)
    )
}

@Composable
private fun FullScreenMessage(
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    messageTestTag: String? = null
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.PaddingMassive),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Top bias to place it slightly higher than center
            Spacer(Modifier.height(Dimens.PaddingGiant))

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(80.dp)
            )

            Spacer(Modifier.height(Dimens.PaddingLarge))

            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.then(
                    if (messageTestTag != null) Modifier.testTag(messageTestTag) else Modifier
                )
            )

            if (actionLabel != null && onAction != null) {
                Spacer(Modifier.height(Dimens.PaddingExtraLarge))
                Button(
                    onClick = onAction,
                    modifier = Modifier.testTag(SharedTestTags.ERROR_RETRY_BUTTON)
                ) {
                    Text(actionLabel)
                }
            }

            // More space at the bottom than the top to push content up
            Spacer(Modifier.height(160.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    CountriesTheme {
        ErrorState(
            message = "Unable to load country data. Please check your internet connection and try again.",
            retryLabel = "Retry",
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    CountriesTheme {
        EmptyState(message = "No countries match your search criteria.")
    }
}
