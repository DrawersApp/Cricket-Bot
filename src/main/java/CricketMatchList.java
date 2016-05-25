import org.drawers.bot.lib.DrawersBotString;
import org.drawers.bot.lib.Operation;
import org.drawers.bot.lib.Response;

import java.util.List;

/**
 * Created by harshit on 23/5/16.
 */
public class CricketMatchList implements UserOperation {

    @Override
    public Response operateInternal(DrawersBotString drawersBotString) {
        return null;
    }

    @Override
    public boolean validateAndParse(DrawersBotString drawersBotString) {
        return false;
    }

    @Override
    public Response operateUser(String userId, DrawersBotString body) {
        List<CricketMatchListModel> cricketMatchListModel = CricketRetrofitAdapter.getCricketRetrofitAdapter().getCricketMatchList();
        return new CricketMatchListResponse(cricketMatchListModel);
    }

    class CricketMatchListResponse implements Response {

        private List<CricketMatchListModel> cricketMatchListModels;

        public CricketMatchListResponse(List<CricketMatchListModel> cricketMatchListModels) {
            this.cricketMatchListModels = cricketMatchListModels;
        }
        @Override
        public String toUserString() {
            StringBuilder outputBuilder = new StringBuilder();
            for (CricketMatchListModel cricketMatchListModel : cricketMatchListModels) {
                outputBuilder.append(cricketMatchListModel.toString()).append("\n");
            }
            return outputBuilder.toString();
        }
    }
}
