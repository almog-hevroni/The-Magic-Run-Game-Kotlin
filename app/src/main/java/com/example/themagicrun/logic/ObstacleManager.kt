package com.example.themagicrun.logic

import androidx.appcompat.widget.LinearLayoutCompat
import com.example.themagicrun.GameBoard
import kotlin.random.Random

class ObstacleManager(
    private val gameBoard: GameBoard,
    private val firstColumn: LinearLayoutCompat,
    private val gameMatrix: LinearLayoutCompat
) {
    private val OBSTACLE_SPAWN_CHANCE = 0.6f

    fun moveObstaclesDown(currentColumn: Int, magicianRow: Int) {
        //goes through all rows from bottom to top
        for (row in (firstColumn.childCount - 1) downTo 0) {
            //goes through all the columns from left to right
            for (col in 0 until gameMatrix.childCount) {
                if (row == 0) {
                    //if it's the top row - cleans the cell (prepares it for a possible new obstacle)
                    gameBoard.obstacleActive[row][col] = false
                } else {
                    //copies the state of the cell above to the current cell- active/not active
                    gameBoard.obstacleActive[row][col] = gameBoard.obstacleActive[row - 1][col]
                    //passes the type of obstacle (good/bad)
                    gameBoard.obstacleType[row][col] = gameBoard.obstacleType[row - 1][col]
                }
                gameBoard.updateCellVisibility(row, col, currentColumn, magicianRow)
            }
        }
        //after all obstacles have moved down, checks whether to create a new obstacle
        if (Random.nextFloat() < OBSTACLE_SPAWN_CHANCE) {
            createNewObstacle()
        }
    }

    private fun createNewObstacle() {
        val randomColumn = Random.nextInt(gameMatrix.childCount)
        val isGoodObstacle = Random.nextFloat() < 0.4f

        gameBoard.obstacleActive[0][randomColumn] = true
        gameBoard.obstacleType[0][randomColumn] = if (isGoodObstacle)
            GameBoard.GOOD_OBSTACLE else GameBoard.BAD_OBSTACLE
    }
}