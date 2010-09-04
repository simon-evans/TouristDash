/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.simonevans.touristdash.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.String;

/**
 *
 * @author Simon
 */
public class GameCanvas extends SurfaceView implements SurfaceHolder.Callback {

    public GameCanvas(Context context, Handler handle) {
        super(context);

        this.getHolder().addCallback(this);

        messageHandler = handle;
        canvasThread = new CanvasThread();

        game = new Game();
        Log.d(this.getClass().getName(), "FOO BAR");
        setFocusable(true);
    }

    Handler messageHandler;
    CanvasThread canvasThread;
    Game game;
    int backgroundY;

    Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
    Bitmap cameraManBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.camera_man);
    Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_1);

    @Override
    public void onDraw(Canvas c) {

        c.drawColor(Color.WHITE);
        
        drawBackground(c);

        drawUser(c);
        drawEnemies(c);

        drawScore(c);
        

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(!canvasThread.isAlive()) {
            canvasThread = new CanvasThread();
        }

        backgroundY = -480;
        
        canvasThread.run = true;
        canvasThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

    }

    private void drawUser(Canvas c) {
        c.drawBitmap(playerBitmap, game.userXCoord, game.userYCoord, null);
    }


    private void drawEnemies(Canvas c) {

        Game.Enemy enemy;
        for(int i = 0; i < game.enemies.size(); i++) {
            enemy = game.enemies.get(i);
            c.drawBitmap(cameraManBitmap, enemy.xCoord, enemy.yCoord, null);
        }
    }

    private void drawScore(Canvas c) {

        String text = "Score: ".concat(String.valueOf(game.score));

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        c.drawRect(0,0,320,20, paint);

        paint.setTextSize(20);
        paint.setColor(Color.BLACK);

        paint.setStyle(Paint.Style.STROKE);
        c.drawText(text, 5.0f, 15.0f, paint);
        c.drawLine(0, 20, 320, 20, paint);
    }

    private void drawBackground(Canvas c) {
        c.drawBitmap(backgroundBitmap, 0, backgroundY, null);
    }


    public class CanvasThread extends Thread {

        boolean run = false;
        Handler messageHandler;

        CanvasThread() {
            
        }

        @Override
        public void run() {
            Canvas c;
            while(run) {
                if(game.gameover) {
                    run = false;
                    Message msg = new Message();
                    msg.what = TouristDash.GAMEOVER;
                    GameCanvas.this.messageHandler.sendMessage(msg);
                } else {
                    game.update();
                    backgroundY += 5;
                    if(backgroundY == 0) {
                        backgroundY = -480;
                    }
                    c = null;
                    try {
                        c = GameCanvas.this.getHolder().lockCanvas();
                        synchronized(GameCanvas.this.getHolder()) {
                            onDraw(c);
                        }
                    } finally {
                        if(c != null) {
                            getHolder().unlockCanvasAndPost(c);
                        }
                    }
                }
            }

        }

    }


}
