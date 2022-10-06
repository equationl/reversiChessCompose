package com.equationl.common.gameLogic

/**
 * 坐标信息
 * */
data class Move(
    val col: Int,
    val row: Int
)

/**
 * 棋子数量
 * */
data class Statistic (
    val PLAYER: Int,
    val AI: Int
)

/**
 * 记录极小极大算法过程中的数据
 */
data class MinimaxResult (
    var mark: Int,
    var move: Move?
)