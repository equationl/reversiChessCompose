package com.equationl.common.viewModel

import androidx.compose.runtime.*
import com.equationl.common.gameLogic.Algorithm
import com.equationl.common.gameLogic.Move
import com.equationl.common.gameLogic.Rule
import com.equationl.common.gameLogic.Statistic
import com.equationl.common.gameView.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import kotlin.random.Random

@Composable
fun gamePresenter(
    gameAction: Flow<GameAction>,
): GameState {
    var gameState by remember { mutableStateOf(GameState()) }

    LaunchedEffect(gameAction) {
        gameAction.collect { action: GameAction ->
            when (action) {
                is GameAction.ClickChess -> {
                    val newState = clickChess(gameState, action.row, action.col)

                    if (newState != null) {
                        gameState = newState

                        withContext(Dispatchers.IO) {
                            // 电脑下子
                            gameState = runAi(gameState)
                            // 检查游戏是否已结束
                            gameState = checkIfGameOver(gameState)
                        }
                    }
                }
                is GameAction.ClickRequestNewGame -> {
                    gameState = gameState.copy(
                        gameState = NeedNewGame
                    )
                }
                is GameAction.ClickTip -> {
                    // TODO 暂时不写这个
                }
                is GameAction.CLickNewGame -> {
                    gameState = GameState(
                        whoFirst = action.whoFirst,
                        aiLevel = action.aiLevel,
                        gameState = action.whoFirst
                    )

                    if (action.whoFirst == AiRound) {
                        withContext(Dispatchers.IO) {
                            // 电脑下子
                            gameState = runAi(gameState)
                        }
                    }
                }
            }
        }
    }

    return gameState
}

private fun clickChess(gameState: GameState, row: Int, col: Int): GameState? {
    // 黑白棋规则是黑子先手，所以如果是AI先手的话，意味着玩家执白子
    val playerColor: Byte = if (gameState.whoFirst == PLayerRound) BlackChess else WhiteChess

    // 判断是否是玩家回合
    if (gameState.gameState != PLayerRound) {
        return null
    }

    // 下子区域不合法
    if (!Rule.isLegalMove(gameState.chessBoardState.chessBoardArray, Move(col, row), playerColor)) {
        return null
    }

    // FIXME 这里有一个BUG，可能会出现玩家已无棋可走，但是没有继续跳回AI或者结束游戏导致"卡死"
    val legalMoves = Rule.getLegalMoves(gameState.chessBoardState.chessBoardArray, playerColor)
    if (legalMoves.isEmpty()) { // 玩家已经无棋可走
        return gameState.copy(
            gameState = AiRound
        )
    }

    val move = Move(col, row)
    // 调用该方法后会更新传入的 chessBoardArray 并返回关联更改的棋子信息
    val moves = Rule.move(gameState.chessBoardState.chessBoardArray, move, playerColor) // TODO moves 可以用来做动画效果

    // 计算棋子数量
    val statistic: Statistic = Rule.analyse(gameState.chessBoardState.chessBoardArray, playerColor)

    return gameState.copy(
        chessBoardState = ChessBoardState(gameState.chessBoardState.chessBoardArray),
        gameState = AiRound,
        playerChessNum = statistic.PLAYER,
        aiChessNum = statistic.AI
    )
}

private suspend fun runAi(gameState: GameState): GameState {
    val delayTime: Long = Random(System.currentTimeMillis()).nextLong(200, 1000)
    delay(delayTime) // 假装AI在思考（

    val aiColor: Byte = if (gameState.whoFirst == AiRound) BlackChess else WhiteChess

    val legalMoves: Int = Rule.getLegalMoves(gameState.chessBoardState.chessBoardArray, aiColor).size

    if (legalMoves > 0) {
        val move: Move? = Algorithm.getGoodMove(
            gameState.chessBoardState.chessBoardArray,
            Algorithm.depth[gameState.aiLevel.level],
            aiColor,
            gameState.aiLevel.level
        )
        if (move != null) {
            val moves = Rule.move(gameState.chessBoardState.chessBoardArray, move, aiColor) // TODO moves 可以用来做动画效果

            // 计算棋子数量
            val statistic: Statistic = Rule.analyse(gameState.chessBoardState.chessBoardArray, (-aiColor).toByte())

            return gameState.copy(
                chessBoardState = ChessBoardState(gameState.chessBoardState.chessBoardArray),
                gameState = PLayerRound,
                playerChessNum = statistic.PLAYER,
                aiChessNum = statistic.AI
            )
        }
    }

    return gameState.copy(
        gameState = PLayerRound
    )
}

private fun checkIfGameOver(gameState: GameState): GameState {
    val aiColor: Byte = if (gameState.whoFirst == AiRound) BlackChess else WhiteChess
    val playerColor: Byte = if (gameState.whoFirst == PLayerRound) BlackChess else WhiteChess

    val aiLegalMoves: Int = Rule.getLegalMoves(gameState.chessBoardState.chessBoardArray, aiColor).size
    val playerLegalMoves: Int = Rule.getLegalMoves(gameState.chessBoardState.chessBoardArray, playerColor).size

    if (aiLegalMoves == 0 && playerLegalMoves == 0) {
        // 两方都无子可走，游戏结束
        val statistic = Rule.analyse(gameState.chessBoardState.chessBoardArray, playerColor)
        val newState = if (statistic.AI > statistic.PLAYER) GameOverWithAi
        else if (statistic.AI < statistic.PLAYER) GameOverWithPLayer
        else GameOverWithTie

        return gameState.copy(
            gameState = newState
        )
    }

    return gameState
}
