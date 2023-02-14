package aero.board;

import aero.board.model.DbObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonFromSearchListTest {

    JsonFromSearchList jsonFromSearchList = new JsonFromSearchList();

    @Test
    void parse() {
        List<DbObject> list = new ArrayList<>();
        list.add(new DbObject("кольцово", "Ekaterinburg, Koltsovo"));
        String st1 = "[{\"searchRequest\":\"кольцово\",\"airportName\":\"Ekaterinburg, Koltsovo\",\"data\":";
        String  st2 = "\"id\":\"0\"}]";
        String resolt = jsonFromSearchList.parse(list);
        boolean test = ((resolt.contains(st1))&&(resolt.contains(st2)));
        assertEquals(true, test);

    }
}