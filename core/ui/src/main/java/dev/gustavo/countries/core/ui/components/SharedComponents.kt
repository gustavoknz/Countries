package dev.gustavo.countries.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.Dimens

@Composable
fun FlagImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    backgroundColor: Color = Color.Transparent
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier
            .clip(RoundedCornerShape(Dimens.CornerRadiusSmall))
            .background(backgroundColor)
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
        actionLabel = retryLabel,
        onAction = onRetry,
        modifier = modifier
    )
}

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FullScreenMessage(
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.PaddingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(Dimens.PaddingExtraLarge))
        Button(onClick = onAction) {
            Text(actionLabel)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStatePreview() {
    CountriesTheme {
        ErrorState(
            message = "Something went wrong. Something went wrong. Something went wrong. ",
            retryLabel = "Retry",
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    CountriesTheme {
        EmptyState(message = "No data found")
    }
}
