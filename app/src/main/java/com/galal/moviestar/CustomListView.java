package com.galal.moviestar;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomListView extends ListView {


    android.view.ViewGroup.LayoutParams params;
    int counter = 0;

    public CustomListView(Context context, AttributeSet attr) {
        super(context, attr);
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        int height;
        int count = getCount();
        if (count != counter)
        {
            height = getChildAt(0).getHeight() + 1 ;
            counter = count;
            params = getLayoutParams();
            params.height = count * height;
            setLayoutParams(params);
        }
        super.onDraw(canvas);
    }
}
