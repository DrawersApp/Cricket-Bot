import org.drawers.bot.lib.BotStringElement;
import org.drawers.bot.lib.BotStringType;
import org.drawers.bot.lib.DrawersBotString;
import org.drawers.bot.lib.Response;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by harshit on 25/5/16.
 */
public class UnSubscribe implements UserOperation {
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
            if (!Subscribe.mapClient.containsKey(matchId)) {
                return new UnSubscribeResponse(matchId);
            }
            Subscribe.mapClient.get(matchId).remove(userId);
            return new UnSubscribeResponse(matchId);
        }
        return new UnSubscribeFailedResponse();
    }

    @Override
    public Response operateInternal(DrawersBotString drawersBotString) {
        return null;
    }

    @Override
    public boolean validateAndParse(DrawersBotString drawersBotString) {
        return false;
    }

    public static class UnSubscribeResponse implements Response {
        private int matchId;

        public UnSubscribeResponse(int matchId) {
            this.matchId = matchId;
        }

        @Override
        public String toUserString() {
            return "You are unsubscribed from" + matchId;
        }
    }

    int getValue(BotStringElement botStringElement) {
        try {
            int number = Integer.parseInt(botStringElement.getText());
            return number;
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    public static class UnSubscribeFailedResponse implements Response {
        @Override
        public String toUserString() {
            return "UnSubscription failed";
        }
    }
}
