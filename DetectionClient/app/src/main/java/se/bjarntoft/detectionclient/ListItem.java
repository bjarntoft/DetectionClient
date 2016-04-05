package se.bjarntoft.detectionclient;

/**
 * Created by Andreas on 2016-04-05.
 */
public class ListItem {
    private String name;
    private String status;


    public ListItem(String name, String status) {
        this.name = name;
        this.status = status;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getName() {
        return name;
    }


    public String getStatus() {
        return status;
    }
}
