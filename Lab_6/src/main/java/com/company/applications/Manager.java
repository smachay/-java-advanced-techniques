package com.company.applications;

import com.company.advertisement.Advertisement;
import com.company.billboards.IBillboard;
import com.company.billboards.IManager;
import com.company.billboards.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.util.*;

public class Manager extends JFrame implements ActionListener, IManager {
    JTable table;
    JButton delBtn;
    JPanel tablePanel;
    DefaultTableModel model;
    Object columns = new String[]{"Id", "Czas wyswietlania", "Tekst", "Bilbord Id"};

    private int newOrderId = 0;
    private int newBillboardId = 0;
    private static Manager manager;
    private HashMap<Integer,IBillboard> billboards = new HashMap<>();
    private HashMap<Integer, Advertisement> advertisements = new HashMap<>();


    public static void main(String[] args) {
        manager = new Manager();
        try {
            LocateRegistry.createRegistry(2002);
            Registry registry = LocateRegistry.getRegistry(2002);
            registry.bind("manager",(IManager) UnicastRemoteObject.exportObject(manager,0));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    Manager(){
        model = new DefaultTableModel((Object[]) columns,0);
        updateTable();

        tablePanel = new JPanel();
        tablePanel.setBounds(5,5,485,350);

        table = new JTable(model);
        tablePanel.add(new JScrollPane(table));
        this.add(tablePanel);

        delBtn = new JButton("Usun zamowienie");
        delBtn.setBounds(20,370, 150, 30);
        delBtn.addActionListener(this);
        this.add(delBtn);

        this.setSize(510, 450);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public int bindBillboard(IBillboard billboard) throws RemoteException {
        billboards.put(newBillboardId,billboard);
        billboard.setDisplayInterval(Duration.ofSeconds(10));
        newBillboardId++;

        return (newBillboardId-1);
    }

    @Override
    public boolean unbindBillboard(int billboardId) throws RemoteException {
        if(billboards.remove(billboardId)!=null)
            return true;
        else
            return false;

    }

    @Override
    public boolean placeOrder(Order order) throws RemoteException, InterruptedException {
        order.client.setOrderId(newOrderId);

        boolean placedNewOrder = false;
        int billboardId = 0;

        for(Map.Entry<Integer,IBillboard> entry: billboards.entrySet()){
            if(entry.getValue().getCapacity()[1]!=0){
                boolean isFirst = entry.getValue().getCapacity()[0] == entry.getValue().getCapacity()[1];
                billboardId= entry.getKey();
                placedNewOrder = entry.getValue().addAdvertisement(order.advertText,order.displayPeriod,newOrderId);

                if(isFirst)
                    entry.getValue().start();

                break;
            }
        }
        if(placedNewOrder){
            int id = newOrderId;
            advertisements.put(newOrderId,new Advertisement(billboardId,id,order));

            newOrderId++;
            updateTable();

            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean withdrawOrder(int orderId) throws RemoteException {
        Advertisement ad = advertisements.get(orderId);
        if (advertisements.remove(orderId) != null){
            updateTable();
            billboards.get(ad.billboardId).removeAdvertisement(orderId);
            return true;
        }else{
            return false;
        }

    }
    public void updateTable(){
        model.setRowCount(0);

        Object[][] data = new Object[advertisements.keySet().size()][4];
        int i = 0;
        for(Map.Entry<Integer,Advertisement> entry : advertisements.entrySet()){
            Advertisement ad = entry.getValue();
            data[i]=(new Object[]{entry.getKey(), ad.order.displayPeriod, ad.order.advertText,ad.billboardId});
            i++;
        }
        for (i=0;i< data.length;i++){
            System.out.println(i);
            model.addRow(data[i]);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == delBtn){
            int id = table.getSelectedColumn()-1;
            int delId = (int) model.getValueAt(id,0);

            try {
                this.withdrawOrder(delId);

            } catch (RemoteException ex) {
                ex.printStackTrace();
            }

        }
    }
}
