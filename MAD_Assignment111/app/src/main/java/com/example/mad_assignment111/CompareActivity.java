package com.example.mad_assignment111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class CompareActivity extends AppCompatActivity {

    private TextView questionTextView, leftNumberTextView, rightNumberTextView, scoreTextView,questionNumber;
    private Button leftButton, rightButton, nextButton, finishButton;
    private ImageView homeButton;

    private int leftNumber, rightNumber;
    private boolean isGreaterThanQuestion;
    private int score = 0;
    private int questionCount = 0;
    private final int TOTAL_QUESTIONS = 5; // Set total questions to 5
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView);
        leftNumberTextView = findViewById(R.id.leftNumberTextView);
        rightNumberTextView = findViewById(R.id.rightNumberTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        questionNumber = findViewById(R.id.questionNumber);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        nextButton = findViewById(R.id.nextButton);
        finishButton = findViewById(R.id.finishButton);
        homeButton = findViewById(R.id.homeButton);

        // Initially hide the finish button
        finishButton.setVisibility(View.GONE);

        // Set up click listeners
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true); // User selected left number
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false); // User selected right number
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionCount < TOTAL_QUESTIONS) {
                    generateQuestion();
                    nextButton.setVisibility(View.INVISIBLE);
                    leftButton.setEnabled(true);
                    rightButton.setEnabled(true);
                }
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to main activity or show final results
                finish();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to main activity
            }
        });

        // Generate first question
        generateQuestion();
        updateScoreDisplay();
        updateQuestionDisplay();
    }

    private void generateQuestion() {
        // Determine if asking for greater than or less than
        isGreaterThanQuestion = random.nextBoolean();

        if (isGreaterThanQuestion) {
            questionTextView.setText("Which number is BIGGER?");
        } else {
            questionTextView.setText("Which number is SMALLER?");
        }

        // Generate two random numbers (max 3 digits)
        do {
            leftNumber = random.nextInt(999) + 1; // 1-999
            rightNumber = random.nextInt(999) + 1; // 1-999
        } while (leftNumber == rightNumber); // Ensure numbers are different

        // Update UI
        leftNumberTextView.setText(String.valueOf(leftNumber));
        rightNumberTextView.setText(String.valueOf(rightNumber));
    }

    private void checkAnswer(boolean selectedLeft) {
        boolean isCorrect;

        // If we're asking for the bigger number
        if (isGreaterThanQuestion) {
            if (selectedLeft) {
                isCorrect = leftNumber > rightNumber;
            } else {
                isCorrect = rightNumber > leftNumber;
            }
        }
        // If we're asking for the smaller number
        else {
            if (selectedLeft) {
                isCorrect = leftNumber < rightNumber;
            } else {
                isCorrect = rightNumber < leftNumber;
            }
        }

        // Update score and provide feedback
        if (isCorrect) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        }

        questionCount++;
        updateScoreDisplay();
        updateQuestionDisplay();

        // Disable buttons until next question
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);

        // Check if we've reached the question limit
        if (questionCount >= TOTAL_QUESTIONS) {
            nextButton.setVisibility(View.GONE);
            finishButton.setVisibility(View.VISIBLE);

            // Show final score in a dialog
            showFinalScoreDialog();
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    private void showFinalScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Completed!");

        // Create a custom message based on score
        String message;
        if (score == TOTAL_QUESTIONS) {
            message = "Perfect! You got all " + score + " questions right!";
        } else if (score >= TOTAL_QUESTIONS-2) {
            message = "Good job! Your final score is " + score + "/" + TOTAL_QUESTIONS;
        } else {
            message = "Your final score is " + score + "/" + TOTAL_QUESTIONS + "\nKeep practicing!";
        }

        builder.setMessage(message);

        // Add a button to dismiss the dialog
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateScoreDisplay() {
        scoreTextView.setText("Score: " + score + "/" + TOTAL_QUESTIONS);
    }

    private void updateQuestionDisplay() {
        questionNumber.setText("Question " + questionCount + "/" + TOTAL_QUESTIONS);
    }


}

