package com.equationl.common.gameLogic

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.equationl.common.gameView.BlackChess
import com.equationl.common.gameView.ChessBoardScale
import com.equationl.common.gameView.NoneChess
import com.equationl.common.gameView.WhiteChess
import kotlin.math.floor

fun initChessBoard(): Array<ByteArray> {
    return arrayOf(
        ByteArray(8) { NoneChess },
        ByteArray(8) { NoneChess },
        ByteArray(8) { NoneChess },
        ByteArray(8)
        {
            when (it) {
                3 -> WhiteChess
                4 -> BlackChess
                else -> NoneChess
            }
        },
        ByteArray(8)
        {
            when (it) {
                3 -> BlackChess
                4 -> WhiteChess
                else -> NoneChess
            }
        },
        ByteArray(8) { NoneChess },
        ByteArray(8) { NoneChess },
        ByteArray(8) { NoneChess },
    )
}

fun getChessCoordinate(
    size: IntSize,
    offset: Offset,
    onClick: (row: Int, col: Int) -> Unit
) {
    // 棋盘内容边界
    val chessBoardSide = size.width * ChessBoardScale
    // 棋盘线长
    val lineLength = size.width - chessBoardSide * 2
    // 棋盘格子尺寸
    val boxSize = lineLength / 8

    if (offset.x in chessBoardSide..size.width-chessBoardSide
        && offset.y in chessBoardSide..size.width-chessBoardSide) { // 判断是否在有效范围内
        // 计算点击坐标
        val row = floor((offset.x - chessBoardSide) / boxSize).toInt()
        val col = floor((offset.y - chessBoardSide) / boxSize).toInt()

        // 回调点击函数
        onClick(row, col)
    }
}

//拷贝棋盘二维数组
fun copyBinaryArray(src: Array<ByteArray>, dest: Array<ByteArray>) {
    for (i in 0 until 8) {
        System.arraycopy(src[i], 0, dest[i], 0, 8)
    }
}