package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int? = null): List<GameResult> {
        // 1. Gesamte sortierte Liste holen
        val sortedLeaderboard = gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

        // 2. Wenn kein Rang gefragt ist -> alles zurückgeben
        if (rank == null) {
            return sortedLeaderboard
        }

        // 3. Fehler abfangen (ungültiger Rang)
        if (rank <= 0 || rank > sortedLeaderboard.size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        // 4. Start- und End-Index berechnen
        val targetIndex = rank - 1
        val startIndex = maxOf(0, targetIndex - 3)
        val endIndex = minOf(sortedLeaderboard.size, targetIndex + 4)

        // 5. Den passenden Ausschnitt zurückgeben
        return sortedLeaderboard.subList(startIndex, endIndex)
    }
}