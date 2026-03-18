package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_addGameResult_callsService() {
        val result = GameResult(0, "Player", 10, 10.0)

        controller.addGameResult(result)

        // Prüft, ob der Controller das Ergebnis wirklich an den Service weiterreicht
        verify(mockedService).addGameResult(result)
    }

    @Test
    fun test_getGameResult_existingId_returnsResult() {
        val expected = GameResult(1, "Player", 10, 10.0)
        whenever(mockedService.getGameResult(1)).thenReturn(expected)

        val actual = controller.getGameResult(1)

        assertEquals(expected, actual)
    }

    @Test
    fun test_getGameResult_nonExistingId_returnsNull() {
        whenever(mockedService.getGameResult(99)).thenReturn(null)

        val actual = controller.getGameResult(99)

        assertNull(actual)
    }

    @Test
    fun test_deleteGameResult_callsService() {
        controller.deleteGameResult(1)

        // Prüft, ob der Löschbefehl beim Service ankommt
        verify(mockedService).deleteGameResult(1)
    }
}