package com.example.themagicrun.logic

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.themagicrun.R

class PlayerManager(
    private val matrixCells: Array<Array<AppCompatImageView>>,
    private val gameMatrix: LinearLayoutCompat,
    private val magicianRow: Int
) {
    var currentColumn = 2
        private set

    fun moveMagician(direction: Int) {
        val newColumn = currentColumn + direction
        if (newColumn in 0 until gameMatrix.childCount) {
            //removes the player from the current position
            matrixCells[magicianRow][currentColumn].visibility = View.INVISIBLE
            //shows the player at the new location
            matrixCells[magicianRow][newColumn].setImageResource(R.drawable.magician)
            matrixCells[magicianRow][newColumn].visibility = View.VISIBLE
            currentColumn = newColumn
        }
    }
}