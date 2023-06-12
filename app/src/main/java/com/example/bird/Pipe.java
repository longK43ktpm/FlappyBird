package com.example.bird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Pipe extends BaseObject{
    public static float speed;
    public Pipe(float x, float y, int width, int height) {
        super(x, y, width, height);
        speed = Constants.SCREEN_WIDTH * 0.3f/1080;
    }
    public void drawWhenPause(Canvas canvas){
        canvas.drawBitmap(this.bitmap, this.x, this.y, null);
    }

    public void draw(Canvas canvas, int time){
        //pipe sẽ di chuyển dựa trên số FPS hiện tại
        this.x -= this.speed * time;
        canvas.drawBitmap(this.bitmap, this.x, this.y, null);
    }

    @Override
    public void setBitmap(Bitmap bm){
        this.bitmap =  Bitmap.createScaledBitmap(bm, this.width, this.height, true);
    }

    public void randomY(){
        Random r = new Random();
        this.y = r.nextInt(this.height/2) - this.height/2;
    }
}
