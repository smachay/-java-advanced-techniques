package ex.api;

/**
 * Interfejs serwisu pozwalającego przeprowadzić analizę skupień.
 * Zakładamy, że serwis działa asynchronicznie.
 * Na początek należy do serwisu załadować dane.
 * Potem można z serwisu pobrać wyniki analizy.
 * W przypadku niepowodzenia wykonania jakiejś metody wyrzucony zostanie wyjątek.
 *
 * @author tkubik
 *
 */
public interface ClusterAnalysisService {
    public void setOptions(String[] options) throws ClusteringException; // ustawia opcje
    // metoda zwracająca nazwę algorytmu
    public String getName();
    // metoda pozwalająca przekazać dane do analizy
    // wyrzuca wyjątek, jeśli aktualnie trwa przetwarzanie danych
    public void submit(DataSet ds) throws ClusteringException;
    // metoda pozwalająca pobrać wynik analizy
    // zwraca null - jeśli trwa jeszcze przetwarzanie lub nie przekazano danych do analizy
    // wyrzuca wyjątek - jeśli podczas przetwarzania doszło do jakichś błędów
    // clear = true - jeśli wyniki po pobraniu mają zniknąć z serwisu
    public DataSet retrieve(boolean clear) throws ClusteringException;
}

