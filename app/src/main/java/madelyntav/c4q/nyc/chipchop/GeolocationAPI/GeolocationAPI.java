package madelyntav.c4q.nyc.chipchop.GeolocationAPI;

import retrofit.Callback;
import retrofit.http.Query;

/**
 * Created by c4q-anthonyf on 8/16/15.
 */
public interface GeolocationAPI {

    @Query("address=")
    public void getGeolocation(Callback<Geolocation> response);

}
