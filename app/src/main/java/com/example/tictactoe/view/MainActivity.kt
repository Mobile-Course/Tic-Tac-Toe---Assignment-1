package com.example.tictactoe.view

import android.os.Bundle
import android.view.View
import android.widget.Button
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
    private val buttons = Array(3) { arrayOfNulls<Button>(3) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeViewModel()
    }

    private fun initViews() {
        // Map buttons to array
        buttons[0][0] = binding.cell00
        buttons[0][1] = binding.cell01
        buttons[0][2] = binding.cell02
        buttons[1][0] = binding.cell10
        buttons[1][1] = binding.cell11
        buttons[1][2] = binding.cell12
        buttons[2][0] = binding.cell20
        buttons[2][1] = binding.cell21
        buttons[2][2] = binding.cell22

        // Set click listeners
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]?.setOnClickListener {
                    viewModel.onCellClicked(i, j)
                }
            }
        }

        binding.buttonPlayAgain.setOnClickListener {
            viewModel.onPlayAgainClicked()
        }
    }

    private fun observeViewModel() {
        viewModel.board.observe(this) { board ->
            updateBoard(board)
        }

        viewModel.gameStatus.observe(this) { status ->
            updateStatus(status)
        }

        viewModel.currentPlayer.observe(this) { player ->
            if (viewModel.gameStatus.value == GameStatus.IN_PROGRESS) {
                updateTurnMessage(player)
            }
        }
    }

    private fun updateBoard(board: Array<Array<CellValue>>) {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]?.text = when (board[i][j]) {
                    CellValue.X -> "X"
                    CellValue.O -> "O"
                    CellValue.EMPTY -> ""
                }
            }
        }
    }

    private fun updateStatus(status: GameStatus) {
        when (status) {
            GameStatus.IN_PROGRESS -> {
                binding.buttonPlayAgain.visibility = View.INVISIBLE
                binding.buttonPlayAgain.isEnabled = false
                updateTurnMessage(viewModel.currentPlayer.value ?: Player.X)
            }
            GameStatus.PLAYER_X_WINS -> {
                binding.statusText.text = getString(R.string.win_message_x)
                binding.buttonPlayAgain.visibility = View.VISIBLE
                binding.buttonPlayAgain.isEnabled = true
            }
            GameStatus.PLAYER_O_WINS -> {
                binding.statusText.text = getString(R.string.win_message_o)
                binding.buttonPlayAgain.visibility = View.VISIBLE
                binding.buttonPlayAgain.isEnabled = true
            }
            GameStatus.DRAW -> {
                binding.statusText.text = getString(R.string.draw_message)
                binding.buttonPlayAgain.visibility = View.VISIBLE
                binding.buttonPlayAgain.isEnabled = true
            }
        }
    }

    private fun updateTurnMessage(player: Player) {
        binding.statusText.text = if (player == Player.X) {
            getString(R.string.turn_message_x)
        } else {
            getString(R.string.turn_message_o)
        }
    }
}
