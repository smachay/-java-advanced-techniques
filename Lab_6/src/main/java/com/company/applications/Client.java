package com.company.applications;

import com.company.billboards.IClient;
import com.company.billboards.IManager;
import com.company.billboards.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;

public class Client extends JFrame implements ActionListener, IClient {
    JTable table;
    DefaultTableModel model;
    JLabel orderLabel;
    JTextField orderField;
    JLabel timeLabel;
    JTextField timeField;
    JButton delBtn;
    JButton addBtn;
    JPanel tablePanel;

    private int orderId;
    private IManager iManager;
    private Registry registry;
    private IClient iClient;
    private static Client client;
    //private HashMap<Integer,Order> orders = new HashMap<>();

    public static void main(String[] args) {
        client = new Client();
        client.setClientData();
    }

    Client(){
        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Czas wyswietlania");
        model.addColumn("Tekst");

        tablePanel = new JPanel();
        tablePanel.setBounds(5,250,485,200);

        table = new JTable(model);
        tablePanel.add(new JScrollPane(table));

        orderLabel = new JLabel("Wprowadz tekst reklamy:");
        orderLabel.setBounds(20,20,200,30);
        this.add(orderLabel);

        orderField = new JTextField();
        orderField.setBounds(220,20,250,30);
        this.add(orderField);

        timeLabel = new JLabel("Czas wyswietlania reklamy [s]:");
        timeLabel.setBounds(20,70,210,30);
        this.add(timeLabel);

        timeField = new JTextField();
        timeField.setBounds(220,70,50,30);
        this.add(timeField);

        addBtn = new JButton("Dodaj zamowienie");
        addBtn.setBounds(20,120,150,30);
        addBtn.addActionListener(this);
        this.add(addBtn);

        delBtn = new JButton("Usun zamowienie");
        delBtn.setBounds(20,200,150,30);
        delBtn.addActionListener(this);
        this.add(delBtn);

        this.add(tablePanel);
        this.setSize(510, 450);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public void setClientData(){
        try {
            registry = LocateRegistry.getRegistry("localhost",2002);
            iManager = (IManager) registry.lookup("manager");
            iClient = (IClient) UnicastRemoteObject.exportObject(client,0);

        } catch (RemoteException | NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void setOrderId(int orderId) throws RemoteException {
        this.orderId = orderId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addBtn){
            Order order = new Order();
            order.advertText = orderField.getText();

            order.client = iClient;
            order.displayPeriod = Duration.ofSeconds(Long.parseLong(timeField.getText()));

            try {
                if(iManager.placeOrder(order)){
                    //orders.put(orderId, order);
                    model.addRow(new Object[]{orderId, order.displayPeriod, order.advertText});
                }else{
                    JOptionPane.showMessageDialog(null, "Brak wolnych bilbordow!");
                }

            } catch (RemoteException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        if(e.getSource() == delBtn){
            int id = table.getSelectedColumn();

            int delId = (int) model.getValueAt(id,0);

            try {
                if(iManager.withdrawOrder(delId)){
                    model.removeRow(delId);
                    //orders.remove(delId);

                }else{
                    JOptionPane.showMessageDialog(null, "Nie udalo sie wycofac zamowienia");
                }

            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

}