package com.dsquare.hibour.utils;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Aditya Ravikanti on 2/29/2016.
 */
public class MySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    int spanPos, spanCnt1, spanCnt2;

    public MySpanSizeLookup(int spanPos, int spanCnt1, int spanCnt2) {
        super();
        this.spanPos = spanPos;
        this.spanCnt1 = spanCnt1;
        this.spanCnt2 = spanCnt2;
    }

    @Override
    public int getSpanSize(int position) {
        return (position % spanPos ==0 ? spanCnt2 : spanCnt1);
    }
}