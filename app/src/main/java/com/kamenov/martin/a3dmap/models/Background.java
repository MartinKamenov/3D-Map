package com.kamenov.martin.a3dmap.models;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kamenov.martin.a3dmap.engine.constants.EngineConstants;
import com.kamenov.martin.a3dmap.engine.models.game_objects.contracts.GameObject;

/**
 * Created by Martin on 10.3.2018 г..
 */

public class Background implements GameObject {
    private final Paint paint;

    public Background(int color)
    {
        this.paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(0, 0,
                EngineConstants.SCREEN_WIDTH,
                EngineConstants.SCREEN_HEIGHT, paint);
    }

    @Override
    public void update() {

    }
}
