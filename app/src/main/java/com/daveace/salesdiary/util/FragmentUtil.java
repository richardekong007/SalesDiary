package com.daveace.salesdiary.util;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.daveace.salesdiary.R;

public class FragmentUtil {

    public static void replaceFragment(FragmentManager fManager, Fragment fragment, Bundle args, boolean stackOnBackStack) {

        FragmentTransaction fTransaction;
        if (fragmentManagerIsNotNull(fManager)) {

            fTransaction = fManager.beginTransaction();
            if (fragmentIsNotNull(fragment) && argumentsAreNotNull(args)) {
                fragment.setArguments(args);
            }
            fTransaction.replace(R.id.content_layout, fragment, fragment.getClass().getSimpleName());
            new Handler().post(() -> {
                if (stackOnBackStack) {
                    fTransaction.addToBackStack(null);
                } else {
                    fManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                fTransaction.commitAllowingStateLoss();
            });
        }
    }

    public static void takeOffBackStack(FragmentManager fManager, Fragment fragment) {

        FragmentTransaction fTransaction;
        if (fragmentManagerIsNotNull(fManager) && fragmentIsNotNull(fragment)) {

            fTransaction = fManager.beginTransaction();
            fTransaction.remove(fragment);
            fTransaction.commit();
            fManager.popBackStack();
        }
    }

    public static void retainFragmentInstance(FragmentManager fManager, Fragment fragment){
        if (fragmentIsNotNull(fragment) && fManager.getFragments().contains(fragment)){
            fragment.setRetainInstance(true);
        }
    }


    private static boolean fragmentManagerIsNotNull(FragmentManager fManager) {
        return fManager != null;
    }

    private static boolean argumentsAreNotNull(Bundle arguments) {
        return arguments != null;
    }

    private static boolean fragmentIsNotNull(Fragment fragment) {
        return fragment != null;
    }
}
