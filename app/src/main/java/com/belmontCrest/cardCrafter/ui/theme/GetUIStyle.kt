package com.belmontCrest.cardCrafter.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme

/** Our UI class that will return the color
 * scheme as well as some custom colors
 * */
class GetUIStyle(
    private var cS: ColorSchemeClass,
    private var isDarkTheme: Boolean,
    private var isDynamicTheme: Boolean,
    private var isCuteTheme: Boolean,
) {
    fun getIsDarkTheme(): Boolean {
        return isDarkTheme
    }

    fun getIsCuteTheme(): Boolean {
        return isCuteTheme
    }

    fun getColorScheme(): ColorScheme {
        return cS.colorScheme
    }

    fun buttonColor(): Color {
        return cS.colorScheme.primaryContainer
    }

    fun iconColor(): Color {
        return cS.colorScheme.onPrimaryContainer
    }

    fun secondaryButtonColor(): Color {
        return cS.colorScheme.secondaryContainer
    }

    fun buttonTextColor(): Color {
        return cS.colorScheme.onSecondaryContainer
    }

    fun titleColor(): Color {
        return cS.colorScheme.onBackground
    }

    fun tertiaryButtonColor(): Color {
        return cS.colorScheme.tertiaryContainer
    }

    fun onTertiaryButtonColor(): Color {
        return cS.colorScheme.onTertiaryContainer
    }

    fun choiceColor(): Color {
        return when {
            isDarkTheme -> {
                if (isDynamicTheme) {
                    blendColors(cS.colorScheme.onTertiary, darkChoiceColor, 0.25f)
                } else {
                    if (isCuteTheme) {
                        darkCuteChoiceColor
                    } else {
                        darkChoiceColor
                    }
                }
            }

            else -> {
                if (isDynamicTheme) {
                    blendColors(cS.colorScheme.onTertiary, choiceColor, 0.25f)
                } else {
                    if (isCuteTheme){
                        cuteChoiceColor
                    } else {
                        choiceColor
                    }
                }
            }
        }
    }

    fun pickedChoice(): Color {
        return when {
            isDarkTheme -> {
                if (isCuteTheme) {
                    darkCutePickedChoice
                } else {
                    darkPickedChoice
                }
            }

            else -> {
                if (isCuteTheme) {
                    cutePickedChoice
                } else {
                    pickedChoice
                }
            }
        }
    }

    fun correctChoice(): Color {
        return when {
            isDarkTheme -> {
                if (isCuteTheme) {
                    darkCuteCorrectChoice
                } else {
                    darkCorrectChoice
                }
            }
            else -> {
                if (isCuteTheme) {
                    cuteCorrectChoice
                } else {
                    correctChoice
                }
            }
        }
    }

    fun onCorrectChoice(): Color {
        return when {
            isCuteTheme -> {
                if (isDarkTheme) {
                    onDarkCuteCorrectChoice
                } else {
                    onCuteCorrectChoice
                }
            }
            else -> cS.colorScheme.onSurfaceVariant
        }
    }

    fun isThemeOn(): Color {
        return if (isDarkTheme) {
            Color.White
        } else {
            darkBackground
        }
    }

    fun altBackground(): Color {
        return when {
            isDarkTheme -> {
                if (isCuteTheme) {
                    darkCuteSecondaryBC
                } else {
                    secondaryDBC
                }
            }

            else -> {
                if (isCuteTheme) {
                    cuteSecondaryBC
                } else {
                    secondaryBC
                }
            }
        }
    }

    fun background(): Color {
        return cS.colorScheme.background
    }

    fun navBarColor(): Color {
        return when {
            isDarkTheme -> {
                if (!isDynamicTheme) {
                    if (isCuteTheme) {
                        darkCuteButton
                    } else {
                        darkNavBar
                    }
                } else {
                    cS.colorScheme.primaryContainer
                }
            }

            else -> {
                if (isCuteTheme && !isDynamicTheme) {
                    cuteButton
                } else {
                    cS.colorScheme.primaryContainer
                }
            }
        }
    }

    fun dialogColor(): Color {
        return when {
            isDarkTheme -> {
                if (isCuteTheme) {
                    Color(70, 20, 50, 240)
                } else {
                    dialogDarkBackground
                }
            }

            else -> {
                if (isCuteTheme) {
                    Color(255, 230, 240, 240)
                } else {
                    dialogLightBackground
                }
            }
        }
    }

    fun importingDeckColor(): Color {
        return when {
            isDarkTheme -> {
                if (isCuteTheme) {
                    Color(100, 40, 80)
                } else {
                    Color.DarkGray
                }
            }

            else -> {
                if (isCuteTheme) {
                    Color(255, 192, 203, 180)
                } else {
                    Color.Gray
                }
            }
        }
    }

    fun blendColors(base: Color, overlay: Color, overlayAlpha: Float): Color {
        // overlayAlpha in [0..1]
        val r = overlay.red * overlayAlpha + base.red * (1 - overlayAlpha)
        val g = overlay.green * overlayAlpha + base.green * (1 - overlayAlpha)
        val b = overlay.blue * overlayAlpha + base.blue * (1 - overlayAlpha)
        val a = overlay.alpha * overlayAlpha + base.alpha * (1 - overlayAlpha)
        return Color(r, g, b, a)
    }
}