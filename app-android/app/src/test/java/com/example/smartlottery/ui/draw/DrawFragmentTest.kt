package com.example.smartlottery.ui.draw

import com.example.smartlottery.model.AiPrediction
import com.example.smartlottery.model.Draw
import com.example.smartlottery.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.Response

@ExperimentalCoroutinesApi
class DrawFragmentTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var fragment: DrawFragment
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fragment = DrawFragment()
        apiService = mock(ApiService::class.java)
        fragment.apiService = apiService
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testLoadData() = testDispatcher.runBlockingTest {
        val draw = Draw(1, "ssq", "2023135", "2023-11-21", "01,02,03,04,05,06", "07", 1000000)
        val aiPrediction = AiPrediction("01,02,03,04,05,06+07", "cot analysis")
        `when`(apiService.getLatestDraw("ssq")).thenReturn(Response.success(draw))
        `when`(apiService.getAiPrediction("ssq")).thenReturn(Response.success(aiPrediction))

        fragment.loadData()

        verify(fragment).updateDrawUI(draw)
        verify(fragment).updateAiPredictionUI(aiPrediction.prediction)
    }
}
