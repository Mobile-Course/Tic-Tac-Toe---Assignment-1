package com.example.tictactoe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tictactoe.model.CellValue
import com.example.tictactoe.model.GameModel
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player

class GameViewModel : ViewModel() {

    private val gameModel = GameModel()

    private val _board = MutableLiveData<Array<Array<CellValue>>>()
    val board: LiveData<Array<Array<CellValue>>> = _board

    private val _gameStatus = MutableLiveData<GameStatus>()
    val gameStatus: LiveData<GameStatus> = _gameStatus

    private val _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player> = _currentPlayer

    init {
        updateGameState()
    }

    fun onCellClicked(row: Int, col: Int) {
        if (gameModel.makeMove(row, col)) {
            updateGameState()
        }
    }

    fun onPlayAgainClicked() {
        gameModel.resetGame()
        updateGameState()
    }

    private fun updateGameState() {
        _board.value = gameModel.board
        _gameStatus.value = gameModel.status
        _currentPlayer.value = gameModel.currentPlayer
    }
}
