package com.daveace.salesdiary.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.daveace.salesdiary.R;

import java.util.List;

public class StringUtil {

    public static <T extends TextView> boolean fieldsAreValid(Context ctx, List<T> editTexts) {
        boolean valid = true;
        for (T editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setError(ctx.getString(R.string.required));
                valid = false;
            } else {
                editText.setError(null);
                String trimmed = editText.getText().toString().trim();
                editText.setText(trimmed);
            }
        }
        return valid;
    }

    @SafeVarargs
    public static <T extends TextView> boolean fieldsAreValid(Context ctx, T... editTexts) {
        boolean valid = true;
        for (T editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText())) {
                editText.setError(ctx.getString(R.string.required));
                valid = false;
            } else {
                editText.setError(null);
                String trimmed = editText.getText().toString().trim();
                editText.setText(trimmed);

            }
        }
        return valid;
    }

    public static boolean isEmpty(String... strings) {
        boolean empty = false;
        for (String string : strings) {
            if (TextUtils.isEmpty(string))
                empty = true;
        }
        return empty;
    }

    @SafeVarargs
    public static <T extends TextView> void clear(T ... editTexts) {
        for (T editText : editTexts) {
            editText.setText("");
        }
    }

    public static void clear(String... texts) {
        for (String text : texts) {
            text = "";
        }
    }

}
