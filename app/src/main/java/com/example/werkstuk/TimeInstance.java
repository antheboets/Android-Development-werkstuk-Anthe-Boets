package com.example.werkstuk;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "timeInstance_table")
@TypeConverters(DateConverter.class)
public class TimeInstance {



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

    public enum EnumInterval{
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

    public static EnumInterval intToEnumInterval(int x){
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

    private boolean isInWeekdays(){
        if(!mo){
            return false;
        }
        if(!tu){
            return false;
        }
        if(!we){
            return false;
        }
        if(!th){
            return false;
        }
        if(!fr){
            return false;
        }
        return true;
    }

    private boolean weekends(){
        if(!sa){
            return false;
        }
        if(!su){
            return false;
        }
        return true;
    }
    private boolean everyDay(){
        if(!mo){
            return false;
        }
        if(!tu){
            return false;
        }
        if(!we){
            return false;
        }
        if(!th){
            return false;
        }
        if(!fr){
            return false;
        }
        if(!sa){
            return false;
        }
        if(!su){
            return false;
        }
        return true;
    }

    private String getDayStr(boolean firstItem, String dayString, boolean day){
        if(day){
            if(firstItem){
                firstItem = false;
                return  dayString;
            }
            else{
                return ", " + dayString;
            }
        }
        return "";
    }

    private boolean isFirstTime(boolean firstTime, String str){
        if(!firstTime){
            return false;
        }
        return str.equals("");
    }

    public String getDaysStr(){

        String str = "";
        boolean firstItem = true;

        if(everyDay()){
            return App.getAppResources().getString(R.string.every_day);
        }

        if(isInWeekdays()){
            str += App.getAppResources().getString(R.string.weekdays);
            firstItem = false;
        }
        else{

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
        if(weekends()){
            if(firstItem){
                str += App.getAppResources().getString(R.string.weekends);
                firstItem = false;
            }
            else{
                str = App.getAppResources().getString(R.string.weekends) + ", " + str;
            }
        }
        else{
            str += getDayStr(firstItem, App.getAppResources().getString(R.string.saturdayShort), sa);
            firstItem = isFirstTime(firstItem, str);
            str += getDayStr(firstItem, App.getAppResources().getString(R.string.sundayShort), su);
            firstItem = isFirstTime(firstItem, str);
        }

        if(str.equals("")){
            str = App.getAppResources().getString(R.string.never);
        }
        return str;
    }
    public String getTimeStr(){

        SimpleDateFormat ft;

        if(true){
            ft = new SimpleDateFormat ("hh:mm:ss a");
        }
        else{
            ft = new SimpleDateFormat ("kk:mm:ss");
        }

        String str = ft.format(start);
        str += " - ";
        str+= ft.format(end);
        return str;
    }

    public static String[] getIntervalStringArray(){
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


    public void setIntervalByIntevalNumber(EnumInterval enumInterval){
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
    public TimeInstance(boolean on, String name, Date start, Date end, boolean mo, boolean tu, boolean we, boolean th, boolean fr, boolean sa, boolean su,EnumInterval enumInterval) {
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
