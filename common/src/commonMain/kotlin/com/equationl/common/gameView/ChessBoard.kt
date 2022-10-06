package com.equationl.common.gameView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.equationl.common.gameLogic.getChessCoordinate
import com.equationl.common.viewModel.AiLevel

@Composable
fun GameView(
    chessBoard: Array<ByteArray>,
    playerChessNum: Int,
    aiChessNum: Int,
    gameState: Int,
    aiLevel: AiLevel,
    whoFirst: Int,
    onClickChess: (row: Int, col: Int) -> Unit,
    onRequestNewGame: () -> Unit,
    onNewGame: (whoFirst: Int, aiLevel: AiLevel) -> Unit,
    onTip: () -> Unit
) {
    val screenWidth = chessboardSize()
    val playerChessBitmap =
        if (whoFirst == PLayerRound) loadImageBitmap(resourceName = Resource.BlackChess)
        else loadImageBitmap(resourceName = Resource.WhiteChess)

    val aiChessBitmap =
        if (whoFirst == AiRound) loadImageBitmap(resourceName = Resource.BlackChess)
        else loadImageBitmap(resourceName = Resource.WhiteChess)

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // 顶部信息栏
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .background(if (gameState == PLayerRound) Color.LightGray else Color.Unspecified),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "您",
                    Modifier.padding(bottom = 8.dp),
                    fontSize = 18.sp
                )
                Row(
                    Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = playerChessBitmap,
                        contentDescription = "black")
                    Text(text = "x$playerChessNum", Modifier.padding(2.dp))
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(0.3f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "VS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(0.3f)
                    .background(if (gameState == AiRound) Color.LightGray else Color.Unspecified),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "电脑(${aiLevel.showName})",
                    Modifier.padding(bottom = 8.dp),
                    fontSize = 18.sp
                )
                Row(
                    Modifier.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        bitmap = aiChessBitmap,
                        contentDescription = "black")
                    Text(text = "x$aiChessNum", Modifier.padding(2.dp))
                }
            }
        }

        // 游戏棋盘
        ReversiView(
            modifier = Modifier.size(screenWidth.dp),
            chessBoard = chessBoard,
            onClick = onClickChess
        )

        // 底部控制按钮
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { onRequestNewGame() }) {
                Text(text = "重新开始")
            }
            Button(onClick = { onTip() }) {
                Text(text = "提示")
            }
        }
    }

    // 游戏结束弹窗
    if (gameState >= 3) {
        RequestNewGameDialog(gameState) {
            onRequestNewGame()
        }
    }

    // 新游戏弹窗
    if (gameState == NeedNewGame) {
        NewGameDialog(onStart = { whoFirst: Int, aiLevel: AiLevel ->
            onNewGame(whoFirst, aiLevel)
        })
    }
}

/**
 * @param modifier 必须指定大小，且长宽必须一致
 * @param chessBoard 棋盘信息数组
 * */
@Composable
fun ReversiView(
    modifier: Modifier,
    chessBoard: Array<ByteArray>,
    onClick: (row: Int, col: Int) -> Unit
) {
    val backgroundImage = loadImageBitmap(resourceName = Resource.Background)
    val whiteChess = loadImageBitmap(resourceName = Resource.WhiteChess)
    val blackChess = loadImageBitmap(resourceName = Resource.BlackChess)

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { offset: Offset ->
                    getChessCoordinate(
                        size, offset, onClick
                    )
                }
            )
        }
    ) {
        // 棋盘内容边界
        val chessBoardSide = size.width * ChessBoardScale
        // 棋盘线长
        val lineLength = size.width - chessBoardSide * 2
        // 棋盘格子尺寸
        val boxSize = lineLength / 8

        // 画棋盘背景
        drawImage(
            image = backgroundImage,
            srcOffset = IntOffset(0, 0),
            dstSize = IntSize(size.width.toInt(), size.width.toInt())
        )

        // 画棋盘线
        for (i in 0..8) {
            // 横线
            drawLine(
                color = Color.Black,
                start = Offset(chessBoardSide, chessBoardSide + i * boxSize),
                end = Offset(lineLength+chessBoardSide, chessBoardSide + i * boxSize)
            )
            // 竖线
            drawLine(
                color = Color.Black,
                start = Offset(chessBoardSide + i * boxSize, chessBoardSide),
                end = Offset(chessBoardSide + i * boxSize, lineLength+chessBoardSide)
            )
        }

        // 画棋子
        for (col in 0 until 8) {
            for (row in 0 until 8) {
                if (chessBoard[col][row] == BlackChess) {  // 黑子
                    drawImage(
                        image = blackChess,
                        srcOffset = IntOffset(0, 0),
                        dstOffset = IntOffset(
                            (chessBoardSide + col * boxSize).toInt(),
                            (chessBoardSide + row * boxSize).toInt()
                        ),
                        dstSize = IntSize(boxSize.toInt(), boxSize.toInt())
                    )
                }
                if (chessBoard[col][row] == WhiteChess) {  // 白子
                    drawImage(
                        image = whiteChess,
                        srcOffset = IntOffset(0, 0),
                        dstOffset = IntOffset(
                            (chessBoardSide + col * boxSize).toInt(),
                            (chessBoardSide + row * boxSize).toInt()
                        ),
                        dstSize = IntSize(boxSize.toInt(), boxSize.toInt())
                    )
                }
            }
        }
    }
}

