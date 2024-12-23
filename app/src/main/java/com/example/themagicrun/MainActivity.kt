package com.example.themagicrun

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.themagicrun.Utilities.Constants
import com.example.themagicrun.logic.gameManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.Timer
import java.util.TimerTask
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var main_BTN_right: ExtendedFloatingActionButton
    private lateinit var main_BTN_left: ExtendedFloatingActionButton
    private lateinit var heart1: ImageView
    private lateinit var heart2: ImageView
    private lateinit var heart3: ImageView

    private val MAGICIAN_ROW = 5
    private val OBSTACLE_SPAWN_CHANCE = 0.6f  // 60% chance for a new obstacle

    private var currentColumn = 1    // current magician column
    private lateinit var timer: Timer
    private var timerOn: Boolean = false

    private lateinit var matrixCells: Array<Array<AppCompatImageView>>
    private lateinit var obstacleActive: Array<Array<Boolean>>
    private lateinit var gameMatrix: LinearLayoutCompat
    private lateinit var firstColumn: LinearLayoutCompat
    private lateinit var magician: AppCompatImageView
    private lateinit var gameManager: gameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameMatrix = findViewById(R.id.game_matrix)
        firstColumn = findViewById(R.id.matrix_column_1)

        findViews()
        initViews()
        initializeMatrix()
        initializeMagician()
        gameManager = gameManager()
        startObstaclesMovement()

    }

    private fun initViews() {
        main_BTN_right.setOnClickListener { moveMagician(1) }
        main_BTN_left.setOnClickListener { moveMagician(-1) }
    }


    private fun findViews() {
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_BTN_left = findViewById(R.id.main_BTN_left)
        heart1 = findViewById(R.id.main_IMG_heart1)
        heart2 = findViewById(R.id.main_IMG_heart2)
        heart3 = findViewById(R.id.main_IMG_heart3)

        matrixCells = Array(firstColumn.childCount) {
            Array(gameMatrix.childCount) { AppCompatImageView(this) }
        }

        val cellIds = arrayOf(
            arrayOf(R.id.cell_0_0, R.id.cell_0_1, R.id.cell_0_2),
            arrayOf(R.id.cell_1_0, R.id.cell_1_1, R.id.cell_1_2),
            arrayOf(R.id.cell_2_0, R.id.cell_2_1, R.id.cell_2_2),
            arrayOf(R.id.cell_3_0, R.id.cell_3_1, R.id.cell_3_2),
            arrayOf(R.id.cell_4_0, R.id.cell_4_1, R.id.cell_4_2),
            arrayOf(R.id.cell_5_0, R.id.cell_5_1, R.id.cell_5_2),
            arrayOf(R.id.cell_6_0, R.id.cell_6_1, R.id.cell_6_2)
        )

        for (row in 0 until firstColumn.childCount) {
            for (col in 0 until gameMatrix.childCount) {
                matrixCells[row][col] = findViewById(cellIds[row][col])
            }
        }
    }

    private fun initializeMatrix() {
        obstacleActive = Array(firstColumn.childCount) {
            Array(gameMatrix.childCount) { false }
        }
    }

    private fun initializeMagician() {
        magician = matrixCells[MAGICIAN_ROW][1].apply {
            setImageResource(R.drawable.magician)
            visibility = View.VISIBLE
        }
        currentColumn = 1
    }

    private fun moveMagician(direction: Int) {
        val newColumn = currentColumn + direction
        if (newColumn in 0 until gameMatrix.childCount) {
            matrixCells[MAGICIAN_ROW][currentColumn].visibility = View.INVISIBLE
            matrixCells[MAGICIAN_ROW][newColumn].setImageResource(R.drawable.magician)
            matrixCells[MAGICIAN_ROW][newColumn].visibility = View.VISIBLE
            currentColumn = newColumn
        }
    }

    private fun startObstaclesMovement() {
        if (!timerOn) {
            timerOn = true
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        moveObstaclesDown()
                        checkAndHandleCollision()
                    }
                }
            }, 0, 700)
        }
    }

    private fun moveObstaclesDown() {
        // move obstacles down and clear row 0 together
        for (row in (firstColumn.childCount - 1) downTo 0) {
            for (col in 0 until gameMatrix.childCount) {
                obstacleActive[row][col] = if (row == 0) false else obstacleActive[row - 1][col]
                updateCellVisibility(row, col)
            }
        }
        // create new obstacles in the top row
        if (Random.nextFloat() < OBSTACLE_SPAWN_CHANCE) {
            createNewObstacle()
        }
    }

    private fun updateCellVisibility(row: Int, col: Int) {
        matrixCells[row][col].apply {
            //magician row
            if (row == MAGICIAN_ROW && col == currentColumn) {
                setImageResource(R.drawable.magician)
                visibility = View.VISIBLE
            } else {
                setImageResource(R.drawable.bad_witch)
                visibility = if (obstacleActive[row][col]) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    private fun createNewObstacle() {
        val randomColumn = Random.nextInt(gameMatrix.childCount)
        matrixCells[0][randomColumn].visibility = View.VISIBLE
        obstacleActive[0][randomColumn] = true
    }

    private fun checkAndHandleCollision() {
        if (obstacleActive[MAGICIAN_ROW][currentColumn]) {
            val currentTime = System.currentTimeMillis()
            val hearts = listOf(heart3, heart2, heart1)
            gameManager.handleCollision(currentTime, hearts)

            if (gameManager.isGameOver) {
                timer.cancel()
                changeActivity("Magic Failed! ")
            }
            obstacleActive[MAGICIAN_ROW][currentColumn] = false
        }
    }

    private fun changeActivity(message: String) {
        stopTimer()
        val intent = Intent(this, GameOverActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constants.BundleKeys.STATUS_KEY, message)
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun stopTimer() {
        if (timerOn) {
            timerOn = false
            timer.cancel()
        }
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
    }

    override fun onResume() {
        super.onResume()
        if (!gameManager.isGameOver) {
            startObstaclesMovement()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
    }

}

