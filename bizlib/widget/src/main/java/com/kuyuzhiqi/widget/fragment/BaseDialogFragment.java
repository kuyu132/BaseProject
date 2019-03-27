package com.kuyuzhiqi.widget.fragment;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class BaseDialogFragment extends DialogFragment {

    protected OnDialogButtonListener onDialogButtonListener;

    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        try {
            ft.remove(this).commit();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setOnDialogButtonListener(OnDialogButtonListener onDialogButtonListener) {
        this.onDialogButtonListener = onDialogButtonListener;
    }

    public interface OnDialogButtonListener {
        void onPositiveButtonClick();

        void onNegativeButtonClick();

        void onCancelListener();
    }
}
