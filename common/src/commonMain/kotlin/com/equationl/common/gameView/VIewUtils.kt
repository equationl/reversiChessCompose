package com.equationl.common.gameView

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap


/**
 * 读取本地图片
 * */
expect fun loadImageBitmap(resourceName: Resource): ImageBitmap

/**
 * 获取棋盘尺寸
 * */
expect fun chessboardSize(): Int

/**
 * 由于基础 Dialog 的某个参数的参数名在 compose-jb 和 jetpack-compose 不一样，所以需要给 Dialog 做一个差异化处理
 * */
expect fun BaseDialog(onCloseRequest: () -> Unit, content: @Composable (() -> Unit))

