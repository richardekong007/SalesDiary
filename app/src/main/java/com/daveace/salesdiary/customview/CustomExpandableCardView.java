package com.daveace.salesdiary.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;


public class CustomExpandableCardView extends ExpandableCardView {

    private static final int TITLE_RESOURCE_ID = com.alespero.expandablecardview.R.id.title;
    private static final int TITLE_RESOURCE_COLOR = com.daveace.salesdiary.R.color.colorPrimary;

    public CustomExpandableCardView(Context context) {
        super(context);
    }

    public CustomExpandableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExpandableCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        TextView titleTextView = findViewById(TITLE_RESOURCE_ID);
        titleTextView.setTextColor(getContext().getColor(TITLE_RESOURCE_COLOR));
    }

    public void setTitleTextColor(int color){
        TextView titleTextView = findViewById(TITLE_RESOURCE_ID);
        titleTextView.setTextColor(getContext().getColor(color));
    }
}
