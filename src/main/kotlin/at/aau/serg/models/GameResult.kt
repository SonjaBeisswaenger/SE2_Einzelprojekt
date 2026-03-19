package at.aau.serg.models

// Datenmodell für ein Spielergebnis.
// Repräsentiert einen Eintrag in der Highscore-Liste.
data class GameResult(var id: Long, var playerName: String, var score: Int, var timeInSeconds: Double)