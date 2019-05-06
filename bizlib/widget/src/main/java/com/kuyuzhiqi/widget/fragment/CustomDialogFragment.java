package cn.smartinspection.widget.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import cn.smartinspection.widget.R;

/**
 * Created by kuyuzhiqi on 2017/5/5.
 * 普通样式的对话框
 */

public class CustomDialogFragment extends DialogFragment {

    private Context context;
    private OnDialogButtonListener onDialogButtonListener;
    private boolean mCancelable = true;

    @SuppressLint("ValidFragment")
    private CustomDialogFragment() {
    }

    public static CustomDialogFragment create(String title, String msg, String posBtnText,
            String negBtnText) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        bundle.putString("posBtnText", posBtnText);
        bundle.putString("negBtnText", negBtnText);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CustomDialogFragment create(Context context, String title, String msg) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        bundle.putString("posBtnText", context.getString(R.string.ok));
        bundle.putString("negBtnText", context.getString(R.string.cancel));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String title = bundle.getString("title");
            if (title != null) {
                builder.setTitle(title);
            }
            String msg = bundle.getString("msg");
            if (msg != null) {
                builder.setMessage(msg);
            }
            String posBtnText = bundle.getString("posBtnText");
            if (posBtnText != null) {
                builder.setPositiveButton(posBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onDialogButtonListener != null) {
                            onDialogButtonListener.onPositiveButtonClick();
                        }
                    }
                });
            }

            String negBtnText = bundle.getString("negBtnText");
            if (negBtnText != null) {
                builder.setNegativeButton(negBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onDialogButtonListener != null) {
                            onDialogButtonListener.onNegativeButtonClick();
                        }
                    }
                });
            }
        }
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(mCancelable);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onDialogButtonListener != null) onDialogButtonListener.onCancelListener();
    }

    public void setCancelable(boolean mCancelable) {
        this.mCancelable = mCancelable;
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
