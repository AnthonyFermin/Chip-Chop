package madelyntav.c4q.nyc.chipchop.GeolocationAPI;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Geolocation {

    @Expose
    private List<Result> results = new ArrayList<Result>();
    @Expose
    private String status;

    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

}