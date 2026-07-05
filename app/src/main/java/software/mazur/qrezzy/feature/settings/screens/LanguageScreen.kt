package software.mazur.qrezzy.feature.settings.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyAnimatedStars
import software.mazur.qrezzy.core.designsystem.components.QrezzyListItem
import software.mazur.qrezzy.core.designsystem.components.QrezzyListSection
import software.mazur.qrezzy.core.designsystem.components.QrezzyRadioButton
import software.mazur.qrezzy.core.designsystem.components.QrezzyTopBar
import software.mazur.qrezzy.core.designsystem.theme.QrezzyMintDark
import software.mazur.qrezzy.domain.settings.model.AppLanguage

@Composable
fun LanguageScreen(selectedLanguage: AppLanguage, onLanguageSelected: (AppLanguage) -> Unit, onBackClick: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = LanguageScreenDefaults.horizontalPadding)) {
        QrezzyTopBar(onBackClick = onBackClick, titleResId = R.string.language_screen_title)
        QrezzyAnimatedStars(modifier = Modifier.fillMaxSize(), starsCount = LanguageScreenDefaults.STARS_COUNT) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(LanguageScreenDefaults.itemSpacing)
            ) {
                item {
                    Image(
                        painter = painterResource(R.drawable.qrezzy_mascot_language),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = LanguageScreenDefaults.imageTopPadding)
                            .height(LanguageScreenDefaults.imageHeight),
                        contentDescription = null
                    )
                }
                item {
                    QrezzyListSection(title = stringResource(R.string.language_screen_subtitle)) {
                        Column {
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.language_english),
                                iconSize = LanguageScreenDefaults.iconSize,
                                title = stringResource(R.string.language_english),
                                trailing = {
                                    QrezzyRadioButton(
                                        selected = selectedLanguage == AppLanguage.ENGLISH,
                                        onClick = { onLanguageSelected(AppLanguage.ENGLISH) }
                                    )
                                },
                                iconBackgroundColor = if (selectedLanguage == AppLanguage.ENGLISH) {
                                    QrezzyMintDark
                                } else {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f)
                                }
                            )
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.language_polish),
                                iconSize = LanguageScreenDefaults.iconSize,
                                title = stringResource(R.string.language_polish),
                                trailing = {
                                    QrezzyRadioButton(
                                        selected = selectedLanguage == AppLanguage.POLISH,
                                        onClick = { onLanguageSelected(AppLanguage.POLISH) }
                                    )
                                },
                                iconBackgroundColor = if (selectedLanguage == AppLanguage.POLISH) {
                                    QrezzyMintDark
                                } else {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f)
                                }
                            )
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.language_german),
                                iconSize = LanguageScreenDefaults.iconSize,
                                title = stringResource(R.string.language_german),
                                trailing = {
                                    QrezzyRadioButton(
                                        selected = selectedLanguage == AppLanguage.GERMAN,
                                        onClick = { onLanguageSelected(AppLanguage.GERMAN) }
                                    )
                                },
                                iconBackgroundColor = if (selectedLanguage == AppLanguage.GERMAN) {
                                    QrezzyMintDark
                                } else {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f)
                                }
                            )
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.language_ukrainian),
                                iconSize = LanguageScreenDefaults.iconSize,
                                title = stringResource(R.string.language_ukrainian),
                                trailing = {
                                    QrezzyRadioButton(
                                        selected = selectedLanguage == AppLanguage.UKRAINIAN,
                                        onClick = { onLanguageSelected(AppLanguage.UKRAINIAN) }
                                    )
                                },
                                iconBackgroundColor = if (selectedLanguage == AppLanguage.UKRAINIAN) {
                                    QrezzyMintDark
                                } else {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f)
                                }
                            )
                            QrezzyListItem(
                                iconPainter = painterResource(R.drawable.language_italian),
                                iconSize = LanguageScreenDefaults.iconSize,
                                title = stringResource(R.string.language_italian),
                                trailing = {
                                    QrezzyRadioButton(
                                        selected = selectedLanguage == AppLanguage.ITALIAN,
                                        onClick = { onLanguageSelected(AppLanguage.ITALIAN) }
                                    )
                                },
                                iconBackgroundColor = if (selectedLanguage == AppLanguage.ITALIAN) {
                                    QrezzyMintDark
                                } else {
                                    MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f)
                                }
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(LanguageScreenDefaults.itemSpacing)) }
            }
        }
    }
}

private object LanguageScreenDefaults {
    const val STARS_COUNT = 150
    val horizontalPadding = 16.dp
    val itemSpacing = 16.dp
    val imageTopPadding = 16.dp
    val imageHeight = 150.dp
    val iconSize = 40.dp
}
