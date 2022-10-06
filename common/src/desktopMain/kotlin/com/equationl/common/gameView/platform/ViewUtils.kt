package com.equationl.common.gameView

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.Dialog

actual fun loadImageBitmap(resourceName: Resource): ImageBitmap {
    val resPath = when (resourceName) {
        Resource.WhiteChess -> "white_chess.png"
        Resource.BlackChess -> "black_chess.png"
        Resource.Background -> "mood.png"
    }

    return useResource(resPath) { androidx.compose.ui.res.loadImageBitmap(it) }
}

actual fun chessboardSize(): Int {
    return 300
}

@Composable
actual fun BaseDialog(
    onCloseRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onCloseRequest = onCloseRequest,
        content = {
            content()
        }
    )
}