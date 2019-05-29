package com.kamenov.martin.a3dmap.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kamenov.martin.a3dmap.engine.constants.EngineConstants;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.GameObject;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.Object3D;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.Rotatable;
import com.kamenov.martin.a3dmap.engine.services.DrawingService;
import com.kamenov.martin.a3dmap.engine.services.factories.IFigureFactory;
import com.kamenov.martin.a3dmap.engine.thread.GameThread;
import com.kamenov.martin.a3dmap.models.Background;

import java.util.ArrayList;

/**
 * Created by Martin on 6.3.2018 г..
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, GameObject {
    private ArrayList<Object3D> figures;
    private DrawingService drawingService;
    private GameThread thread;
    private Background background;
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    public GamePanel(Context context, DrawingService drawingService, IFigureFactory figureFactory) {
        super(context);
        x1 = -1;
        x2 = -1;
        this.drawingService = drawingService;
        figures = figureFactory.getFigures();
        background = new Background(Color.parseColor("#ccfffa"));

        getHolder().addCallback(this);

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;

                for(int i = 0; i < figures.size(); i++)
                {
                    Object3D figure = figures.get(i);
                    moveObject(deltaX, deltaY, figure);
                }
                draw();
                x1 = x2;
                y1 = y2;
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                deltaX = x2 - x1;
                deltaY = y2 - y1;
                for(int i = 0; i < figures.size(); i++)
                {
                    Object3D figure = figures.get(i);
                    moveObject(deltaX, deltaY, figure);
                }
                draw();
                break;
        }
        return true;
    }

    private void moveObject(float deltaX, float deltaY, Object3D figure) {
        figure.rotateX3D(deltaY * EngineConstants.ROTATINGCOEF);
        figure.rotateY3D(deltaX * EngineConstants.ROTATINGCOEF);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.thread = new GameThread(GamePanel.this);
        thread.setRunning(true);

        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void update() {
        for(int i = 0; i < figures.size(); i++)
        {
            Rotatable figure = figures.get(i);
            figure.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        background.draw(canvas);
        drawingService.drawFiguresInAdditionOrder(canvas, figures);
    }

    private void draw() {
        Canvas canvas = null;
        try {
            canvas = this.getHolder().lockCanvas();
            synchronized (getHolder()) {
                this.draw(canvas);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(canvas != null) {
                try {
                    getHolder().unlockCanvasAndPost(canvas);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
