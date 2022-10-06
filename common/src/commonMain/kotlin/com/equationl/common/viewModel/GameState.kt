package com.equationl.common.viewModel

import com.equationl.common.gameLogic.initChessBoard
import com.equationl.common.gameView.PLayerRound

data class GameState (
    /**当前棋盘上的棋子信息**/
    val chessBoardState: ChessBoardState = ChessBoardState(),
    /**AI难度等级**/
    val aiLevel: AiLevel = AiLevel.Level1,
    /**游戏状态**/
    val gameState: Int = PLayerRound,
    /**先手**/
    val whoFirst: Int = PLayerRound,
    /**AI当前棋子数量**/
    val aiChessNum: Int = 2,
    /**玩家当前棋子数量**/
    val playerChessNum: Int = 2,
)

enum class AiLevel(val showName: String, val level: Int) {
    Level1("菜鸟", 1),
    Level2("新手", 2),
    Level3("入门", 3),
    Level4("棋手", 4),
    Level5("棋士", 5),
    Level6("大师", 6),
    Level7("宗师", 7),
    Level8("棋圣", 8),
}

data class ChessBoardState (
    val chessBoardArray: Array<ByteArray> = initChessBoard(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChessBoardState

        if (!chessBoardArray.contentDeepEquals(other.chessBoardArray)) return false

        return true
    }

    override fun hashCode(): Int {
        return chessBoardArray.contentDeepHashCode()
    }
}