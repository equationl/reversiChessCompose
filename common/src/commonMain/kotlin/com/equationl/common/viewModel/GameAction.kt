package com.equationl.common.viewModel

sealed class GameAction {
    /**请求开启新的游戏**/
    object ClickRequestNewGame : GameAction()
    /** 请求提示**/
    object ClickTip : GameAction()
    /**点击了棋盘的某一个格子**/
    data class ClickChess(val row: Int, val col: Int) : GameAction()
    /**开始一个新的游戏**/
    data class CLickNewGame(val whoFirst: Int, val aiLevel: AiLevel): GameAction()
}