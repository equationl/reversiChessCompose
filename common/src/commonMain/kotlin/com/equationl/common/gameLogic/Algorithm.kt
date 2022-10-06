package com.equationl.common.gameLogic

import com.equationl.common.gameView.BlackChess
import com.equationl.common.gameView.WhiteChess

/**
 * 算法
 */
object Algorithm {
    // 博弈树搜索深度
    val depth = intArrayOf(0, 1, 2, 3, 7, 3, 5, 2, 4)

    private const val WHITE: Byte = WhiteChess
    private const val BLACK: Byte = BlackChess

    fun getGoodMove(
        chessBoard: Array<ByteArray>,
        depth: Int,
        chessColor: Byte,
        difficulty: Int
    ): Move? {
        return if (chessColor == BLACK) max(
            chessBoard,
            depth,
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            chessColor,
            difficulty
        ).move else min(
            chessBoard,
            depth,
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            chessColor,
            difficulty
        ).move
    }

    private fun max(
        chessBoard: Array<ByteArray>,
        depth: Int,
        alpha: Int,
        beta: Int,
        chessColor: Byte,
        difficulty: Int
    ): MinimaxResult {
        var alpha = alpha
        if (depth == 0) {
            return MinimaxResult(evaluate(chessBoard, difficulty), null)
        }
        val legalMovesMe = Rule.getLegalMoves(chessBoard, chessColor)
        if (legalMovesMe.isEmpty()) {
            return if (Rule.getLegalMoves(chessBoard, (-chessColor).toByte()).isEmpty()) {
                MinimaxResult(evaluate(chessBoard, difficulty), null)
            } else min(chessBoard, depth, alpha, beta, (-chessColor).toByte(), difficulty)
        }
        val tmp = Array(8) {
            ByteArray(
                8
            )
        }
        copyBinaryArray(chessBoard, tmp)
        var best = Int.MIN_VALUE
        var move: Move? = null
        for (i in legalMovesMe.indices) {
            alpha = Math.max(best, alpha)
            if (alpha >= beta) {
                break
            }
            Rule.move(chessBoard, legalMovesMe[i], chessColor)
            val value: Int = min(
                chessBoard, depth - 1, Math.max(best, alpha), beta,
                (-chessColor).toByte(), difficulty
            ).mark
            if (value > best) {
                best = value
                move = legalMovesMe[i]
            }
            copyBinaryArray(tmp, chessBoard)
        }
        return MinimaxResult(best, move)
    }

    private fun min(
        chessBoard: Array<ByteArray>,
        depth: Int,
        alpha: Int,
        beta: Int,
        chessColor: Byte,
        difficulty: Int
    ): MinimaxResult {
        var beta = beta
        if (depth == 0) {
            return MinimaxResult(evaluate(chessBoard, difficulty), null)
        }
        val legalMovesMe = Rule.getLegalMoves(chessBoard, chessColor)
        if (legalMovesMe.isEmpty()) {
            return if (Rule.getLegalMoves(chessBoard, (-chessColor).toByte()).isEmpty()) {
                MinimaxResult(evaluate(chessBoard, difficulty), null)
            } else max(chessBoard, depth, alpha, beta, (-chessColor).toByte(), difficulty)
        }
        val tmp = Array(8) {
            ByteArray(
                8
            )
        }
        copyBinaryArray(chessBoard, tmp)
        var best = Int.MAX_VALUE
        var move: Move? = null
        for (i in legalMovesMe.indices) {
            beta = Math.min(best, beta)
            if (alpha >= beta) {
                break
            }
            Rule.move(chessBoard, legalMovesMe[i], chessColor)
            val value: Int = max(
                chessBoard, depth - 1, alpha, Math.min(best, beta),
                (-chessColor).toByte(), difficulty
            ).mark
            if (value < best) {
                best = value
                move = legalMovesMe[i]
            }
            copyBinaryArray(tmp, chessBoard)
        }
        return MinimaxResult(best, move)
    }

