package com.example.mathgame;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrderNumbersActivity extends AppCompatActivity {

    private RecyclerView numbersRecyclerView;
    private NumbersAdapter numbersAdapter;
    private Button checkAscendingOrderButton, checkDescendingOrderButton, refreshButton;
    private TextView resultText, scoreText;
    private Spinner difficultySpinner, gameModeSpinner;

    private List<Integer> numbersList;
    private int score = 0;

    // Sound effects
    private MediaPlayer correctSound, incorrectSound;

    // Difficulty
    private String difficulty = "Medium";
    // Game mode
    private String gameMode = "Standard";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_numbers);

        // Initialize views
        numbersRecyclerView = findViewById(R.id.numbersRecyclerView);
        checkAscendingOrderButton = findViewById(R.id.checkAscendingOrderButton);
        checkDescendingOrderButton = findViewById(R.id.checkDescendingOrderButton);
        refreshButton = findViewById(R.id.refreshButton);
        resultText = findViewById(R.id.resultText);
        scoreText = findViewById(R.id.scoreText);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        gameModeSpinner = findViewById(R.id.gameModeSpinner);

        // Initialize sounds
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
                refreshNumbers(); // Regenerate numbers based on difficulty
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // Setup game mode spinner
        ArrayAdapter<CharSequence> modeAdapter = ArrayAdapter.createFromResource(
                this, R.array.game_modes, android.R.layout.simple_spinner_item
        );
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpinner.setAdapter(modeAdapter);

        gameModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                gameMode = parentView.getItemAtPosition(position).toString();
                refreshNumbers(); // Possibly different logic per game mode
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        // RecyclerView + Adapter
        numbersList = new ArrayList<>();
        numbersAdapter = new NumbersAdapter(numbersList);
        numbersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        numbersRecyclerView.setAdapter(numbersAdapter);

        // Enable drag-and-drop
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // Allow up/down drag
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
                int fromPosition = source.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(numbersList, fromPosition, toPosition);
                numbersAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Not used
            }
        });
        itemTouchHelper.attachToRecyclerView(numbersRecyclerView);

        // Button listeners
        checkAscendingOrderButton.setOnClickListener(v -> checkAscendingOrder());
        checkDescendingOrderButton.setOnClickListener(v -> checkDescendingOrder());
        refreshButton.setOnClickListener(v -> refreshNumbers());

        // Generate initial list
        generateNumbers();
        updateUI();
    }

    // Generate numbers based on difficulty and game mode
    private void generateNumbers() {
        Random random = new Random();
        numbersList.clear();

        int rangeMin = 1;
        int rangeMax = 50;  // Default for Easy

        if (difficulty.equals("Easy")) {
            rangeMin = 1;
            rangeMax = 50;
        } else if (difficulty.equals("Medium")) {
            rangeMin = 10;
            rangeMax = 99;
        } else if (difficulty.equals("Hard")) {
            rangeMin = 100;
            rangeMax = 999;
        }

        int count = 5; // Default number of items

        // Adjust count or logic to handle game modes differently
        if (gameMode.equals("Challenge")) {
            // Maybe generate more numbers, or a set number for a challenge
            count = 6;
        }

        for (int i = 0; i < count; i++) {
            int randomNumber = random.nextInt(rangeMax - rangeMin + 1) + rangeMin;
            numbersList.add(randomNumber);
        }
    }

    private void updateUI() {
        numbersAdapter.notifyDataSetChanged();
        scoreText.setText("Score: " + score);
        resultText.setText("");
        resultText.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void checkAscendingOrder() {
        if (isAscending(numbersList)) {
            resultText.setText("Correct! Ascending Order.");
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            score++;
            correctSound.start();
        } else {
            resultText.setText("Incorrect. Not Ascending.");
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            incorrectSound.start();
        }
        updateScore();
    }

    private void checkDescendingOrder() {
        if (isDescending(numbersList)) {
            resultText.setText("Correct! Descending Order.");
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            score++;
            correctSound.start();
        } else {
            resultText.setText("Incorrect. Not Descending.");
            resultText.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            incorrectSound.start();
        }
        updateScore();
    }

    private void refreshNumbers() {
        // Reset or keep the score depending on your design
        // If you want to reset score each time, uncomment the line below
        // score = 0;
        generateNumbers();
        updateUI();
    }

    private void updateScore() {
        scoreText.setText("Score: " + score);
    }

    private boolean isAscending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) return false;
        }
        return true;
    }

    private boolean isDescending(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) < list.get(i + 1)) return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release media players
        if (correctSound != null) correctSound.release();
        if (incorrectSound != null) incorrectSound.release();
    }
}
