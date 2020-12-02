package com.example.mycompany.catchtheball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class HardMainActivity extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;
    private ImageView tow_black;

    //サイズ
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //位置
    private float boxY;
    private float orangeX;
    private float orangeY;
    private float pinkX;
    private float pinkY;
    private float blackX;
    private float blackY;
    private float tow_blackX;
    private float tow_blackY;

    // Score
    private int score = 0;

    // Handler & Timer
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //Status
    private boolean action_flg = false;
    private boolean start_flg = false;

    //Sound
    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_main);

        soundPlayer = new SoundPlayer(this);

        scoreLabel = findViewById(R.id.hard_scoreLabel);
        startLabel = findViewById(R.id.hard_startLabel);
        box = findViewById(R.id.hard_box);
        orange = findViewById(R.id.hard_orange);
        pink = findViewById(R.id.hard_pink);
        black = findViewById(R.id.hard_black);
        tow_black = findViewById(R.id.hard_black_two);

        // Screen Size
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        orange.setX(-80.0f);
        orange.setY(-80.0f);
        pink.setX(-80.0f);
        pink.setY(-80.0f);
        black.setX(-80.0f);
        black.setY(-80.0f);
        tow_black.setX(-80.0f);
        tow_black.setY(-80.0f);

        scoreLabel.setText("Score : 0");
    }

    public void changePos(){

        hitCheck();

        // Orange
        orangeX -= 16;
        if (orangeX < 0){
            orangeX = screenWidth + 20;
            orangeY = (float)Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        // Black
        blackX -= 20;
        if (blackX < 0){
            blackX = screenWidth + 10;
            blackY = (float)Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        // Pink
        pinkX -= 25;
        if (pinkX < 0){
            pinkX = screenWidth + 5000;
            pinkY = (float)Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        //Black_Two
        tow_blackX -= 30;
        if (tow_blackX < 0){
            tow_blackX = screenWidth + 30;
            tow_blackY = (float)Math.floor(Math.random() * (frameHeight - tow_black.getHeight()));
        }
        tow_black.setX(tow_blackX);
        tow_black.setY(tow_blackY);

        // Box
        if (action_flg){
            boxY -= 20;

        }else {
            boxY += 20;
        }

        if (boxY < 0) boxY = 0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score : " + score);
    }

    public void hitCheck(){

        // Orange
        float orangeCenterX = orangeX + orange.getWidth() / 2;
        float orangeCenterY =orangeY + orange.getHeight() / 2;

        if (0 <= orangeCenterX && orangeCenterX <= boxSize && boxY <= orangeCenterY
                && orangeCenterY <= boxY + boxSize){

            orangeX = -10.0f;
            score += 10;
            soundPlayer.playHitSound();
        }

        // Pink
        float pinkCenterX = pinkX + pink.getWidth() / 2;
        float pinkCenterY = pinkY + pink.getHeight() / 2;

        if (0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY
                && pinkCenterY <= boxY + boxSize){

            pinkX = -10.0f;
            score += 30;
            soundPlayer.playHitSound();
        }

        // Black
        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;

        if (0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY
                && blackCenterY <= boxY + boxSize){

            soundPlayer.playOverSound();

            // Game Over!
            if (timer != null){
                timer.cancel();
                timer = null;
            }

            // 結果画面へ
            Intent intent = new Intent(getApplicationContext(), HardResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }

        // Tow_Black
        float tow_blackCenterX = tow_blackX + tow_black.getWidth() / 2;
        float tow_blackCenterY = tow_blackY + tow_black.getHeight() / 2;

        if (0 <= tow_blackCenterX && tow_blackCenterX <= boxSize && boxY <= tow_blackCenterY
                && tow_blackCenterY <= boxY + boxSize){

            soundPlayer.playOverSound();

            // Game Over!
            if (timer != null){
                timer.cancel();
                timer = null;
            }

            // 結果画面へ
            Intent intent = new Intent(getApplicationContext(), HardResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (start_flg == false){

            start_flg = true;

            FrameLayout frame = findViewById(R.id.hard_frame);
            frameHeight = frame.getHeight();

            boxY = box.getY();
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            action_flg = true;

        }else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
            action_flg = false;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
    }
}