package com.daveace.salesdiary.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daveace.salesdiary.R;
import com.daveace.salesdiary.model.SalesFigureTableData;

import java.util.ArrayList;

import de.codecrafters.tableview.TableDataAdapter;

public class SalesSummaryTableDataAdapter extends TableDataAdapter<SalesFigureTableData> {

    private final int TEXT_SIZE = 10;

    public SalesSummaryTableDataAdapter(Context ctx, ArrayList<SalesFigureTableData> data) {
        super(ctx, data);
    }


    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        SalesFigureTableData tableData = getRowData(rowIndex);
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
        interceptRowTouch(parentView);
        return renderedView;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void interceptRowTouch(ViewGroup parentView) {
        parentView.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.setBackground(new ColorDrawable(getContext()
                            .getResources().getColor(R.color.blue, null)));
                    break;
                case MotionEvent.ACTION_UP:
                    view.setBackground(new ColorDrawable(getContext()
                            .getResources().getColor(R.color.black, null)));
                    break;
            }
            return true;
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
}
