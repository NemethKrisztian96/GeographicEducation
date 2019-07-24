package com.example.krs.geographiceducation.model.database

/**
 * Data object for storing information corresponding to a game's outcome
 */
class GameResult(gameName: String, correctAnswers: String, gameDuration: String, gameDate: String) {
    var mGameName: String = gameName
    var mCorrectAnswers: String = correctAnswers  // of form correct/total answers
    var mGameDuration: String = gameDuration
    var mGameDate: String = gameDate
}