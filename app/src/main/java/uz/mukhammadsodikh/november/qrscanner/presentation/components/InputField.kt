package uz.mukhammadsodikh.november.qrscanner.presentation.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import uz.mukhammadsodikh.november.qrscanner.core.design.components.InputField

@Composable
fun MyInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    unfocusOnOutsideTap: Boolean = false
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .then(
                if (unfocusOnOutsideTap) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                focusManager.clearFocus()
                            }
                        )
                    }
                } else Modifier
            )
    ) {
        InputField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            maxLines = maxLines,
            singleLine = singleLine,
            keyboardType = keyboardType,
            imeAction = imeAction
        )
    }
}
