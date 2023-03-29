package com.smartsense.measuredata;

import java.lang.System;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u0082\u0001\u0003\u0006\u0007\b\u00a8\u0006\t"}, d2 = {"Lcom/smartsense/measuredata/UiState;", "", "()V", "HeartRateAvailable", "HeartRateNotAvailable", "Startup", "Lcom/smartsense/measuredata/UiState$HeartRateAvailable;", "Lcom/smartsense/measuredata/UiState$HeartRateNotAvailable;", "Lcom/smartsense/measuredata/UiState$Startup;", "app_release"})
public abstract class UiState {
    
    private UiState() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/smartsense/measuredata/UiState$Startup;", "Lcom/smartsense/measuredata/UiState;", "()V", "app_release"})
    public static final class Startup extends com.smartsense.measuredata.UiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.smartsense.measuredata.UiState.Startup INSTANCE = null;
        
        private Startup() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/smartsense/measuredata/UiState$HeartRateAvailable;", "Lcom/smartsense/measuredata/UiState;", "()V", "app_release"})
    public static final class HeartRateAvailable extends com.smartsense.measuredata.UiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.smartsense.measuredata.UiState.HeartRateAvailable INSTANCE = null;
        
        private HeartRateAvailable() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/smartsense/measuredata/UiState$HeartRateNotAvailable;", "Lcom/smartsense/measuredata/UiState;", "()V", "app_release"})
    public static final class HeartRateNotAvailable extends com.smartsense.measuredata.UiState {
        @org.jetbrains.annotations.NotNull()
        public static final com.smartsense.measuredata.UiState.HeartRateNotAvailable INSTANCE = null;
        
        private HeartRateNotAvailable() {
            super();
        }
    }
}