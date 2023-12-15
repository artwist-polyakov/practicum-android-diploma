package ru.practicum.android.diploma.common.utils

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.practicum.android.diploma.R

/**
 * Метод расширение над TextInputEditText управляет цветом подсказки и изображением закрепленным над полем.
 * Принимает элементы TextInputLayout, иконка перехода на фрагмент выбора значения
 * (эта иконка заменяется на ic_cross_24px), контекст и лямбду для настройки дополнительных действий
 * Дополнительно настраивает свойства инпута - отключает слушатель ввода, установку фокуса и курсора.
 */
fun TextInputEditText.setupTextChangeListener(
    textInput: TextInputLayout,
    forwardIcon: ImageView,
    context: Context,
    afterTextChanged: () -> Unit = {}
) {
    val defaultHintColor = ContextCompat.getColor(context, R.color.gray)
    val activeFilterHintColor = ContextCompat.getColor(context, R.color.textHintApearence)
    filterSettings(this)

    this.doOnTextChanged { text, _, _, _ ->
        val hintColor = if (text.toString().isEmpty()) {
            forwardIcon.setImageResource(R.drawable.arrow_forward_24px)
            forwardIcon.isClickable = false
            defaultHintColor
        } else {
            forwardIcon.setImageResource(R.drawable.ic_cross_24px)
            forwardIcon.isClickable = true
            activeFilterHintColor
        }
        textInput.defaultHintTextColor = ColorStateList.valueOf(hintColor)
    }
    afterTextChanged()
}

private fun filterSettings(editText: TextInputEditText) {
    editText.isFocusable = false
    editText.isCursorVisible = false
    editText.keyListener = null
}
