package software.mazur.qrezzy.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedBackground
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyCircleButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow
import software.mazur.qrezzy.feature.onboarding.model.OnboardingItem
import software.mazur.qrezzy.feature.onboarding.model.OnboardingItemType

@Composable
fun OnboardingScreen(onGetStartedClick: () -> Unit = {}) {
    var currentPageIndex by rememberSaveable { mutableIntStateOf(OnboardingDefaults.INITIAL_PAGE_INDEX) }
    val items = OnboardingDefaults.items
    val currentItem = items[currentPageIndex]
    val isLastPage = currentPageIndex == items.lastIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        key(currentItem.accentColor) {
            QrezzyAnimatedBackground(
                leftColor = currentItem.accentColor,
                rightColor = currentItem.accentColor
            )
        }

        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                targetState = currentPageIndex,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(OnboardingDefaults.Animation.DURATION_MILLIS),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) +
                        fadeIn(
                            animationSpec = tween(OnboardingDefaults.Animation.DURATION_MILLIS)
                        ) togetherWith slideOutHorizontally(
                            animationSpec = tween(OnboardingDefaults.Animation.DURATION_MILLIS),
                            targetOffsetX = { fullWidth -> -fullWidth }
                        ) +
                        fadeOut(
                            animationSpec = tween(OnboardingDefaults.Animation.DURATION_MILLIS)
                        )
                },
                label = OnboardingDefaults.Animation.CONTENT_TRANSITION_LABEL,
                modifier = Modifier.weight(OnboardingDefaults.Layout.CONTENT_WEIGHT)
            ) { pageIndex ->
                OnboardingContent(item = items[pageIndex], modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.height(OnboardingDefaults.BottomSection.topSpacing))

            OnboardingBottomSection(
                items = items,
                currentItem = currentItem,
                isLastPage = isLastPage,
                onNextClick = {
                    if (currentPageIndex < items.lastIndex) {
                        currentPageIndex += OnboardingDefaults.Navigation.NEXT_PAGE_STEP
                    }
                },
                onGetStartedClick = onGetStartedClick
            )
        }
    }
}

@Composable
private fun OnboardingContent(item: OnboardingItem, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = item.iconResId),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(OnboardingDefaults.Content.imageTitleSpacing))

            Text(
                text = stringResource(id = item.titleResId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(OnboardingDefaults.Content.titleDescriptionSpacing))

            Text(
                text = stringResource(id = item.descriptionResId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = OnboardingDefaults.Content.descriptionHorizontalPadding),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun OnboardingBottomSection(
    items: List<OnboardingItem>,
    currentItem: OnboardingItem,
    isLastPage: Boolean,
    onNextClick: () -> Unit,
    onGetStartedClick: () -> Unit
) {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(OnboardingDefaults.BottomSection.height)
            .padding(horizontal = OnboardingDefaults.BottomSection.horizontalPadding)
    ) {
        if (isLastPage) {
            QrezzyButton(
                text = stringResource(id = R.string.button_get_started),
                onClick = onGetStartedClick,
                rightIcon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        } else {
            OnboardingNavigationSection(
                items = items,
                currentItem = currentItem,
                nextIcon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                onNextClick = onNextClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun OnboardingNavigationSection(
    items: List<OnboardingItem>,
    currentItem: OnboardingItem,
    nextIcon: ImageVector,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        OnboardingPageIndicators(
            items = items,
            currentItem = currentItem,
            modifier =
            Modifier
                .height(OnboardingDefaults.Navigation.nextButtonContainerHeight)
                .weight(OnboardingDefaults.Layout.INDICATORS_WEIGHT)
        )
        QrezzyCircleButton(color = currentItem.accentColor, icon = nextIcon, onClick = onNextClick)
    }
}

@Composable
private fun OnboardingPageIndicators(items: List<OnboardingItem>, currentItem: OnboardingItem, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items.forEach { item ->
            val isSelected = item == currentItem
            Box(
                modifier =
                Modifier
                    .padding(horizontal = OnboardingDefaults.Indicator.horizontalPadding)
                    .size(
                        if (isSelected) {
                            OnboardingDefaults.Indicator.selectedSize
                        } else {
                            OnboardingDefaults.Indicator.defaultSize
                        }
                    )
                    .background(
                        color = if (isSelected) currentItem.accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = CircleShape
                    )
                    .border(
                        width = OnboardingDefaults.Indicator.borderWidth,
                        color = if (isSelected) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = CircleShape
                    )
            )
        }
    }
}

private object OnboardingDefaults {
    const val INITIAL_PAGE_INDEX = 0
    val items =
        listOf(
            OnboardingItem(
                type = OnboardingItemType.SCAN,
                accentColor = QrezzyMint,
                iconResId = R.drawable.onboarding_scan_icon,
                titleResId = R.string.onboarding_scan_title,
                descriptionResId = R.string.onboarding_scan_description
            ),
            OnboardingItem(
                type = OnboardingItemType.EDIT,
                accentColor = QrezzyYellow,
                iconResId = R.drawable.onboarding_generate_icon,
                titleResId = R.string.onboarding_generate_title,
                descriptionResId = R.string.onboarding_generate_description
            ),
            OnboardingItem(
                type = OnboardingItemType.HISTORY,
                accentColor = QrezzyPink,
                iconResId = R.drawable.onboarding_history_icon,
                titleResId = R.string.onboarding_history_title,
                descriptionResId = R.string.onboarding_history_description
            ),
            OnboardingItem(
                type = OnboardingItemType.SHARE,
                accentColor = QrezzyPurple,
                iconResId = R.drawable.onboarding_share_icon,
                titleResId = R.string.onboarding_share_title,
                descriptionResId = R.string.onboarding_share_description
            ),
            OnboardingItem(
                type = OnboardingItemType.SUMMARY,
                accentColor = QrezzyMint,
                iconResId = R.drawable.onboarding_summary_icon,
                titleResId = R.string.onboarding_summary_title,
                descriptionResId = R.string.onboarding_summary_description
            )
        )

    object Layout {
        const val CONTENT_WEIGHT = 1f
        const val INDICATORS_WEIGHT = 1f
    }

    object Navigation {
        const val NEXT_PAGE_STEP = 1
        val nextButtonContainerHeight = 60.dp
    }

    object Content {
        val descriptionHorizontalPadding = 60.dp
        val imageTitleSpacing = 32.dp
        val titleDescriptionSpacing = 16.dp
    }

    object BottomSection {
        val topSpacing = 16.dp
        val horizontalPadding = 16.dp
        val height = 130.dp
    }

    object Indicator {
        val selectedSize = 20.dp
        val defaultSize = 15.dp
        val horizontalPadding = 10.dp
        val borderWidth = 2.dp
    }

    object Animation {
        const val DURATION_MILLIS = 450
        const val CONTENT_TRANSITION_LABEL = "onboarding_content_transition"
    }
}
