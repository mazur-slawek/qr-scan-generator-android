package software.mazur.qrezzy.feature.onboarding.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class OnboardingItem(
    val type: OnboardingItemType,
    val accentColor: Color,
    @DrawableRes
    val iconResId: Int,
    @StringRes
    val titleResId: Int,
    @StringRes
    val descriptionResId: Int,
)