package at.aau.serg.services

import at.aau.serg.models.GameResult
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.concurrent.atomic.AtomicLong

// Service-Klasse zur Verwaltung der Spielergebnisse.
// Enthält die zentrale Logik für das Leaderboard und die Datenhaltung.
@Service
class GameResultService {

    // Interne Liste zur Speicherung der Ergebnisse.
    private val gameResults = mutableListOf<GameResult>()
    // Thread-sicherer Zähler für die automatische ID-Vergabe.
    private var nextId = AtomicLong(1)

    // Speichert ein Ergebnis und weist ihm eine eindeutige ID zu.
    fun addGameResult(gameResult: GameResult) {
        gameResult.id = nextId.getAndIncrement()
        gameResults.add(gameResult)
    }

    // Sucht ein einzelnes Ergebnis anhand der ID.
    fun getGameResult(id: Long): GameResult? = gameResults.find { it.id == id } // ? allows null

    // Gibt eine Kopie der aktuellen Ergebnisliste zurück.
    fun getGameResults(): List<GameResult> = gameResults.toList() // returns immutable list copy

    /**
     * Kotlin-idiomatic for:
     * fun deleteGameResult(gameResultId: Long) {
     *     gameResults.removeIf({ gameResult -> gameResult.id == gameResultId })
     * }
     */
    // Entfernt ein Ergebnis basierend auf der ID aus der Liste.
    fun deleteGameResult(id: Long) = gameResults.removeIf { it.id == id }

    /**
     * Berechnet das Leaderboard mit optionalem Fokus auf einen bestimmten Rang.
     * @param rank Der gewünschte Rang (1-basiert). Falls null, wird die gesamte Liste geliefert.
     * @return Ein Ausschnitt des Leaderboards (3 davor, der Rang selbst, 3 danach).
     */
    fun getLeaderboard(rank: Int?): List<GameResult> {
        // 1. Gesamte sortierte Liste holen (Aufruf jetzt direkt im Service).
        // Sortierung: Primär nach Score (absteigend), sekundär nach Zeit (aufsteigend).
        val sortedLeaderboard = getGameResults().sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

        // 2. Sonderfall: Wenn kein Rang angegeben ist, die ganze Liste zurückgeben.
        if (rank == null) {
            return sortedLeaderboard
        }

        // 3. Fehler abfangen: Prüfen, ob der angeforderte Rang im gültigen Bereich liegt.
        if (rank <= 0 || rank > sortedLeaderboard.size) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        }

        // 4. Fenster (Start- und End-Index) berechnen.
        val targetIndex = rank - 1
        // Stellt sicher, dass der Start-Index nicht negativ wird (z.B. bei Rang 1).
        val startIndex = maxOf(0, targetIndex - 3)
        // Verhindert, dass der End-Index über die Listengröße hinausgeht.
        val endIndex = minOf(sortedLeaderboard.size, targetIndex + 4)

        // 5. Den berechneten Ausschnitt zurückgeben.
        return sortedLeaderboard.subList(startIndex, endIndex)
    }
}