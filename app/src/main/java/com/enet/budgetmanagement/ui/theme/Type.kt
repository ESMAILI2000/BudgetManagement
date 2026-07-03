package com.enet.budgetmanagement.ui.theme

import androidx.compose.ui.unit.em
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.enet.budgetmanagement.R
import java.util.Locale

// فونت‌های انگلیسی (Inter)
private val InterFontFamily = FontFamily(
    Font(R.font.dsansmono, FontWeight.Normal),
    Font(R.font.dsansmono_oblique, FontWeight.Medium),
    Font(R.font.dsansmono_boldoblique, FontWeight.SemiBold),
    Font(R.font.dsansmonobold, FontWeight.Bold)
)

// فونت‌های فارسی (IRANSans)
private val IranSansFontFamily = FontFamily(
    Font(R.font.vazir, FontWeight.Normal),
    Font(R.font.vazir_medium, FontWeight.Medium),
    Font(R.font.vazir_thin, FontWeight.SemiBold),
    Font(R.font.vazir_bold, FontWeight.Bold)
)

@Composable
fun getAppTypography(): Typography {
    val isPersian = isPersianLocale()

    val defaultFontFamily = if (isPersian) IranSansFontFamily else InterFontFamily

    return Typography(
        displayLarge = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            lineHeight = 48.sp,
            letterSpacing = (-0.02).em
        ),
        headlineLarge = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 40.sp
        ),
        headlineMedium = TextStyle( // برای موبایل
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp
        ),
        titleMedium = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 24.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        labelLarge = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.05.em
        ),
        labelSmall = TextStyle(
            fontFamily = defaultFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            lineHeight = 12.sp
        )
    )
}

@Composable
private fun isPersianLocale(): Boolean {
    val locale = LocalConfiguration.current.locales.get(0)
    return locale.language == "fa"
}