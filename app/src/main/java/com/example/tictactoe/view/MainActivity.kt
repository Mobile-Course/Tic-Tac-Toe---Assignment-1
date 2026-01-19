package com.example.tictactoe.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.R
import com.example.tictactoe.databinding.ActivityMainBinding
import com.example.tictactoe.model.CellValue
import com.example.tictactoe.model.GameStatus
import com.example.tictactoe.model.Player
import com.example.tictactoe.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GameViewModel by viewModels()
    
    // Lazy initialization of buttons 2D array
    private val buttons by lazy {
        arrayOf(
            arrayOf(binding.cell00, binding.cell01, binding.cell02),
            arrayOf(binding.cell10, binding.cell11, binding.cell12),
            arrayOf(binding.cell20, binding.cell21, binding.cell22)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeViewModel()
    }

    private fun initViews() {
        // Set click listeners
        buttons.forEachIndexed { row, buttonRow ->
            buttonRow.forEachIndexed { col, button ->
                button.setOnClickListener {
                    viewModel.onCellClicked(row, col)
                }
            }
        }

        binding.buttonPlayAgain.setOnClickListener {
            viewModel.onPlayAgainClicked()
        }
    }

    private fun observeViewModel() {
        viewModel.board.observe(this) { updateBoard(it) }
        viewModel.gameStatus.observe(this) { updateStatus(it) }
        viewModel.currentPlayer.observe(this) { player ->
            if (viewModel.gameStatus.value == GameStatus.IN_PROGRESS) {
                updateTurnMessage(player)
            }
        }
    }

    private fun updateBoard(board: Array<Array<CellValue>>) {
        buttons.forEachIndexed { i, row ->
            row.forEachIndexed { j, button ->
                button.text = when (board[i][j]) {
                    CellValue.X -> "X"
                    CellValue.O -> "O"
                    CellValue.EMPTY -> ""
                }
                buttons[i][j]?.setImageResource(imageRes)
            }
        }
    }

    private fun updateStatus(status: GameStatus) {
        val isGameOver = status != GameStatus.IN_PROGRESS
        binding.buttonPlayAgain.visibility = if (isGameOver) View.VISIBLE else View.INVISIBLE
        binding.buttonPlayAgain.isEnabled = isGameOver

        if (status == GameStatus.IN_PROGRESS) {
            updateTurnMessage(viewModel.currentPlayer.value ?: Player.X)
            return
        }

        val messageResId = when (status) {
            GameStatus.PLAYER_X_WINS -> R.string.win_message_x
            GameStatus.PLAYER_O_WINS -> R.string.win_message_o
            GameStatus.DRAW -> R.string.draw_message
            else -> 0 // Should not happen
        }
        
        if (messageResId != 0) {
            binding.statusText.text = getString(messageResId)
        }
    }

    private fun updateTurnMessage(player: Player) {
        binding.statusText.text = getString(
            if (player == Player.X) R.string.turn_message_x else R.string.turn_message_o
        )
    }
}
