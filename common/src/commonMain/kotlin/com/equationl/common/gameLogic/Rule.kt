package com.equationl.common.gameLogic

import com.equationl.common.gameView.NoneChess

/**
 * 规则
 */
object Rule {
    /**
     * 该步是否合法
     */
    fun isLegalMove(chessBoard: Array<ByteArray>, move: Move, chessColor: Byte): Boolean {
        val row: Int = move.row
        val col: Int = move.col
        if (!isLegal(row, col) || chessBoard[row][col] != NoneChess) return false

        for (dirX in -1 until 2) {
            for (dirY in -1 until 2) {
                if (dirX == 0 && dirY == 0) continue

                val x = col + dirX
                val y = row + dirY

                if (isLegal(y, x) && chessBoard[y][x].toInt() == -chessColor) {
                    var i = row + dirY * 2
                    var j = col + dirX * 2

                    while (isLegal(i, j)) {
                        if (chessBoard[i][j].toInt() == -chessColor) {
                            i += dirY
                            j += dirX
                            continue
                        } else if (chessBoard[i][j] == chessColor) {
                            return true
                        } else {
                            i += dirY
                            j += dirX
                            break
                        }
                    }
                }
            }
        }

        return false
    }

    fun isLegal(row: Int, col: Int): Boolean {
        return row in 0..7 && col >= 0 && col < 8
    }

    /**
     * 使用前务必先确认该步合法
     */
    fun move(chessBoard: Array<ByteArray>, move: Move, chessColor: Byte): List<Move> {
        val row: Int = move.row
        val col: Int = move.col
        val moves: MutableList<Move> = ArrayList()

        for (dirX in -1 until 2) {
            for (dirY in -1 until 2) {
                if (dirX == 0 && dirY == 0) continue

                var temp = 0
                val x = col + dirX
                val y = row + dirY

                if (isLegal(y, x) && chessBoard[y][x].toInt() == -chessColor) {
                    temp++

                    var i = row + dirY * 2
                    var j = col + dirX * 2

                    while (isLegal(i, j)) {
                        if (chessBoard[i][j].toInt() == -chessColor) {
                            temp++
                            i += dirY
                            j += dirX
                            continue
                        }
                        else if (chessBoard[i][j] == chessColor) {
                            var m = row + dirY
                            var n = col + dirX
                            while (m <= row + temp && m >= row - temp && n <= col + temp && n >= col - temp) {
                                chessBoard[m][n] = chessColor
                                moves.add(Move(m, n))
                                m += dirY
                                n += dirX
                            }
                            i += dirY
                            j += dirX
                            break
                        }
                        else {
                            i += dirY
                            j += dirX
                            break
                        }
                    }
                }
            }
        }

        chessBoard[row][col] = chessColor
        return moves
    }

    fun getLegalMoves(chessBoard: Array<ByteArray>, chessColor: Byte): List<Move> {
        val moves: MutableList<Move> = ArrayList()
        var move: Move
        for (row in 0..7) {
            for (col in 0..7) {
                move = Move(row, col)
                if (isLegalMove(chessBoard, move, chessColor)) {
                    moves.add(move)
                }
            }
        }
        return moves.toList()
    }

    fun analyse(chessBoard: Array<ByteArray>, playerColor: Byte): Statistic {
        var player = 0
        var ai = 0
        for (i in 0..7) {
            for (j in 0..7) {
                if (chessBoard[i][j] == playerColor) player += 1 else if (chessBoard[i][j] == (-playerColor).toByte()) ai += 1
            }
        }
        return Statistic(player, ai)
    }
}
