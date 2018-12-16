package com.example.smith.project_crab;

import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewOutlineProvider;

public class MyOutlineProvider extends ViewOutlineProvider {
    private float radius;
    private Resources mResources;
    private boolean mZeroElevation;

    MyOutlineProvider(float rad, Resources resources ,boolean zeroElevation) {
        radius = rad;
        mResources = resources;
        mZeroElevation = zeroElevation;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Path path;
        if(mZeroElevation)
            view.setElevation(0);
        else
            view.setElevation(mResources.getDimensionPixelSize(R.dimen.elevation));
        path = getPath(view,mResources.getDimensionPixelSize(R.dimen.radius),true,true,true,true);
        outline.setConvexPath(path);

    }

    private Path getPath(View v,float radius, boolean topLeft, boolean topRight,
                         boolean bottomRight, boolean bottomLeft) {

        final Path path = new Path();
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
            radii[4] = 0;
            radii[5] = 0;
        }

        if (bottomLeft) {
            radii[6] = 0;
            radii[7] = 0;
        }

        path.addRoundRect(new RectF(0, 0, v.getWidth(), v.getHeight()),
                radii, Path.Direction.CW);
        return path;
    }
}
