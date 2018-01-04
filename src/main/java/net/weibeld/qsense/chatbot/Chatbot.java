package net.weibeld.qsense.chatbot;

import java.io.IOException;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

public class Chatbot {

    private static final String BOT_NAME = "quanty";
    private static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
    private static final String CHANNEL_NAME = "general";

    private static String mBotId;
    private static SlackSession mSession;

    public static void main(String[] args) {
        connect();
        mSession.addMessagePostedListener((SlackMessagePosted e, SlackSession s) -> {
            if (e.getChannel().getName().equals(CHANNEL_NAME) && e.getMessageContent().contains(mBotId)) {
                sayHello(e, s);
            }
        });
    }

    private static void sayHello(SlackMessagePosted event, SlackSession session) {
        session.sendMessage(event.getChannel(), "Hello " + event.getSender().getRealName() + ".");
    }

    private static void connect() {
        mSession = SlackSessionFactory.createWebSocketSlackSession(AUTH_TOKEN);
        try {
            mSession.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBotId = "<@" + mSession.findUserByUserName(BOT_NAME).getId() + ">";
    }

}
