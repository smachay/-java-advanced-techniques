package com.dataservice;

import com.jayway.jsonpath.JsonPath;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


public class DataService {

    private static HashMap<String,Integer> countries;

    public DataService(){
        countries = new HashMap<>();
    }

    public static Boolean countriesRequest(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.teleport.org/api/countries/")).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(DataService::parseCountries)
                .join();
        return true;
    }

    public static int countryRequest(String uri){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return parsePopulation(response.body());
    }

    private static int parsePopulation(String responseBody) {
        List<Integer> p = JsonPath.read(responseBody, "$..population");
        return p.get(0);
    }

    public static void parseCountries(String responseBody){
        List<String> names = JsonPath.read(responseBody, "$['_links']['country:items'][:6]['name']");
        List<String> links = JsonPath.read(responseBody, "$['_links']['country:items'][:6]['href']");
        for(int i = 0; i<names.size(); i++){
            if (countries.get(names.get(i)) == null) {
                int p = countryRequest(links.get(i));
                countries.put(names.get(i),p);
            }

        }
    }

    public static HashMap<String,Integer> getCountries(){
        return countries;
    }

}
