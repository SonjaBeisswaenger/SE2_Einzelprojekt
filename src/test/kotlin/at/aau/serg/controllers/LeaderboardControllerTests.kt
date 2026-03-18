package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_delegatesToService() {
        // Vorbereitung: Wenn der Controller den Service fragt, soll eine Dummy-Liste zurückkommen
        val expectedList = listOf(GameResult(1, "test", 10, 10.0))
        whenever(mockedService.getLeaderboard(2)).thenReturn(expectedList)

        // Aktion: Controller mit Rank 2 aufrufen
        val result = controller.getLeaderboard(2)

        // Prüfung: Kam die richtige Liste zurück und wurde der Service wirklich mit Rank 2 aufgerufen?
        assertEquals(expectedList, result)
        verify(mockedService).getLeaderboard(2)
    }

    @Test
    fun test_getLeaderboard_withoutParam_usesDefaultNull() {
        val expectedList = listOf(GameResult(1, "Test", 10, 10.0))
        // Den Aufruf mit null mocken, da das der Standardwert ist
        whenever(mockedService.getLeaderboard(null)).thenReturn(expectedList)

        // Klammer bleibt leer
        val result = controller.getLeaderboard()

        assertEquals(expectedList, result)
        verify(mockedService).getLeaderboard(null)
    }
}
