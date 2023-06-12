package com.example.bird;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static TextView textScore, textScoreOver, textBestScore;
    public static RelativeLayout layoutGameOver;
    public static Button buttonStart, buttonRestart, buttonPause, buttonResume;
    private GameView gameView;
    private MediaPlayer mediaPlayer;
    public static DBManager datasource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ẩn thanh trạng thái
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set width and height
        DisplayMetrics disMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(disMetrics);
        Constants.SCREEN_WIDTH = disMetrics.widthPixels;
        Constants.SCREEN_HEIGHT = disMetrics.heightPixels;
        setContentView(R.layout.activity_main);
        anhXa();
        //chạy nhạc nền
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.setStart(true);
                textScore.setVisibility(View.VISIBLE);
                buttonStart.setVisibility(View.INVISIBLE);
                buttonPause.setVisibility(View.VISIBLE);
            }
        });
        layoutGameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStart.setVisibility(View.VISIBLE);
                layoutGameOver.setVisibility(View.INVISIBLE);
                gameView.setStart(false);
                gameView.reset();
            }
        });
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.setPlaying(false);
                buttonPause.setVisibility(View.INVISIBLE);
                buttonResume.setVisibility(View.VISIBLE);
            }
        });
        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.setPlaying(true);
                buttonPause.setVisibility(View.VISIBLE);
                buttonResume.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void anhXa(){
        textScore = findViewById(R.id.text_score);
        textScoreOver = findViewById(R.id.text_score_over);
        textBestScore = findViewById(R.id.text_best_score);
        layoutGameOver = findViewById(R.id.layout_game_over);
        buttonStart = findViewById(R.id.btn_start);
        buttonRestart = findViewById(R.id.btn_restart);
        buttonPause = findViewById(R.id.btn_pause);
        buttonResume = findViewById(R.id.btn_resume);
        gameView = findViewById(R.id.game_view);
        datasource = new DBManager(this);
    }
}