package com.smartsense.measuredata;

import java.lang.System;

/**
 * Activity displaying the app UI. Notably, this binds data from [MainViewModel] to views on screen,
 * and performs the permission check when enabling measure data.
 */
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0014J\b\u0010\'\u001a\u00020$H\u0014J-\u0010(\u001a\u00020$2\u0006\u0010)\u001a\u00020*2\u000e\u0010+\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00140,2\u0006\u0010-\u001a\u00020.H\u0016\u00a2\u0006\u0002\u0010/J\b\u00100\u001a\u00020$H\u0014J\b\u00101\u001a\u00020$H\u0014J\b\u00102\u001a\u00020$H\u0002J\b\u00103\u001a\u00020$H\u0002J\u0010\u00104\u001a\u00020$2\u0006\u00105\u001a\u000206H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\f\"\u0004\b\u0017\u0010\u000eR\u001a\u0010\u0018\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\f\"\u0004\b\u001a\u0010\u000eR\u000e\u0010\u001b\u001a\u00020\u001cX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u001d\u001a\u00020\u001e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b!\u0010\"\u001a\u0004\b\u001f\u0010 \u00a8\u00067"}, d2 = {"Lcom/smartsense/measuredata/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/smartsense/measuredata/databinding/ActivityMainBinding;", "handler", "Landroid/os/Handler;", "handlerThread", "Landroid/os/HandlerThread;", "lat", "", "getLat", "()F", "setLat", "(F)V", "lng", "getLng", "setLng", "permissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "", "potValue", "getPotValue", "setPotValue", "soundLevel", "getSoundLevel", "setSoundLevel", "soundMeter", "Lcom/smartsense/measuredata/SoundMeter;", "viewModel", "Lcom/smartsense/measuredata/MainViewModel;", "getViewModel", "()Lcom/smartsense/measuredata/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "onPause", "onRequestPermissionsResult", "requestCode", "", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "onStart", "startTimer", "stopTimer", "updateViewVisiblity", "uiState", "Lcom/smartsense/measuredata/UiState;", "app_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.smartsense.measuredata.databinding.ActivityMainBinding binding;
    private androidx.activity.result.ActivityResultLauncher<java.lang.String> permissionLauncher;
    private android.os.HandlerThread handlerThread;
    private android.os.Handler handler;
    private com.smartsense.measuredata.SoundMeter soundMeter;
    private float soundLevel = 0.0F;
    private final kotlin.Lazy viewModel$delegate = null;
    private float lat = 0.0F;
    private float lng = 0.0F;
    private float potValue = 0.0F;
    
    public MainActivity() {
        super();
    }
    
    public final float getSoundLevel() {
        return 0.0F;
    }
    
    public final void setSoundLevel(float p0) {
    }
    
    private final com.smartsense.measuredata.MainViewModel getViewModel() {
        return null;
    }
    
    public final float getLat() {
        return 0.0F;
    }
    
    public final void setLat(float p0) {
    }
    
    public final float getLng() {
        return 0.0F;
    }
    
    public final void setLng(float p0) {
    }
    
    public final float getPotValue() {
        return 0.0F;
    }
    
    public final void setPotValue(float p0) {
    }
    
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onStart() {
    }
    
    private final void updateViewVisiblity(com.smartsense.measuredata.UiState uiState) {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onPause() {
    }
    
    private final void startTimer() {
    }
    
    private final void stopTimer() {
    }
}