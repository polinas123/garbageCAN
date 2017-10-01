package com.sillyv.garbagecan.screen.camera;

import android.util.SparseArray;

import java.io.File;

/**
 * Created by Vasili on 01/12/2016.
 *
 */
public class FileUploadEvent {

    private final File file;
    private final int score;
    private double longitude;
    private double latitude;
    private String uploadedFilePath;
    private SparseArray<String> credentialsMap;


    public FileUploadEvent(File file, int score) {
        this.file = file;
        this.score = score;
    }

    private FileUploadEvent(Builder builder) {
        file = builder.file;
        score = builder.score;
        setLongitude(builder.longitude);
        setLatitude(builder.latitude);
        setUploadedFilePath(builder.uploadedFilePath);
        setCredentialsMap(builder.credentialsMap);
    }

    public static Builder newBuilder(File file, int score) {
        return new Builder(file, score);
    }

    public int getScore() {
        return score;
    }

    public File getFile() {
        return file;
    }

    public double getLongitude() {
        return longitude;
    }

    public  void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUploadedFilePath() {
        return uploadedFilePath;
    }

    public  void setUploadedFilePath(String uploadedFilePath) {
        this.uploadedFilePath = uploadedFilePath;
    }

    public void setCredentialsMap(SparseArray<String> credentialsMap) {
        this.credentialsMap = credentialsMap;
    }

    public SparseArray<String> getCredentialsMap() {
        return credentialsMap;
    }


    public static final class Builder {
        private final File file;
        private final int score;
        private double longitude;
        private double latitude;
        private String uploadedFilePath;
        private SparseArray<String> credentialsMap;

        private Builder(File file, int score) {
            this.file = file;
            this.score = score;
        }

        public Builder withLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder withLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder withUploadedFilePath(String uploadedFilePath) {
            this.uploadedFilePath = uploadedFilePath;
            return this;
        }

        public Builder withCredentialsMap(SparseArray<String> credentialsMap) {
            this.credentialsMap = credentialsMap;
            return this;
        }

        public FileUploadEvent build() {
            return new FileUploadEvent(this);
        }
    }
}
