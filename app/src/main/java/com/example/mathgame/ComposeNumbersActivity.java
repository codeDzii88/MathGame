package com.example.mathgame;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.media.MediaPlayer;

import java.util.Random;

public class ComposeNumbersActivity extends AppCompatActivity {

    private TextView targetNumberText, resultText, scoreText;
    private EditText part1EditText, part2EditText;
    private Button composeButton, refreshButton;
    private Spinner difficultySpinner, gameModeSpinner;

    // Score
    private int score = 0;

    // Sounds
    private MediaPlayer correctSound, incorrectSound;

    // Difficulty and game mode
    private String difficulty = "Medium";
    private String gameMode = "Standard";

    private int targetNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_numbers);

        // Initialize views
        targetNumberText = findViewById(R.id.targetNumberText);
        resultText = findViewById(R.id.resultText);
        scoreText = findViewById(R.id.scoreText);
        part1EditText = findViewById(R.id.part1EditText);
        part2EditText = findViewById(R.id.part2EditText);
        composeButton = findViewById(R.id.composeButton);
        refreshButton = findViewById(R.id.refreshButton);
        difficultySpinner = findViewById(R.id.difficultySpinner);

        // Initialize sound
        correctSound = MediaPlayer.create(this, R.raw.correct_sound);
        incorrectSound = MediaPlayer.create(this, R.raw.incorrect_sound);

        // Setup difficulty spinner
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(
                this, R.array.difficulty_levels, android.R.layout.simple_spinner_item
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                difficulty = parentView.getItemAtPosition(position).toString();
                refreshNumbers();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });


        // Buttons
        composeButton.setOnClickListener(v -> checkComposition());
        refreshButton.setOnClickListener(v -> refreshNumbers());

        // Generate first target
        generateTargetNumber();
        updateUI();
    }

    private void generateTargetNumber() {
        Random random = new Random();

        int rangeMin = 1;
        int rangeMax = 999;

        if (difficulty.equals("Easy")) {
            rangeMax = 50; // smaller max
        } else if (difficulty.equals("Medium")) {
            rangeMax = 999;
        } else if (difficulty.equals("Hard")) {
            rangeMax = 9999; // even larger
        }

        targetNumber = random.nextInt(rangeMax - rangeMin + 1) + rangeMin;
    }

    private void checkComposition() {
        String part1Str = part1EditText.getText().toString().trim();
        String part2Str = part2EditText.getText().toString().trim();

        if (part1Str.isEmpty() || part2Str.isEmpty()) {
            resultText.setText("Enter both parts!");
            return;
        }

        int part1 = Integer.parseInt(part1Str);
        int part2 = Integer.parseInt(part2Str);

        if (part1 + part2 == targetNumber) {
            resultText.setText("Correct! " + part1 + " + " + part2 + " = " + targetNumber);
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            score++;
            correctSound.start();
        } else {
            resultText.setText("Oops! That doesn't add up to " + targetNumber);
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            incorrectSound.start();
        }

        updateScore();
    }

    private void refreshNumbers() {
        // If you want to reset the score each time, uncomment
        // score = 0;
        generateTargetNumber();
        updateUI();
        // Clear inputs
        part1EditText.setText("");
        part2EditText.setText("");
    }

    private void updateUI() {
        targetNumberText.setText("Target Number: " + targetNumber);
        scoreText.setText("Score: " + score);
        resultText.setText("");
        resultText.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void updateScore() {
        scoreText.setText("Score: " + score);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (correctSound != null) correctSound.release();
        if (incorrectSound != null) incorrectSound.release();
    }
}


