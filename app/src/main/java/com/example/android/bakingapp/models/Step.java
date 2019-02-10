package com.example.android.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {

    private int stepId;

    private String stepShortDescription;

    private String stepDescription;

    private String stepVideoUrl;

    private String stepThumbnailUrl;

    public Step(int stepId, String stepShortDescription, String stepDescription,
                String stepVideoUrl, String stepThumbnailUrl) {
        this.stepId = stepId;
        this.stepShortDescription = stepShortDescription;
        this.stepDescription = stepDescription;
        this.stepVideoUrl = stepVideoUrl;
        this.stepThumbnailUrl = stepThumbnailUrl;
    }

    private Step(Parcel in) {
        stepId = in.readInt();
        stepShortDescription = in.readString();
        stepDescription = in.readString();
        stepVideoUrl = in.readString();
        stepThumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(stepId);
        parcel.writeString(stepShortDescription);
        parcel.writeString(stepDescription);
        parcel.writeString(stepVideoUrl);
        parcel.writeString(stepThumbnailUrl);
    }

    /**
     * Creates and returns a new Step object, as well as a new Step Array
     */
    public static final Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    /**
     * Returns the id of the step
     */
    public int getStepId() {
        return stepId;
    }

    /**
     * Returns the short description of the step
     */
    public String getStepShortDescription() {
        return stepShortDescription;
    }

    /**
     * Returns the description of the step
     */
    public String getStepDescription() {
        return stepDescription;
    }

    /**
     * Returns the videoUrl of the step
     */
    public String getStepVideoUrl() {
        return stepVideoUrl;
    }

    /**
     * Returns the thumbnailUrl of the step
     */
    public String getStepThumbnailUrl() {
        return stepThumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}