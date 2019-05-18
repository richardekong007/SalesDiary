package com.daveace.salesdiary.interfaces;


import android.os.Build;

public interface Constant {

    String BASE_URL = "https://api.myjson.com/";

    String PHOTO_URI = "PHOTO_URL";

    String STATEMENT = "STATEMENT";

    String BIRTH_PLACE = "BIRTH_PLACE";

    int REQUEST_TIMEOUT = 60;

    String ACCEPT = "Accept";

    String JSON_APPLICATION = "application/json";

    String CONTENT_TYPE = "content_type";

    String AUTHOR = "AUTHOR";

    String LOCATION = "LOCATION";

    int ZOOM_LEVEL = 15;

    int BEARING = 0;

    int TILT_ANGLE = 45;

    int BUILD_VERSION = Build.VERSION.SDK_INT;

    int MASHMALLOW = Build.VERSION_CODES.M;

    int KITKAT = Build.VERSION_CODES.KITKAT;

    int HONEYCOMB = Build.VERSION_CODES.HONEYCOMB;

    String IMAGE_CONTENT_TYPE = "image/*";

    CharSequence SELECT_IMAGE_INSTRUCTION = "Select Image";

    int IMAGE_PICK_CODE = 100;

    int REQUEST_IMAGE_CAPTURE = 101;

    int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 102;

    int SAVE_SIGNATURE_PERMISSION_REQUEST_CODE = 103;


    int REQUEST_PERMISSION_LOCATION = 104;

    String DATE_FORMAT = "yyyyMMdd_HHmmss";

    String IMAGE_FILE_TYPE = ".jpg";

    String ONLINE_QUOTE = "ONLINE_QUOTE";

    String LOCAL_LOCATION = "LOCAL_LOCATION";

    String LOCAL_QUOTE = "LOCAL_QUOTE";

    String ONLINE_LOCATION = "ONLINE_LOCATION";

    String QUOTE_TAG = "QUOTE_TAG";

    String ADDRESS = "ADDRESS";

    String LATTITUDE = "LATITUDE";

    String LONGITUDE = "LONGITUDE";

    String USERNAME = "USERNAME";

    String TABLE_USER_CREDENTIALS = "USER_CREDENTIALS";

    String COLUMN_USERNAME = "USERNAME";

    String COLUMN_PHOTO_URI = "PHOTO_URI";

    String COLUMN_PASSWORD = "PASSWORD";

    long LOAD_TIME = 3000;

    long MIN_LOCATION_UPDATE_TIME = 5000;

    String ADDRESS_FORMAT = "%s, %s, %s";

    String USERS = "Users";

    String PRODUCTS = "Products";

    String CUSTOMERS = "Customers";

    String SALESEVENTS = "SalesEvents";

    String DAILY_SALES_REPORT = "Daily Sales Report";

    String WEEKLY_SALES_REPORT = "Weekly Sales Report";

    String MONTHLY_SALES_REPORT = "Monthly Sales Report";

    String QUARTERLY_SALES_REPORT = "Quarterly Sales Report";

    String SEMESTER_SALES_REPORT = "Semester Sales Report";

    String YEARLY_SALES_REPORT = "Yearly Sales Report";

    String GENERAL_SALES_REPORT = "General Sales Report";

    String SALES_EVENTS_REPORT = "SALES_EVENTS_REPORT";

    String EVENT_RELATED_PRODUCT = "EVENT_RELATED_PRODUCT";

    String EVENTS_RELATED_CUSTOMER = "EVENT_RELATED_CUSTOMER";

    String SALES_EVENTS_REPORTS = "SALES_EVENTS_REPORTS";

    String EVENT_RELATED_PRODUCTS = "EVENT_RELATED_PRODUCTS";

    String EVENTS_RELATED_CUSTOMERS = "EVENT_RELATED_CUSTOMERS";

    String REPORT_TYPE = "REPORT_TYPE";

    String SALES_EVENT_DATE_FORMAT = "MMM d, y";

    String SALES_EVENT_INTERPRETATION = "SALES_EVENT_INTERPRETATION";

}