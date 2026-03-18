package at.aau.serg.services

import at.aau.serg.models.GameResult
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.concurrent.atomic.AtomicLong

@Service
class GameResultService {

    private val gameResults = mutableListOf<GameResult>()
    private var nextId = AtomicLong(1)

    fun addGameResult(gameResult: GameResult) {
        gameResult.id = nextId.getAndIncrement()
        gameResults.add(gameResult)
    }

    fun getGameResult(id: Long): GameResult? = gameResults.find { it.id == id } // ? allows null

    fun getGameResults(): List<GameResult> = gameResults.toList() // returns immutable list copy

    /**
     * Kotlin-idiomatic for:
     * fun deleteGameResult(gameResultId: Long) {
     *     gameResults.removeIf({ gameResult -> gameResult.id == gameResultId })
     * }
     */
    fun deleteGameResult(id: Long) = gameResults.removeIf { it.id == id }

    fun getLeaderboard(rank: Int?): List<GameResult> {
        // 1. Gesamte sortierte Liste holen (Aufruf jetzt direkt im Service)
        val sortedLeaderboard = getGameResults().sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

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