import com.drawers.dao.ChatConstant;
import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.packets.MqttChat;
import com.drawers.dao.packets.listeners.NewMessageListener;
import org.drawers.bot.lib.*;
import org.drawers.bot.mqtt.DrawersBot;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by harshit on 23/5/16.
 */
public class CricketMessageListener implements NewMessageListener {
    private final DrawersBot bot;
    private final String clientId;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CricketMessageListener(DrawersBot bot, String clientId) {
        this.bot = bot;
        this.clientId = clientId;
    }

    @Override
    public void receiveMessage(MqttChatMessage mqttChatMessage) {
        executorService.submit((Runnable) () -> {
            DrawersBotString drawersBotString = DrawersBotString.fromString(mqttChatMessage.message);
            Response response = operations.get(drawersBotString.getOperationsType()).operateUser(mqttChatMessage.senderUid, drawersBotString);
            MqttChat mqttChat = new MqttChat(mqttChatMessage.senderUid, UUID.randomUUID().toString(), response.toUserString(), false, ChatConstant.ChatType.TEXT, clientId);
            mqttChat.sendStanza(bot);
        });

    }

    @Override
    public void acknowledgeStanza(MqttChatMessage mqttChatMessage) {

    }

    private enum OperationChuck {
        CricketMatchList,
        CricketMatchScore,
        Subscribe,
        UnSubscribe;
    }

    private static DrawersBotString cricketMatchList;
    private static DrawersBotString cricketMatchScore;
    private static DrawersBotString subscribe;
    private static DrawersBotString unsubscribe;
    private static Map<String, UserOperation> operations = new HashMap<>();

    static {
        cricketMatchList = new DrawersBotString(
                Collections.singletonList(new BotStringElement(BotStringType.U, "CricketMatchList", null)),
                OperationChuck.CricketMatchList.name());
        List<BotStringElement> botStringElements = new ArrayList<>();
        botStringElements.add(new BotStringElement(BotStringType.U, "Score", "Match"));
        botStringElements.add(new BotStringElement(BotStringType.S, "id", null));
        cricketMatchScore = new DrawersBotString(botStringElements,
                OperationChuck.CricketMatchScore.name());

        botStringElements = new ArrayList<>();
        botStringElements.add(new BotStringElement(BotStringType.U, "Subscribe", "Subscribe"));
        botStringElements.add(new BotStringElement(BotStringType.S, "match id", null));
        subscribe = new DrawersBotString(botStringElements,
                OperationChuck.Subscribe.name());
        botStringElements = new ArrayList<>();
        botStringElements.add(new BotStringElement(BotStringType.U, "UnSubscribe", "UnSubscribe"));
        botStringElements.add(new BotStringElement(BotStringType.S, "match id", null));
        unsubscribe = new DrawersBotString(botStringElements, OperationChuck.UnSubscribe.name());
        operations.put(OperationChuck.CricketMatchList.name(), new CricketMatchList());
        operations.put(OperationChuck.CricketMatchScore.name(), new CricketMatchScore());
        operations.put(OperationChuck.Subscribe.name(), new Subscribe());
        operations.put(OperationChuck.UnSubscribe.name(), new UnSubscribe());
        DrawersBotStringHelp.getDrawersBotStringHelp().getDrawersBotStrings().add(cricketMatchList);
        DrawersBotStringHelp.getDrawersBotStringHelp().getDrawersBotStrings().add(cricketMatchScore);
        DrawersBotStringHelp.getDrawersBotStringHelp().getDrawersBotStrings().add(subscribe);
        DrawersBotStringHelp.getDrawersBotStringHelp().getDrawersBotStrings().add(unsubscribe);
        System.out.println(DrawersBotStringHelp.getDrawersBotStringHelp().toJsonString());

    }
}
