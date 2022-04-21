package serviceloader.client;

import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ServiceLoader;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class UI extends JFrame implements ActionListener {
    DefaultTableModel model;
    DataSet dataSet;
    static ClusterAnalysisService service;
    JTable table;
    JButton addRowBtn, calculateBtn;
    JScrollPane sp;
    JComboBox algorythms;
    String[][] currData;

    static HashMap<String, ClusterAnalysisService> services = new HashMap<>();
    static ArrayList<String> algorithmsInfo = new ArrayList<>();

    public static void main(String[] args) {
        ServiceLoader<ClusterAnalysisService> loader = ServiceLoader.load(ClusterAnalysisService.class);

        for (ClusterAnalysisService s : loader) {
            services.put(s.getName(), s);
            algorithmsInfo.add(s.getName());
        }

        UI gui = new UI();


    }

    UI(){
        dataSet = new DataSet();
        model = new DefaultTableModel();

        addRowBtn = new JButton("Dodaj");
        addRowBtn.addActionListener(this);
        addRowBtn.setBounds(30, 400, 100, 30);
        this.add(addRowBtn);

        calculateBtn = new JButton("Oblicz");
        calculateBtn.addActionListener(this);
        calculateBtn.setBounds(160, 400, 100, 30);
        this.add(calculateBtn);

        table = new JTable(model);
        table.setBounds(30, 40, 200, 300);

        model.addColumn("ID");
        model.addColumn("X0");
        model.addColumn("X1");
        model.addColumn("X2");
        model.addColumn("X3");
        model.addColumn("Wynik");

        algorythms =new JComboBox<>(algorithmsInfo.toArray());
        algorythms.setBounds(290, 400, 160, 30);
        this.add(algorythms);

        sp = new JScrollPane(table);
        this.add(sp);



        this.setSize(500, 500);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] columnNames = new String[model.getColumnCount()];

        if(e.getSource() == addRowBtn)
            model.addRow(new Object[]{String.valueOf(model.getRowCount())});

        if(e.getSource() == calculateBtn) {
            service = services.get(algorythms.getSelectedItem());

            currData = new String[model.getRowCount()][model.getColumnCount()];
            var dataBuff = model.getDataVector();

           for(int i=0;i< model.getRowCount();i++){
               String[] newRow = new String[model.getColumnCount()];
               for(int j=0;j< model.getColumnCount();j++){
                   currData[i][j] = (String) dataBuff.get(i).get(j);
                   if(i==0)
                       columnNames[j] = model.getColumnName(j);

               }
           }

           dataSet.setHeader(columnNames);
           dataSet.setData(currData);
            try {
                service.submit(dataSet);
                sleep(1000);
                dataSet = service.retrieve(false);
            } catch (ClusteringException | InterruptedException ex) {
                ex.printStackTrace();
            }
            currData = dataSet.getData();
            model.setDataVector(currData,columnNames);

        }

    }
}
