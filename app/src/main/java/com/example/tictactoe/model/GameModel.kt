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
        if (row < 0 || row > 2 || col < 0 || col > 2) return false
        if (board[row][col] != CellValue.EMPTY) return false
        if (status != GameStatus.IN_PROGRESS) return false

        val value = if (currentPlayer == Player.X) CellValue.X else CellValue.O
        board[row][col] = value

        if (checkWin(row, col, value)) {
            status = if (currentPlayer == Player.X) GameStatus.PLAYER_X_WINS else GameStatus.PLAYER_O_WINS
        } else if (checkDraw()) {
            status = GameStatus.DRAW
        } else {
            currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
        }
        return true
    }

    private fun checkWin(lastRow: Int, lastCol: Int, playerValue: CellValue): Boolean {
        // Check row
        if (board[lastRow].all { it == playerValue }) return true
        
        // Check col
        if (board.all { it[lastCol] == playerValue }) return true

        // Check main diagonal
        if (lastRow == lastCol && (0..2).all { board[it][it] == playerValue }) return true

        // Check anti-diagonal
        if (lastRow + lastCol == 2 && (0..2).all { board[it][2 - it] == playerValue }) return true

        return false
    }

    private fun checkDraw(): Boolean {
        return board.all { row -> row.all { it != CellValue.EMPTY } }
    }
}
