package aero.board;

import io.restassured.path.json.JsonPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParsingFlifgtList {
    public String inOut = "departures";
    public String dataNotFound = "";
    public void setInOut(String inOut) {
        this.inOut = inOut;
    }



    public String parse(String jsonAeroLine) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        if (jsonAeroLine.isEmpty()) {
            System.out.println("Data not found");
        }

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonAeroLine);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer("");
        int i = 0;

        JSONArray listOfBoard = new JSONArray();

        if (inOut.equals("arrivals")) {
            while (JsonPath.from(String.valueOf(jsonObject)).getString("arrivals [" + i + "].number") != null) {
                JSONObject obj = new JSONObject();
                String time = (JsonPath.from(String.valueOf(jsonObject)).getString("arrivals [" + i + "].arrival.actualTimeLocal"));
                String aeroport = (JsonPath.from(String.valueOf(jsonObject)).getString("arrivals [" + i + "].departure.airport.name"));
                String number = (JsonPath.from(String.valueOf(jsonObject)).getString("arrivals [" + i + "].number"));
                String status = (JsonPath.from(String.valueOf(jsonObject)).getString("arrivals [" + i + "].status"));
                String company = (JsonPath.from(String.valueOf(jsonObject)).getString("arrivals [" + i + "].airline.name"));
                String aircraft = (JsonPath.from(String.valueOf(jsonObject)).getString("arrivals [" + i + "].aircraft.model"));

            if (time == null) {
                time = (JsonPath.from(String.valueOf(jsonObject)).getString("arrivals [" + i + "].arrival.scheduledTimeLocal"));}
                time = time.split("\\+")[0]; // убираем часовой пояс и год из даты
                time = time.split(" ")[1];


                obj.put("time", time);
                obj.put("aeroport", aeroport);
                obj.put("number", number);
                obj.put("status", status);
                obj.put("company", company);
                obj.put("aircraft", aircraft);

                listOfBoard.add(i, obj);

                i++;

            }
        }
        if (inOut.equals("departures")){
            while (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].number") != null) {
                JSONObject obj = new JSONObject();
                String time = (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].departure.actualTimeLocal"));
                String aeroport = (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].arrival.airport.name"));
                String number = (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].number"));
                String status = (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].status"));
                String company = (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].airline.name"));
                String aircraft = (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].aircraft.model"));

                if (time == null) {
                    time = (JsonPath.from(String.valueOf(jsonObject)).getString("departures[" + i + "].departure.scheduledTimeLocal"));}
                time = time.split("\\+")[0]; // убираем часовой пояс и год из даты
                time = time.split(" ")[1];


                obj.put("time", time);
                obj.put("aeroport", aeroport);
                obj.put("number", number);
                obj.put("status", status);
                obj.put("company", company);
                obj.put("aircraft", aircraft);

                listOfBoard.add(i, obj);

                i++;
            }
        }

        return String.valueOf(listOfBoard);
    }
}
