package net.weibeld.qs.chatbot;

import java.io.IOException;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

class SlackManager {

    private static SlackManager instance = null;

    private final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
    private final String CHANNEL_NAME = "general";
    private final String BOT_NAME = "quanty";

    private String mBotId;
    private SlackSession mSession;
    private SlackChannel mChannel;

    protected SlackManager() {}

    public static SlackManager getInstance() {
        if(instance == null) {
            instance = new SlackManager();
        }
        return instance;
    }

    /**
     * Connect to Slack (i.e. get online).
     */
    public void connect() {
        mSession = SlackSessionFactory.createWebSocketSlackSession(AUTH_TOKEN);
        try {
            mSession.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Get the channel the chatbot is listening on
        mChannel = mSession.getChannels().stream()
            .filter(channel -> channel.getName() != null)
            .filter(channel -> channel.getName().equals(CHANNEL_NAME))
            .findFirst()
            .get();
        mBotId = "<@" + mSession.findUserByUserName(BOT_NAME).getId() + ">";
    }

    /**
     * Send a message to the defined channel.
     */
    public void send(String msg) {
        mSession.sendMessage(mChannel, msg);
    }

    /**
     * Add a listener for handling messages addressed at the chatbot.
     *
     * Messages addressed at the chatbot are messages that are (a) mentioning
     * the chatbot ("@ChatbotName"), and (b) sent in the defined channel.
     */
    public void addMessageListener(SlackMessageListener listener) {
        mSession.addMessagePostedListener((SlackMessagePosted e, SlackSession s) -> {
            String msg = e.getMessageContent();
            if (e.getChannel().getName().equals(CHANNEL_NAME) && msg.contains(mBotId)) {
                String sender = e.getSender().getRealName();
                listener.onReceive(msg, sender);
            }
        });
    }

    /**
     * To be implemented by subscribers for messages addresses at the chatbot.
     */
    interface SlackMessageListener {
        void onReceive(String msg, String sender);
    }

}
