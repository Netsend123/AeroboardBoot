package aero.board;

import aero.board.model.Airport;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchAeroportTest {

        SearchAeroport searchAeroport = new SearchAeroport();


    @Test
    void searchAeroportToAPI() {
        searchAeroport.searchAeroportToAPI("кольцово");
        String test = "{\"searchBy\":\"кольцово\",\"items\":[{\"icao\":\"USSS\",\"iata\":\"SVX\",\"localCode\":\"КЛЦ\",\"name\":\"Ekaterinburg, Koltsovo\",\"shortName\":\"Koltsovo\",\"municipalityName\":\"Ekaterinburg\",\"location\":{\"lat\":56.7431,\"lon\":60.8027},\"countryCode\":\"RU\"}]}";
        assertEquals(test, searchAeroport.icao);
    }

    @Test
    void parse() {
        SearchAeroport searchAeroport = new SearchAeroport();
        searchAeroport.parse("кольцово");
        List<Airport> list = new ArrayList<>();
        list.add(new Airport("Ekaterinburg, Koltsovo","USSS"));
        assertEquals(searchAeroport.airportList, list);
    }
}