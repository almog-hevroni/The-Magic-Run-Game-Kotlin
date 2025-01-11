package com.example.themagicrun

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.themagicrun.Utilities.Constants
import com.example.themagicrun.Utilities.GameMode
import com.example.themagicrun.Utilities.GameSpeed
import com.example.themagicrun.Utilities.TiltDetector
import com.example.themagicrun.interfaces.TiltCallback
import com.example.themagicrun.logic.ObstacleManager
import com.example.themagicrun.logic.PlayerManager
import com.example.themagicrun.logic.gameManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    private lateinit var main_BTN_right: ExtendedFloatingActionButton
    private lateinit var main_BTN_left: ExtendedFloatingActionButton
    private lateinit var heart1: ImageView
    private lateinit var heart2: ImageView
    private lateinit var heart3: ImageView
    private lateinit var main_LBL_score: MaterialTextView
    private lateinit var gameMatrix: LinearLayoutCompat
    private lateinit var firstColumn: LinearLayoutCompat
    private lateinit var matrixCells: Array<Array<AppCompatImageView>>
    private lateinit var gameSpeed: GameSpeed
    private lateinit var playerName: String

    private val MAGICIAN_ROW = 8
    private val TILT_THRESHOLD = 3.0f
    private val FORWARD_TILT_THRESHOLD = -3.0f  // negative value for forward tilt
    private val BACKWARD_TILT_THRESHOLD = 3.0f   // positive value for backward tilt

    // game managers
    private lateinit var gameBoard: GameBoard
    private lateinit var obstacleManager: ObstacleManager
    private lateinit var playerManager: PlayerManager
    private lateinit var gameTimer: GameTimer
    private lateinit var gameManager: gameManager
    private lateinit var tiltDetector: TiltDetector
    private lateinit var gameMode: GameMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // parameters from the menu screen
        gameMode = GameMode.valueOf(intent.getStringExtra(Constants.BundleKeys.GAME_MODE_KEY)!!)
        gameSpeed = GameSpeed.valueOf(intent.getStringExtra(Constants.BundleKeys.GAME_SPEED_KEY)!!)
        playerName = intent.getStringExtra("PLAYER_NAME") ?: ""

        gameMatrix = findViewById(R.id.game_matrix)
        firstColumn = findViewById(R.id.matrix_column_1)

        findViews()
        initializeManagers()
        gameTimer.setBaseSpeed(gameSpeed) //the selected difficulty level easy/hard
        initViews()
        if (gameMode == GameMode.SENSORS) {
            initTiltDetector()
        }
        gameTimer.setSpeed(gameSpeed) //the speed can change base on tilt
        startGame()
    }

    private fun findViews() {
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_BTN_left = findViewById(R.id.main_BTN_left)
        heart1 = findViewById(R.id.main_IMG_heart1)
        heart2 = findViewById(R.id.main_IMG_heart2)
        heart3 = findViewById(R.id.main_IMG_heart3)
        main_LBL_score = findViewById(R.id.main_LBL_score)

        matrixCells = Array(firstColumn.childCount) {
            Array(gameMatrix.childCount) { AppCompatImageView(this) }
        }

        val cellIds = arrayOf(
            arrayOf(R.id.cell_0_0, R.id.cell_0_1, R.id.cell_0_2, R.id.cell_0_3, R.id.cell_0_4),
            arrayOf(R.id.cell_1_0, R.id.cell_1_1, R.id.cell_1_2, R.id.cell_1_3, R.id.cell_1_4),
            arrayOf(R.id.cell_2_0, R.id.cell_2_1, R.id.cell_2_2, R.id.cell_2_3, R.id.cell_2_4),
            arrayOf(R.id.cell_3_0, R.id.cell_3_1, R.id.cell_3_2, R.id.cell_3_3, R.id.cell_3_4),
            arrayOf(R.id.cell_4_0, R.id.cell_4_1, R.id.cell_4_2, R.id.cell_4_3, R.id.cell_4_4),
            arrayOf(R.id.cell_5_0, R.id.cell_5_1, R.id.cell_5_2, R.id.cell_5_3, R.id.cell_5_4),
            arrayOf(R.id.cell_6_0, R.id.cell_6_1, R.id.cell_6_2, R.id.cell_6_3, R.id.cell_6_4),
            arrayOf(R.id.cell_7_0, R.id.cell_7_1, R.id.cell_7_2, R.id.cell_7_3, R.id.cell_7_4),
            arrayOf(R.id.cell_8_0, R.id.cell_8_1, R.id.cell_8_2, R.id.cell_8_3, R.id.cell_8_4),
            arrayOf(R.id.cell_9_0, R.id.cell_9_1, R.id.cell_9_2, R.id.cell_9_3, R.id.cell_9_4)
        )

        for (row in 0 until firstColumn.childCount) {
            for (col in 0 until gameMatrix.childCount) {
                matrixCells[row][col] = findViewById(cellIds[row][col])
            }
        }
    }

    private fun initializeManagers() {
        gameManager = gameManager()
        gameManager.initSound(this)
        gameBoard = GameBoard(matrixCells, firstColumn, gameMatrix)
        obstacleManager = ObstacleManager(gameBoard, firstColumn, gameMatrix)
        playerManager = PlayerManager(matrixCells, gameMatrix, MAGICIAN_ROW)

        gameTimer = GameTimer {
            runOnUiThread {
                obstacleManager.moveObstaclesDown(playerManager.currentColumn, MAGICIAN_ROW)
                gameManager.checkAndHandleCollision(
                    gameBoard = gameBoard,
                    currentColumn = playerManager.currentColumn,
                    magicianRow = MAGICIAN_ROW,
                    hearts = listOf(heart1, heart2, heart3),
                    scoreView = main_LBL_score,
                    onGameOver = { changeActivity("Magic Failed!") }
                )
            }
        }
    }

    private fun initViews() {
        when (gameMode) {
            GameMode.BUTTONS -> {
                main_BTN_right.visibility = View.VISIBLE
                main_BTN_left.visibility = View.VISIBLE
                main_BTN_right.setOnClickListener { playerManager.moveMagician(1) }
                main_BTN_left.setOnClickListener { playerManager.moveMagician(-1) }
            }
            GameMode.SENSORS -> {
                main_BTN_right.visibility = View.GONE
                main_BTN_left.visibility = View.GONE
            }
        }
        main_LBL_score.text = gameManager.score.toString()
    }

    private fun initTiltDetector() {
        tiltDetector = TiltDetector(
            context = this,
            tiltCallback = object : TiltCallback {
                override fun tiltX(direction: Float) {
                    if (direction > TILT_THRESHOLD) {
                        playerManager.moveMagician(-1)
                    } else if (direction < -TILT_THRESHOLD) {
                        playerManager.moveMagician(1)
                    }
                }

                override fun tiltY(direction: Float) {
                    when {
                        direction < FORWARD_TILT_THRESHOLD -> {
                            // forward tilt - higher speed
                            gameTimer.adjustSpeedBasedOnTilt(true)
                        }
                        direction > BACKWARD_TILT_THRESHOLD -> {
                            // tilt back - lower speed
                            gameTimer.adjustSpeedBasedOnTilt(false)
                        }
                    }
                }
            }
        )
    }

    private fun startGame() {
        gameTimer.start()
    }

    private fun changeActivity(message: String) {
        gameTimer.stop()
        val intent = Intent(this, GameOverActivity::class.java).apply {
            putExtra(Constants.BundleKeys.STATUS_KEY, message)
            putExtra("SCORE", gameManager.score)
            putExtra("PLAYER_NAME", playerName)
        }
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        gameTimer.stop()
        if (gameMode == GameMode.SENSORS) {
            tiltDetector.stop()
        }
    }


    override fun onResume() {
        super.onResume()
        if (!gameManager.isGameOver) {
            gameTimer.start()
            if (gameMode == GameMode.SENSORS) {
                tiltDetector.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        gameTimer.stop()
        if (gameMode == GameMode.SENSORS) {
            tiltDetector.stop()
        }
    }
}

