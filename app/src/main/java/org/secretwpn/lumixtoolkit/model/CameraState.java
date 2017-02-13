package org.secretwpn.lumixtoolkit.model;

/**
 * Model to handle getstate request response
 */

public class CameraState {
    private String model = "gh3";
    private String result;
    private String battery;
    private String mode;
    private String availablePhoto;
    private String sdCardStatus;
    private String sdMemory;
    private String availableVideo;
    private String isRecording;
    private String burstIntervalStatus;
    private String sdAccess;

    public CameraState() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSdMemory() {
        return sdMemory;
    }

    public void setSdMemory(String sdMemory) {
        this.sdMemory = sdMemory;
    }

    public int getBattery() {
        int charge;
        switch (battery) {
            case "3/3":
                charge = 100;
                break;
            case "2/3":
                charge = 66;
                break;
            case "1/3":
                charge = 33;
                break;
            default:
                charge = 0;
                break;
        }
        return charge;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAvailablePhoto() {
        return availablePhoto;
    }

    public void setAvailablePhoto(String availablePhoto) {
        this.availablePhoto = availablePhoto;
    }

    public String getSdCardStatus() {
        return sdCardStatus;
    }

    public void setSdCardStatus(String sdCardStatus) {
        this.sdCardStatus = sdCardStatus;
    }

    public String getAvailableVideo() {
        return availableVideo;
    }

    public void setAvailableVideo(String availableVideo) {
        this.availableVideo = availableVideo;
    }

    public String getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(String isRecording) {
        this.isRecording = isRecording;
    }

    public String getBurstIntervalStatus() {
        return burstIntervalStatus;
    }

    public void setBurstIntervalStatus(String burstIntervalStatus) {
        this.burstIntervalStatus = burstIntervalStatus;
    }

    public String getSdAccess() {
        return sdAccess;
    }

    public void setSdAccess(String sdAccess) {
        this.sdAccess = sdAccess;
    }

    @Override
    public String toString() {
        return String.format("battery: %s", getBattery());
    }

    public String getModel() {
        return model;
    }
}
