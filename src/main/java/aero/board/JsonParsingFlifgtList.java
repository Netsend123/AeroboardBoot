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
        time = time.split("\\+")[0]; // убираем часовой пояс и год из строки с датой
        time = time.split(" ")[1];
        return time;
    }

    public long timeDiff (String timeStart, String timeFinish){ // вычиление разницы в минутах между двумя значениями времени
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
        long elapsedms = date1.getTime() - date2.getTime();
        long diff = TimeUnit.MINUTES.convert(elapsedms, TimeUnit.MILLISECONDS);
        return diff;
    }
    public String flightDuration(String takeOf, String takeOn) { // вычисление продолжительности полета
        if (takeOf == null || takeOn == null) return "";
        takeOf = takeOf.substring(0, takeOf.length() - 1);
        takeOn = takeOn.substring(0, takeOn.length() - 1);
        long diff = timeDiff(takeOf, takeOn);
        long hours = diff / 60;
        long min = diff % 60;
        return hours + "h" + min + "m";
    }

    public String delay(String timeScheduled, String timeReal) { // вычисление задержки прилета/вылета
        timeScheduled = timeScheduled.split("\\+")[0];
        timeReal = timeReal.split("\\+")[0];
        long diff = timeDiff(timeReal,timeScheduled);
        String stdiff = String.valueOf(diff);
        if (diff > 0) {
            stdiff = "+" + stdiff;
        }
        return stdiff;
    }

    public String parse(String jsonAeroLine) { // создание нового json для вывода в таблицу в браузер
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
        JSONArray arrivals = (JSONArray) jsonObject.get("arrivals"); // из одного json объекта получаем 2 отдельных массива
        JSONArray departures = (JSONArray) jsonObject.get("departures");

        int i = 0;

        JSONArray listOfBoard = new JSONArray();

        if (inOut.equals("arrivals")) {
            while (i < arrivals.size()) {
                JSONObject obj = new JSONObject();
                JSONObject resive = (JSONObject) arrivals.get(i);

                String timeActual = (JsonPath.from(String.valueOf(resive)).getString("arrival.actualTimeLocal")); // время призелмения фактическое
                String timeScheduled = (JsonPath.from(String.valueOf(resive)).getString("arrival.scheduledTimeLocal")); // время приземления по расписанию
                if (timeActual != null) { //если самолет приземлися то вычисляем разницу мжеду расписанием и фактическим

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
            }
        }
        if (inOut.equals("departures")) {

            while (i < departures.size()) {
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

                obj.put("time", timeActual);
                obj.put("timeScheduled", timeScheduled);
                obj.put("timeDuration", timeDuration);
                obj.put("timeDurationActual", timeDurationActual);
                obj.put("aeroport", JsonPath.from(String.valueOf(resive)).getString("arrival.airport.name"));
                obj.put("number", JsonPath.from(String.valueOf(resive)).getString("number"));
                obj.put("status", JsonPath.from(String.valueOf(resive)).getString("status"));
                obj.put("company", JsonPath.from(String.valueOf(resive)).getString("airline.name"));
                obj.put("aircraft", JsonPath.from(String.valueOf(resive)).getString("aircraft.model"));

                listOfBoard.add(i, obj);

                i++;
            }
        }
        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("Прошло времени, мс: " + elapsed);
        return String.valueOf(listOfBoard);
    }
}
