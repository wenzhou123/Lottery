package com.example.smartlottery.ui.draw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartlottery.R
import com.example.smartlottery.model.Draw
import androidx.lifecycle.lifecycleScope
import com.example.smartlottery.service.ApiService
import com.example.smartlottery.service.RetrofitClient
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrawFragment : Fragment() {

    private lateinit var lotteryName: TextView
    private lateinit var issueNumber: TextView
    private lateinit var drawDate: TextView
    private lateinit var ballsContainer: LinearLayout
    private lateinit var jackpotPool: TextView
    private lateinit var aiPredictionBallsContainer: LinearLayout
    private lateinit var cotAnalysis: TextView
    private lateinit var lotteryTabs: TabLayout
    private lateinit var userThoughtInput: android.widget.EditText
    private lateinit var analyzeButton: Button

    private var activeTab = "ssq"
    internal var apiService: ApiService = RetrofitClient.javaApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_draw, container, false)

        lotteryName = view.findViewById(R.id.lottery_name)
        issueNumber = view.findViewById(R.id.issue_number)
        drawDate = view.findViewById(R.id.draw_date)
        ballsContainer = view.findViewById(R.id.balls_container)
        jackpotPool = view.findViewById(R.id.jackpot_pool)
        aiPredictionBallsContainer = view.findViewById(R.id.ai_prediction_balls_container)
        cotAnalysis = view.findViewById(R.id.cot_analysis)
        lotteryTabs = view.findViewById(R.id.lottery_tabs)
        userThoughtInput = view.findViewById(R.id.user_thought_input)
        analyzeButton = view.findViewById(R.id.analyze_button)

        analyzeButton.setOnClickListener {
            analyzeWithThought()
        }

        lotteryTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                activeTab = when (tab?.position) {
                    0 -> "ssq"
                    1 -> "dlt"
                    2 -> "fc3d"
                    3 -> "kl8"
                    else -> "ssq"
                }
                loadData()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        loadData()

        return view
    }

    internal fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val drawResponse = apiService.getLatestDraw(activeTab)
                if (drawResponse.isSuccessful) {
                    val draw = drawResponse.body()
                    withContext(Dispatchers.Main) {
                        updateDrawUI(draw)
                    }
                }

                val aiResponse = RetrofitClient.aiApi.getAiPrediction(activeTab)
                if (aiResponse.isSuccessful) {
                    val aiPrediction = aiResponse.body()
                    withContext(Dispatchers.Main) {
                        aiPrediction?.let {
                            updateAiPredictionUI(it.prediction)
                            cotAnalysis.text = it.cot_analysis
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    internal fun updateDrawUI(draw: Draw?) {
        draw?.let {
            lotteryName.text = getLotteryName(it.lotteryCode)
            issueNumber.text = "第${it.issueNumber}期"
            drawDate.text = "开奖日期: ${it.drawDate}"
            jackpotPool.text = "奖池: ${formatMoney(it.jackpotPool)}"
            addBallsToContainer(ballsContainer, it.redBalls, it.blueBalls)
        }
    }

    internal fun updateAiPredictionUI(prediction: String) {
        val parts = prediction.split("+").map { it.trim() }
        val redBalls = parts[0]
        val blueBalls = if (parts.size > 1) parts[1] else null
        addBallsToContainer(aiPredictionBallsContainer, redBalls, blueBalls)
    }

    private fun addBallsToContainer(container: LinearLayout, redBalls: String, blueBalls: String?) {
        container.removeAllViews()
        redBalls.split(",").forEach { ball ->
            val ballView = createBallView(ball, R.drawable.red_ball_background)
            container.addView(ballView)
        }
        blueBalls?.split(",")?.forEach { ball ->
            val ballView = createBallView(ball, R.drawable.blue_ball_background)
            container.addView(ballView)
        }
    }

    private fun createBallView(ball: String, backgroundRes: Int): TextView {
        val ballView = TextView(context)
        ballView.text = ball
        ballView.setBackgroundResource(backgroundRes)
        ballView.setTextColor(resources.getColor(android.R.color.white))
        ballView.gravity = android.view.Gravity.CENTER
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 8, 8, 8)
        ballView.layoutParams = layoutParams
        return ballView
    }

    private fun getLotteryName(code: String): String {
        return when (code) {
            "ssq" -> "双色球"
            "dlt" -> "大乐透"
            "fc3d" -> "3D"
            "kl8" -> "快乐8"
            else -> code
        }
    }

    private fun formatMoney(amount: Long): String {
        if (amount > 100_000_000) {
            return "${(amount / 100_000_000.0).toBigDecimal().setScale(1, 4)}亿"
        }
        return "${(amount / 10000)}万"
    }

    private fun analyzeWithThought() {
        val thought = userThoughtInput.text.toString()
        if (thought.isBlank()) {
            return
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val aiResponse = RetrofitClient.aiApi.getAiPrediction(activeTab) // In a real app, you'd pass the thought to the API
                if (aiResponse.isSuccessful) {
                    val aiPrediction = aiResponse.body()
                    withContext(Dispatchers.Main) {
                        aiPrediction?.let {
                            updateAiPredictionUI(it.prediction)
                            cotAnalysis.text = it.cot_analysis
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to get AI prediction", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
