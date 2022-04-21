package com.processing;

public class Add implements Processor, Runnable {
    private String inputText;
    private String result;
    private StatusListener statusListener;
    public String taskName = "Sumowanie";
    private int inputLength;

    @Override
    public void run() {
        int progressUnit = 100 / inputLength;
        int result = 0;
        String buff = "";

        for (int i = 0; i < inputLength; i++) {
            if (this.inputText.charAt(i) >= '0' && this.inputText.charAt(i) <= '9'){
                buff += inputText.charAt(i);
            }

            if (this.inputText.charAt(i) == '+'){
                result += Integer.parseInt(buff);
                buff = "";
            }

            if (i==inputLength-1)
                result += Integer.parseInt(buff);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.statusListener.statusChanged(new Status(this.taskName, i * progressUnit ));
        }

        this.result = Integer.toString(result);
        this.statusListener.statusChanged(new Status(this.taskName, 100));

    }

    private String removeZeros(String number) {
        String buff = "0";

        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) != '0') {
                buff = number.substring(i);
                return buff;
            }
        }
        return buff;
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
        return "Klasa pozwalajaca na dodawanie liczb podanych jako string";
    }

    @Override
    public String getResult() {
        return this.result;
    }
}
