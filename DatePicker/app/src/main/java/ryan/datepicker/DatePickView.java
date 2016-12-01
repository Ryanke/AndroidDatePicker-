package ryan.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;


import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;


/**
 * pickView
 * Created by kerui on 2016/11/27.
 */


public class DatePickView extends RelativeLayout implements NumberPicker.OnValueChangeListener {
    private NumberPicker idDatePickerYear;
    private NumberPicker idDatePickerMonth;
    private NumberPicker idDatePickerDay;
    private NumberPicker idDatePickerHour;
    private int year;
    private int month;
    private int day;
    private String time;
    private String[] stringsTimes = {"0:00", "0:30", "1:00", "1:30", "2:00", "2:30", "3:00", "3:30", "4:00", "4:30", "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:0", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "24:00"};

    public void setReturnTimeListener(ReturnTimeListener returnTimeListener) {
        this.returnTimeListener = returnTimeListener;
    }

    private ReturnTimeListener returnTimeListener;

    public DatePickView(Context context) {
        super(context);
    }

    public DatePickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initListener();
        initPicker();
    }

    private void initView() {
        View view = View.inflate(getContext(), R.layout.view_date_picker, this);
        idDatePickerYear = (NumberPicker) view.findViewById(R.id.id_date_picker_year);
        idDatePickerMonth = (NumberPicker) view.findViewById(R.id.id_date_picker_month);
        idDatePickerDay = (NumberPicker) view.findViewById(R.id.id_date_picker_day);
        idDatePickerHour = (NumberPicker) view.findViewById(R.id.id_date_picker_hour);

        setNumberPickerDividerColor(idDatePickerYear);
        setNumberPickerDividerColor(idDatePickerMonth);
        setNumberPickerDividerColor(idDatePickerDay);
        setNumberPickerDividerColor(idDatePickerHour);


        idDatePickerYear.setMinValue(2016);
        idDatePickerYear.setMaxValue(2030);

        idDatePickerMonth.setMinValue(1);
        idDatePickerMonth.setMaxValue(12);

        idDatePickerDay.setMinValue(1);
        idDatePickerDay.setMaxValue(31);


        idDatePickerHour.setDisplayedValues(stringsTimes);
        idDatePickerHour.setMinValue(0);
        idDatePickerHour.setMaxValue(stringsTimes.length - 1);
        idDatePickerHour.setValue(4);


        idDatePickerYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        idDatePickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        idDatePickerDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        idDatePickerHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


    }

    private void initPicker() {
        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);  //刻度值 实际： now.get(Calendar.YEAR)
        int nowMonth = now.get(Calendar.MONTH) + 1;       //刻度值 实际： now.get(Calendar.MONTH) + 1
        int nowDay = now.get(Calendar.DAY_OF_MONTH);  //刻度值 实际： now.get(Calendar.DAY_OF_MONTH
        int nowHour = now.get(Calendar.HOUR_OF_DAY) * 2; //刻度值 实际：now.get(Calendar.HOUR_OF_DAY)


        idDatePickerYear.setValue(nowYear);
        idDatePickerMonth.setValue(nowMonth);
        idDatePickerDay.setValue(nowDay);
        idDatePickerHour.setValue(nowHour);

        year = idDatePickerYear.getValue();
        month = idDatePickerMonth.getValue();
        monthDays(month);
        day = idDatePickerDay.getValue();
        time = stringsTimes[idDatePickerHour.getValue()];

        if (returnTimeListener != null) {
            returnTimeListener.returnTime(jointTime());
        }
    }


    private void setNumberPickerDividerColor(NumberPicker numberPicker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    //设置分割线的颜色值 透明
                    pf.set(numberPicker, new ColorDrawable(this.getResources().getColor(android.R.color.transparent)));
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void initListener() {
        idDatePickerYear.setOnValueChangedListener(this);
        idDatePickerMonth.setOnValueChangedListener(this);
        idDatePickerDay.setOnValueChangedListener(this);
        idDatePickerHour.setOnValueChangedListener(this);

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.id_date_picker_year:
                year = newVal;
                if (returnTimeListener != null) {
                    returnTimeListener.returnTime(jointTime());
                }

                break;
            case R.id.id_date_picker_month:
                month = newVal;
                monthDays(month);
                if (returnTimeListener != null) {
                    returnTimeListener.returnTime(jointTime());
                }
                break;
            case R.id.id_date_picker_day:
                day = newVal;
                if (returnTimeListener != null) {
                    returnTimeListener.returnTime(jointTime());
                }
                break;
            case R.id.id_date_picker_hour:
                time = stringsTimes[picker.getValue()];
                if (returnTimeListener != null) {
                    returnTimeListener.returnTime(jointTime());
                }
                break;
            default:
                break;


        }

    }

    /**
     * 拼接时间
     */
    private long jointTime() {
        String stringBuffer = String.valueOf(year) + "-" +
                month + "-" +
                day + " " +
                time;
        Date date = DateUtils.getDateByFormat(DateUtils.SimpleDateFormatForHouse, stringBuffer);
        assert date != null;
        return date.getTime();
    }

    public interface ReturnTimeListener {
        void returnTime(long time);
    }

    /**
     * 闰年
     *
     * @param year 年
     * @return 是否为平年
     */
    private boolean leapYear(int year) {

        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {

                    return true;// "\(year) is a leap year"
                } else {

                    return false;// "\(year) is not a leap year"
                }
            } else {

                return true;// "\(year) is a leap year"
            }

        }
        return false;
    }

    /**
     * 月份天数
     *
     * @param m 月份
     */
    private void monthDays(int m) {
        if (m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12) {
            idDatePickerDay.setMinValue(1);
            idDatePickerDay.setMaxValue(31);
        } else if (m == 4 || m == 6 || m == 9 || m == 11) {
            idDatePickerDay.setMinValue(1);
            idDatePickerDay.setMaxValue(30);
        } else if (m == 2) {
            if (leapYear(year)) {
                idDatePickerDay.setMinValue(1);
                idDatePickerDay.setMaxValue(29);
            } else if (!leapYear(year)) {
                idDatePickerDay.setMinValue(1);
                idDatePickerDay.setMaxValue(28);
            }
        }

    }


}
