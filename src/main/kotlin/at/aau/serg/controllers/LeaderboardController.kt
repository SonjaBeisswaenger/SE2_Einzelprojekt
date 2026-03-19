package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

// Controller für die Anzeige des Leaderboards.
// Greift auf die zentrale Logik im GameResultService zu.
@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    // Gibt das sortierte Leaderboard zurück.
    // Nutzt den optionalen Parameter [rank], um ein Fenster um diesen Rang anzuzeigen.
    // Wenn kein Rang angegeben wird, liefert der Service die vollständige Liste.
    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int? = null): List<GameResult> {
        // Die gesamte Berechnungslogik wurde in den GameResultService ausgelagert.
        return gameResultService.getLeaderboard(rank)
    }
}