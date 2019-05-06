package cn.smartinspection.widget.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import com.squareup.timessquare.CalendarPickerView;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("ValidFragment")
public class SelectDateDialogFragment extends DialogFragment {
    public final static String TAG = SelectDateDialogFragment.class.getSimpleName();

    private Dialog mDialog;
    private OnSelectDateListener mOnSelectDateListener;
    private TextView mTvShowWeek;
    private TextView mTvShowYear;
    private TextView mTvShowMonth;
    private TextView mTvShowDay;
    /** 已经选择的日期Date **/
    private long mCurrentChosenTimeMillis = 0L;

    public SelectDateDialogFragment(long nowTime, OnSelectDateListener listener) {
        this.mCurrentChosenTimeMillis = nowTime;
        this.mOnSelectDateListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_select_date_dialog, null);
        CalendarPickerView calendarPickerView = (CalendarPickerView) view.findViewById(R.id.calendar_view);
        mTvShowWeek = (TextView) view.findViewById(R.id.tv_show_week);
        mTvShowYear = (TextView) view.findViewById(R.id.tv_show_year);
        mTvShowMonth = (TextView) view.findViewById(R.id.tv_show_month);
        mTvShowDay = (TextView) view.findViewById(R.id.tv_show_day);
        //设置最大时间(一年)
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.MILLISECOND, 0);
        tomorrow.add(Calendar.DATE, 1);
        final Calendar oneYear = Calendar.getInstance();
        oneYear.setTime(tomorrow.getTime());
        oneYear.add(Calendar.YEAR, 1);
        //如果有已选则显示已选
        Calendar calendar = Calendar.getInstance();
        if (mCurrentChosenTimeMillis != 0L) {
            if (mCurrentChosenTimeMillis < TimeUtils.getCurrentTimeInLong()) {
                calendarPickerView.init(new Date(mCurrentChosenTimeMillis), oneYear.getTime())
                        .inMode(CalendarPickerView.SelectionMode.SINGLE)
                        .withSelectedDate(new Date(mCurrentChosenTimeMillis));
                calendar.setTimeInMillis(mCurrentChosenTimeMillis);
            } else {
                calendarPickerView.init(Calendar.getInstance().getTime(), oneYear.getTime())
                        .inMode(CalendarPickerView.SelectionMode.SINGLE)
                        .withSelectedDate(new Date(mCurrentChosenTimeMillis));
                calendar.setTimeInMillis(mCurrentChosenTimeMillis);
            }
        } else {
            calendarPickerView.init(Calendar.getInstance().getTime(), oneYear.getTime())
                    .inMode(CalendarPickerView.SelectionMode.SINGLE);
            calendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        }
        updateShowTime(calendar);
        //选中时间
        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                mCurrentChosenTimeMillis = date.getTime();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                updateShowTime(calendar);

                //选中后关闭对话框
                mOnSelectDateListener.onSelectDateListener(mCurrentChosenTimeMillis);
                mDialog.dismiss();
            }

            @Override
            public void onDateUnselected(Date date) {
            }
        });

        //构建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(null);
        builder.setView(view);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(R.string.clear_corrective_time, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mOnSelectDateListener.onSelectDateListener(0);
                dialog.dismiss();
            }
        });

        mDialog = builder.create();
        return mDialog;
    }

    private void updateShowTime(Calendar calendar) {
        mTvShowWeek.setText(this.getWeekDayName(calendar));
        mTvShowYear.setText(calendar.get(Calendar.YEAR) + "");
        mTvShowMonth.setText(getActivity().getString(R.string.month2, String.valueOf(calendar.get(Calendar.MONTH) + 1)));
        mTvShowDay.setText(calendar.get(Calendar.DATE) + "");
    }

    public interface OnSelectDateListener {
        void onSelectDateListener(long time);
    }

    /**
     * 获取星期名
     */
    public String getWeekDayName(Calendar calendar) {
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        String weekName = "";
        switch (weekDay) {
            case Calendar.SUNDAY:
                weekName = getActivity().getString(R.string.sunday);
                break;
            case Calendar.MONDAY:
                weekName = getActivity().getString(R.string.monday);
                break;
            case Calendar.TUESDAY:
                weekName = getActivity().getString(R.string.tuesday);
                break;
            case Calendar.WEDNESDAY:
                weekName = getActivity().getString(R.string.wednesday);
                break;
            case Calendar.THURSDAY:
                weekName = getActivity().getString(R.string.thursday);
                break;
            case Calendar.FRIDAY:
                weekName = getActivity().getString(R.string.friday);
                break;
            case Calendar.SATURDAY:
                weekName = getActivity().getString(R.string.saturday);
                break;
        }
        return weekName;
    }
}

