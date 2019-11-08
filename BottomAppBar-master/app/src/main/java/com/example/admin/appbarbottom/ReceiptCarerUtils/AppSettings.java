package com.example.admin.appbarbottom.ReceiptCarerUtils;

public class AppSettings {
//    Settings Point
    private boolean showInfoGraphics = true;
    private boolean isFirstAttemptToShow = true;

    private static final AppSettings ourInstance = new AppSettings();

    public static AppSettings getInstance() {
        return ourInstance;
    }

    private AppSettings() {
    }


    public boolean isShowInfoGraphics() {
        return showInfoGraphics;
    }

    public void setShowInfoGraphics(boolean showInfoGraphics) {
        this.showInfoGraphics = showInfoGraphics;
    }

    public void toggleShowInfoGraphics() {
        this.showInfoGraphics = !showInfoGraphics;
    }

    public boolean isFirstAttemptToRun() {
        return isFirstAttemptToShow;
    }
}
