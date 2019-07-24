package com.example.krs.geographiceducation.model.database

class GameResult(gameName: String, correctAnswers: String, gameDuration: String, gameDate: String) {
    var mGameName: String = gameName
    var mCorrectAnswers: String = correctAnswers  // of form correct/total answers
    var mGameDuration: String = gameDuration
    var mGameDate: String = gameDate
}