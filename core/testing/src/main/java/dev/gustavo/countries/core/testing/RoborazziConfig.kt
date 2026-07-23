package dev.gustavo.countries.core.testing

import com.github.takahirom.roborazzi.RoborazziOptions
import com.dropbox.differ.SimpleImageComparator

/**
 * Default Roborazzi options for the project to ensure consistent screenshot testing.
 *
 * Includes:
 * - 1% change threshold to account for minor rendering differences between OSs.
 * - 1-pixel shift tolerance to handle font rendering variations.
 */
val DEFAULT_ROBORAZZI_OPTIONS = RoborazziOptions(
    compareOptions = RoborazziOptions.CompareOptions(
        changeThreshold = 0.01f,
        imageComparator = SimpleImageComparator(
            maxDistance = 0.007f,
            hShift = 1,
            vShift = 1
        )
    )
)