    private fun evaluate(chessBoard: Array<ByteArray>, difficulty: Int): Int {
        var whiteEvaluate = 0
        var blackEvaluate = 0
        when (difficulty) {
            1 -> {
                var i = 0
                while (i < 8) {
                    var j = 0
                    while (j < 8) {
                        if (chessBoard[i][j] == WHITE) {
                            whiteEvaluate += 1
                        } else if (chessBoard[i][j] == BLACK) {
                            blackEvaluate += 1
                        }
                        j++
                    }
                    i++
                }
            }
            2, 3, 4 -> {
                var i = 0
                while (i < 8) {
                    var j = 0
                    while (j < 8) {
                        if ((i == 0 || i == 7) && (j == 0 || j == 7)) {
                            if (chessBoard[i][j] == WHITE) {
                                whiteEvaluate += 5
                            } else if (chessBoard[i][j] == BLACK) {
                                blackEvaluate += 5
                            }
                        } else if (i == 0 || i == 7 || j == 0 || j == 7) {
                            if (chessBoard[i][j] == WHITE) {
                                whiteEvaluate += 2
                            } else if (chessBoard[i][j] == BLACK) {
                                blackEvaluate += 2
                            }
                        } else {
                            if (chessBoard[i][j] == WHITE) {
                                whiteEvaluate += 1
                            } else if (chessBoard[i][j] == BLACK) {
                                blackEvaluate += 1
                            }
                        }
                        j++
                    }
                    i++
                }
            }
            5, 6 -> {
                var i = 0
                while (i < 8) {
                    var j = 0
                    while (j < 8) {
                        if ((i == 0 || i == 7) && (j == 0 || j == 7)) {
                            if (chessBoard[i][j] == WHITE) {
                                whiteEvaluate += 5
                            } else if (chessBoard[i][j] == BLACK) {
                                blackEvaluate += 5
                            }
                        } else if (i == 0 || i == 7 || j == 0 || j == 7) {
                            if (chessBoard[i][j] == WHITE) {
                                whiteEvaluate += 2
                            } else if (chessBoard[i][j] == BLACK) {
                                blackEvaluate += 2
                            }
                        } else {
                            if (chessBoard[i][j] == WHITE) {
                                whiteEvaluate += 1
                            } else if (chessBoard[i][j] == BLACK) {
                                blackEvaluate += 1
                            }
                        }
                        j++
                    }
                    i++
                }
                blackEvaluate = blackEvaluate * 2 + Rule.getLegalMoves(chessBoard, BLACK).size
                whiteEvaluate = whiteEvaluate * 2 + Rule.getLegalMoves(chessBoard, WHITE).size
            }
            7, 8 -> {
                /**
                 * 稳定度
                 */
                var i = 0
                while (i < 8) {
                    var j = 0
                    while (j < 8) {
                        val weight = intArrayOf(2, 4, 6, 10, 15)
                        if (chessBoard[i][j] == WHITE) {
                            whiteEvaluate += weight[getStabilizationDegree(chessBoard, Move(i, j))]
                        } else if (chessBoard[i][j] == BLACK) {
                            blackEvaluate += weight[getStabilizationDegree(chessBoard, Move(i, j))]
                        }
                        j++
                    }
                    i++
                }
                /**
                 * 行动力
                 */
                blackEvaluate += Rule.getLegalMoves(chessBoard, BLACK).size
                whiteEvaluate += Rule.getLegalMoves(chessBoard, WHITE).size
            }
        }
        return blackEvaluate - whiteEvaluate
    }

    private fun getStabilizationDegree(chessBoard: Array<ByteArray>, move: Move): Int {
        val chessColor = chessBoard[move.row][move.col].toInt()
        val drow: Array<IntArray>
        val dcol: Array<IntArray>
        val row = IntArray(2)
        val col = IntArray(2)
        var degree = 0
        drow = arrayOf(intArrayOf(0, 0), intArrayOf(-1, 1), intArrayOf(-1, 1), intArrayOf(1, -1))
        dcol = arrayOf(intArrayOf(-1, 1), intArrayOf(0, 0), intArrayOf(-1, 1), intArrayOf(-1, 1))
        for (k in 0..3) {
            row[1] = move.row
            row[0] = row[1]
            col[1] = move.col
            col[0] = col[1]
            for (i in 0..1) {
                while (Rule.isLegal(row[i] + drow[k][i], col[i] + dcol[k][i])
                    && chessBoard[row[i] + drow[k][i]][col[i] + dcol[k][i]].toInt() == chessColor
                ) {
                    row[i] += drow[k][i]
                    col[i] += dcol[k][i]
                }
            }
            if (!Rule.isLegal(row[0] + drow[k][0], col[0] + dcol[k][0])
                || !Rule.isLegal(row[1] + drow[k][1], col[1] + dcol[k][1])
            ) {
                degree += 1
            } else if (chessBoard[row[0] + drow[k][0]][col[0] + dcol[k][0]].toInt() == -chessColor && chessBoard[row[1] + drow[k][1]][col[1] + dcol[k][1]].toInt() == -chessColor) {
                degree += 1
            }
        }
        return degree
    }
}
