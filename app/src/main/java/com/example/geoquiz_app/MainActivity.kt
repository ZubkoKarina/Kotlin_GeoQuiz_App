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
    private val quizViewModel : QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,  "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener{view: View ->
            val userAnswer = true
            checkAnswer(userAnswer)
        }
        binding.falseButton.setOnClickListener{view: View ->
            val userAnswer = false
            checkAnswer(userAnswer)
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
            quizViewModel.moveToNext()
            updateQuestion()
        }
        updateQuestion()
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
}
