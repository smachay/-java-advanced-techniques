package com.processing;

public class Reverse implements Processor, Runnable {
    private String inputText;
    private String result;
    private StatusListener statusListener;
    public String taskName = "Odwrocenie zdania";
    private int inputLength;

    @Override
    public void run() {
        int progressUnit = 100/inputLength;
        //int progressUnit =
        int j = 0;
        for(int i=this.inputLength-1; i>=0; i--){
            this.result += inputText.charAt(i);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.statusListener.statusChanged(new Status(this.taskName, j * progressUnit));
            j++;
        }
        this.statusListener.statusChanged(new Status(this.taskName, 100));
    }

    @Override
    public boolean submitTask(String task, StatusListener statusListener) {
        this.inputText = task;
        this.statusListener = statusListener;
        this.inputLength = inputText.length();
        this.result = " ";
        Thread t = new Thread(this);
        t.start();
        return false;
    }

    @Override
    public String getInfo() {
        return "Klasa odwracajaca litery w podanym zdaniu";
    }

    @Override
    public String getResult() {
        return this.result;
    }
}
