package com.footinit.base_mvvm.data.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhijit on 17-11-2017.
 */

@Entity(tableName = "opensource")
public class OpenSource implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long openSourceId;

    @ColumnInfo(name = "project_url")
    @Expose @SerializedName("project_url")
    private String projectUrl;

    @ColumnInfo(name = "img_url")
    @Expose @SerializedName("img_url")
    private String imgUrl;

    @Expose
    private String title;

    @Expose
    private String author;

    @Expose
    private String description;


    public long getOpenSourceId() {
        return openSourceId;
    }

    public void setOpenSourceId(long openSourceId) {
        this.openSourceId = openSourceId;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(openSourceId);
        parcel.writeString(projectUrl);
        parcel.writeString(imgUrl);
        parcel.writeString(title);
        parcel.writeString(author);
        parcel.writeString(description);
    }

    public static Creator<OpenSource> CREATOR = new Creator<OpenSource>() {
        @Override
        public OpenSource createFromParcel(Parcel parcel) {
            OpenSource openSource = new OpenSource();

            openSource.openSourceId = parcel.readLong();
            openSource.projectUrl = parcel.readString();
            openSource.imgUrl = parcel.readString();
            openSource.title = parcel.readString();
            openSource.author = parcel.readString();
            openSource.description = parcel.readString();

            return openSource;
        }

        @Override
        public OpenSource[] newArray(int i) {
            return new OpenSource[i];
        }
    };
}
