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
            </style>
            """.trimIndent()
    }

    const val COLOR_LOCALE = "#%06X"
    const val COLOR_FORMAT = 0xFFFFFF
}


//object CssStyle {
//    fun getStyle(context: Context): String {
//        val mainFontFamilyPath = "file:///res/font/ys_display_regular.ttf"
//        val headerFontFamilyPath = "file:///res/font/ys_display_medium.ttf"
//        val boldFontFamilyPath = "file:///res/font/ys_display_bold.ttf"
//        val colorInt = ContextCompat.getColor(context, R.color.htmlText)
//        val colorHex = String.format(Locale.US, "#%06X", 0xFFFFFF and colorInt)
//        val fontSize = 16
//
//
//        return """
//         <style type="text/css">
//         @font-face {
//             font-family: 'CustomFont';
//             src: url('$mainFontFamilyPath')
//         }
//
//         @font-face {
//            font-family: 'HeaderFont';
//            src: url('$headerFontFamilyPath')
//         }
//
//         body {
//             font-family: 'CustomFont', 'Arial', sans-serif;
//             font-size: ${fontSize}px;
//             color: $colorHex;
//         }
//         h1 {
//             font-family: 'HeaderFont', 'Arial', sans-serif;
//             font-size: 22px;
//             color: $colorHex;
//         }
//         </style>
//         """.trimIndent()
//    }
//}
