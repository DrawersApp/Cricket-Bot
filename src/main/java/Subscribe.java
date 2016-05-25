import org.drawers.bot.lib.*;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by harshit on 23/5/16.
 */
public class Subscribe implements UserOperation {

    public static Map<Integer, Set<String>> mapClient = new ConcurrentHashMap<>();

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
            if (!mapClient.containsKey(matchId)) {
                mapClient.put(matchId, new CopyOnWriteArraySet<>());
            }
            mapClient.get(matchId).add(userId);
            return new SubscribeResponse(matchId);
        }
        return new SubscribeFailedResponse();
    }

    int getValue(BotStringElement botStringElement) {
        try {
            int number = Integer.parseInt(botStringElement.getText());
            return number;
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    public static class SubscribeResponse implements Response {
        private int matchId;

        public SubscribeResponse(int matchId) {
            this.matchId = matchId;
        }

        @Override
        public String toUserString() {
            return "You are subscribed to" + matchId;
        }
    }

    public static class SubscribeFailedResponse implements Response {
        @Override
        public String toUserString() {
            return "Subscription failed";
        }
    }
}
