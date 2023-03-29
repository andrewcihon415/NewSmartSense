package com.smartsense.measuredata;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0006\n\u0000\u001a\u0010\u0010\u0015\u001a\u00020\u00102\b\u0010\u0016\u001a\u0004\u0018\u00010\u0010\u001a6\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u001e\u001a\u0006\u0010!\u001a\u00020\u0018\u001a\u0006\u0010\"\u001a\u00020\u0010\u001a\u0006\u0010#\u001a\u00020\u0001\u001a\u0006\u0010\n\u001a\u00020\u0018\u001a\u001e\u0010$\u001a\u00020\u00182\u0006\u0010%\u001a\u00020\u001b2\u0006\u0010&\u001a\u00020\u001b2\u0006\u0010\'\u001a\u00020\u001b\u001a\u000e\u0010(\u001a\u00020\u00182\u0006\u0010)\u001a\u00020*\"\u001a\u0010\u0000\u001a\u00020\u0001X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u0004\b\u0004\u0010\u0005\"\u001a\u0010\u0006\u001a\u00020\u0001X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\u0003\"\u0004\b\b\u0010\u0005\"\u001e\u0010\t\u001a\u0004\u0018\u00010\u0001X\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u000e\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\r\"\u001a\u0010\u000f\u001a\u00020\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014\u00a8\u0006+"}, d2 = {"currentHeartRate", "", "getCurrentHeartRate", "()I", "setCurrentHeartRate", "(I)V", "organization", "getOrganization", "setOrganization", "userID", "getUserID", "()Ljava/lang/Integer;", "setUserID", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "userName", "", "getUserName", "()Ljava/lang/String;", "setUserName", "(Ljava/lang/String;)V", "capitalize", "str", "changeScreenColor", "", "HR", "gas", "", "sound", "myTextViewHR", "Landroid/widget/TextView;", "myTextViewGas", "myTextViewSound", "createUserName", "getDeviceName", "getHeartRate", "sendData", "amp", "lat", "lng", "updateHeartRate", "newHeartRate", "", "app_debug"})
public final class MainViewModelKt {
    
    /**
     * Holds most of the interaction logic and UI state for the app.
     */
    @org.jetbrains.annotations.Nullable()
    private static java.lang.Integer userID;
    private static int currentHeartRate = 0;
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String userName;
    private static int organization = 1;
    
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.Integer getUserID() {
        return null;
    }
    
    public static final void setUserID(@org.jetbrains.annotations.Nullable()
    java.lang.Integer p0) {
    }
    
    public static final int getCurrentHeartRate() {
        return 0;
    }
    
    public static final void setCurrentHeartRate(int p0) {
    }
    
    public static final void updateHeartRate(double newHeartRate) {
    }
    
    public static final int getHeartRate() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String getUserName() {
        return null;
    }
    
    public static final void setUserName(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public static final int getOrganization() {
        return 0;
    }
    
    public static final void setOrganization(int p0) {
    }
    
    public static final void createUserName() {
    }
    
    public static final void getUserID() {
    }
    
    public static final void sendData(float amp, float lat, float lng) {
    }
    
    public static final void changeScreenColor(int HR, float gas, float sound, @org.jetbrains.annotations.NotNull()
    android.widget.TextView myTextViewHR, @org.jetbrains.annotations.NotNull()
    android.widget.TextView myTextViewGas, @org.jetbrains.annotations.NotNull()
    android.widget.TextView myTextViewSound) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String getDeviceName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String capitalize(@org.jetbrains.annotations.Nullable()
    java.lang.String str) {
        return null;
    }
}