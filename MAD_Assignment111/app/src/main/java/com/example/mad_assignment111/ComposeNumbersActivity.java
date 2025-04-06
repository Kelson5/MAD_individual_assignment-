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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComposeNumbersActivity extends AppCompatActivity {

    private TextView targetNumberTextView, firstNumberTextView, secondNumberTextView, sumTextView, scoreTextView, questionNumber;
    private Button checkButton, resetButton, nextButton, finishButton;
    private ImageView homeButton;

    private Button[] numberButtons = new Button[6]; // Six fixed buttons

    private int targetNumber;
    private int firstNumber = 0;
    private int secondNumber = 0;
    private boolean firstNumberSelected = false;
    private boolean secondNumberSelected = false;

    private Button selectedFirstButton;
    private Button selectedSecondButton;

    private int score = 0;
    private int questionCount = 0;
    private final int TOTAL_QUESTIONS = 5; // Limit to 5 questions
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_numbers);

        // Initialize views
        targetNumberTextView = findViewById(R.id.targetNumberTextView);
        firstNumberTextView = findViewById(R.id.firstNumberTextView);
        secondNumberTextView = findViewById(R.id.secondNumberTextView);
        sumTextView = findViewById(R.id.sumTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        questionNumber = findViewById(R.id.questionNumber);
        checkButton = findViewById(R.id.checkButton);
        resetButton = findViewById(R.id.resetButton);
        nextButton = findViewById(R.id.nextButton);
        finishButton = findViewById(R.id.finishButton);
        homeButton = findViewById(R.id.homeButton);

        // Initialize number buttons
        numberButtons[0] = findViewById(R.id.number1);
        numberButtons[1] = findViewById(R.id.number2);
        numberButtons[2] = findViewById(R.id.number3);
        numberButtons[3] = findViewById(R.id.number4);
        numberButtons[4] = findViewById(R.id.number5);
        numberButtons[5] = findViewById(R.id.number6);

        // Initially hide the finish button
        finishButton.setVisibility(View.GONE);

        // Set up click listeners for number buttons
        for (Button button : numberButtons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button clickedButton = (Button) v;
                    int number = Integer.parseInt(clickedButton.getText().toString());
                    selectNumber(clickedButton, number);
                }
            });
        }

        // Set up other button click listeners
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelection();
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
                finish(); // Return to main activity
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
        resetSelection();

        // Generate a target number (max 3 digits)
        targetNumber = random.nextInt(20) + 5; // 5-24 (suitable for fixed button grid)
        targetNumberTextView.setText(String.valueOf(targetNumber));

        // Generate numbers for the buttons
        generateNumbers();
    }

    private void generateNumbers() {
        // Get two numbers that add up to the target number
        int num1 = random.nextInt(targetNumber - 1) + 1; // 1 to (target-1)
        int num2 = targetNumber - num1;

        // Create an array for all the numbers we'll use
        List<Integer> allNumbers = new ArrayList<>();
        allNumbers.add(num1);
        allNumbers.add(num2);

        // Add additional random numbers
        while (allNumbers.size() < 6) {
            int randomNum = random.nextInt(15) + 1; // 1-15
            if (!allNumbers.contains(randomNum)) {
                allNumbers.add(randomNum);
            }
        }

        // Shuffle the numbers
        java.util.Collections.shuffle(allNumbers);

        // Assign numbers to buttons
        for (int i = 0; i < 6; i++) {
            numberButtons[i].setText(String.valueOf(allNumbers.get(i)));
            numberButtons[i].setEnabled(true);
            numberButtons[i].setBackgroundTintList(getResources().getColorStateList(R.color.colorCompose));
        }
    }

    private void selectNumber(Button button, int number) {
        // First number selection
        if (!firstNumberSelected) {
            firstNumber = number;
            firstNumberSelected = true;
            firstNumberTextView.setText(String.valueOf(number));
            selectedFirstButton = button;
            button.setEnabled(false);
            button.setBackgroundTintList(getResources().getColorStateList(R.color.colorDisabled));
            updateSum();
        }
        // Second number selection
        else if (!secondNumberSelected) {
            secondNumber = number;
            secondNumberSelected = true;
            secondNumberTextView.setText(String.valueOf(number));
            selectedSecondButton = button;
            button.setEnabled(false);
            button.setBackgroundTintList(getResources().getColorStateList(R.color.colorDisabled));
            updateSum();

            // Enable check button when both numbers are selected
            checkButton.setEnabled(true);
        }
    }

    private void updateSum() {
        if (firstNumberSelected && secondNumberSelected) {
            int sum = firstNumber + secondNumber;
            sumTextView.setText(String.valueOf(sum));
        } else if (firstNumberSelected) {
            sumTextView.setText("?");
        } else {
            sumTextView.setText("?");
        }
    }

    private void resetSelection() {
        firstNumber = 0;
        secondNumber = 0;
        firstNumberSelected = false;
        secondNumberSelected = false;

        firstNumberTextView.setText("?");
        secondNumberTextView.setText("?");
        sumTextView.setText("?");

        // Reset button states
        for (Button button : numberButtons) {
            button.setEnabled(true);

        }

        selectedFirstButton = null;
        selectedSecondButton = null;

        checkButton.setEnabled(false);
    }

    private void checkAnswer() {
        // Check if the sum equals the target number
        int sum = firstNumber + secondNumber;
        boolean isCorrect = sum == targetNumber;

        // Update score and provide feedback
        if (isCorrect) {
            score++;
            Toast.makeText(this, "Correct! " + firstNumber + " + " + secondNumber + " = " + targetNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong! " + firstNumber + " + " + secondNumber + " = " + sum + ", not " + targetNumber, Toast.LENGTH_SHORT).show();
        }

        questionCount++;
        updateScoreDisplay();
        updateQuestionDisplay();

        // Disable buttons until next question
        checkButton.setEnabled(false);
        for (Button button : numberButtons) {
            button.setEnabled(false);
        }

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
        } else if (score >= TOTAL_QUESTIONS / 2) {
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