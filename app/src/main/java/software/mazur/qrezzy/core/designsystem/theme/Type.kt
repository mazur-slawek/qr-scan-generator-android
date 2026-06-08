package software.mazur.qrezzy.core.designsystem.theme

import androidx.compose.material3.Typography

val Typography = Typography().run {
    copy(
        displayLarge = displayLarge.copy(fontFamily = QrezzyFontFamily), // Def: 57sp | 64sp | Regular
        displayMedium = displayMedium.copy(fontFamily = QrezzyFontFamily), // Def: 45sp | 52sp | Regular
        displaySmall = displaySmall.copy(fontFamily = QrezzyFontFamily), // Def: 36sp | 44sp | Regular
        //
        headlineLarge = headlineLarge.copy(fontFamily = QrezzyFontFamily), // Def: 32sp | 40sp | Regular
        headlineMedium = headlineMedium.copy(fontFamily = QrezzyFontFamily), // Def: 28sp | 36sp | Regular
        headlineSmall = headlineSmall.copy(fontFamily = QrezzyFontFamily), // Def: 24sp | 32sp | Regular
        //
        titleLarge = titleLarge.copy(fontFamily = QrezzyFontFamily), // Def: 22sp | 28sp | Regular
        titleMedium = titleMedium.copy(fontFamily = QrezzyFontFamily), // Def: 16sp | 24sp | Medium
        titleSmall = titleSmall.copy(fontFamily = QrezzyFontFamily), // Def: 14sp | 20sp | Medium
        //
        bodyLarge = bodyLarge.copy(fontFamily = QrezzyFontFamily), // Def: 16sp | 24sp | Regular
        bodyMedium = bodyMedium.copy(fontFamily = QrezzyFontFamily), // Def: 14sp | 20sp | Regular
        bodySmall = bodySmall.copy(fontFamily = QrezzyFontFamily), // Def: 12sp | 16sp | Regular
        //
        labelLarge = labelLarge.copy(fontFamily = QrezzyFontFamily), // Def: 14sp | 20sp | Medium
        labelMedium = labelMedium.copy(fontFamily = QrezzyFontFamily), // Def: 12sp | 16sp | Medium
        labelSmall = labelSmall.copy(fontFamily = QrezzyFontFamily), // Def: 11sp | 16sp | Medium
    )
}