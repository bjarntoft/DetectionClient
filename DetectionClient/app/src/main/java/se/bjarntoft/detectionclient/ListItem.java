package se.bjarntoft.detectionclient;

/**
 * Created by Andreas on 2016-04-05.
 */
public class ListItem {
    private String id;
    private String name;
    private String status;
    private String connected;


    public ListItem(String id, String name, String status, String connected) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.connected = connected;
    }


    public void setId(String id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public void setConnected(String connected) {
        this.connected = connected;
    }


    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getStatus() {
        return status;
    }


    public String getConnected() {
        return connected;
    }
}
