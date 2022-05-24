package com.company.gui;

import com.company.dao.EventDao;
import com.company.dao.InstallmentDao;
import com.company.dao.PaymentDao;
import com.company.dao.PersonDao;
import com.company.dbConnection.DBConnection;
import com.company.tableObjects.Event;
import com.company.tableObjects.Installment;
import com.company.tableObjects.Payment;
import com.company.tableObjects.Person;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static javax.swing.JOptionPane.showMessageDialog;

public class MainWindow{
    private final Connection connection;
    private PersonDao personService;
    private EventDao eventService;
    private PaymentDao paymentService;
    private InstallmentDao installmentService;

    private ArrayList<Person> persons;
    private ArrayList<Event> events;
    private ArrayList<Payment> payments;
    private ArrayList<Installment> installments;


    private DefaultTableModel personModel;
    private DefaultTableModel eventModel;
    private DefaultTableModel paymentModel;
    private DefaultTableModel installmentModel;

    private JTabbedPane contentPane;
    private JPanel contentPanel;
    private JPanel personsPane;
    private JPanel eventsPane;
    private JPanel paymentsPane;
    private JPanel installmentsPane;
    private JTabbedPane personCards;
    private JPanel showPersonsPanel;
    private JPanel newPersonPanel;
    private JTextField personNameInput;
    private JTextField personSurnameInput;
    private JButton addPersonBtn;
    private JTable personsTable;
    private JButton delPersonBtn;
    private JButton editPersonBtn;
    private JTabbedPane eventCards;
    private JPanel newEventPanel;
    private JButton addEventBtn;
    private JTextField eventNameInput;
    private JTextField eventPlaceInput;
    private JButton delEventBtn;
    private JButton editEventBtn;
    private JTable eventsTable;
    private JTabbedPane paymentCards;
    private JTabbedPane installmentCards;
    private JButton delPaymentBtn;
    private JTable paymentsTable;
    private JComboBox paymentEventsCB;
    private JButton addPaymentBtn;
    private JComboBox paymentInstallmentsCB;
    private JButton delInstallmentBtn;
    private JTable installmentsTable;
    private JButton editInstallmentBtn;
    private JComboBox paymentPersonsCB;
    private JComboBox installmentEventsCB;
    private JTextField installmentNumberInput;
    private JTextField installmentAmountInput;
    private JButton addInstallmentBtn;
    private JTextField eventDateInput;
    private JPanel installmentPane;
    private JTextField deadlineInput;
    private JSpinner paymentNumberInput;
    private JLabel amountLabel;
    private JTextField eventYearInput;
    String dateFormat="yyy-MM-dd";

