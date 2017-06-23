package com.hejia.myview;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.hejia.tp_launcher_v3.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimePickDialogUtil implements OnDateChangedListener, OnTimeChangedListener {
    private DatePicker datePicker;
    private TimePicker timePicker;
    // private AlertDialog ad;
    private Dialog dialog;
    private TextView tv_title;
    private String dateTime;
    private String initDateTime;
    private Activity activity;
    private Button btn_yes;
    private Button btn_no;

    public DateTimePickDialogUtil(Activity activity, String initDateTime) {
        this.activity = activity;
        this.initDateTime = initDateTime;

    }

    public void init(DatePicker datePicker, TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日 " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        }

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                this);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }


    //弹出日期时间选择框方法
// inputDate 为需要设置的日期时间文本编辑框


    // TODO new
    public Dialog dateTimePicKDialog() {
        RelativeLayout dateTimeLayout = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.pop_timeanddate,
                null);
        datePicker = (DatePicker) dateTimeLayout.findViewById(R.id.datepicker);
        timePicker = (TimePicker) dateTimeLayout.findViewById(R.id.timepicker);
        tv_title = (TextView) dateTimeLayout.findViewById(R.id.tv_pop_date_title);
        btn_yes = (Button) dateTimeLayout.findViewById(R.id.btn_pop_date_yes);
        btn_no = (Button) dateTimeLayout.findViewById(R.id.btn_pop_date_no);
        init(datePicker, timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        init(datePicker, timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);

        // ad = new
        // AlertDialog.Builder(activity).setTitle(initDateTime).setView(dateTimeLayout)
        // .setPositiveButton("设置", new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int whichButton) {
        // // TODO 更新时间
        // }
        // }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
        // public void onClick(DialogInterface dialog, int whichButton) {
        // // TODO 取消更新
        // }
        // }).show();
        dialog = new Dialog(activity, R.style.MyDialog);
        dialog.setContentView(dateTimeLayout);
        dialog.show();
        btn_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 添加确定按键点击事件
                dialog.dismiss();
            }
        });
        btn_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 添加取消按键点击事件
                dialog.dismiss();
            }
        });
        onDateChanged(null, 0, 0, 0);
        return dialog;
    }

    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateChanged(null, 0, 0, 0);
    }

    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();

        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

        dateTime = sdf.format(calendar.getTime());
        tv_title.setText(dateTime);
    }

    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     *
     * @param initDateTime 初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();

        // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
        String date = spliteString(initDateTime, "日", "index", "front"); // 日期
        String time = spliteString(initDateTime, "日", "index", "back"); // 时间

        String yearStr = spliteString(date, "年", "index", "front"); // 年份
        String monthAndDay = spliteString(date, "年", "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, "月", "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, "月", "index", "back"); // 日

        String hourStr = spliteString(time, ":", "index", "front"); // 时
        String minuteStr = spliteString(time, ":", "index", "back"); // 分

        // int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        // int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
        // int currentDay = Integer.valueOf(dayStr.trim()).intValue();
        // int currentHour = Integer.valueOf(hourStr.trim()).intValue();
        // int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();
        int currentYear = Integer.parseInt(yearStr.trim());
        int currentMonth = Integer.parseInt(monthStr.trim());
        int currentDay = Integer.parseInt(dayStr.trim());
        int currentHour = Integer.parseInt(hourStr.trim());
        int currentMinute = Integer.parseInt(minuteStr.trim());

        calendar.set(currentYear, currentMonth, currentDay, currentHour, currentMinute);
        return calendar;
    }

    public static String spliteString(String srcStr, String pattern, String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if (indexOrLast.equalsIgnoreCase("index")) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if (frontOrBack.equalsIgnoreCase("front")) {
            if (loc != -1)
                result = srcStr.substring(0, loc); // 截取子串
        } else {
            if (loc != -1)
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
        }
        return result;
    }

}
