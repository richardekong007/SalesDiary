package com.daveace.salesdiary.util;

import android.util.Log;

import com.daveace.salesdiary.entity.SalesEvent;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportUtil {

    public static List<SalesEvent> getDailySalesEventReports(List<SalesEvent> salesEvents) {

        List<SalesEvent> dailySalesEventReports = new ArrayList<>();
        try {
            for (SalesEvent event : salesEvents) {
                boolean dateInInterval = dateIsBetween(convertJavaDateToLocalDateTime(event.getDate()),
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now());
                if (dateInInterval) {
                    dailySalesEventReports.add(event);
                }
            }
        } catch (NullPointerException e) {
            Log.e("DailySalesEvent Error", e.getMessage());
        }
        return dailySalesEventReports;
    }

    public static List<SalesEvent> getWeeklySalesEventReports(List<SalesEvent> salesEvents) {
        List<SalesEvent> weeklySalesEventReports = new ArrayList<>();
        try {
            for (SalesEvent event : salesEvents) {
                boolean dateInInterval = dateIsBetween(
                        convertJavaDateToLocalDateTime(event.getDate()),
                        LocalDateTime.now().minusDays(7),
                        LocalDateTime.now());
                if (dateInInterval) {
                    weeklySalesEventReports.add(event);
                }
            }
        } catch (NullPointerException e) {
            Log.e("WeeklySalesEvent Error", e.getMessage());
        }
        return weeklySalesEventReports;
    }

    public static List<SalesEvent> getMonthlySalesEventReports(List<SalesEvent> salesEvents) {
        List<SalesEvent> monthlySalesEventReports = new ArrayList<>();
        try {
            for (SalesEvent event : salesEvents) {
                boolean dateInInterval = dateIsBetween(
                        convertJavaDateToLocalDateTime(event.getDate()),
                        LocalDateTime.now().minusMonths(1),
                        LocalDateTime.now()
                );
                if (dateInInterval) {
                    monthlySalesEventReports.add(event);
                }
            }
        } catch (NullPointerException e) {
            Log.e("MonthlySalesEvent Error", e.getMessage());
        }
        return monthlySalesEventReports;
    }

    public static List<SalesEvent> getQuarterlySalesEventReports(List<SalesEvent> salesEvents) {
        List<SalesEvent> quarterlySalesEventReports = new ArrayList<>();
        try {
            for (SalesEvent event : salesEvents) {
                boolean dateInInterval = dateIsBetween(
                        convertJavaDateToLocalDateTime(event.getDate()),
                        LocalDateTime.now().minusMonths(3),
                        LocalDateTime.now()
                );
                if (dateInInterval) {
                    quarterlySalesEventReports.add(event);
                }
            }
        } catch (NullPointerException e) {
            Log.e("QuarterSalesEvent Error", e.getMessage());
        }
        return quarterlySalesEventReports;
    }

    public static List<SalesEvent> getSemesterSalesEventReports(List<SalesEvent> salesEvents) {
        List<SalesEvent> semesterSalesEventReports = new ArrayList<>();
        try {
            for (SalesEvent event : salesEvents) {
                boolean dateInInterval = dateIsBetween(
                        convertJavaDateToLocalDateTime(event.getDate()),
                        LocalDateTime.now().minusMonths(6),
                        LocalDateTime.now()
                );
                if (dateInInterval) {
                    semesterSalesEventReports.add(event);
                }
            }
        } catch (NullPointerException e) {
            Log.e("SemesterSales Error", e.getMessage());
        }
        return semesterSalesEventReports;
    }

    public static List<SalesEvent> getYearlySalesEventReports(List<SalesEvent> salesEvents) {
        List<SalesEvent> yearlySalesEventReports = new ArrayList<>();
        try {
            for (SalesEvent event : salesEvents) {
                boolean dateInInterval = dateIsBetween(
                        convertJavaDateToLocalDateTime(event.getDate()),
                        LocalDateTime.now().minusYears(1),
                        LocalDateTime.now()
                );
                if (dateInInterval) {
                    yearlySalesEventReports.add(event);
                }
            }
        } catch (NullPointerException e) {
            Log.e("YearlySalesEvent Error", e.getMessage());
        }
        return yearlySalesEventReports;
    }

    public static List<SalesEvent> getGeneralSalesReports(List<SalesEvent> salesEvents) {
        List<SalesEvent> generalSalesReport = new ArrayList<>();
        try {
            generalSalesReport = salesEvents;
        } catch (NullPointerException e) {
            Log.e("General Report Error", e.getMessage());
        }
        return generalSalesReport;
    }

    private static boolean dateIsBetween(LocalDateTime dateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return (dateTime.isAfter(startDateTime) || dateTime.isEqual(startDateTime))
                && (dateTime.isBefore(endDateTime) || dateTime.isEqual(endDateTime));
    }

    private static LocalDateTime convertJavaDateToLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
