    package com.example.themagicrun.logic

    import android.content.Context
    import android.view.View
    import android.widget.ImageView
    import com.example.themagicrun.GameBoard
    import com.example.themagicrun.Utilities.Constants
    import com.example.themagicrun.Utilities.SignalManager
    import com.example.themagicrun.Utilities.SingleSoundPlayer
    import com.google.android.material.textview.MaterialTextView

    class gameManager(lifeCount: Int = 3) {
        private var singleSoundPlayer: SingleSoundPlayer? = null
        private var remainingLives = lifeCount
        private var isCollisionChecking = true

        var score: Int = 0
            private set

        val isGameOver: Boolean
            get() = remainingLives <= 0

        fun initSound(context: Context) {
            singleSoundPlayer = SingleSoundPlayer(context)
        }

        fun handleBadCollision(hearts: List<ImageView>) {
            isCollisionChecking = false
            singleSoundPlayer?.playSound(Constants.Sounds.WITCH_SOUND)

            remainingLives--
            updateLivesDisplay(hearts)

            SignalManager.getInstance().vibrate()

            if (remainingLives >= 1) {
                SignalManager.getInstance().toast("OH NO!")
                isCollisionChecking = true
            }
        }

        fun handleGoodCollision() {
            singleSoundPlayer?.playSound(Constants.Sounds.MAGIC_SOUND)
            score += Constants.GameLogic.ANSWER_POINTS
            SignalManager.getInstance().toast("Great!")
        }

        fun checkAndHandleCollision(
            gameBoard: GameBoard,
            currentColumn: Int,
            magicianRow: Int,
            hearts: List<ImageView>,
            scoreView: MaterialTextView,
            onGameOver: () -> Unit
        ) {
            //checks if there is an obstacle at the magician's location
            if (gameBoard.obstacleActive[magicianRow][currentColumn]) {
                //checks what kind of obstacle it is
                if (gameBoard.obstacleType[magicianRow][currentColumn] == GameBoard.BAD_OBSTACLE) {
                    //collision with BAD_OBSTACLE
                    handleBadCollision(hearts)

                    if (isGameOver) {
                        onGameOver()
                    }
                } else {  //collision with GOOD_OBSTACLE
                    handleGoodCollision()
                    scoreView.text = score.toString()
                }
                gameBoard.obstacleActive[magicianRow][currentColumn] = false
            }
        }

        private fun updateLivesDisplay(hearts: List<ImageView>) {
            hearts.forEachIndexed { index, heart ->
                heart.visibility = if (index < remainingLives) View.VISIBLE else View.INVISIBLE
            }
        }
    }
