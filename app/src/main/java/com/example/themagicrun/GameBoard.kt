package com.example.themagicrun

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat

class GameBoard(
    private val matrixCells: Array<Array<AppCompatImageView>>,
    firstColumn: LinearLayoutCompat,
    private val gameMatrix: LinearLayoutCompat
) {
    var obstacleActive: Array<Array<Boolean>> //array that indicates whether there is an obstacle in each cell
    var obstacleType: Array<Array<Int>> //array that indicates the type of obstacle in each cell

    init {
        obstacleActive = Array(firstColumn.childCount) {
            Array(gameMatrix.childCount) { false }
        }
        obstacleType = Array(firstColumn.childCount) {
            Array(gameMatrix.childCount) { BAD_OBSTACLE }
        }
    }

    fun updateCellVisibility(row: Int, col: Int, currentColumn: Int, magicianRow: Int) {
        matrixCells[row][col].apply {
            when {
                //case 1: this is the magician's location
                row == magicianRow && col == currentColumn -> {
                    setImageResource(R.drawable.magician)
                    visibility = View.VISIBLE
                }
                //case 2: there is an active obstacle in this cell
                obstacleActive[row][col] -> {
                    setImageResource(
                        if (obstacleType[row][col] == GOOD_OBSTACLE)
                            R.drawable.magic_hat
                        else
                            R.drawable.bad_witch
                    )
                    visibility = View.VISIBLE
                }
                //case 3: the cell is empty
                else -> visibility = View.INVISIBLE
            }
        }
    }

    companion object {
        const val BAD_OBSTACLE = 1
        const val GOOD_OBSTACLE = 2
    }
}
