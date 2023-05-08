package com.example.android_up_sdk.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;



public class RoundedCornerImageView extends androidx.appcompat.widget.AppCompatImageView {

    private float radius = 20.0f;
    private Path path;
    RectF rect;
    private boolean topLeft, topRight, bottomRight, bottomLeft;

    public RoundedCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
        topLeft = true;
        topRight = true;
        bottomRight = true;
        bottomLeft = true;

    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setRadius(float radius, boolean topLeft, boolean topRight,
                          boolean bottomRight, boolean bottomLeft) {
        this.radius = radius;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        rect = new RectF(0, 0, this.getWidth(), this.getHeight());

        //Handle each corner radius
        final float[] radii = new float[8];
        if (topLeft) {
            radii[0] = radius;
            radii[1] = radius;
        }

        if (topRight) {
            radii[2] = radius;
            radii[3] = radius;
        }

        if (bottomRight) {
            radii[4] = radius;
            radii[5] = radius;
        }

        if (bottomLeft) {
            radii[6] = radius;
            radii[7] = radius;
        }
        path.addRoundRect(rect, radii, Path.Direction.CW);
        canvas.clipPath(path);

        super.onDraw(canvas);
    }
}