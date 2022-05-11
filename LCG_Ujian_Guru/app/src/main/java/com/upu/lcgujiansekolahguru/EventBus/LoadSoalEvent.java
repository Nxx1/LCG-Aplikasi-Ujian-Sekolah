package com.upu.lcgujiansekolahguru.EventBus;

public class LoadSoalEvent {
    private int status;

    public LoadSoalEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
