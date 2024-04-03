package org.example.models;

import java.text.SimpleDateFormat;
import java.util.Date;
public class Photo {
    private final String title;
    private final Date date;

    public Photo(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    public Date getDate() { return date; }

    @Override
    public String toString() {
        final String D = ";";
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

        return getTitle() +D + dateFormatter.format(getDate());
    }

}