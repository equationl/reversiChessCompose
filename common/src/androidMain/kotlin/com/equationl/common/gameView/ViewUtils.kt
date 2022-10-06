package com.equationl.common.gameView

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.window.Dialog
import com.equationl.common.R

@Composable
actual fun loadImageBitmap(resourceName: Resource): ImageBitmap {
    val resId = when (resourceName) {
        Resource.WhiteChess -> R.drawable.white_chess
        Resource.BlackChess -> R.drawable.black_chess
        Resource.Background -> R.drawable.mood
    }
    return ImageBitmap.imageResource(id = resId)
}

@Composable
actual fun chessboardSize(): Int {
    return LocalConfiguration.current.screenWidthDp
}

@Composable
actual fun BaseDialog(
    onCloseRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onCloseRequest,
        content = content
    )
}