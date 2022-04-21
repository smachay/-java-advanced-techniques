package com.ui;

import com.dataservice.DataService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UI extends JFrame implements ActionListener {

    DataService dataService = new DataService();
    HashMap<String,Integer> countries;
    Boolean dataIsLoaded = false;
    JLabel question, score;
    JButton firstAnswer,secondAnswer;
    String correctAnswer;
    static int count = 0;

    public UI(){
        dataIsLoaded = dataService.countriesRequest();
        while (!dataIsLoaded){}

        countries = DataService.getCountries();

        question = new JLabel();
        question.setText("Which country has a larger population?");
        question.setBounds(100,30,300,30);
        this.add(question);

        firstAnswer = new JButton();
        firstAnswer.addActionListener(this);
        firstAnswer.setBounds(100, 90,200,30);
        this.add(firstAnswer);

        secondAnswer = new JButton();
        secondAnswer.addActionListener(this);
        secondAnswer.setBounds(100, 140,200,30);
        this.add(secondAnswer);

        score = new JLabel();
        score.setBounds(100,190,300,30);
        this.add(score);

        loadAnswers();

        this.setSize(400,300);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }



    public int[] drawCountry(){
        Random rand = new Random();
        int id1 = -1, id2 = -1;
        while(id1 == id2 ){
            id1 = rand.nextInt(countries.size());
            id2 = rand.nextInt(countries.size());
        }
       return new int[]{id1, id2};
    }
    ArrayList<String> getCountryById(int id){
        int i = 0;
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e: countries.entrySet())
        {
            if(i == id){
                result.add(e.getKey());
                result.add(String.valueOf(e.getValue()));
                return result;
            }
            i++;
        }
        return null;
    }

    private void loadAnswers() {
        int[] ids =  drawCountry();
        ArrayList<String> fAnswer = getCountryById(ids[0]);
        ArrayList<String>  sAnswer = getCountryById(ids[1]);

        firstAnswer.setText(fAnswer.get(0));
        secondAnswer.setText(sAnswer.get(0));

        if(Integer.parseInt(fAnswer.get(1)) > Integer.parseInt(sAnswer.get(1))){
            correctAnswer = fAnswer.get(0);
        }else{
            correctAnswer = sAnswer.get(0);
        }


        score.setText("Score:"+count);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(correctAnswer == ((JButton) e.getSource()).getText() )
            count++;
        loadAnswers();
    }
}
