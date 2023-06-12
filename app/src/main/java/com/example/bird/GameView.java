package com.example.bird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.logging.LogRecord;

public class GameView extends View {
    private Bird bird;
    private android.os.Handler handler; //handler để sleep
    private Runnable runnable;
    private ArrayList<Pipe> arrPipes;
    private int totalPipe, distance;
    private int score;
    private boolean start, isPlaying, loadedSound;
    private int soundJump;
    private SoundPool soundPool;
    private int FRAME_PER_SECOND;
    private int TIME_PER_FRAME;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        FRAME_PER_SECOND = 40;
        TIME_PER_FRAME = 1000 / FRAME_PER_SECOND;
        score = 0;
        start = false;
        isPlaying = true;
        initBird();
        initPipe();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        //load sound
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                loadedSound = true;
            }
        });
        soundJump = soundPool.load(context, R.raw.click2, 1);
    }

    private void initPipe() {
        arrPipes = new ArrayList<>();
        totalPipe = 4;
        distance = Constants.SCREEN_HEIGHT*500/1920;
        for (int i = 0; i < totalPipe; i++) {
            if (i < totalPipe/2) {
                this.arrPipes.add(new Pipe(Constants.SCREEN_WIDTH + i*((Constants.SCREEN_WIDTH+Constants.SCREEN_WIDTH*200/1080)/(totalPipe/2)),
                        0, Constants.SCREEN_WIDTH*200/1080, Constants.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe_2));
                this.arrPipes.get(this.arrPipes.size()-1).randomY();
            }
            else {
                this.arrPipes.add(new Pipe(this.arrPipes.get(i-totalPipe/2).getX(), this.arrPipes.get(i-totalPipe/2).getY()
                    + this.arrPipes.get(i-totalPipe/2).getHeight() + this.distance, Constants.SCREEN_WIDTH*200/1080, Constants.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pipe_1));
            }
        }
    }

    private void initBird() {
        bird = new Bird();
        bird.setX(Constants.SCREEN_WIDTH*100/1080);
        bird.setY(Constants.SCREEN_HEIGHT/2 - bird.getHeight()/2);
        bird.setWidth(Constants.SCREEN_WIDTH*250/1080);
        bird.setHeight(Constants.SCREEN_HEIGHT*150/1920);
        ArrayList<Bitmap> animation = new ArrayList<>();
        animation.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cat1));
        animation.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cat2));
        animation.add(BitmapFactory.decodeResource(this.getResources(), R.drawable.cat3));
        bird.setAnimation(animation);
    }


    @Override
    public void draw (Canvas canvas) {
        if (isPlaying) {
            super.draw(canvas);
            updateAndDraw(canvas);
            handler.postDelayed(runnable, TIME_PER_FRAME);
        } else {
            super.draw(canvas);
            bird.drawWhenPause(canvas);
            for (int i = 0; i < totalPipe; i++) {
                arrPipes.get(i).drawWhenPause(canvas);
            }
            handler.postDelayed(runnable, TIME_PER_FRAME);
        }
    }

    private void updateAndDraw(Canvas canvas){
        //khi nhấn nút START
        if (start) {

            for (int i = 0; i < totalPipe; i++) {
                //kiểm tra va chạm
                if (bird.getRect().intersect(arrPipes.get(i).getRect()) || bird.getY() - bird.getHeight() < 0 || bird.getY() > Constants.SCREEN_HEIGHT) {
                    Pipe.speed = 0;
                    MainActivity.textScoreOver.setText(MainActivity.textScore.getText());
                    int score = Integer.parseInt(MainActivity.textScore.getText().toString());
                    int best = MainActivity.datasource.UpdateBestScore(score);
                    MainActivity.textBestScore.setText("Best Score: " + best);
                    MainActivity.textScore.setVisibility(INVISIBLE);
                    MainActivity.layoutGameOver.setVisibility(VISIBLE);
                    MainActivity.buttonPause.setVisibility(INVISIBLE);
                    MainActivity.buttonResume.setVisibility(INVISIBLE);
                }
                //kiểm tra ghi điểm
                if (this.bird.getX() + this.bird.getWidth() > arrPipes.get(i).getX() + arrPipes.get(i).getWidth() / 2f
                        && this.bird.getX() + this.bird.getWidth() <= arrPipes.get(i).getX() + arrPipes.get(i).getWidth() / 2f + arrPipes.get(i).speed * TIME_PER_FRAME
                        && i < totalPipe / 2) {
                    score++;
                    MainActivity.textScore.setText("" + score);
                }
                //update khi pipe đi hết màn hình
                if (this.arrPipes.get(i).getX() < -arrPipes.get(i).getWidth()) {
                    this.arrPipes.get(i).setX(Constants.SCREEN_WIDTH);
                    if (i < totalPipe / 2)
                        this.arrPipes.get(i).randomY();
                    else
                        arrPipes.get(i).setY(this.arrPipes.get(i - totalPipe / 2).getY()
                                + this.arrPipes.get(i - totalPipe / 2).getHeight() + this.distance);
                }
                //draw tất cả pipe
                this.arrPipes.get(i).draw(canvas, TIME_PER_FRAME);

            }
            bird.draw(canvas, TIME_PER_FRAME);
        }
        //khi chưa nhấn nút START
        else {
            if (bird.getY() > Constants.SCREEN_HEIGHT / 2)
                bird.setDrop(Constants.SCREEN_HEIGHT * -20 / 1920);
            bird.draw(canvas, TIME_PER_FRAME);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            bird.setDrop(-38f);
            //tiếng chim kiêu khi chạm vào màn hình
            if (loadedSound)
                soundPool.play(soundJump, 0.5f, 0.5f, 1, 0, 1f);
        }
        return super.onTouchEvent(event);
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity.textScore.setText("0");
        score = 0;
        initBird();
        initPipe();
    }
    public void setPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }
}
