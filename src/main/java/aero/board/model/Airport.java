package aero.board.model;


import lombok.Data;

@Data
public class Airport {
    String name;
    String icao;

    public Airport(String name, String icao) {
        this.name = name;
        this.icao = icao;
    }

    public String getIcao() {
        return icao;
    }

    public String getName() {
        return name;
    }
}
