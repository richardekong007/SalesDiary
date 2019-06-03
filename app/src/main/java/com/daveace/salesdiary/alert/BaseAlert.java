package com.daveace.salesdiary.alert;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class BaseAlert {

    private Context ctx;
    private View rootView;
    private String message;
    private Drawable icon;
    private View.OnClickListener action;

    public BaseAlert(){

    }

    private BaseAlert(final Builder builder) {

        this.ctx = builder.ctx;
        this.rootView = builder.rootView;
        this.message = builder.message;
        this.icon = builder.icon;
        this.action = builder.action;
    }

    public Context getContext() {
        return ctx;
    }

    public View getRootView() {
        return rootView;
    }

    public String getMessage() {
        return message;
    }

    public Drawable getIcon() {
        return icon;
    }

    public View.OnClickListener getAction() {
        return action;
    }

    public static abstract class Builder {

        protected Context ctx;
        protected View rootView;
        protected String message;
        protected Drawable icon;
        protected View.OnClickListener action;
        protected String actionCommand;
        protected int backgroundColor;
        protected int foregroundColor;

        public Builder setContext(Context ctx){
            this.ctx = ctx;
            return this;
        }

        protected void setBackgroundColor(int color){
            this.backgroundColor = color;
        }

        protected void setForegroundColor(int color){
            this.foregroundColor = color;
        }

        public BaseAlert.Builder setRootView(View rootView) {
            this.rootView = rootView;
            return this;
        }

        public BaseAlert.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public BaseAlert.Builder setIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public BaseAlert.Builder setAction(View.OnClickListener action) {
            this.action = action;
            return this;
        }

        public BaseAlert.Builder setActionCommand(String command){
            this.actionCommand = command;
            return this;
        }

        protected void placeIcon(TextView messageView) {
            messageView.setCompoundDrawablesWithIntrinsicBounds(
                    icon, null, null, null);
            messageView.setCompoundDrawablePadding(50);
        }

        public abstract Snackbar build();
    }
}
