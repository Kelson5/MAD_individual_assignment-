package com.example.mad_assignment111;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderNumbersActivity extends AppCompatActivity {

    private TextView questionTextView, scoreTextView, questionNumber;
    private Button[] numberButtons = new Button[4];
    private Button checkButton, nextButton, finishButton;
    private ImageView homeButton;

    private Integer[] numbers = new Integer[4];
    private Integer[] orderedNumbers = new Integer[4];
    private int[] selectedOrder = new int[4];
    private int selectionCount = 0;

    private boolean isAscendingOrder;
    private int score = 0;
    private int questionCount = 0;
    private final int TOTAL_QUESTIONS = 5; // Limit to 5 questions
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_numbers);

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        questionNumber = findViewById(R.id.questionNumber);

        numberButtons[0] = findViewById(R.id.numberButton1);
        numberButtons[1] = findViewById(R.id.numberButton2);
        numberButtons[2] = findViewById(R.id.numberButton3);
        numberButtons[3] = findViewById(R.id.numberButton4);

        checkButton = findViewById(R.id.checkButton);
        nextButton = findViewById(R.id.nextButton);
        finishButton = findViewById(R.id.finishButton);
        homeButton = findViewById(R.id.homeButton);

        // Initially hide the finish button
        finishButton.setVisibility(View.GONE);

        // Set up click listeners for number buttons
        for (int i = 0; i < 4; i++) {
            final int buttonIndex = i;
            numberButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectNumber(buttonIndex);
                }
            });
        }

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionCount < TOTAL_QUESTIONS) {
                    generateQuestion();
                    nextButton.setVisibility(View.INVISIBLE);
                    checkButton.setEnabled(true);
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
        // Reset UI state
        resetSelections();

        // Determine if asking for ascending or descending order
        isAscendingOrder = random.nextBoolean();

        if (isAscendingOrder) {
            questionTextView.setText("Put numbers in order from SMALLEST to BIGGEST");
        } else {
            questionTextView.setText("Put numbers in order from BIGGEST to SMALLEST");
        }

        // Generate four random numbers (max 3 digits)
        for (int i = 0; i < 4; i++) {
            numbers[i] = random.nextInt(999) + 1; // 1-999
        }

        // Create a copy for ordering
        for (int i = 0; i < 4; i++) {
            orderedNumbers[i] = numbers[i];
        }

        // Sort the orderedNumbers array
        Arrays.sort(orderedNumbers);

        // If descending order, reverse the array
        if (!isAscendingOrder) {
            List<Integer> list = Arrays.asList(orderedNumbers);
            Collections.reverse(list);
            orderedNumbers = list.toArray(new Integer[4]);
        }

        // Update the button text
        for (int i = 0; i < 4; i++) {
            numberButtons[i].setText(String.valueOf(numbers[i]));
            numberButtons[i].setEnabled(true);
            numberButtons[i].setBackgroundResource(android.R.drawable.btn_default);
        }
    }

    private void selectNumber(int buttonIndex) {
        if (numberButtons[buttonIndex].isEnabled()) {
            selectedOrder[selectionCount] = Integer.parseInt(numberButtons[buttonIndex].getText().toString());

            // Update button appearance to show selection order
            numberButtons[buttonIndex].setEnabled(false);
            numberButtons[buttonIndex].setBackgroundResource(android.R.drawable.btn_default_small);
            numberButtons[buttonIndex].setText(numberButtons[buttonIndex].getText() + " (" + (selectionCount + 1) + ")");

            selectionCount++;

            // Enable check button when all selections are made
            if (selectionCount == 4) {
                checkButton.setEnabled(true);
            }
        }
    }

    private void resetSelections() {
        selectionCount = 0;
        Arrays.fill(selectedOrder, 0);

        for (Button button : numberButtons) {
            button.setEnabled(true);
            button.setBackgroundResource(android.R.drawable.btn_default);
        }

        checkButton.setEnabled(false);
    }

    private void checkAnswer() {
        // Check if the selected order matches the expected order
        boolean isCorrect = true;
        for (int i = 0; i < 4; i++) {
            if (selectedOrder[i] != orderedNumbers[i]) {
                isCorrect = false;
                break;
            }
        }

        // Update score and provide feedback
        if (isCorrect) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
        }

        questionCount++;

        updateScoreDisplay();
        updateQuestionDisplay();

        // Disable buttons until next question
        checkButton.setEnabled(false);

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
        } else if (score >= TOTAL_QUESTIONS - 2) {
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