package com.kamenov.martin.a3dmap.views;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.kamenov.martin.a3dmap.R;
import com.kamenov.martin.a3dmap.engine.GamePanel;
import com.kamenov.martin.a3dmap.engine.constants.EngineConstants;
import com.kamenov.martin.a3dmap.engine.models.game_objects.ComplexObject;
import com.kamenov.martin.a3dmap.engine.models.game_objects.Cube;
import com.kamenov.martin.a3dmap.engine.models.game_objects.PartsObject;
import com.kamenov.martin.a3dmap.engine.models.game_objects.Sphere;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.DeepPoint;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.DrawingPart;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.Object3D;
import com.kamenov.martin.a3dmap.engine.services.DrawingService;
import com.kamenov.martin.a3dmap.engine.services.PaintService;
import com.kamenov.martin.a3dmap.engine.services.SortingService;
import com.kamenov.martin.a3dmap.engine.services.factories.FigureFactory;
import com.kamenov.martin.a3dmap.models.Background;
import com.kamenov.martin.a3dmap.models.Town;
import com.kamenov.martin.a3dmap.models.TownsGraphManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    private GamePanel gamePanel;
    private DrawingService drawingService;
    public float centerX;
    public float centerY;
    public float sizeCoef;
    private View calculateButton;
    private EditText fromCity;
    private EditText toCity;
    private Town[] towns;

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
        drawingService.setEdgePaint(PaintService.createEdgePaint("#00966E"));
        createMapObject(-1, -1);
        gamePanel = new GamePanel(this, drawingService, FigureFactory.getInstance());

        relativeLayout.addView(gamePanel);

        LinearLayout buttonContainer = findViewById(R.id.selection_container);
        buttonContainer.bringToFront();

        fromCity = findViewById(R.id.from_city);
        toCity = findViewById(R.id.to_city);
        calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(this);
    }

    public void createMapObject(int firstCubeIndex, int secondCubeIndex) {
        centerX = 25.151561f;
        centerY = 42.624143f;
        sizeCoef = EngineConstants.SCREEN_WIDTH / 8;
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
                new double[] {44.019347, 27.391076},
                new double[] {44.041141, 27.643449},
                new double[] {43.992073, 27.919364},
                new double[] {43.835482, 27.991742},
                new double[] {43.737827, 28.585375},
                new double[] {43.537904, 28.604006},
                new double[] {43.383755, 28.112099},
                new double[] {43.184404, 27.901145},
                new double[] {43.049496, 27.906485},
                new double[] {42.921384, 27.897696},
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
                new double[] {44.093995, 22.618968},
                new double[] {44.215051, 22.675117}
        };

        DeepPoint[] points = new DeepPoint[pointsReversed.length];
        for(int i = 0; i < pointsReversed.length; i++) {
            float x = ((float)pointsReversed[i][1] - centerX) * sizeCoef;
            float y = ((float)pointsReversed[i][0] - centerY) * -sizeCoef;
            points[i] = new DeepPoint(x, y, 0);
        }

        ArrayList<DeepPoint[]> parts = new ArrayList<>();
        parts.add(points);

        PartsObject mapObject = new PartsObject(
                EngineConstants.SCREEN_WIDTH / 2,
                EngineConstants.SCREEN_HEIGHT / 2,
                0,
                PaintService.createEdgePaint("white"),
                PaintService.createWallPaint("black"),
                1,
                points,
                parts
        );

        InputStream inputStream = getResources().openRawResource(
                getResources().getIdentifier("bg",
                        "raw", getPackageName()));
        String townsString = getStringFromInputStream(inputStream);

        Gson gson = new Gson();
        towns = gson.fromJson(townsString, Town[].class);

        TownsGraphManager manager = TownsGraphManager.getInstance(towns);

        ArrayList<Object3D> objects = new ArrayList<>();
        int townsSizeCoef = 118500;

        for(int i = 0; i < towns.length; i++) {
            float x = ((float)towns[i].lng - centerX) * sizeCoef;
            float y = ((float)towns[i].lat - centerY) * -sizeCoef;

            // Radius is calculated adding town's size proportion to two
            float radius = 2 + (towns[i].population / townsSizeCoef);
            Object3D town;
            if(i == firstCubeIndex || i == secondCubeIndex) {
                town = new Sphere(
                        EngineConstants.SCREEN_WIDTH / 2 + x,
                        EngineConstants.SCREEN_HEIGHT / 2 + y,
                        0,
                        PaintService.createWallPaint("aa"),
                        PaintService.createWallPaint("red"),
                        1,
                        15
                );
            } else {
                town = new Sphere(
                        EngineConstants.SCREEN_WIDTH / 2 + x,
                        EngineConstants.SCREEN_HEIGHT / 2 + y,
                        0,
                        PaintService.createWallPaint("aa"),
                        PaintService.createWallPaint("white"),
                        1,
                        radius
                );
            }

            objects.add(town);
        }

        ComplexObject townsObject = new ComplexObject(
                EngineConstants.SCREEN_WIDTH / 2,
                EngineConstants.SCREEN_HEIGHT / 2,
                0,
                PaintService.createEdgePaint("red"),
                PaintService.createWallPaint("white"),
                1,
                objects
        );

        ArrayList<Object3D> allFigures = new ArrayList();
        allFigures.add(mapObject);
        allFigures.add(townsObject);
        FigureFactory.getInstance().setFigures(allFigures);
    }

    private static String getStringFromInputStream(InputStream stream) {
        try {
            int n = 0;
            char[] buffer = new char[1024 * 4];
            InputStreamReader reader = new InputStreamReader(stream, "UTF8");
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
            return writer.toString();
        } catch (UnsupportedEncodingException ex) {
            Log.d(ex.getMessage(), ex.getMessage());
        } catch (IOException ex) {
            Log.d(ex.getMessage(), ex.getMessage());
        }

        return "";
    }

    @Override
    public void onClick(View view) {
        String fromCityText = fromCity.getText().toString().toLowerCase();
        String toCityText = toCity.getText().toString().toLowerCase();
        int fromIndex = -1;
        int toIndex = -1;
        for(int i = 0; i < towns.length; i++) {
            String townsToLower = towns[i].city.toLowerCase();
            if(townsToLower.contains(fromCityText)) {
                fromIndex = i;
            }

            if(townsToLower.contains(toCityText)) {
                toIndex = i;
            }
        }

        createMapObject(fromIndex, toIndex);
        gamePanel.updateFigures();
    }
}
