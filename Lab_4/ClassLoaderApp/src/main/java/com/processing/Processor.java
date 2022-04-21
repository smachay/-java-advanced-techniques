package com.processing;


public interface Processor {

    boolean submitTask(String task, StatusListener statusListener);

    String getInfo();

    String getResult();

}
