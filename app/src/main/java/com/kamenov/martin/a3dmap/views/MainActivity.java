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
        float centerX = 25.151561f;
        float centerY = 42.624143f;
        double[][] pointsReversed = new double[][] {
                new double[] {44.215051, 22.675117},
                new double[] {44.067670, 23.042662},
                new double[] {43.835241, 22.881895},
                new double[] {43.847773, 23.408668},
                new double[] {43.793957, 23.771212},
                new double[] {43.684626, 24.166852},
                new double[] {43.756583, 24.550032},
                new double[] {43.685967, 25.204951},
                new double[] {43.655887, 25.580375},
                new double[] {43.803657, 25.893936},
                new double[] {43.948027, 26.099117},
                new double[] {44.040448, 26.407004},
                new double[] {44.067670, 23.042662},
                new double[] {44.067670, 23.042662},
                new double[] {44.019347, 27.391076},
                new double[] {44.041141, 27.643449},
                new double[] {43.992073, 27.919364},
                new double[] {43.835482, 27.991742},
                new double[] {43.737827, 28.585375},
                new double[] {43.537904, 28.604006},
                new double[] {44.067670, 23.042662},
                new double[] {43.383755, 28.112099},
                new double[] {43.184404, 27.901145},
                new double[] {43.049496, 27.906485},
                new double[] {42.921384, 27.897696},
                new double[] {43.184404, 27.901145},
                new double[] {42.712788, 27.745465},
                new double[] {42.670692, 27.710957},
                new double[] {42.595708, 27.628591},
                new double[] {42.523553, 27.487200},
                new double[] {42.457813, 27.454512},
                new double[] {42.407881, 27.728172},
                new double[] {42.148233, 27.878426},
                new double[] {41.985411, 28.010047},
                new double[] {41.949358, 27.830366},
                new double[] {42.093687, 27.266324},
                new double[] {41.963430, 26.626988},
                new double[] {41.713715, 26.353521},
                new double[] {41.664097, 26.059472},
                new double[] {41.353433, 26.127286},
                new double[] {41.243400, 25.268450},
                new double[] {41.556681, 24.525588},
                new double[] {41.459533, 24.069095},
                new double[] {41.400306, 23.283040},
                new double[] {41.329405, 23.204184},
                new double[] {41.338045, 22.944268},
                new double[] {41.653307, 22.962949},
                new double[] {41.998912, 22.860332},
                new double[] {42.170618, 22.498501},
                new double[] {42.331539, 22.360466},
                new double[] {42.481075, 22.550355},
                new double[] {42.572907, 22.438416},
                new double[] {42.817753, 22.442063},
                new double[] {42.890653, 22.747667},
                new double[] {43.123483, 22.990001},
                new double[] {43.194259, 23.007497},
                new double[] {43.390994, 22.752641},
                new double[] {43.477704, 22.523036},
                new double[] {43.774954, 22.372485},
                new double[] {43.922791, 22.393988},
                new double[] {44.004352, 22.418618},
                new double[] {44.048154, 22.542222},
                new double[] {44.093995, 22.618968}
        };

        DeepPoint[] points = new DeepPoint[pointsReversed.length];
        for(int i = 0; i < pointsReversed.length; i++) {
            points[i] = new DeepPoint((float)pointsReversed[i][1] - centerX,
                    (float)pointsReversed[i][0] - centerY, 0);
        }

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