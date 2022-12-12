package aero.board;


import aero.board.model.Airport;
import io.restassured.path.json.JsonPath;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class SearchAeroport {

    public List<Airport> airportList = new ArrayList<>();
    public String icao;

    public void searchAeroportToAPI(String city) { //формируем на основе полученой строки поиска Api запрос, на поиск нужного аэропорта
        java.net.http.HttpRequest request;

       // searchDAO.saveSearch(city);
        request = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create("https://aerodatabox.p.rapidapi.com/airports/search/term?q=" + city + "&limit=10"))
                .header("X-RapidAPI-Key", "2ea3972599mshf337a6ce1622083p183087jsn9b1ac0ee54b4")
                .header("X-RapidAPI-Host", "aerodatabox.p.rapidapi.com")
                .method("GET", java.net.http.HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(request);
        icao = response.body();
    }

    public String getIcao() {
        return icao;
    }

    public void parse(String city) { //парсинг json ответа. на выходе получаем код icao нужного аэропорта для дальнейшей вставки в Api запрос списка рейсов по этому аэропрту
        searchAeroportToAPI(city);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) jsonParser.parse(icao);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int i = 0;

        while (JsonPath.from(String.valueOf(jsonObject)).getString("items[" + i + "].name") != null) { //формируем список аэропортов из результатов поиска
            String name = (JsonPath.from(String.valueOf(jsonObject)).getString("items[" + i + "].name"));
            String icao = (JsonPath.from(String.valueOf(jsonObject)).getString("items[" + i + "].icao"));
            airportList.add(new Airport(name, icao));
            i++;
        }
    }
}

