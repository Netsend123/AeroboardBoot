package aero.board;

import io.restassured.path.json.JsonPath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    public long timeDiff (String timeStart, String timeFinish){
        String pattern = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(timeFinish);
            date2 = sdf.parse(timeStart);
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        // получаем разницу между двумя датами в минутах
        long elapsedms = date1.getTime() - date2.getTime();
        long diff = TimeUnit.MINUTES.convert(elapsedms, TimeUnit.MILLISECONDS);
        return diff;
    }
    public String flightDuration(String takeOf, String takeOn) {
        if (takeOf == null || takeOn == null) return "";
        takeOf = takeOf.substring(0, takeOf.length() - 1);
        takeOn = takeOn.substring(0, takeOn.length() - 1);
        long diff = timeDiff(takeOf, takeOn);
        long hours = diff / 60;
        long min = diff % 60;
        return hours + "h" + min + "m";
    }

    public String delay(String timeScheduled, String timeReal) {
        timeScheduled = timeScheduled.split("\\+")[0];
        timeReal = timeReal.split("\\+")[0];
        long diff = timeDiff(timeReal,timeScheduled);
        String stdiff = String.valueOf(diff);
        if (diff > 0) {
            stdiff = "+" + stdiff;
        }
        return stdiff;
    }

    public String parse(String jsonAeroLine) {
        long start = System.currentTimeMillis();
        System.out.println(jsonAeroLine);
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

                String timeActual = (JsonPath.from(String.valueOf(resive)).getString("arrival.actualTimeLocal"));
                String timeScheduled = (JsonPath.from(String.valueOf(resive)).getString("arrival.scheduledTimeLocal"));
                if (timeActual != null) {

                    timeActual = dateCorrect(timeActual) + "(" + delay(timeActual, timeScheduled) + ")";
                }
                else  {
                    timeActual = "no data";
                }

                String timeTakeOf = (JsonPath.from(String.valueOf(resive)).getString("departure.scheduledTimeUtc"));
                String timeTakeOn = (JsonPath.from(String.valueOf(resive)).getString("arrival.scheduledTimeUtc"));
                String timeDuration = flightDuration(timeTakeOf, timeTakeOn);
                String timeTakeOfActual = (JsonPath.from(String.valueOf(resive)).getString("departure.actualTimeUtc"));
                String timeTakeOnActual = (JsonPath.from(String.valueOf(resive)).getString("arrival.actualTimeUtc"));
                String timeDurationActual = flightDuration(timeTakeOfActual, timeTakeOnActual);
                timeScheduled = dateCorrect(timeScheduled);

                obj.put("time", timeActual);
                obj.put("timeScheduled", timeScheduled);
                obj.put("timeDuration", timeDuration);
                obj.put("timeDurationActual", timeDurationActual);
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

                String timeActual = (JsonPath.from(String.valueOf(resive)).getString("departure.actualTimeLocal"));
                String timeScheduled = (JsonPath.from(String.valueOf(resive)).getString("departure.scheduledTimeLocal"));
                if (timeActual != null) {

                    timeActual = dateCorrect(timeActual) + "(" + delay(timeActual, timeScheduled) + ")";
                }
                else  {
                    timeActual = "no data";
                }
                String timeTakeOf = (JsonPath.from(String.valueOf(resive)).getString("arrival.scheduledTimeUtc"));
                String timeTakeOn = (JsonPath.from(String.valueOf(resive)).getString("departure.scheduledTimeUtc"));
                String timeDuration = flightDuration(timeTakeOn, timeTakeOf);
                String timeDurationActual = "n/a";
                timeScheduled = dateCorrect(timeScheduled);

                //String time = (JsonPath.from(String.valueOf(resive)).getString("departure.actualTimeLocal"));
                //String timeArrival = (JsonPath.from(String.valueOf(resive)).getString("arrival.scheduledTimeLocal"));
                String aeroport = (JsonPath.from(String.valueOf(resive)).getString("arrival.airport.name"));
                String number = (JsonPath.from(String.valueOf(resive)).getString("number"));
                String status = (JsonPath.from(String.valueOf(resive)).getString("status"));
                String company = (JsonPath.from(String.valueOf(resive)).getString("airline.name"));
                String aircraft = (JsonPath.from(String.valueOf(resive)).getString("aircraft.model"));



                obj.put("time", timeActual);
                obj.put("timeScheduled", timeScheduled);
                obj.put("timeDuration", timeDuration);
                obj.put("timeDurationActual", timeDurationActual);
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
