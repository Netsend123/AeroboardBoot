package aero.board;

import aero.board.model.DbObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class JsonFromSearchList {

    JSONObject jsonObject = null;
    JSONArray listOfSearsh = new JSONArray();

    public String parse(List<DbObject> list) {

        int i = 0;
        while (i < list.size()) {
            JSONObject obj = new JSONObject();
            String id = String.valueOf(list.get(i).getId());
            String searchRequest = list.get(i).getSearchRequest();
            String airportName = list.get(i).getAirportName();
            String data = list.get(i).getData();

            obj.put("id", id);
            obj.put("searchRequest", searchRequest);
            obj.put("airportName", airportName);
            obj.put("data", data);

            listOfSearsh.add(i, obj);

            i++;

        }
        System.out.println(listOfSearsh);
        return String.valueOf(listOfSearsh);

    }

}
