import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import retrofit.RestAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by harshit on 23/5/16.
 */
public final class CricketRetrofitAdapter {

    private CricketInterface cricketInterface;

    private CricketRetrofitAdapter() {
        createCricketInterface();
    }

    public static CricketRetrofitAdapter getCricketRetrofitAdapter() {
        return cricketRetrofitAdapter;
    }

    public static final CricketRetrofitAdapter cricketRetrofitAdapter = new CricketRetrofitAdapter();

    private void createCricketInterface() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://cricapp-1206.appspot.com")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        cricketInterface = restAdapter.create(CricketInterface.class);
    }

    DefaultCacheManager cacheManager = new DefaultCacheManager();

    Cache<String, List<CricketMatchListModel>> cricketMatchList = cacheManager.getCache("MatchList");
    Cache<Integer, CricketMatchModel> cricketMatchModelCache = cacheManager.getCache("Match");
    public List<CricketMatchListModel> getCricketMatchList() {
        List<CricketMatchListModel> cricketMatchListModels = cricketMatchList.get("Match");
        if (cricketMatchListModels == null) {
            cricketMatchListModels = cricketInterface.getMatchSummary();
            cricketMatchList.put("Match", cricketMatchListModels, 5, TimeUnit.MINUTES);
        }
        return cricketMatchListModels;
    }

    public CricketMatchModel getCricketMatchModel(int matchId) {
        CricketMatchModel cricketMatchModel = cricketMatchModelCache.get(matchId);
        if (cricketMatchModel == null) {
            cricketMatchModel = cricketInterface.getCricketMatch(matchId);
            cricketMatchModelCache.put(matchId, cricketMatchModel, 30, TimeUnit.SECONDS);
        }
        return cricketMatchModel;
    }
}
