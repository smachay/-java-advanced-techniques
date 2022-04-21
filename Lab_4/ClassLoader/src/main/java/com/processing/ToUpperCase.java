package com.processing;

public class ToUpperCase implements Processor, Runnable {
    private String inputText;
    private String result;
    public StatusListener statusListener;
    public String taskName = "Zamien na duze litery";
    private int inputLength;

    @Override
    public void run() {
        int progressUnit = 100/inputLength;

        for(int i=0; i< this.inputLength; i++){
            this.result += Character.toUpperCase(inputText.charAt(i));

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.statusListener.statusChanged(new Status(this.taskName, i * progressUnit));
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
        return "Klasa zamieniajaca male litery na duze";
    }

    @Override
    public String getResult() {
        return this.result;
    }
}
