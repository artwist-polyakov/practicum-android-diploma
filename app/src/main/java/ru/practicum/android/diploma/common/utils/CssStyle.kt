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
            <link href="https://db.onlinewebfonts.com/c/ee1831a251980402ea7540f4caf2c63f?family=YS+Display+Medium+Regular" rel="stylesheet">
            <link href="https://db.onlinewebfonts.com/c/dcd470f69035d68d8d0aba8ed7ba9d6d?family=YS+Display+Regular+Regular" rel="stylesheet">
            <style type="text/css">
                body {
                    font-weight: lighter;
                    font-family: 'YS Display Regular Regular';
                    font-size: ${fontSizeBody}px;
                    color: $mainColorHex;
                }
                .title {
                    font-family: 'YS Display Medium Regular';
                    font-size: ${fontSizeHeader}px;
                    color: $mainColorHex;
                }
                a {
                    font-family: 'YS Display Regular Regular';
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
                    font-family: 'YS Display Medium Regular';
                    font-size: ${fontSizeBody}px;
                }
                ul {
                    margin: 0px;
                    padding-left: 16px;
                }
                ol {
                    margin: 12px;
                    padding-left: 24px;
                }
            </style>
            """.trimIndent()
    }

    const val COLOR_LOCALE = "#%06X"
    const val COLOR_FORMAT = 0xFFFFFF
}
