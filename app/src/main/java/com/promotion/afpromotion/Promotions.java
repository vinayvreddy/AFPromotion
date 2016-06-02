package com.promotion.afpromotion;

import android.os.Parcel;
import android.os.Parcelable;

public class Promotions implements Parcelable {
     String buttonTarget;
     String buttonTitle;
     String description;
     String footer;
     String imageUrl;
     String title;

    private Promotions(Parcel in){
        this.buttonTarget = in.readString();
        this.buttonTitle = in.readString();
        this.description = in.readString();
        this.footer = in.readString();
        this.imageUrl = in.readString();
        this.title = in.readString();
    }

    public Promotions(){

    }

    public String getButtonTarget(){
        return buttonTarget;
    }
    public String getButtonTitle(){
        return buttonTitle;
    }
    public String getDescription(){
        return description;

    }
    public String getFooter(){
        return footer;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public String getTitle(){
        return title;
    }

    public void setButtonTarget(String target){
         buttonTarget = target;
    }
    public void setButtonTitle(String title){
        buttonTitle = title;
    }
    public void setDescription(String desc){
        description = desc;
    }
    public void setFooter(String footer){
        this.footer = footer;
    }
    public void setImageUrl(String url){
        imageUrl = url;
    }


    public void setTitle(String title){
        this.title = title;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(buttonTarget);
        dest.writeString(buttonTitle);
        dest.writeString(description);
        dest.writeString(footer);
        dest.writeString(imageUrl);
        dest.writeString(title);

    }
    public static final Parcelable.Creator<Promotions> CREATOR = new Parcelable.Creator<Promotions>() {

        @Override
        public Promotions createFromParcel(Parcel source) {
            return new Promotions(source);
        }

        @Override
        public Promotions[] newArray(int size) {
            return new Promotions[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
}
