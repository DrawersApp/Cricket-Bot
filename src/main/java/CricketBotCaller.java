import com.drawers.dao.ChatConstant;
import com.drawers.dao.MqttChatMessage;
import com.drawers.dao.packets.MqttChat;
import com.drawers.dao.packets.MqttProviderManager;
import com.drawers.dao.packets.SubscribeOthers;
import org.drawers.bot.listener.DrawersMessageListener;
import org.drawers.bot.mqtt.DrawersBot;
import org.drawers.bot.util.SendMail;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 3 actions - match list, match score and match score every 5 mins.
 */
public class CricketBotCaller implements DrawersMessageListener {
    private static DrawersBot bot;
    private static CricketBotCaller client;
    private MqttProviderManager mqttProviderManager;
    private String clientId;

    public CricketBotCaller(String clientId, String password) {
        bot = new DrawersBot(clientId, password, this);
        mqttProviderManager = MqttProviderManager.getInstanceFor(bot);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java DrawersClientCli <clientId> <password> <admin-email-id>");
        } else {
            String clientId = args[0];
            String password = args[1];
            String adminEmail = args[2];
            SendMail.getInstance().setAdminEmail(adminEmail);
            SendMail.getInstance().sendMail("Welcome to Drawers Bot", "Your bot is up and running now.");
            client = new CricketBotCaller(clientId, password);
            client.clientId = clientId;
            client.startBot();
        }
    }

    private void startBot() {
        mqttProviderManager.addMessageListener(new CricketMessageListener(bot, clientId));
        bot.start();
        timer.schedule(hourlyTask, 0l, 1000 * 10);

    }

    Timer timer = new Timer();
    TimerTask hourlyTask = new TimerTask() {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " sending joke");
            for (Integer integer : Subscribe.mapClient.keySet()) {
                String response = CricketRetrofitAdapter.getCricketRetrofitAdapter().getCricketMatchModel(integer).toString();
                for (String s : Subscribe.mapClient.get(integer)) {
                    MqttChat mqttChat = new MqttChat(s, UUID.randomUUID().toString(), response, false, ChatConstant.ChatType.TEXT, clientId);
                    mqttChat.sendStanza(bot);
                }
            }
        }
    };

    public void onConnected() {
        bot.subscribe(new SubscribeOthers(SubscribeOthers.OTHERS_NAMESPACE, clientId).getChannel(), 1, null, null);
    }

    public MqttChatMessage processMessageAndReply(MqttChatMessage message) {
        System.out.println("Received new message: " + message);
        return message;
    }
}
