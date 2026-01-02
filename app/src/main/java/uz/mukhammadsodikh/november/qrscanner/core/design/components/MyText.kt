package uz.mukhammadsodikh.november.qrscanner.core.design.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun MyText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

/* ---------- TITLE ---------- */

@Composable
fun TitleText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = MaterialTheme.typography.displayMedium,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign? = null
) {
    MyText(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

/* ---------- SUBTITLE ---------- */

@Composable
fun SubtitleText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null
) {
    MyText(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign
    )
}

/* ---------- CAPTION ---------- */

@Composable
fun CaptionText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null
) {
    MyText(
        text = text,
        modifier = modifier,
        color = color,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}
