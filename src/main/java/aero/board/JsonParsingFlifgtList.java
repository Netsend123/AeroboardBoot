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

    public String dateCorrect(String time) {
        time = time.split("\\+")[0]; // убираем часовой пояс и год из даты
        time = time.split(" ")[1];
        return time;
    }

    public String parse(String jsonAeroLine) {
        long start = System.currentTimeMillis();

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
        JSONArray arrivals = (JSONArray) jsonObject.get("arrivals");
        JSONArray departures = (JSONArray) jsonObject.get("departures");

        int i = 0;

        JSONArray listOfBoard = new JSONArray();

        if (inOut.equals("arrivals")) {
            while (i < arrivals.size()) {
                long st = System.currentTimeMillis();
                JSONObject obj = new JSONObject();
                JSONObject resive = (JSONObject) arrivals.get(i);

                String time = (JsonPath.from(String.valueOf(resive)).getString("arrival.actualTimeLocal"));

                if (time == null) {
                    time = (JsonPath.from(String.valueOf(resive)).getString("arrival.scheduledTimeLocal"));
                }
                time = dateCorrect(time);


                obj.put("time", time);
                obj.put("aeroport", JsonPath.from(String.valueOf(resive)).getString("departure.airport.name"));
                obj.put("number", JsonPath.from(String.valueOf(resive)).getString("number"));
                obj.put("status", JsonPath.from(String.valueOf(resive)).getString("status"));
                obj.put("company", JsonPath.from(String.valueOf(resive)).getString("airline.name"));
                obj.put("aircraft", JsonPath.from(String.valueOf(resive)).getString("aircraft.model"));

                listOfBoard.add(i, obj);

                i++;
                long finish = System.currentTimeMillis();
                long elapsed = finish - st;
                System.out.println(elapsed);

            }
        }
        if (inOut.equals("departures")) {

            while (i < departures.size()) {
                long st = System.currentTimeMillis();
                JSONObject obj = new JSONObject();
                JSONObject resive = (JSONObject) departures.get(i);
                String time = (JsonPath.from(String.valueOf(resive)).getString("departure.actualTimeLocal"));
                String aeroport = (JsonPath.from(String.valueOf(resive)).getString("arrival.airport.name"));
                String number = (JsonPath.from(String.valueOf(resive)).getString("number"));
                String status = (JsonPath.from(String.valueOf(resive)).getString("status"));
                String company = (JsonPath.from(String.valueOf(resive)).getString("airline.name"));
                String aircraft = (JsonPath.from(String.valueOf(resive)).getString("aircraft.model"));

                if (time == null) {
                    time = (JsonPath.from(String.valueOf(resive)).getString("departure.scheduledTimeLocal"));
                }
                time = dateCorrect(time);


                obj.put("time", time);
                obj.put("aeroport", aeroport);
                obj.put("number", number);
                obj.put("status", status);
                obj.put("company", company);
                obj.put("aircraft", aircraft);

                listOfBoard.add(i, obj);

                i++;
                long finish = System.currentTimeMillis();
                long elapsed = finish - st;
                System.out.println(elapsed);
            }
        }
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("Прошло времени, мс: " + elapsed);
        return String.valueOf(listOfBoard);
    }
}
