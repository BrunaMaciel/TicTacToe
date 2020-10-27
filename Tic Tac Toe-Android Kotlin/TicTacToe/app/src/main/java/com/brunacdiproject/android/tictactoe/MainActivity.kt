package com.brunacdiproject.android.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

private const val CROSS = "X"
private const val CIRCLE = "O"

class MainActivity : AppCompatActivity() {

    private val boardGame: Array<Array<Button?>> = Array<Array<Button?>>(3) {arrayOfNulls(3)}

    private var player1Turn = true
    private var gameStarted = false
    private var roundCount = 0
    private var p1Points = 0
    private var p2Points = 0

    private var namePlayer1: String = "Player 1"
    private var namePlayer2: String = "Player 2"

    private lateinit var editTextPlayer1: EditText
    private lateinit var editTextPlayer2: EditText

    private lateinit var textViewP1Points: TextView
    private lateinit var textViewP2Points: TextView
    private lateinit var textViewP1Name: TextView
    private lateinit var textViewP2Name: TextView
    private lateinit var textViewGameMessage: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPlayer1 = findViewById(R.id.player1_name)
        editTextPlayer2 = findViewById(R.id.player2_name)
        textViewP1Points = findViewById(R.id.player1_points)
        textViewP2Points = findViewById(R.id.player2_points)
        textViewP1Name = findViewById(R.id.player1_name_points)
        textViewP2Name = findViewById(R.id.player2_name_points)
        textViewGameMessage = findViewById(R.id.game_message)
        textViewGameMessage.text = getString(R.string.type_players_name)

        for (i in 0..2) {
            for (j in 0..2) {
                val positionID = "button_$i$j"
                val resID = resources.getIdentifier(positionID, "id", packageName)
                boardGame[i][j] = findViewById<View>(resID) as Button
                boardGame[i][j]?.setOnClickListener {
                    if (!gameStarted){
                        val message = getString(R.string.type_players_name)
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                    else{
                        val b: Button = it as Button
                        if (b.text.isEmpty()) {
                            if (player1Turn) {
                                b.text = CROSS
                                b.background = getDrawable(R.drawable.cross)
                            } else {
                                b.text = CIRCLE
                                b.background = getDrawable(R.drawable.circle)
                            }
                            roundCount++
                            when {
                                checkForWin() -> {
                                    winGame()
                                }
                                roundCount == 9 -> {
                                    draw()
                                }
                                else -> player1Turn = !player1Turn
                            }
                            updateUI()
                        }
                    }
                }
            }
        }

        val newGame:Button = findViewById(R.id.newGame_button)
        newGame.setOnClickListener{
            if(editTextPlayer1.text.isNotEmpty() && editTextPlayer2.text.isNotEmpty()){
                resetBoard()
                player1Turn=true
                p1Points = 0
                p2Points = 0
                namePlayer1 = editTextPlayer1.text.toString().capitalize(Locale.getDefault())
                namePlayer2 = editTextPlayer2.text.toString().capitalize(Locale.getDefault())
                gameStarted = true
                updateUI()

            }
            else {
                val message = getString(R.string.type_players_name)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                gameStarted = false
            }
        }
        updateUI()
    }
    private fun updateUI(){

        textViewP1Points.text = getString(R.string.player_points,p1Points)
        textViewP2Points.text = getString(R.string.player_points,p2Points)
        textViewP1Name.text = namePlayer1
        textViewP2Name.text = namePlayer2

        if(!gameStarted)
            textViewGameMessage.text = getString(R.string.type_players_name)
        else if(roundCount == 0){
            if(player1Turn)
                textViewGameMessage.text = getString(R.string.new_game, namePlayer1)
            else{
                textViewGameMessage.text = getString(R.string.new_game, namePlayer2)
            }
        }
        else{
            if(player1Turn)
                textViewGameMessage.text = getString(R.string.player_turn,namePlayer1, CROSS)
            else
                textViewGameMessage.text = getString(R.string.player_turn,namePlayer2,CIRCLE)
        }


    }
    private fun checkForWin(): Boolean {
        //array of string that receives a copy of each button text
        val boardString = Array(3) { Array(3) { "" } }
        for (i in 0..2) {
            for (j in 0..2) {
                boardString[i][j] = boardGame[i][j]?.text.toString()
            }
        }
        //check for win in rows
        for (i in 0..2) {
            if (boardString[i][0] == boardString[i][1] && boardString[i][0] == boardString[i][2] && boardString[i][0].isNotBlank()) {
                return true
            }
        }
        //check for win in columns
        for (i in 0..2) {
            if (boardString[0][i] == boardString[1][i] && boardString[0][i] == boardString[2][i] && boardString[0][i].isNotBlank()) {
                return true
            }
        }

        //check for win diagonal top-left to bottom-right
        if (boardString[0][0] == boardString[1][1] && boardString[0][0] == boardString[2][2] && boardString[0][0].isNotBlank()) {
            return true
        }
        //check for win diagonal top-rigth to bottom-left
        if (boardString[0][2] == boardString[1][1] && boardString[0][2] == boardString[2][0] && boardString[0][2].isNotBlank()) {
            return true
        }

        //no wins found
        return false
    }
    private fun winGame(){
        val winner:String
        if (player1Turn){
            winner = namePlayer1
            p1Points++
        }
        else{
            winner = namePlayer2
            p2Points++
        }
        val messageResId = "$winner won the match!"
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        updateUI()
        resetBoard()
    }
    private fun draw(){
        val messageResId = "Game ended with a draw."
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        updateUI()
        resetBoard()
    }

    private fun resetBoard(){
        roundCount = 0
        for (i in 0..2) {
            for (j in 0..2) {
                boardGame[i][j]?.text = ""
                boardGame[i][j]?.background = getDrawable(android.R.drawable.btn_default)
            }
        }
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)

        savedInstanceState.putInt("roundCount", roundCount)
        savedInstanceState.putInt("player1Points", p1Points)
        savedInstanceState.putInt("player2Points", p2Points)
        savedInstanceState.putBoolean("player1Turn", player1Turn)
        savedInstanceState.putBoolean("gameStarted", gameStarted)
        savedInstanceState.putString("namePlayer1", namePlayer1)
        savedInstanceState.putString("namePlayer2", namePlayer2)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        roundCount = savedInstanceState.getInt("roundCount", 0)
        p1Points = savedInstanceState.getInt("player1Points", 0)
        p2Points = savedInstanceState.getInt("player2Points", 0)
        player1Turn = savedInstanceState.getBoolean("player1Turn", true)
        gameStarted = savedInstanceState.getBoolean("gameStarted", false)
        namePlayer1 =  savedInstanceState.getString("namePlayer1", "Player 1") ?: "Player 1"
        namePlayer2 =  savedInstanceState.getString("namePlayer2", "Player 2") ?: "Player 2"

        updateButtons()
        updateUI()

    }

    private fun updateButtons(){
        for (i in 0..2) {
            for (j in 0..2) {
                if(boardGame[i][j]?.text.toString() == CROSS) {
                    boardGame[i][j]?.background = getDrawable(R.drawable.cross)
                } else if (boardGame[i][j]?.text.toString() == CIRCLE)
                    boardGame[i][j]?.background = getDrawable(R.drawable.circle)
            }
        }
    }
}


