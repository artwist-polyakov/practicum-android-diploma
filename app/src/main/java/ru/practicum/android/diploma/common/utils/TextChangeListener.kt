package ru.practicum.android.diploma.common.utils

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.practicum.android.diploma.R

fun TextInputEditText.setupTextChangeListener(textInput: TextInputLayout, forwardIcon: ImageView, context: Context) {
    val defaultHintColor = ContextCompat.getColor(context, R.color.inputTextHint)
    val activeFilterHintColor = ContextCompat.getColor(context, R.color.red)
    val forwardIconResId = R.drawable.arrow_forward_24px
    val crossIconResId = R.drawable.ic_cross_24px

    this.doOnTextChanged { text, _, _, _ ->
        val hintColor = if (text.isNullOrEmpty()) {
            forwardIcon.setImageResource(forwardIconResId)
            forwardIcon.isClickable = false
            defaultHintColor
        } else {
            forwardIcon.setImageResource(crossIconResId)
            forwardIcon.isClickable = true
            activeFilterHintColor
        }
        textInput.hintTextColor = ColorStateList.valueOf(hintColor)
    }
}
