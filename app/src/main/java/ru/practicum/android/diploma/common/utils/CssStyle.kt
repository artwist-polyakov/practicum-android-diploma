package ru.practicum.android.diploma.common.utils

object CssStyle {
    fun getStyle(colorHex: String): String {
        return "<link href=\"https://db.onlinewebfonts.com/c/" +
            "dcd470f69035d68d8d0aba8ed7ba9d6d?family=YS+Display+Regular+Regular\" " +
            "rel=\"stylesheet\">\n" +
            "<style type=\"text/css\">" +
            "body{" +
            "font-weight:lighter;" +
            "font-family:'YS Display Regular Regular';" +
            "font-size:16px;" +
            "color:$colorHex" +
            "}" +
            "</style>"
    }
}
