package aero.board.model;



import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "airportlist")
public class DbObject {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "request")
    private String searchRequest;
    @Column(name = "nameairport")
    private String airportName;
    @Column(name = "date")
    private String data;


    public DbObject(String searchRequest, String airportName) {
        this.searchRequest = searchRequest;
        this.airportName = airportName;
        data = new Date().toString();
    }

    public DbObject() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(String searchRequest) {
        this.searchRequest = searchRequest;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
