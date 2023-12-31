package com.example.geoquiz_app

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }
        binding.trueButton.setOnClickListener {
            val userAnswer = true
            checkAnswer(userAnswer)
        }
        binding.falseButton.setOnClickListener {
            val userAnswer = false
            checkAnswer(userAnswer)
        }
        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }
        binding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.cheatButton.setOnClickListener {
            val intent = quizViewModel.cheatIntent(this)
            startActivity(intent)
        }
        updateQuestion()
        binding.cheatButton.setOnClickListener {
            if (quizViewModel.canCheat()) {
                quizViewModel.useCheat()
                val intent = quizViewModel.cheatIntent(this)
                startActivity(intent)
                if (!quizViewModel.canCheat()) {
                    binding.cheatButton.isEnabled = false
                    Toast.makeText(this, R.string.no_more_cheats, Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, R.string.no_more_cheats, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        updateButtons()
    }

    private fun updateButtons() {
        val isEnabled = quizViewModel.isQuestionAnswered
        binding.trueButton.isEnabled = !isEnabled
        binding.falseButton.isEnabled = !isEnabled
        if (quizViewModel.isQuizFinished) {
            val score = quizViewModel.score
            Toast.makeText(this, "Score: $score%", Toast.LENGTH_LONG).show()
            binding.nextButton.isEnabled = false
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val messageResId = if (quizViewModel.checkAnswer(userAnswer)) {
            R.string.correct_snackbart
        } else {
            R.string.incorrect_snackbart
        }
        Snackbar.make(
            binding.root,
            messageResId,
            Snackbar.LENGTH_SHORT
        ).show()
        updateButtons()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            4.0f,
            4.0f,
            Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)
    }
}
