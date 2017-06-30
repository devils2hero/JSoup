package com.example.kimhao.jsoup.models;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Author by AsianTech
 * Created by kimha on 29/06/2017.
 */
@Data
public class Topic implements Parcelable {
    private String imageSubject;
    private String titleSubject;
    private String urlSubject;

    public Topic(Parcel in) {
        imageSubject = in.readString();
        titleSubject = in.readString();
        urlSubject = in.readString();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    public Topic() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(imageSubject);
        dest.writeString(titleSubject);
        dest.writeString(urlSubject);
    }
}
