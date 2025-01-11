package com.example.themagicrun.Utilities

import com.example.themagicrun.model.HighScore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object DataManager {
    private const val MAX_SCORES = 10
    private val sp = SharedPreferencesManager.getInstance()
    private val gson = Gson()

    //saving a new result
    fun saveScore(newScore: HighScore) {
        try {
            //gets all available results
            val currentScores = getScores().toMutableList()
            //adds the new result
            currentScores.add(newScore)

            //sort by score from highest to lowest
            currentScores.sortByDescending { it.score }

            // saving the 10 best results only
            if (currentScores.size > MAX_SCORES) {
                currentScores.removeAt(currentScores.lastIndex)
            }

            val scoresJson = gson.toJson(currentScores)
            //saving the data in the sp file
            sp.putString(Constants.SP_keys.SCORES_LIST_KEY, scoresJson)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getScores(): List<HighScore> {
        return try {
            val json = sp.getString(Constants.SP_keys.SCORES_LIST_KEY, "[]")
            val type = object : TypeToken<List<HighScore>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}