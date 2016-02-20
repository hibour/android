package com.dsquare.hibour.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by deepthi on 2/19/16.
 */
public class CirclePacker
{
    public List<Circle> mCircles = new ArrayList<Circle>();
    public Circle mDraggingCircle = null;
    protected static Vector2 mPackingCenter;
    public float mMinSeparation = 1f;


    public CirclePacker(Vector2 pPackingCenter, int pNumCircles,
                        double pMinRadius, double pMaxRadius)
    {
        this.mPackingCenter = pPackingCenter;

        // Create random circles
        this.mCircles.clear();
        Random Rnd = new Random((new Date()).getTime());
        for (int i = 0; i < pNumCircles; i++)
        {
            Vector2 nCenter = new Vector2((float)(this.mPackingCenter.x +
                    Rnd.nextDouble() * pMinRadius),
                    (float)(this.mPackingCenter.y +
                            Rnd.nextDouble() * pMinRadius));
            float nRadius = (float)(pMinRadius + Rnd.nextDouble() *
                    (pMaxRadius - pMinRadius));
            this.mCircles.add(new Circle(nCenter, nRadius));
        }
    }

    private static double DistanceToCenterSq(Circle pCircle)
    {
        return Vector2.distance(pCircle.mCenter, mPackingCenter);
    }

    public static Comparator<Circle> comparer = new Comparator<Circle>() {

        public int compare(Circle p1, Circle p2) {
            double d1 = DistanceToCenterSq(p1);
            double d2 = DistanceToCenterSq(p2);
            if (d1 < d2)
                return 1;
            else if (d1 > d2)
                return -1;
            else return 0;
        }
    };

    public void OnFrameMove(long iterationCounter)
    {
        // Sort circles based on the distance to center
        Collections.sort(mCircles, comparer);

        float minSeparationSq = mMinSeparation * mMinSeparation;
        for (int i = 0; i < mCircles.size() - 1; i++)
        {
            for (int j = i + 1; j < mCircles.size(); j++)
            {
                if (i == j)
                    continue;

                Vector2 AB = Vector2.distanceInVector(mCircles.get(j).mCenter, mCircles.get(i).mCenter);
                double r = mCircles.get(i).mRadius + mCircles.get(j).mRadius;

                // Length squared = (dx * dx) + (dy * dy);
                double d = Vector2.lengthSquared(AB) - minSeparationSq;
                double minSepSq = Math.min(d, minSeparationSq);
                d -= minSepSq;

                if (d < (r * r) - 0.01 )
                {
                    AB = Vector2.normalize(AB);

                    AB = Vector2.multiplyWithConstant(AB, (float)((r - Math.sqrt(d)) * 0.5f));

                    if (mCircles.get(j) != mDraggingCircle)
                        mCircles.get(i).mCenter = Vector2.add(mCircles.get(i).mCenter, AB);
                    if (mCircles.get(j) != mDraggingCircle)
                        mCircles.get(j).mCenter = Vector2.distanceInVector(mCircles.get(j).mCenter, AB);
                }

            }
        }


        float damping = 0.1f / (float)(iterationCounter);
        for (int i = 0; i < mCircles.size(); i++)
        {
            if (mCircles.get(i) != mDraggingCircle)
            {
                Vector2 v = Vector2.distanceInVector(mCircles.get(i).mCenter, this.mPackingCenter);
                v = Vector2.multiplyWithConstant(v, damping);
                mCircles.get(i).mCenter = Vector2.distanceInVector(mCircles.get(i).mCenter, v);
            }
        }
    }
}