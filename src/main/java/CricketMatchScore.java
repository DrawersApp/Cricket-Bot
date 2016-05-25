import org.drawers.bot.lib.BotStringElement;
import org.drawers.bot.lib.BotStringType;
import org.drawers.bot.lib.DrawersBotString;
import org.drawers.bot.lib.Response;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by harshit on 23/5/16.
 */
public class CricketMatchScore implements UserOperation {
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
        if (body.getBotStringElements().size() != 2) {
            throw new IllegalArgumentException("Incorrect no. of arguments");
        }
        BotStringElement matchIdElement = body.getBotStringElements().get(1);
        if (matchIdElement.getType() != BotStringType.S) {
            throw new IllegalArgumentException("Incorrect type");
        }
        int matchId = getValue(matchIdElement);
        if (matchId != -1) {
            CricketMatchModel cricketMatchModel = CricketRetrofitAdapter.getCricketRetrofitAdapter().getCricketMatchModel(matchId);
            return new CricketMatchScoreResponse(cricketMatchModel);
        }
        return new FailedResponse();
    }

    int getValue(BotStringElement botStringElement) {
        try {
            int number = Integer.parseInt(botStringElement.getText());
            return number;
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    public static class CricketMatchScoreResponse implements Response {
        public CricketMatchScoreResponse(CricketMatchModel cricketMatchScore) {
            this.cricketMatchScore = cricketMatchScore;
        }

        private CricketMatchModel cricketMatchScore;
        @Override
        public String toUserString() {
            return cricketMatchScore.toString();
        }
    }

    public static class FailedResponse implements Response {
        @Override
        public String toUserString() {
            return "Something went wrong";
        }
    }
}
