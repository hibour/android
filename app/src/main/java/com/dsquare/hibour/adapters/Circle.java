package com.dsquare.hibour.adapters;

/**
 * Created by deepthi on 2/19/16.
 */
public class Circle
{
    public Vector2 mCenter;
    public float mRadius;

    /// <summary>
    ///
    /// </summary>
    /// <param name="iCenter"></param>
    /// <param name="Radius"></param>
    public Circle(Vector2 iCenter, float Radius)
    {
        mCenter = iCenter;
        mRadius = Radius;
    }
    /// <summary>
    ///
    /// </summary>
    /// <returns></returns>
//    public override string ToString()
//{
//    return "Rad: " + mRadius + " _ Center: " + mCenter.ToString();
//}
}