package dev.gustavo.countries.feature.list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.gustavo.countries.core.ui.theme.Dimens
import dev.gustavo.countries.feature.list.ListTestTags
import dev.gustavo.countries.feature.list.R

@Composable
internal fun ModernSearchBar(
    searchQuery: String,
    isOffline: Boolean,
    focusRequester: FocusRequester,
    onSearchQueryChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = {
            Text(
                text = stringResource(R.string.list_search_placeholder),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchQueryChanged("")
                        focusRequester.requestFocus()
                    },
                    modifier = Modifier.testTag(ListTestTags.SEARCH_CLEAR_BUTTON)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.list_search_clear_description)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(Dimens.CornerRadiusLarge),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingExtraLarge)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isOffline) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant
                ),
                shape = RoundedCornerShape(Dimens.CornerRadiusLarge)
            )
            .focusRequester(focusRequester)
            .testTag(ListTestTags.SEARCH_FIELD)
    )
}
