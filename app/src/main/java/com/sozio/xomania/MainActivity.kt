package com.sozio.xomania

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.sozio.xomania.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    enum class Turn {
        NOUGHT,
        CROSS
    }

    companion object {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private var boardList = mutableListOf<Button>()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBoard()
    }

    private fun initBoard(){
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)

    }

    fun boardTapped(view: View) {
        if (view !is Button) {
            return
        }
        addToBoard(view)

        if(checkForVictory(NOUGHT)){
            noughtsScore++
            result("O Win!")
        }
        if(checkForVictory(CROSS)){
            crossesScore++
            result("X Win!")
        }

        if(fullBoard()){
            result("Draw")
        }
    }

    private fun checkForVictory(s : String): Boolean{
        val grid = arrayOf(
            arrayOf(binding.a1, binding.a2, binding.a3),
            arrayOf(binding.b1, binding.b2, binding.b3),
            arrayOf(binding.c1, binding.c2, binding.c3)
        )
        //horizontal control
        for (row in grid ){
            if(row.all { match(it,s)}) return true
        }
        //vertical control
        for (col in 0..2){
            if(grid.all {match(it[col], s)}) return true
        }
        //diagonal control
        val diagonal1 = listOf(Pair(0,0), Pair(1,1), Pair(2,2))
        val diagonal2 = listOf(Pair(0,2), Pair(1,1), Pair(2,0))
        if (diagonal1.all {match(grid[it.first][it.second], s)} ||
            diagonal2.all {match(grid[it.first][it.second], s)}) return true
        return false
    }

    private fun match (button: Button, symbol : String) = button.text == symbol

    private fun result (title: String) {
        val message = "\nO: $noughtsScore\n\nX: $crossesScore"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset Score"){
                _,_ ->
                resetBoard()
                noughtsScore=0
                crossesScore=0
            }
            .setNeutralButton("Continue"){
                _,_ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }
    private fun resetBoard(){
        for(button in boardList){
            button.text = ""
        }
        firstTurn = if(firstTurn == Turn.NOUGHT) Turn.CROSS else Turn.NOUGHT
        currentTurn = firstTurn
        setTurnLabel()

    }
    private fun fullBoard() : Boolean {
        for (button in boardList){
            if(button.text == "") return false
        }
        return true
    }
    private fun addToBoard(button: Button){
        if(button.text != ""){
            return
        }
        if(currentTurn == Turn.NOUGHT){
            button.text = NOUGHT
            currentTurn = Turn.CROSS
        } else if(currentTurn == Turn.CROSS){
            button.text = CROSS
            currentTurn = Turn.NOUGHT
        }
        setTurnLabel()
    }

    private fun setTurnLabel(){
        var turnText = ""
        if (currentTurn == Turn.CROSS) turnText = "Turn $CROSS"
        else turnText = "Turn $NOUGHT"

        binding.turnTV.text = turnText
    }

}