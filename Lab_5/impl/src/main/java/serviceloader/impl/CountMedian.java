package serviceloader.impl;

import com.google.auto.service.AutoService;
import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

import java.util.ArrayList;
import java.util.Arrays;

@AutoService(ClusterAnalysisService.class)
public class CountMedian implements ClusterAnalysisService, Runnable{
    DataSet dataSet;
    Thread t = new Thread(this);
    boolean sortMax = false;

    void printData(DataSet ds){
        String[][] data = ds.getData();
        for (String[] row:data) {
            for (String record : row){
                System.out.print(record+" ");
            }
            System.out.println(" ");
        }
    }

    @Override
    public void setOptions(String[] options) throws ClusteringException {
        if (options[0].equals("true")) {
            sortMax = true;
        } else {
            sortMax = false;
        }
    }

    @Override
    public String getName() {
        return "Mediana";
    }

    @Override
    public void submit(DataSet ds) throws ClusteringException {
        if(t.isAlive()) {
            throw new ClusteringException("Przetwarzanie danych nadal trwa!");
        }
        t = new Thread(this);
        dataSet = ds;
        t.start();
    }

    @Override
    public void run() {
        ArrayList<String[]> rows = new ArrayList<>();
        int lng = 0;

        for (String[] row : dataSet.getData()) {
            lng = row.length;
            String[] newRow = row;

            var numRow = Arrays.stream(row)
                    .map(num -> (num == null ? 0 : Float.parseFloat(num)))
                    .toArray(Float[]::new);
            Arrays.sort(numRow,1,lng-1);

            if((lng-2)%2 == 0){
                float m1 = numRow[((lng-2)/2)];
                float m2 = numRow[((lng-2)/2)+1];
                newRow[lng-1] = String.valueOf((m1+m2)/2);

            }else{
                newRow[lng-1]= String.valueOf(numRow[((lng-2)/2)+1]);
            }

            rows.add(newRow);
        }


        String[][] newData = new String[rows.size()][lng];
        newData = rows.toArray(newData);

        dataSet.setData(newData);
    }

    @Override
    public DataSet retrieve(boolean clear) throws ClusteringException {
        if(t.isAlive()==true || dataSet == null){
            return null;
        }

        if(clear){
            dataSet = null;
        }

        return dataSet;
    }


}
