package com.example.tictactoe.model

enum class Player {
    X, O
}

enum class CellValue {
    X, O, EMPTY
}

enum class GameStatus {
    IN_PROGRESS,
    PLAYER_X_WINS,
    PLAYER_O_WINS,
    DRAW
}

class GameModel {

    var board: Array<Array<CellValue>> = Array(3) { Array(3) { CellValue.EMPTY } }
        private set

    var currentPlayer: Player = Player.X
        private set

    var status: GameStatus = GameStatus.IN_PROGRESS
        private set

    fun resetGame() {
        board = Array(3) { Array(3) { CellValue.EMPTY } }
        currentPlayer = Player.X
        status = GameStatus.IN_PROGRESS
    }

    fun makeMove(row: Int, col: Int): Boolean {
        if (!isValidMove(row, col)) return false

        board[row][col] = if (currentPlayer == Player.X) CellValue.X else CellValue.O

        if (checkWin(row, col)) {
            status = if (currentPlayer == Player.X) GameStatus.PLAYER_X_WINS else GameStatus.PLAYER_O_WINS
        } else if (checkDraw()) {
            status = GameStatus.DRAW
        } else {
            switchPlayer()
        }
        return true
    }

    private fun isValidMove(row: Int, col: Int): Boolean {
        return row in 0..2 && col in 0..2 && 
               board[row][col] == CellValue.EMPTY && 
               status == GameStatus.IN_PROGRESS
    }

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
    }

    private fun checkWin(lastRow: Int, lastCol: Int): Boolean {
        val symbol = board[lastRow][lastCol]
        val n = 3

        // Check Row
        if ((0 until n).all { board[lastRow][it] == symbol }) return true
        
        // Check Column
        if ((0 until n).all { board[it][lastCol] == symbol }) return true

        // Check Main Diagonal
        if (lastRow == lastCol && (0 until n).all { board[it][it] == symbol }) return true

        // Check Anti-Diagonal
        if (lastRow + lastCol == n - 1 && (0 until n).all { board[it][n - 1 - it] == symbol }) return true

        return false
    }

    private fun checkDraw(): Boolean {
        return board.all { row -> row.all { it != CellValue.EMPTY } }
    }
}
