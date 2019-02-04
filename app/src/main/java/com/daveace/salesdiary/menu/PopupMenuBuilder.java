package com.daveace.salesdiary.menu;

import android.content.Context;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.appcompat.widget.PopupMenu;

public class PopupMenuBuilder {

    private Context context;

    private View view;

    private int resourceMenu;

    private PopupMenuBuilder(Context context, View view, int menuRes) {
        this.context = context;
        this.view = view;
        this.resourceMenu = menuRes;
    }

    public static PopupMenuBuilder from(Context context, View view, int menuRes) {
        return new PopupMenuBuilder(context, view, menuRes);
    }

    public PopupMenu build() {

        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(resourceMenu);
        forcefullySetMenuIcons(popupMenu);
        return popupMenu;
    }

    private static void forcefullySetMenuIcons(PopupMenu menu) {
        try {

            Field[] fields = menu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(menu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}