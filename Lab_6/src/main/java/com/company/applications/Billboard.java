package com.company.applications;

import com.company.billboards.IBillboard;
import com.company.billboards.IClient;
import com.company.billboards.IManager;
import com.company.billboards.Order;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Billboard extends JFrame implements ActionListener, IBillboard {
    static JTextArea adText;
    static Billboard billboard;
    private static HashMap<Integer, Order> orders = new HashMap<>();
    private Duration displayInterval;
    private IManager iManager;
    private Registry registry;
    private IBillboard iBillboard;
    private Thread displatThread;
    public int id;

    private int capacity = 5;
    public static void main(String[] args) throws InterruptedException {
        billboard = new Billboard();
        billboard.setBillboardData();


    }

    Billboard(){
        adText = new JTextArea("Brak reklamy!");
        adText.setBounds(20,20,440,230);
        adText.setFont((adText.getFont().deriveFont(12f)));
        this.add(adText);

        this.setSize(500, 300);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public void setBillboardData(){
        try {
            registry = LocateRegistry.getRegistry("localhost",2002);
            iManager = (IManager) registry.lookup("manager");
            iBillboard = (IBillboard) UnicastRemoteObject.exportObject(billboard,0);
            iManager.bindBillboard(iBillboard);


        } catch (RemoteException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean addAdvertisement(String advertText, Duration displayPeriod, int orderId) throws RemoteException {
        if(orders.size()!=capacity){
            Order order = new Order();
            order.advertText = advertText;
            order.displayPeriod = displayPeriod;
            orders.put(orderId,order);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean removeAdvertisement(int orderId) throws RemoteException {
        if(orders.remove(orderId)!= null){
            return true;
        }
        return false;
    }

    @Override
    public int[] getCapacity() throws RemoteException {
        return new int[]{capacity,capacity-orders.size()};
    }

    @Override
    public void setDisplayInterval(Duration displayInterval) throws RemoteException {
        this.displayInterval=displayInterval;
    }

    @Override
    public boolean  start() throws RemoteException, InterruptedException {
        displatThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(!orders.isEmpty()){
                        long time = displayInterval.get(ChronoUnit.SECONDS);

                        for (Map.Entry<Integer, Order> entry : orders.entrySet()) {
                            adText.setText(entry.getValue().advertText);

                            if (entry.getValue().displayPeriod.compareTo(displayInterval) > 0) {
                                entry.getValue().displayPeriod.minus(displayInterval);
                            } else {
                                time = entry.getValue().displayPeriod.get(ChronoUnit.SECONDS);
                                try {
                                    iManager.withdrawOrder(entry.getKey());
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                sleep(1000 * time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                }
                adText.setText("Brak reklamy!");
            }
        });
        if(!displatThread.isAlive())
            displatThread.start();

        /*
        */
        return true;
    }

    @Override
    public boolean stop() throws RemoteException {
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
