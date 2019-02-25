package org.csuc.utils.dashboard;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

/**
 * @author amartinez
 */
public class Identifier {

    private int day;
    private int month;
    private int year;

    public Identifier() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        JsonAdapter<Identifier> jsonAdapter = new Moshi.Builder().build().adapter(Identifier.class);
        return jsonAdapter.toJson(this);
    }
}