@Composable
private fun RequestNewGameDialog(gameState: Int, onStart: () -> Unit) {
    val text = when (gameState) {
        3 -> "恭喜，你赢了！"
        4 -> "抱歉，电脑赢了"
        5 -> "游戏结束，这次是平局哦"
        else -> "游戏结束"
    }

    BaseDialog(onCloseRequest = { }) {
        Card(backgroundColor = Color.White) {
            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = text, fontSize = 24.sp, modifier = Modifier.padding(vertical = 6.dp))
                Button(onClick = { onStart() }) {
                    Text(text = "重新开始")
                }
            }
        }
    }
}

@Composable
private fun NewGameDialog(onStart: (whoFirst: Int, aiLevel: AiLevel) -> Unit) {
    var isPLayerFirst by remember { mutableStateOf(true) }
    var aiLevel by remember { mutableStateOf(AiLevel.Level1) }

    BaseDialog(onCloseRequest = {  }) {
        Card(backgroundColor = Color.White) {
            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isPLayerFirst, onCheckedChange = {isPLayerFirst = !isPLayerFirst})
                    Text(text = "玩家先手")
                }

                Text(text = "AI难度")

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 6.dp)) {
                    RadioButton(
                        selected = aiLevel == AiLevel.Level1,
                        onClick = { aiLevel = AiLevel.Level1 },
                        modifier = Modifier.size(26.dp)
                    )
                    Text(text = AiLevel.Level1.showName)

                    RadioButton(
                        selected = aiLevel == AiLevel.Level2,
                        onClick = { aiLevel = AiLevel.Level2 },
                        modifier = Modifier.size(26.dp)
                    )
                    Text(text = AiLevel.Level2.showName)

                    RadioButton(
                        selected = aiLevel == AiLevel.Level3,
                        onClick = { aiLevel = AiLevel.Level3 },
                        modifier = Modifier.size(26.dp)
                    )
                    Text(text = AiLevel.Level3.showName)

                    RadioButton(
                        selected = aiLevel == AiLevel.Level4,
                        onClick = { aiLevel = AiLevel.Level4 },
                        modifier = Modifier.size(26.dp)
                    )
                    Text(text = AiLevel.Level4.showName)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = aiLevel == AiLevel.Level5,
                        onClick = { aiLevel = AiLevel.Level5 },
                        modifier = Modifier.size(26.dp)
                    )
                    Text(text = AiLevel.Level5.showName)

                    RadioButton(
                        selected = aiLevel == AiLevel.Level6,
                        onClick = { aiLevel = AiLevel.Level6 },
                        modifier = Modifier.size(26.dp)
                    )
                    Text(text = AiLevel.Level6.showName)

                    RadioButton(
                        selected = aiLevel == AiLevel.Level7,
                        onClick = { aiLevel = AiLevel.Level7 },
                        modifier = Modifier.size(26.dp)
                    )
                    Text(text = AiLevel.Level7.showName)

                    RadioButton(
                        selected = aiLevel == AiLevel.Level8,
                        onClick = { aiLevel = AiLevel.Level8 },
                        modifier = Modifier.size(26.dp)
                    )
                    Text(text = AiLevel.Level8.showName)
                }


                Button(
                    onClick = {
                        onStart(if (isPLayerFirst) PLayerRound else AiRound, aiLevel)
                    },
                    modifier = Modifier.padding(6.dp)
                ) {
                    Text(text = "开始")
                }
            }
        }
    }
}