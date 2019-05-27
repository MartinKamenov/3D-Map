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
        gamePanel = new GamePanel(this, drawingService, FigureFactory.getInstance());

        relativeLayout.addView(gamePanel);
    }

    private void createMapObject() {
        DeepPoint[] points = new DeepPoint[] {
                new DeepPoint(100, 100, 0),
                new DeepPoint(100, -100, 0),
                new DeepPoint(-100, 0, 0)
        };

        ArrayList<DeepPoint[]> parts = new ArrayList<>();
        parts.add(points);

        PartsObject mapObject = new PartsObject(
                EngineConstants.SCREEN_WIDTH / 2,
                EngineConstants.SCREEN_HEIGHT / 2,
                0,
                PaintService.createEdgePaint("red"),
                PaintService.createWallPaint("blue"),
                1,
                points,
                parts
        );

        ArrayList<Object3D> objects = new ArrayList<>();
        objects.add(mapObject);
        FigureFactory.getInstance().setFigures(objects);
    }
}
