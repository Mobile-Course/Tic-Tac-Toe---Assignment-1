package com.example.tictactoe.model

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameModelTest {

    private lateinit var gameModel: GameModel

    @Before
    fun setUp() {
        gameModel = GameModel()
    }

    @Test
    fun `initial state is correct`() {
        assertEquals(Player.X, gameModel.currentPlayer)
        assertEquals(GameStatus.IN_PROGRESS, gameModel.status)
        // Check all cells are empty
        for (row in gameModel.board) {
            for (cell in row) {
                assertEquals(CellValue.EMPTY, cell)
            }
        }
    }

    @Test
    fun `making a move updates board and switches player`() {
        val success = gameModel.makeMove(0, 0)
        assertTrue(success)
        assertEquals(CellValue.X, gameModel.board[0][0])
        assertEquals(Player.O, gameModel.currentPlayer)
    }

    @Test
    fun `cannot move on occupied cell`() {
        gameModel.makeMove(0, 0) // X moves
        val success = gameModel.makeMove(0, 0) // O tries to move on same spot
        assertFalse(success)
        assertEquals(CellValue.X, gameModel.board[0][0]) // Should still be X
        assertEquals(Player.O, gameModel.currentPlayer) // Should still be O's turn
    }

    @Test
    fun `player x wins horizontal`() {
        gameModel.makeMove(0, 0) // X
        gameModel.makeMove(1, 0) // O
        gameModel.makeMove(0, 1) // X
        gameModel.makeMove(1, 1) // O
        gameModel.makeMove(0, 2) // X wins

        assertEquals(GameStatus.PLAYER_X_WINS, gameModel.status)
    }

    @Test
    fun `player o wins vertical`() {
        gameModel.makeMove(0, 0) // X
        gameModel.makeMove(0, 1) // O
        gameModel.makeMove(1, 1) // X
        gameModel.makeMove(1, 1) // O
        gameModel.makeMove(2, 2) // X
        gameModel.makeMove(2, 1) // O wins

        assertEquals(GameStatus.PLAYER_O_WINS, gameModel.status)
    }

    @Test
    fun `draw game`() {
        // X O X
        // X O X
        // O X O
        gameModel.makeMove(0, 0) // X
        gameModel.makeMove(0, 1) // O
        gameModel.makeMove(0, 2) // X
        
        gameModel.makeMove(1, 1) // O
        gameModel.makeMove(1, 0) // X
        gameModel.makeMove(1, 2) // O (Wait, let's be careful)

        gameModel.resetGame()
        // sequence to draw
        // X O X
        // X O O
        // O X X
        gameModel.makeMove(0, 0) // X
        gameModel.makeMove(0, 1) // O
        gameModel.makeMove(0, 2) // X
        
        gameModel.makeMove(1, 1) // O
        gameModel.makeMove(1, 0) // X
        gameModel.makeMove(1, 2) // O
        
        gameModel.makeMove(2, 1) // X
        gameModel.makeMove(2, 0) // O
        gameModel.makeMove(2, 2) // X
        
        assertEquals(GameStatus.DRAW, gameModel.status)
    }
}
