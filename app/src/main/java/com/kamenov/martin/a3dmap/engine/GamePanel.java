package com.kamenov.martin.a3dmap.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kamenov.martin.a3dmap.engine.constants.EngineConstants;
import com.kamenov.martin.a3dmap.engine.models.game_objects.ComplexObject;
import com.kamenov.martin.a3dmap.engine.models.game_objects.Cube;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.GameObject;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.Object3D;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.Rotatable;
import com.kamenov.martin.a3dmap.engine.services.DrawingService;
import com.kamenov.martin.a3dmap.engine.services.PaintService;
import com.kamenov.martin.a3dmap.engine.services.factories.FigureFactory;
import com.kamenov.martin.a3dmap.engine.services.factories.IFigureFactory;
import com.kamenov.martin.a3dmap.engine.thread.GameThread;
import com.kamenov.martin.a3dmap.models.Background;
import com.kamenov.martin.a3dmap.views.MainActivity;

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
    private float initialX;
    private float initialY;
    private float smallestDifference = 10;
    private MainActivity mainActivity;

    public GamePanel(MainActivity mainActivity, DrawingService drawingService, IFigureFactory figureFactory) {
        super(mainActivity.getApplicationContext());
        this.mainActivity = mainActivity;
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
                initialX = event.getX();
                initialY = event.getY();
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
                float deltaXInitial = Math.abs(x2 - initialX);
                float deltaYInitial = Math.abs(y2 - initialY);
                if(deltaXInitial < smallestDifference && deltaYInitial < smallestDifference) {
                    addCube(x2, y2, (ComplexObject) figures.get(1));
                }
                else {
                    for (int i = 0; i < figures.size(); i++) {
                        Object3D figure = figures.get(i);
                        moveObject(deltaX, deltaY, figure);
                    }
                }
                draw();
                break;
        }
        return true;
    }

    private void addCube(float eventX, float eventY, ComplexObject figure) {
        ArrayList<Object3D> objects = figure.getObjects();
        if(objects.size() == 0) {
            return;
        }
        Object3D closestObject = objects.get(0);
        int index = 0;
        double closestDifference = getDifferenceUsingPythagoras(
                eventX,
                closestObject.x,
                eventY,
                closestObject.y);

        for(int i = 1; i < objects.size(); i++) {
            double currentDifference = getDifferenceUsingPythagoras(
                    eventX, objects.get(i).x,
                    eventY, objects.get(i).y);
            if(currentDifference < closestDifference) {
                closestDifference = currentDifference;
                index = i;
                closestObject = objects.get(i);
            }
        }

        mainActivity.createMapObject(index, -1);
        this.updateFigures();
    }

    private void updateFigures() {
        this.figures = FigureFactory.getInstance().getFigures();
    }

    private double getDifferenceUsingPythagoras(float x1, float x2, float y1, float y2) {
        return Math.sqrt(
                (x1 - x2)*(x1 - x2) +
                        (y1 - y2) * (y1 - y2)
        );
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
