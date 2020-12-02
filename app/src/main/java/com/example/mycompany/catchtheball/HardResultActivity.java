package com.example.mycompany.catchtheball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HardResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_result);

        TextView scoreLabel = findViewById(R.id.hard_scoreLabel);
        TextView highScoreLabel = findViewById(R.id.hard_high_score);

        int score = getIntent().getIntExtra("SCORE",0 );
        scoreLabel.setText(score + "");

        SharedPreferences sharedPreferences = getSharedPreferences("HARD_GAME_DATA",
                Context.MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("HARD_HIGH_SCORE", 0);

        if (score > highScore){
            highScoreLabel.setText("High Score : " + score);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HARD_HIGH_SCORE", score);
            editor.apply();

        }else {
            highScoreLabel.setText("High Score : " + highScore);
        }
    }
    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), HardMainActivity.class));
    }
    public void Normal(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}