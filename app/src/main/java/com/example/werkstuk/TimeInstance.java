package com.example.werkstuk;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "timeInstance_table")
@TypeConverters(DateConverter.class)
public class TimeInstance {

    final private static long dayInMilliseconds = 86400000;
    @PrimaryKey(autoGenerate = true)
    private int id;

    private boolean on;
    private String name;
    private Date start;
    private Date end;
    private int interval;


    private boolean mo;
    private boolean tu;
    private boolean we;
    private boolean th;
    private boolean fr;
    private boolean sa;
    private boolean su;

    public enum EnumInterval {
        ONEMINUTE,
        TWOMINUTES,
        FIVEMINUTES,
        TENMINUTES,
        QUARTERHOUR,
        HALFHOUR,
        ONEHOUR,
        TWOHOURS,
        THREEHOURS,
        FOURHOURS,
        SIXHOURS,
        TWELVEHOURS;

        public static EnumInterval intToEnumInterval(int x) {
            switch (x) {
                case 0:
                    return ONEMINUTE;
                case 1:
                    return TWOMINUTES;
                case 2:
                    return FIVEMINUTES;
                case 3:
                    return TENMINUTES;
                case 4:
                    return QUARTERHOUR;
                case 5:
                    return HALFHOUR;
                case 6:
                    return ONEHOUR;
                case 7:
                    return TWOHOURS;
                case 8:
                    return THREEHOURS;
                case 9:
                    return FOURHOURS;
                case 10:
                    return SIXHOURS;
                case 11:
                    return TWELVEHOURS;
            }
            return null;
        }
    }

    private boolean isInWeekdays() {
        if (!mo) {
            return false;
        }
        if (!tu) {
            return false;
        }
        if (!we) {
            return false;
        }
        if (!th) {
            return false;
        }
        if (!fr) {
            return false;
        }
        return true;
    }

    private boolean weekends() {
        if (!sa) {
            return false;
        }
        if (!su) {
            return false;
        }
        return true;
    }

    private boolean everyDay() {
        if (!mo) {
            return false;
        }
        if (!tu) {
            return false;
        }
        if (!we) {
            return false;
        }
        if (!th) {
            return false;
        }
        if (!fr) {
            return false;
        }
        if (!sa) {
            return false;
        }
        if (!su) {
            return false;
        }
        return true;
    }

    private String getDayStr(boolean firstItem, String dayString, boolean day) {
        if (day) {
            if (firstItem) {
                firstItem = false;
                return dayString;
            } else {
                return ", " + dayString;
            }
        }
        return "";
    }

