package com.kamenov.martin.a3dmap.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.kamenov.martin.a3dmap.R;
import com.kamenov.martin.a3dmap.engine.GamePanel;
import com.kamenov.martin.a3dmap.engine.constants.EngineConstants;
import com.kamenov.martin.a3dmap.engine.models.game_objects.PartsObject;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.DeepPoint;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.DrawingPart;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.Object3D;
import com.kamenov.martin.a3dmap.engine.services.DrawingService;
import com.kamenov.martin.a3dmap.engine.services.PaintService;
import com.kamenov.martin.a3dmap.engine.services.SortingService;
import com.kamenov.martin.a3dmap.engine.services.factories.FigureFactory;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private GamePanel gamePanel;
    private DrawingService drawingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        EngineConstants.SCREEN_WIDTH = dm.widthPixels;
        EngineConstants.SCREEN_HEIGHT = dm.heightPixels;

        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = findViewById(R.id.container);
        drawingService = DrawingService.getInstance(SortingService.getInstance());
        createMapObject();
        //createCubeObject();
        gamePanel = new GamePanel(this, drawingService, FigureFactory.getInstance());

        relativeLayout.addView(gamePanel);
    }

    private void createCubeObject() {
        FigureFactory.getInstance().createCube(0, 0, 0, 100, PaintService.createEdgePaint("red"),
                PaintService.createWallPaint("blue"), 1);
    }

    private void createMapObject() {
        Float[][] mapPoints = new Float[][] {
                new Float[] {EngineConstants.SCREEN_WIDTH / 2 + 0f, EngineConstants.SCREEN_HEIGHT / 2 + 0f},
                new Float[] {EngineConstants.SCREEN_WIDTH / 2 + 5f, EngineConstants.SCREEN_HEIGHT / 2 + 5f},
                new Float[] {EngineConstants.SCREEN_WIDTH / 2 + 0f, EngineConstants.SCREEN_HEIGHT / 2 + 5f}
        };

        DeepPoint[] mapDeepPoints = new DeepPoint[mapPoints.length];
        ArrayList<DeepPoint[]> parts = new ArrayList<>();
        for(int i = 0; i < mapPoints.length; i++) {
            DeepPoint point = new DeepPoint(mapPoints[i][0], mapPoints[i][1], 0);
            mapDeepPoints[i] = point;
            if(i > 0) {
                DeepPoint[] drawingPartPoints = new DeepPoint[] {mapDeepPoints[i - 1], mapDeepPoints[i]};
                parts.add(drawingPartPoints);
            }

            if(i == mapPoints.length - 1) {
                DeepPoint[] drawingPartPoints = new DeepPoint[] {mapDeepPoints[0], mapDeepPoints[i]};
                parts.add(drawingPartPoints);
            }
        }

        PartsObject mapObject = new PartsObject(
                EngineConstants.SCREEN_WIDTH / 2,
                EngineConstants.SCREEN_HEIGHT / 2,
                0,
                PaintService.createEdgePaint("red"),
                PaintService.createWallPaint("blue"),
                1,
                mapDeepPoints,
                parts
        );

        ArrayList<Object3D> objects = new ArrayList<>();
        objects.add(mapObject);
        FigureFactory.getInstance().setFigures(objects);
    }
}
