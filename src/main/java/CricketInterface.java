import retrofit.http.GET;
import retrofit.http.Query;

import java.util.List;

/**
 * Created by harshit on 23/5/16.
 */
public interface CricketInterface {

    @GET("/csa")
    List<CricketMatchListModel> getMatchSummary();

    @GET("/csa")
    CricketMatchModel getCricketMatch(@Query("id") int id);
}
