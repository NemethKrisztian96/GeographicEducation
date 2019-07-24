package com.example.krs.geographiceducation.model.database

class GameResult(gameName: String, correctAnswers: Int, gameDuration: String, gameDate: String) {
    var mGameName: String = gameName
    var mCorrectAnswers: Int = correctAnswers
    var mGameDuration: String = gameDuration
    var mGameDate: String = gameDate
}