    private boolean isFirstTime(boolean firstTime, String str) {
        if (!firstTime) {
            return false;
        }
        return str.equals("");
    }
    //for test
    public String getDaysStrWithStr() {

        String str = "";
        boolean firstItem = true;

        if (everyDay()) {
            return "every day";
        }

        if (isInWeekdays()) {
            str += "weekdays";
            firstItem = false;
        } else {

            str += getDayStr(firstItem, "mo", mo);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, "tu", tu);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, "we", we);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, "th", th);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, "fr", fr);
            firstItem = isFirstTime(firstItem, str);
        }
        if (weekends()) {
            if (firstItem) {
                str += "weekends";
                firstItem = false;
            } else {
                str = "weekends" + ", " + str;
            }
        } else {
            str += getDayStr(firstItem, "sa", sa);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, "su", su);
            firstItem = isFirstTime(firstItem, str);
        }

        if (str.equals("")) {
            str = "never";
        }
        return str;
    }

    public long calMiniSecForNextAlarm(){
        Date currentDate = new Date(0);
        currentDate.setMinutes(new Date().getMinutes());
        currentDate.setHours(new Date().getHours());
        if((currentDate.getTime() + interval) >=  end.getTime()){
            return dayInMilliseconds  - currentDate.getTime() +  start.getTime();
        }
        return interval;
    }

    public String getDaysStr() {

        String str = "";
        boolean firstItem = true;

        if (everyDay()) {
            return App.getAppResources().getString(R.string.every_day);
        }

        if (isInWeekdays()) {
            str += App.getAppResources().getString(R.string.weekdays);
            firstItem = false;
        } else {

            str += getDayStr(firstItem, App.getAppResources().getString(R.string.mondayShort), mo);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, App.getAppResources().getString(R.string.tuesdayShort), tu);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, App.getAppResources().getString(R.string.wednesdayShort), we);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, App.getAppResources().getString(R.string.thursdayShort), th);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, App.getAppResources().getString(R.string.fridayShort), fr);
            firstItem = isFirstTime(firstItem, str);
        }
        if (weekends()) {
            if (firstItem) {
                str += App.getAppResources().getString(R.string.weekends);
                firstItem = false;
            } else {
                str = App.getAppResources().getString(R.string.weekends) + ", " + str;
            }
        } else {
            str += getDayStr(firstItem, App.getAppResources().getString(R.string.saturdayShort), sa);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, App.getAppResources().getString(R.string.sundayShort), su);
            firstItem = isFirstTime(firstItem, str);
        }

        if (str.equals("")) {
            str = App.getAppResources().getString(R.string.never);
        }
        return str;
    }

    public boolean[] getDaysArray() {
        return new boolean[]{mo, tu, we, th, fr, sa, su};
    }


    public String getTimeStr() {

        SimpleDateFormat ft;

        if (true) {
            ft = new SimpleDateFormat("hh:mm:ss a");
        } else {
            ft = new SimpleDateFormat("kk:mm:ss");
        }

        String str = ft.format(start);
        str += " - ";
        str += ft.format(end);
        return str;
    }

    public static String[] getIntervalStringArray() {
        return new String[]{
                "1 " + App.getAppResources().getString(R.string.minute),
                "2 " + App.getAppResources().getString(R.string.minutes),
                "5 " + App.getAppResources().getString(R.string.minutes),
                "10 " + App.getAppResources().getString(R.string.minutes),
                "15 " + App.getAppResources().getString(R.string.minutes),
                "30 " + App.getAppResources().getString(R.string.minutes),
                "1 " + App.getAppResources().getString(R.string.hour),
                "2 " + App.getAppResources().getString(R.string.hours),
                "3 " + App.getAppResources().getString(R.string.hours),
                "4 " + App.getAppResources().getString(R.string.hours),
                "6 " + App.getAppResources().getString(R.string.hours),
                "12 " + App.getAppResources().getString(R.string.hours)
        };
    }


    public void setIntervalByIntevalNumber(EnumInterval enumInterval) {
        switch (enumInterval) {
            case ONEMINUTE:
                this.interval = 60;
                break;
            case TWOMINUTES:
                this.interval = 60 * 2;
                break;
            case FIVEMINUTES:
                this.interval = 60 * 5;
                break;
            case TENMINUTES:
                this.interval = 60 * 10;
                break;
            case QUARTERHOUR:
                this.interval = 60 * 15;
                break;
            case HALFHOUR:
                this.interval = 60 * 30;
                break;
            case ONEHOUR:
                this.interval = 60 * 60;
                break;
            case TWOHOURS:
                this.interval = 60 * 60 * 2;
                break;
            case THREEHOURS:
                this.interval = 60 * 60 * 3;
                break;
            case FOURHOURS:
                this.interval = 60 * 60 * 4;
                break;
            case SIXHOURS:
                this.interval = 60 * 60 * 6;
                break;
            case TWELVEHOURS:
                this.interval = 60 * 60 * 12;
                break;
        }
    }

    public EnumInterval getEnumIntervalFromInterval() {
        switch (interval) {
            case 60:
                return EnumInterval.intToEnumInterval(0);
            case 60 * 2:
                return EnumInterval.intToEnumInterval(1);
            case 60 * 5:
                return EnumInterval.intToEnumInterval(2);
            case 60 * 10:
                return EnumInterval.intToEnumInterval(3);
            case 60 * 15:
                return EnumInterval.intToEnumInterval(4);
            case 60 * 30:
                return EnumInterval.intToEnumInterval(5);
            case 60 * 60:
                return EnumInterval.intToEnumInterval(6);
            case 60 * 60 * 2:
                return EnumInterval.intToEnumInterval(7);
            case 60 * 60 * 3:
                return EnumInterval.intToEnumInterval(8);
            case 60 * 60 * 4:
                return EnumInterval.intToEnumInterval(9);
            case 60 * 60 * 6:
                return EnumInterval.intToEnumInterval(10);
            case 60 * 60 * 12:
                return EnumInterval.intToEnumInterval(11);
            default:
                return EnumInterval.intToEnumInterval(0);
        }
    }

    public int getIntfromEnum(){
        switch (interval) {
            case 60:
                return 0;
            case 60 * 2:
                return 1;
            case 60 * 5:
                return 2;
            case 60 * 10:
                return 3;
            case 60 * 15:
                return 4;
            case 60 * 30:
                return 5;
            case 60 * 60:
                return 6;
            case 60 * 60 * 2:
                return 7;
            case 60 * 60 * 3:
                return 8;
            case 60 * 60 * 4:
                return 9;
            case 60 * 60 * 6:
                return 10;
            case 60 * 60 * 12:
                return 11;
            default:
                return 0;
        }
    }


    //Room
    public TimeInstance(boolean on, String name, Date start, Date end, int interval, boolean mo, boolean tu, boolean we, boolean th, boolean fr, boolean sa, boolean su) {
        this.on = on;
        this.name = name;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.mo = mo;
        this.tu = tu;
        this.we = we;
        this.th = th;
        this.fr = fr;
        this.sa = sa;
        this.su = su;
    }

    //Fragments
    public TimeInstance(boolean on, String name, Date start, Date end, boolean mo, boolean tu, boolean we, boolean th, boolean fr, boolean sa, boolean su, EnumInterval enumInterval) {
        this.on = on;
        this.name = name;
        this.start = start;
        this.end = end;
        this.mo = mo;
        this.tu = tu;
        this.we = we;
        this.th = th;
        this.fr = fr;
        this.sa = sa;
        this.su = su;
        setIntervalByIntevalNumber(enumInterval);
    }

    public TimeInstance(boolean[] arr){
        if(arr != null){
            this.mo = arr[0];
            this.tu = arr[1];
            this.we = arr[2];
            this.th = arr[3];
            this.fr = arr[4];
            this.sa = arr[5];
            this.su = arr[6];
        }
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean isMo() {
        return mo;
    }

    public void setMo(boolean mo) {
        this.mo = mo;
    }

    public boolean isTu() {
        return tu;
    }

    public void setTu(boolean tu) {
        this.tu = tu;
    }

    public boolean isWe() {
        return we;
    }

    public void setWe(boolean we) {
        this.we = we;
    }

    public boolean isTh() {
        return th;
    }

    public void setTh(boolean th) {
        this.th = th;
    }

    public boolean isFr() {
        return fr;
    }

    public void setFr(boolean fr) {
        this.fr = fr;
    }

    public boolean isSa() {
        return sa;
    }

    public void setSa(boolean sa) {
        this.sa = sa;
    }

    public boolean isSu() {
        return su;
    }

    public void setSu(boolean su) {
        this.su = su;
    }
}
