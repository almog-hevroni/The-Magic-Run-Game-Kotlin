package com.example.themagicrun.logic

import android.view.View
import android.widget.ImageView
import com.example.themagicrun.Utilities.SignalManager

class gameManager (lifeCount: Int = 3) {

    private var remainingLives = lifeCount
    private var isCollisionChecking = true
    private var lastCollisionTime = 0L
    private val COLLISION_COOLDOWN = 500L

    val isGameOver: Boolean
        get() = remainingLives <= 0

    fun handleCollision(currentTime: Long, hearts: List<ImageView>) {
        if (currentTime - lastCollisionTime < COLLISION_COOLDOWN) {
            return
        }

        lastCollisionTime = currentTime
        isCollisionChecking = false

        remainingLives--
        updateLivesDisplay(hearts)

        SignalManager.getInstance().vibrate()

        if (remainingLives >= 1) {
            SignalManager.getInstance().toast("OH NO! The Witch got you!")
            isCollisionChecking = true
        }
    }

    private fun updateLivesDisplay(hearts: List<ImageView>) {
        hearts.forEachIndexed { index, heart ->
            heart.visibility = if (index < remainingLives) View.VISIBLE else View.INVISIBLE
        }
    }
}