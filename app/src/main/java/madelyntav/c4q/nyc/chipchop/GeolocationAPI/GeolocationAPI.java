package madelyntav.c4q.nyc.chipchop.GeolocationAPI;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by c4q-anthonyf on 8/16/15.
 */
public interface GeolocationAPI {

    @GET("/json")
    public void getGeolocation(@Query("address=") String queryString, Callback<Geolocation> response);

}
