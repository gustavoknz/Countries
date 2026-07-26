package dev.gustavo.countries.core.ui.util

import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Phone", device = "spec:width=411dp,height=891dp,dpi=420")
@Preview(name = "Foldable", device = "spec:width=673dp,height=841dp,dpi=420")
@Preview(name = "Tablet", device = "spec:width=1280dp,height=800dp,dpi=420")
annotation class DevicePreviews

@Preview(name = "Font 0.85", fontScale = 0.85f)
@Preview(name = "Font 1.0", fontScale = 1.0f)
@Preview(name = "Font 1.5", fontScale = 1.5f)
@Preview(name = "Font 2.0", fontScale = 2.0f)
annotation class FontScalePreviews

@DevicePreviews
@FontScalePreviews
annotation class CombinedPreviews
