package at.aau.serg.services

import at.aau.serg.models.GameResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.server.ResponseStatusException
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GameResultServiceTests {

    private lateinit var service: GameResultService

    @BeforeEach
    fun setup() {
        service = GameResultService()
    }

    @Test
    fun test_getGameResults_emptyList() {
        val result = service.getGameResults()

        assertEquals(emptyList<GameResult>(), result)
    }

    @Test
    fun test_addGameResult_getGameResults_containsSingleElement() {
        val gameResult = GameResult(1, "player1", 17, 15.3)

        service.addGameResult(gameResult)
        val res = service.getGameResults()

        assertEquals(1, res.size)
        assertEquals(gameResult, res[0])
    }

    @Test
    fun test_getGameResultById_existingId_returnsObject() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val res = service.getGameResult(1)

        assertEquals(gameResult, res)
    }

    @Test
    fun test_getGameResultById_nonexistentId_returnsNull() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val res = service.getGameResult(22)

        assertNull(res)
    }

    @Test
    fun test_addGameResult_multipleEntries_correctId() {
        val gameResult1 = GameResult(0, "player1", 17, 15.3)
        val gameResult2 = GameResult(0, "player2", 25, 16.0)

        service.addGameResult(gameResult1)
        service.addGameResult(gameResult2)

        val res = service.getGameResults()

        assertEquals(2, res.size)

        assertEquals(gameResult1, res[0])
        assertEquals(1, res[0].id)

        assertEquals(gameResult2, res[1])
        assertEquals(2, res[1].id)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        service.addGameResult(GameResult(0, "second", 15, 10.0))
        service.addGameResult(GameResult(0, "first", 20, 20.0))
        service.addGameResult(GameResult(0, "third", 10, 15.0))

        val res = service.getLeaderboard(null)

        assertEquals(3, res.size)
        assertEquals("first", res[0].playerName)
        assertEquals("second", res[1].playerName)
        assertEquals("third", res[2].playerName)
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        // Gleiche Punkte, aber unterschiedliche Zeiten (weniger ist besser)
        service.addGameResult(GameResult(0, "second", 20, 10.0))
        service.addGameResult(GameResult(0, "first", 20, 20.0))
        service.addGameResult(GameResult(0, "third", 20, 15.0))

        val res = service.getLeaderboard(null)

        assertEquals(3, res.size)
        assertEquals("second", res[0].playerName)
        assertEquals("third", res[1].playerName)
        assertEquals("first", res[2].playerName)
    }

    @Test
    fun test_getLeaderboard_rankTooSmall_throwsException() {
        assertThrows<ResponseStatusException> {
            service.getLeaderboard(0) // Service ist leer -> wirft Fehler
        }
    }

    @Test
    fun test_getLeaderboard_rankTooBig_throwsException() {
        assertThrows<ResponseStatusException> {
            service.getLeaderboard(1) // Service ist leer, Platz 1 gibt es nicht -> wirft Fehler
        }
    }
}