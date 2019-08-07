package com.daveace.salesdiary.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.FrameLayout;

import com.daveace.salesdiary.Adapter.SalesSummaryTableDataAdapter;
import com.daveace.salesdiary.R;
import com.daveace.salesdiary.model.SalesFigureTableData;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class TableUtil {

    public static void prepare(Context ctx, TableView<SalesFigureTableData> tableView,
                               String[] header, ArrayList<SalesFigureTableData> data) {

        setHeader(ctx, tableView, header);
        setData(ctx, tableView, data);
        setColumnCount(tableView, header.length);
        styleTable(ctx, tableView);
        tableView.setLayoutParams(new FrameLayout
                .LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT,
                100 * (data.size() + 1)));

    }

    private static void styleTable(Context ctx, TableView<SalesFigureTableData> tableView) {
        tableView.setHeaderBackgroundColor(ctx.getResources()
                .getColor(R.color.colorPrimary, null));
        tableView.setDataRowBackgroundProvider((rowIndex, rowData)
                -> new ColorDrawable(ctx.getResources()
                .getColor(R.color.black, null)));
    }

    private static void setColumnCount(TableView tableView, int counts) {
        tableView.setColumnCount(counts);
    }

    private static void setHeader(Context ctx, TableView<SalesFigureTableData> tableView, String... header) {
        SimpleTableHeaderAdapter adapter = new SimpleTableHeaderAdapter(ctx, header);
        adapter.setTextColor(ctx.getResources()
                .getColor(R.color.white, null));
        adapter.setTextSize(12);
        tableView.setHeaderAdapter(adapter);
    }

    private static void setData(Context ctx, TableView<SalesFigureTableData> tableView,
                                ArrayList<SalesFigureTableData> data) {
        tableView.setDataAdapter(new SalesSummaryTableDataAdapter(ctx, data));
    }

}
