package aero.board;

import org.junit.Assert;
import org.junit.Test;

public class SearchAirportTest {
    @Test
    public void searchairportTest() {
        SearchAeroport searchAeroport = new SearchAeroport();
        searchAeroport.parse("кольцово");
        if (searchAeroport.airportList.size() == 1 && searchAeroport.airportList.get(0).toString().equals("Airport(name=Ekaterinburg, Koltsovo, icao=USSS)"))
            System.out.println("Test search airport by string - pass");
        else System.out.println("Test search airport by string - fail");
    }
}