    MainWindow(){
        FlatLightLaf.setup();
        DBConnection con = new DBConnection();
        connection = DBConnection.getConnection();
        personService = new PersonDao(connection);
        eventService = new EventDao(connection);
        paymentService = new PaymentDao(connection);
        installmentService = new InstallmentDao(connection);

        personModel = new DefaultTableModel();
        personModel.addColumn("Id");
        personModel.addColumn("Name");
        personModel.addColumn("Surname");
        personsTable.setModel(personModel);

        eventModel = new DefaultTableModel();
        eventModel.addColumn("Id");
        eventModel.addColumn("Name");
        eventModel.addColumn("Place");
        eventModel.addColumn("Date");
        eventsTable.setModel(eventModel);

        paymentModel = new DefaultTableModel();
        paymentModel.addColumn("Id");
        paymentModel.addColumn("Date");
        paymentModel.addColumn("Amount");
        paymentModel.addColumn("Member Id");
        paymentModel.addColumn("Number of installments");
        paymentsTable.setModel(paymentModel);

        installmentModel = new DefaultTableModel();
        installmentModel.addColumn("Id");
        installmentModel.addColumn("Event Id");
        installmentModel.addColumn("Number of installments");
        installmentModel.addColumn("Deadline");
        installmentModel.addColumn("Amount");
        installmentsTable.setModel(installmentModel);

        addPersonBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = personNameInput.getText();
                String surname = personSurnameInput.getText();

                if(!name.isEmpty()&&!surname.isEmpty()) {
                    personService.save(new Person(name, surname));
                    personNameInput.setText("");
                    personSurnameInput.setText("");
                }
                else{
                    showMessageDialog(null, "Incorrect user data");
                }

            }
        });

        personCards.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                if (personCards.getSelectedIndex()==1) {
                    persons = personService.getAll();
                    updateTable(personModel,persons.size());
                }

            }
        });

        eventCards.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (eventCards.getSelectedIndex()==1) {
                    events = eventService.getAll();
                    updateTable(eventModel,events.size());
                }

            }
        });

        addEventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = eventNameInput.getText();
                String place = eventPlaceInput.getText();
                String dateStr = eventDateInput.getText();

                if(!name.isEmpty()&&!place.isEmpty()&&!dateStr.isEmpty()){
                    if(validateDate(dateStr)){
                        Date date = Date.valueOf(dateStr);
                        eventNameInput.setText("");
                        eventPlaceInput.setText("");
                        eventDateInput.setText("");
                        eventService.save(new Event(name,place,date));
                    }else{
                        showMessageDialog(null, "Incorrect date format");
                    }
                }else{
                    showMessageDialog(null, "Incorrect event data");
                }

            }
        });

        paymentCards.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (paymentCards.getSelectedIndex()==1) {
                    payments = paymentService.getAll();
                    updateTable(paymentModel,payments.size());
                }

            }
        });

        paymentInstallmentsCB.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int eventId = events.stream()
                        .filter(event -> event.getId() == getId((String) paymentEventsCB.getSelectedItem()))
                        .findFirst().get().getId();

                paymentInstallmentsCB.removeAllItems();
                installments = installmentService.getEventInstallments(eventId);
                installments.forEach(el->{
                    paymentInstallmentsCB.addItem(el.getId()+" "+el.getAmount());
                });;
            }
        });

        addPaymentBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String strEventId = (String) paymentEventsCB.getSelectedItem();
                String strPersonId = (String) paymentPersonsCB.getSelectedItem();
                String strAmount = (String) paymentInstallmentsCB.getSelectedItem();
                if(!strPersonId.isEmpty()&&!strEventId.isEmpty()&&!strAmount.isEmpty()){
                    int num = (int)paymentNumberInput.getValue();
                    double amount = num*Double.parseDouble(getValue(strAmount));


                    Optional<Installment> installment = installments.stream()
                            .filter(el->el.getEventId()==getId(strEventId))
                            .findFirst();

                        if(installment.isPresent()){
                            System.out.println("updejcik");
                            String[] parms = new String[4];
                            int installmentNumber = installment.get().getNumber()-num;
                            if(installmentNumber>=0){
                                parms[0]=String.valueOf(installment.get().getEventId());
                                parms[1]=String.valueOf(installmentNumber);
                                parms[2]=String.valueOf(installment.get().getDeadline());
                                parms[3]=String.valueOf(installment.get().getAmount());

                                paymentService.save(new Payment(amount,getId(strPersonId),getId(strEventId),num));
                                paymentNumberInput.setValue(0);

                                //updates number of payments left for given event
                                installmentService.update(installment.get(),parms);
                            }
                        }


                }else{
                    showMessageDialog(null, "Incorrect payment data");
                }

            }
        });

        installmentCards.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (installmentCards.getSelectedIndex()==1) {
                    installments = installmentService.getAll();
                    updateTable(installmentModel,installments.size());
                }

            }
        });

        addInstallmentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateStr = deadlineInput.getText();
                if(!events.isEmpty()&&validateDate(dateStr)){
                    int eventId = events.stream()
                            .filter(event -> event.getName()==installmentEventsCB.getSelectedItem())
                            .findFirst().get().getId();

                    String number = installmentNumberInput.getText();
                    double amount = Double.parseDouble(installmentAmountInput.getText());

                    if(!number.isEmpty()&&amount!=0){
                        installmentService.save((new Installment(eventId,Integer.parseInt(number),Date.valueOf(dateStr),amount)));
                    }else{
                        showMessageDialog(null, "Incorrect installment data");
                    }
                }else{
                    showMessageDialog(null, "Incorrect installment data");
                }
            }
        });

        contentPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (contentPane.getSelectedIndex()==2) {
                    events = eventService.getAll();
                    installmentEventsCB.removeAllItems();
                    events.forEach(el->{
                        installmentEventsCB.addItem(el.getName());
                    });
                }
                if(contentPane.getSelectedIndex()==3){
                    events = eventService.getAll();

                    persons = personService.getAll();
                    events.forEach(el->{
                        paymentEventsCB.addItem(el.getId()+" "+el.getName());
                    });

                    persons.forEach(el->{
                        paymentPersonsCB.addItem(el.getId()+" "+el.getName()+" "+el.getSurname());
                    });
                }

            }
        });


        delPersonBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int rowId;
                if( (rowId = personsTable.getSelectedRow())!= -1){
                    int id = (int) personsTable.getValueAt(rowId,0);
                    Person person = persons.stream()
                            .filter(el -> el.getId()==id)
                            .findFirst().get();

                    personService.delete(person);
                    personModel.removeRow(rowId);
                }
            }
        });

        delEventBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int rowId;
                if( (rowId = eventsTable.getSelectedRow())!= -1){
                    int id = (int) eventsTable.getValueAt(rowId,0);
                    Event event = events.stream()
                            .filter(el -> el.getId()==id)
                            .findFirst().get();
                    eventService.delete(event);
                    eventModel.removeRow(rowId);
                }
            }
        });
        delInstallmentBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int rowId;
                if( (rowId = installmentsTable.getSelectedRow())!= -1){
                    int id = (int) installmentsTable.getValueAt(rowId,0);
                    Installment installment = installments.stream()
                            .filter(el -> el.getId()==id)
                            .findFirst().get();
                    installmentService.delete(installment);
                    installmentModel.removeRow(rowId);
                }
            }
        });

        delPaymentBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int rowId;
                if( (rowId = paymentsTable.getSelectedRow())!= -1){
                    int id = (int) paymentsTable.getValueAt(rowId,0);
                    Payment payment = payments.stream()
                            .filter(el -> el.getId()==id)
                            .findFirst().get();

                    paymentService.delete(payment);
                    paymentModel.removeRow(rowId);
                }
            }
        });

        editPersonBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int rowId = personsTable.getSelectedRow();
                int id = (int) personsTable.getValueAt(rowId,0);
                String name = (String) personsTable.getValueAt(rowId,1);
                String surname = (String) personsTable.getValueAt(rowId,2);
                if(!name.isEmpty()&&!surname.isEmpty())
                    personService.update(new Person(id,name,surname),new String[]{name,surname});
            }
        });
        editEventBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int rowId = eventsTable.getSelectedRow();
                int id = (int) eventsTable.getValueAt(rowId,0);
                String name = (String) eventsTable.getValueAt(rowId,1);
                String place = (String) eventsTable.getValueAt(rowId,2);
                String strDate = String.valueOf(eventsTable.getValueAt(rowId,3));
                if(!name.isEmpty()&&!place.isEmpty()&&validateDate(strDate)){
                    eventService.update(new Event(id,name,place, Date.valueOf(strDate)),new String[]{});
                }
            }
        });

        editInstallmentBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int rowId = installmentsTable.getSelectedRow();
                int id = Integer.parseInt(String.valueOf(installmentsTable.getValueAt(rowId,0)));
                int eventId = Integer.parseInt(String.valueOf(installmentsTable.getValueAt(rowId,1)));
                int number = Integer.parseInt(String.valueOf(installmentsTable.getValueAt(rowId,2)));
                String strDeadline = String.valueOf(installmentsTable.getValueAt(rowId,3));
                double amount = Double.parseDouble(String.valueOf(installmentsTable.getValueAt(rowId,4)));
                if(eventId!=0&&number>=0&&validateDate(strDeadline)&&amount!=0){
                    installmentService.update(new Installment(id,eventId,number, Date.valueOf(strDeadline),amount),new String[]{});
                }

            }
        });
    }

    public void updateTable(DefaultTableModel model, int rows){
        model.setRowCount(0);
        Object[][] data = new Object[rows][model.getColumnCount()];

        if(model.equals(personModel) && !persons.isEmpty()){
            persons.forEach(e->{
                personModel.addRow(new Object[]{e.getId(), e.getName(), e.getSurname()});
            });
        }else if(model.equals(eventModel)){
            events.forEach(e->{
                eventModel.addRow(new Object[]{e.getId(), e.getName(), e.getPlace(), e.getDate()});
            });
        }else if(model.equals(paymentModel)){
            payments.forEach(e->{
                paymentModel.addRow(new Object[]{e.getId(),e.getDate(),e.getAmount(),e.getPersonId(),e.getInstallmentNumber()});
            });
        }else if(model.equals(installmentModel)){
            installments.forEach(e->{
                installmentModel.addRow(new Object[]{e.getId(),e.getEventId(),e.getNumber(),e.getDeadline(),e.getAmount()});
            });
        }

    }
    public boolean validateDate(String date)  {
        DateFormat sdf = new SimpleDateFormat(dateFormat);;

        sdf.setLenient(false);
        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;

    }

    public int getId(String item){
        StringBuilder id = new StringBuilder(item);
        for (int i = 0;i<item.length();i++){
            if(item.charAt(i)==' '){
                return Integer.parseInt(String.valueOf(id.delete(i,item.length())));
            }
        }
        return 0;
    }

    public String getValue(String item){
        StringBuilder amount = new StringBuilder(item);
        for (int i = 0;i<item.length();i++){
            if(item.charAt(i)==' '){
                return String.valueOf(amount.delete(0,i));
            }
        }
        return "";
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setContentPane(new MainWindow().contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750,600);
        frame.setVisible(true);
    }

}
