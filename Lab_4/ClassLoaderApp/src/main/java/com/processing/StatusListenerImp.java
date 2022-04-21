package com.processing;

public class StatusListenerImp implements StatusListener {
    private int progress;

    @Override
    public void statusChanged(Status status) {
        progress = status.getProgress();
    }

    public int getProgress() {
        return progress;
    }
}
