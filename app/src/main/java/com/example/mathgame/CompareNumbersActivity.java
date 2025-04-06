package com.example.mathgame;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.media.MediaPlayer; // For sound effects

import java.util.Random;

public class CompareNumbersActivity extends AppCompatActivity {

    private TextView number1Text, number2Text, resultText, scoreText;
    private Button greaterButton, lessButton, equalButton, refreshButton;
    private int number1, number2;
    private int score = 0; // Track score

    // MediaPlayer to play sound effects
    private MediaPlayer correctSound, incorrectSound;

    // Difficulty Spinner
    private Spinner difficultySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_numbers);

        // Initialize views
        number1Text = findViewById(R.id.number1Text);
        number2Text = findViewById(R.id.number2Text);
        resultText = findViewById(R.id.resultText);
        scoreText = findViewById(R.id.scoreText);  // Bind the scoreText TextView
        greaterButton = findViewById(R.id.greaterButton);
        lessButton = findViewById(R.id.lessButton);
        equalButton = findViewById(R.id.equalButton);
        refreshButton = findViewById(R.id.refreshButton);

        // Initialize Spinner
        difficultySpinner = findViewById(R.id.difficultySpinner);

        // Initialize sounds
        correctSound = MediaPlayer.create(this, R.raw.correct_sound); // Correct sound
        incorrectSound = MediaPlayer.create(this, R.raw.incorrect_sound); // Incorrect sound

        // Set up difficulty spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        // Set up listener for difficulty selection
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDifficulty = parentView.getItemAtPosition(position).toString();
                setDifficulty(selectedDifficulty); // Update difficulty based on selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Set up buttons for actions
        greaterButton.setOnClickListener(v -> checkGreater());
        lessButton.setOnClickListener(v -> checkLess());
        equalButton.setOnClickListener(v -> checkEqual());
        refreshButton.setOnClickListener(v -> refreshNumbers());

        // Generate initial numbers
        generateNumbers();
        updateUI();
    }

    private String difficulty = "Medium";  // Default difficulty

    private void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        generateNumbers();  // Regenerate numbers based on new difficulty
        updateUI();
    }

    private void generateNumbers() {
        Random random = new Random();
        int min = 1;
        int max = 10;  // Default for Easy

        if ("Medium".equals(difficulty)) {
            min = 10;
            max = 999;
        } else if ("Hard".equals(difficulty)) {
            min = 1000;
            max = 9999;
        }

        // Generate numbers within the correct range
        number1 = random.nextInt((max - min) + 1) + min;
        number2 = random.nextInt((max - min) + 1) + min;
    }

    private void updateUI() {
        number1Text.setText("Number 1: " + number1);
        number2Text.setText("Number 2: " + number2);
        scoreText.setText("Score: " + score);  // Update score
    }

    private void checkGreater() {
        if (number1 > number2) {
            resultText.setText("Correct! " + number1 + " is greater than " + number2);
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Correct answer visual feedback
            score++;
            correctSound.start();
        } else {
            resultText.setText("Oops! " + number1 + " is not greater than " + number2);
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Incorrect answer visual feedback
            incorrectSound.start();
        }
    }

    private void checkLess() {
        if (number1 < number2) {
            resultText.setText("Correct! " + number1 + " is less than " + number2);
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Correct answer visual feedback
            score++;
            correctSound.start();
        } else {
            resultText.setText("Oops! " + number1 + " is not less than " + number2);
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Incorrect answer visual feedback
            incorrectSound.start();
        }
    }

    private void checkEqual() {
        if (number1 == number2) {
            resultText.setText("Correct! " + number1 + " is equal to " + number2);
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light)); // Correct answer visual feedback
            score++;
            correctSound.start();
        } else {
            resultText.setText("Oops! " + number1 + " is not equal to " + number2);
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Incorrect answer visual feedback
            incorrectSound.start();
        }
    }

    private void refreshNumbers() {
        generateNumbers(); // Generate new random numbers
        updateUI(); // Update the UI with new numbers
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources
        if (correctSound != null) {
            correctSound.release();
        }
        if (incorrectSound != null) {
            incorrectSound.release();
        }
    }
}




