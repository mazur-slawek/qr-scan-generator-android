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
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedBackground
import software.mazur.qrezzy.core.designsystem.components.QrezzyButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyNextButton
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMint
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPink
import software.mazur.qrezzy.core.designsystem.theme.QrezzyPurple
import software.mazur.qrezzy.core.designsystem.theme.QrezzyYellow
import software.mazur.qrezzy.feature.onboarding.model.OnboardingItem
import software.mazur.qrezzy.feature.onboarding.model.OnboardingItemType

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit = {},
) {
    val currentPageIndex = remember {
        mutableIntStateOf(OnboardingDefaults.INITIAL_PAGE_INDEX)
    }
    val currentItem = onboardingItems[currentPageIndex.intValue]
    val isLastPage = currentPageIndex.intValue == onboardingItems.lastIndex

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        key(currentItem.accentColor) {
            QrezzyAnimatedBackground(
                leftColor = currentItem.accentColor,
                rightColor = currentItem.accentColor,
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            AnimatedContent(
                targetState = currentPageIndex.intValue,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(
                            durationMillis = OnboardingDefaults.PAGE_TRANSITION_DURATION_MS,
                        ),
                        initialOffsetX = {fullWidth -> fullWidth},
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = OnboardingDefaults.PAGE_TRANSITION_DURATION_MS,
                        ),
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(
                            durationMillis = OnboardingDefaults.PAGE_TRANSITION_DURATION_MS,
                        ),
                        targetOffsetX = {fullWidth -> -fullWidth},
                    ) + fadeOut(
                        animationSpec = tween(
                            durationMillis = OnboardingDefaults.PAGE_TRANSITION_DURATION_MS,
                        ),
                    )
                },
                label = OnboardingDefaults.CONTENT_TRANSITION_LABEL,
                modifier = Modifier.weight(OnboardingDefaults.CONTENT_WEIGHT),
            ) {pageIndex ->
                OnboardingContent(
                    item = onboardingItems[pageIndex],
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Spacer(
                modifier = Modifier.height(OnboardingDefaults.BOTTOM_SECTION_TOP_SPACING),
            )

            OnboardingBottomSection(
                items = onboardingItems,
                currentItem = currentItem,
                isLastPage = isLastPage,
                onNextClick = {
                    currentPageIndex.intValue += OnboardingDefaults.NEXT_PAGE_STEP
                },
                onGetStartedClick = onGetStartedClick,
            )
        }
    }
}

@Composable
private fun OnboardingContent(
    item: OnboardingItem,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = item.iconResId),
                contentDescription = null,
            )

            Spacer(
                modifier = Modifier.height(OnboardingDefaults.IMAGE_TITLE_SPACING),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = item.titleResId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
            )

            Spacer(
                modifier = Modifier.height(OnboardingDefaults.TITLE_DESCRIPTION_SPACING),
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = OnboardingDefaults.DESCRIPTION_HORIZONTAL_PADDING),
                text = stringResource(id = item.descriptionResId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
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
    onGetStartedClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = OnboardingDefaults.BOTTOM_SECTION_HORIZONTAL_PADDING)
            .height(OnboardingDefaults.BOTTOM_SECTION_HEIGHT),
        verticalAlignment = Alignment.Top,
    ) {
        if (isLastPage) {
            QrezzyButton(
                onClick = onGetStartedClick,
                text = stringResource(id = R.string.button_get_started),
                rightIcon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            )
        } else {
            OnboardingNavigationSection(
                items = items,
                currentItem = currentItem,
                nextIcon = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                onNextClick = onNextClick,
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
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        OnboardingPageIndicators(
            items = items,
            currentItem = currentItem,
            modifier = Modifier
                .height(OnboardingDefaults.NEXT_BUTTON_CONTAINER_HEIGHT)
                .weight(OnboardingDefaults.INDICATORS_WEIGHT),
        )

        QrezzyNextButton(
            color = currentItem.accentColor,
            icon = nextIcon,
            onClick = onNextClick,
        )
    }
}

@Composable
private fun OnboardingPageIndicators(
    items: List<OnboardingItem>,
    currentItem: OnboardingItem,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        items.forEach {item ->
            val isSelected = item == currentItem

            Spacer(
                modifier = Modifier
                    .padding(horizontal = OnboardingDefaults.INDICATOR_HORIZONTAL_PADDING)
                    .size(
                        if (isSelected) {
                            OnboardingDefaults.SELECTED_INDICATOR_SIZE
                        } else {
                            OnboardingDefaults.DEFAULT_INDICATOR_SIZE
                        },
                    )
                    .background(
                        color = if (isSelected) {
                            currentItem.accentColor
                        } else {
                            OnboardingDefaults.INACTIVE_INDICATOR_COLOR
                        },
                        shape = CircleShape,
                    )
                    .border(
                        width = OnboardingDefaults.INDICATOR_BORDER_WIDTH,
                        color = if (isSelected) {
                            Color.Black
                        } else {
                            OnboardingDefaults.INACTIVE_INDICATOR_COLOR
                        },
                        shape = CircleShape,
                    ),
            )
        }
    }
}

