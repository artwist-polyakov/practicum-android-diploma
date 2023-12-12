package ru.practicum.android.diploma.common.utils

import android.content.Context
import androidx.core.content.ContextCompat
import ru.practicum.android.diploma.R
import java.util.Locale

object CssStyle {
    fun getStyle(context: Context): String {
        val mainColorInt = ContextCompat.getColor(context, R.color.htmlText)
        val mainColorHex = String.format(Locale.US, COLOR_LOCALE, COLOR_FORMAT and mainColorInt)

        val blueColorInt = ContextCompat.getColor(context, R.color.blue)
        val blueColorHex = String.format(Locale.US, COLOR_LOCALE, COLOR_FORMAT and blueColorInt)
        val fontSizeBody = 16
        val fontSizeHeader = 22

        return """
            <style type="text/css">
                font-face {
                   font-family: 'custom_regular';
                   src: url('file:///android_asset/fonts/ys_display_regular.ttf') format('truetype');
                 }
                 @font-face {
                   font-family: 'custom_medium';
                   src: url('file:///android_asset/fonts/ys_display_medium.ttf') format('truetype');
                 }
                 @font-face {
                   font-family: 'custom_bold
                   src: url('file:///android_asset/fonts/ys_display_bold.ttf') format('truetype');
                 }
                body {
                    font-family: 'custom_regular';
                    font-size: ${fontSizeBody}px;
                    color: $mainColorHex;
                }
                .title {
                    font-family: 'custom_medium';
                    font-size: ${fontSizeHeader}px;
                    color: $mainColorHex;
                }
                strong {
                    font-weight: bold;
                    font-family: 'custom_regular';
                    font-size: ${fontSizeBody}px;
                    color: $mainColorHex;
                }
                b {
                    font-weight: bold;
                    font-family: 'custom_regular';
                    font-size: ${fontSizeBody}px;
                    color: $mainColorHex;
                }
                a {
                    font-family: 'custom_regular';
                    font-size: ${fontSizeBody}px;
                    color: $blueColorHex; 
                    text-decoration: none;
                }
                .margin {
                    margin-top: 16px;
                    display: block;
                }
                .margin-bottom {
                    margin-bottom: 16px;
                }
                .contact-info {
                    font-family: 'custom_medium';
                    font-size: ${fontSizeBody}px;
                }
                ul ol {
                    margin: 12px;
                    padding-left: 16px;
                }
                li {
                    padding-left: 8px;
                }
            </style>
        """.trimIndent()
    }

    const val COLOR_LOCALE = "#%06X"
    const val COLOR_FORMAT = 0xFFFFFF
}
