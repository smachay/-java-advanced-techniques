package serviceloader.impl;

import com.google.auto.service.AutoService;
import ex.api.ClusterAnalysisService;
import ex.api.ClusteringException;
import ex.api.DataSet;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

@AutoService(ClusterAnalysisService.class)
public class CountStandardDeviation implements ClusterAnalysisService, Runnable{
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
        return "Odchylenie standardowe";
    }

    @Override
    public void submit(DataSet ds) throws ClusteringException {
        if(t.isAlive()) {
            throw new ClusteringException("Przetwarzanie danych nadal trwa!");
        }
        t = new Thread(this);
        dataSet = ds;
        //printData(dataSet);
        t.start();
    }

    @Override
    public void run() {
        ArrayList<String[]> rows = new ArrayList<>();
        int lng = 1;

        for (String[] row : dataSet.getData()) {
            String[] newRow = row;
            lng = row.length;
            Float[] rowData = Arrays.stream(Arrays.copyOfRange(row,1,lng-1))
                    .map(num -> (num == null ? 0 : Float.parseFloat(num)))
                    .toArray(Float[]::new);

            float rowAvg = Arrays.stream(rowData)
                    .reduce((float) 0, Float::sum)/(lng-2);

            float result = (float)sqrt(Arrays.stream(rowData).map(num-> (float)pow((num-rowAvg),2))
                    .reduce((float) 0, Float::sum)/(lng-2));

            newRow[lng-1]= String.valueOf(result);
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
        //printData(dataSet);
        return dataSet;
    }


}