private val onboardingItems = listOf(
    OnboardingItem(
        type = OnboardingItemType.SCAN,
        accentColor = QrezzyMint,
        iconResId = R.drawable.onboarding_scan_icon,
        titleResId = R.string.onboarding_scan_title,
        descriptionResId = R.string.onboarding_scan_description,
    ),
    OnboardingItem(
        type = OnboardingItemType.EDIT,
        accentColor = QrezzyYellow,
        iconResId = R.drawable.onboarding_generate_icon,
        titleResId = R.string.onboarding_generate_title,
        descriptionResId = R.string.onboarding_generate_description,
    ),
    OnboardingItem(
        type = OnboardingItemType.HISTORY,
        accentColor = QrezzyPink,
        iconResId = R.drawable.onboarding_history_icon,
        titleResId = R.string.onboarding_history_title,
        descriptionResId = R.string.onboarding_history_description,
    ),
    OnboardingItem(
        type = OnboardingItemType.SHARE,
        accentColor = QrezzyPurple,
        iconResId = R.drawable.onboarding_share_icon,
        titleResId = R.string.onboarding_share_title,
        descriptionResId = R.string.onboarding_share_description,
    ),
    OnboardingItem(
        type = OnboardingItemType.SUMMARY,
        accentColor = QrezzyMint,
        iconResId = R.drawable.onboarding_summary_icon,
        titleResId = R.string.onboarding_summary_title,
        descriptionResId = R.string.onboarding_summary_description,
    ),
)

private object OnboardingDefaults {
    const val INITIAL_PAGE_INDEX = 0
    const val NEXT_PAGE_STEP = 1
    const val CONTENT_WEIGHT = 1f
    const val INDICATORS_WEIGHT = 1f
    val DESCRIPTION_HORIZONTAL_PADDING = 60.dp
    val IMAGE_TITLE_SPACING = 32.dp
    val TITLE_DESCRIPTION_SPACING = 16.dp
    val BOTTOM_SECTION_TOP_SPACING = 16.dp
    val BOTTOM_SECTION_HORIZONTAL_PADDING = 32.dp
    val BOTTOM_SECTION_HEIGHT = 130.dp
    val NEXT_BUTTON_CONTAINER_HEIGHT = 60.dp
    val SELECTED_INDICATOR_SIZE = 20.dp
    val DEFAULT_INDICATOR_SIZE = 15.dp
    val INDICATOR_HORIZONTAL_PADDING = 10.dp
    val INDICATOR_BORDER_WIDTH = 2.dp
    val INACTIVE_INDICATOR_COLOR = Color.Gray
    const val PAGE_TRANSITION_DURATION_MS = 450
    const val CONTENT_TRANSITION_LABEL = "onboarding_content_transition"
}