package org.example.globetrotterapplication.service;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.example.globetrotterapplication.api.model.Destination;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DestinationService {

    private final List<Destination> destinations;

    public DestinationService() throws IOException {

        String jsonInput = FileUtils.readFileToString(new File("src/main/resources/static/data.json"), StandardCharsets.UTF_8);
        destinations = new ArrayList<>();
        JSONArray destinationsJsonArray = new JSONArray(jsonInput);

        int size = destinationsJsonArray.length();
        for (int i = 0; i < size; i++) {
            JSONObject destinationJson = destinationsJsonArray.getJSONObject(i);

            String city = destinationJson.getString("city");
            String country = destinationJson.getString("country");
            List<String> clues = jsonArrayToList(destinationJson.getJSONArray("clues"));
            List<String> funFact = jsonArrayToList(destinationJson.getJSONArray("fun_fact"));
            List<String> trivia = jsonArrayToList(destinationJson.getJSONArray("trivia"));

            Destination destination = new Destination(city, country, clues, funFact, trivia);
            destinations.add(destination);
        }

        for (int i = 0; i < size; i++) {
           Destination destination = destinations.get(i);
           List<String> incorrectOptions = getIncorrectOptions(i,3,size);
           int randomIndex = ThreadLocalRandom.current().nextInt(0, 4);
           incorrectOptions.add(randomIndex,destination.getCity());
           destination.setOptions(incorrectOptions);
        }

    }

    private List<String> getIncorrectOptions(Integer rightOption,Integer num,Integer destCount){
        List<String> incorrectOptions = new ArrayList<>();
        while (num > 0){
            int randomIndex = ThreadLocalRandom.current().nextInt(0, destCount);
            if(randomIndex != rightOption){
                incorrectOptions.add(destinations.get(randomIndex).getCity());
                num--;
            }

        }
        return incorrectOptions;
    }

    private List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    public Destination getDestination(int index) {
        return destinations.get(index);
    }
}
