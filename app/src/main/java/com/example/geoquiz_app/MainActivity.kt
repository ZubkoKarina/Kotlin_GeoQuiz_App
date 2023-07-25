package com.example.geoquiz_app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivityK"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var correctAnswers = 0
    private var answeredQuestions = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,  "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener{view: View ->
            checkAnswer(true)
            updateButtons()
        }
        binding.falseButton.setOnClickListener{view: View ->
            checkAnswer(false)
            updateButtons()
        }
        binding.nextButton.setOnClickListener{
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        binding.prevButton.setOnClickListener{
            currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
            updateQuestion()
        }
        binding.questionTextView.setOnClickListener{view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        updateQuestion()
    }

    private fun updateButtons() {
        binding.trueButton.isEnabled = !questionBank[currentIndex].isAnswered
        binding.falseButton.isEnabled = !questionBank[currentIndex].isAnswered
        if (answeredQuestions == questionBank.size) {
            val score = 100 * correctAnswers / questionBank.size
            Toast.makeText(this, "Score: $score%", Toast.LENGTH_LONG).show()
            binding.nextButton.isEnabled = false
        }
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView.setText(questionTextResId)
        updateButtons()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val question = questionBank[currentIndex]
        if (question.isAnswered) return
        question.isAnswered = true
        answeredQuestions++
        val correctAnswer = question.answer
        val messageResId = if (userAnswer == correctAnswer) {
            correctAnswers++
            R.string.correct_snackbart
        } else {
            R.string.incorrect_snackbart
        }
        Snackbar.make(
            binding.root,
            messageResId,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
