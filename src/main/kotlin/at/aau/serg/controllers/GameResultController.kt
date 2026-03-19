package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.*

// Controller für die Verwaltung einzelner Spielergebnisse.
// Bietet Endpunkte zum Erstellen, Abrufen und Löschen von Ergebnissen.
@RestController
@RequestMapping("/game-results")
class GameResultController(
    private val gameResultService: GameResultService
) {

    // Ruft ein spezifisches Spielergebnis anhand seiner ID ab.
    @GetMapping("/{gameResultId}")
    fun getGameResult(@PathVariable gameResultId: Long): GameResult? {
        return gameResultService.getGameResult(gameResultId)
    }

    // Gibt eine Liste aller gespeicherten Spielergebnisse zurück.
    @GetMapping
    fun getAllGameResults(): List<GameResult> {
        return gameResultService.getGameResults()
    }

    // Speichert ein neues Spielergebnis im System.
    @PostMapping
    fun addGameResult(@RequestBody gameResult: GameResult) {
        gameResultService.addGameResult(gameResult)
    }

    // Löscht ein Spielergebnis dauerhaft aus der Liste.
    @DeleteMapping("/{gameResultId}")
    fun deleteGameResult(@PathVariable gameResultId: Long) {
        gameResultService.deleteGameResult(gameResultId)
    }
    
}