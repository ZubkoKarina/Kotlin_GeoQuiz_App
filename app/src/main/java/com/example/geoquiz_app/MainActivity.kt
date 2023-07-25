package com.example.geoquiz_app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivityK"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var correctAnswers = 0
    private var answeredQuestions = 0
    private val quizViewModel : QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,  "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener{view: View ->
            checkAnswer(true)
            updateButtons()
        }
        binding.falseButton.setOnClickListener{view: View ->
            checkAnswer(false)
            updateButtons()
        }
        binding.nextButton.setOnClickListener{
            quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.prevButton.setOnClickListener{
            quizViewModel.moveToPrev()
            updateQuestion()
        }
        binding.questionTextView.setOnClickListener{view: View ->
            quizViewModel.currentIndex = (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size
            updateQuestion()
        }
        updateQuestion()
    }

    private fun updateButtons() {
        binding.trueButton.isEnabled = !quizViewModel.questionBank[quizViewModel.currentIndex].isAnswered
        binding.falseButton.isEnabled = !quizViewModel.questionBank[quizViewModel.currentIndex].isAnswered
        if (answeredQuestions == quizViewModel.questionBank.size) {
            val score = 100 * correctAnswers / quizViewModel.questionBank.size
            Toast.makeText(this, "Score: $score%", Toast.LENGTH_LONG).show()
            binding.nextButton.isEnabled = false
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        updateButtons()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val question = quizViewModel.questionBank[quizViewModel.currentIndex]
        if (question.isAnswered) return
        question.isAnswered = true
        answeredQuestions++
        val correctAnswer = quizViewModel.currentQuestionAnswer
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
