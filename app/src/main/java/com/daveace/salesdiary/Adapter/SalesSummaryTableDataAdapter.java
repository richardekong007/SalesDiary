package com.daveace.salesdiary.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.model.SalesSummaryFigureDatum;

import java.util.ArrayList;

import de.codecrafters.tableview.TableDataAdapter;

public class SalesSummaryTableDataAdapter extends TableDataAdapter<SalesSummaryFigureDatum> {

    private final int TEXT_SIZE = 10;
    private OnRowClickListener onRowClickListener;

    public SalesSummaryTableDataAdapter(Context ctx, ArrayList<SalesSummaryFigureDatum> data) {
        super(ctx, data);
    }


    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        SalesSummaryFigureDatum tableData = getRowData(rowIndex);
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderString(String.valueOf(tableData.getId()));
                break;
            case 1:
                renderedView = renderString(tableData.getProduct());
                break;
            case 2:
                renderedView = renderProfit(tableData.getProfit());
                break;
            case 3:
                renderedView = renderString(String.valueOf(tableData.getSalesPrice()));
                break;
            case 4:
                renderedView = renderString(String.valueOf(tableData.getCostPrice()));
        }
        interceptRowTouch(parentView, tableData);

        return renderedView;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void interceptRowTouch(ViewGroup parentView, SalesSummaryFigureDatum tableData) {
        parentView.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.setBackground(new ColorDrawable(getContext()
                            .getResources().getColor(R.color.blue, null)));
                    interceptRowClick(parentView, tableData);
                    break;
                case MotionEvent.ACTION_UP:
                    view.setBackground(new ColorDrawable(getContext()
                            .getResources().getColor(R.color.black, null)));
                    break;
            }
            return false;
        });
    }

    private void interceptRowClick(ViewGroup parentView, SalesSummaryFigureDatum datum) {
        parentView.setOnClickListener(view -> {
            if (onRowClickListener != null) onRowClickListener.rowClick(datum);
        });
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(getContext().getResources().getColor(R.color.white, null));
        return textView;
    }

    private View renderProfit(double value) {

        final TextView textView = new TextView(getContext());
        if (value <= 0.0) {
            textView.setTextColor(getContext().getResources().getColor(R.color.red, null));
        } else if (value >= 1.0 && value <= 9999.0) {
            textView.setTextColor(getContext().getResources().getColor(R.color.orange, null));
        } else if (value >= 10000.0 && value <= 30000.0) {
            textView.setTextColor(getContext().getResources().getColor(R.color.blue, null));
        } else {
            textView.setTextColor(getContext().getResources().getColor(R.color.deep_green, null));
        }
        textView.setTextSize(TEXT_SIZE);
        textView.setText(String.valueOf(value));
        return textView;
    }

    public void setOnRowClickListener(OnRowClickListener listener) {
        if (listener != null)
            this.onRowClickListener = listener;
    }

    public interface OnRowClickListener {
        void rowClick(SalesSummaryFigureDatum datum);
    }

}
