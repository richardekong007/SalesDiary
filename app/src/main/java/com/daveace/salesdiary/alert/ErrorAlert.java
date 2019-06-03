package com.daveace.salesdiary.alert;

import android.view.View;
import android.widget.TextView;

import com.daveace.salesdiary.R;
import com.google.android.material.snackbar.Snackbar;

import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;

public class ErrorAlert extends BaseAlert {

    private ErrorAlert(){
        super();
    }

    public static ErrorAlert.Builder Builder(){
        return new ErrorAlert.Builder();
    }

    public static class Builder extends BaseAlert.Builder{
        @Override
        public Snackbar build() {

            if (ctx == null)
                throw new IllegalStateException("Context must not be null");
            if (rootView == null)
                throw new IllegalStateException("Root view must not be null");
            if (message == null || message.length() < 1)
                throw new IllegalStateException("Provide valid message");

            setBackgroundColor(ctx.getResources().getColor(R.color.white, null));
            setForegroundColor(ctx.getResources().getColor(R.color.colorPrimary, null));
            setIcon(ctx.getResources().getDrawable(R.drawable.ic_error_red, null));
            Snackbar snackbar = Snackbar
                    .make(rootView, message, LENGTH_LONG)
                    .setAction(actionCommand, action)
                    .setActionTextColor(foregroundColor);

            View view = snackbar.getView();
            view.setBackgroundColor(backgroundColor);
            TextView messageView = view
                    .findViewById(com.google.android.material.R.id.snackbar_text);
            messageView.setTextColor(foregroundColor);
            placeIcon(messageView);

            return snackbar;
        }
    }
}
