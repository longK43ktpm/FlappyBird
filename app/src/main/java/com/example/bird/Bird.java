package com.example.bird;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Bird extends BaseObject{
    private int count, maxCount, currentBitmap;
    private ArrayList<Bitmap> animation;
    private float drop;
    public Bird(){
        count = 0;
        maxCount = 3;
        currentBitmap = 0;
        drop = 0;
    }
    public void drawWhenPause(Canvas canvas){
        canvas.drawBitmap(this.getBitmap(), this.x, this.y, null);
    }
    public void draw(Canvas canvas, int time){
        //bird sẽ di chuyển dựa trên số FPS hiện tại
        drop(time);
        canvas.drawBitmap(this.getBitmap(), this.x, this.y, null);
    }

    private void drop(int time) {
        this.drop += 0.1 * time;
        if (drop > 10f/((float)time/100f))
            drop = 10f/((float)time/100f);
        this.y += this.drop * time/30;
    }

    public ArrayList<Bitmap> getAnimation() {
        return animation;
    }

    public void setAnimation(ArrayList<Bitmap> animation) {
        this.animation = animation;
        //phóng to hình ảnh vừa với width và heigh
        for (int i = 0; i < animation.size(); i++)
            animation.set(i, Bitmap.createScaledBitmap(animation.get(i), this.width, this.height, true));
    }
    @Override
    public Bitmap getBitmap(){
        count++;
        if (count == maxCount){
            count = 0;
            currentBitmap++;
            if (currentBitmap == animation.size())
                currentBitmap = 0;
        }
        return this.getAnimation().get(currentBitmap);
    }

    public float getDrop() {
        return drop;
    }

    public void setDrop(float drop) {
        this.drop = drop;
    }
}
