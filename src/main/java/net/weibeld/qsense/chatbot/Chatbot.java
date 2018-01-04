package net.weibeld.qsense.chatbot;

import java.io.IOException;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

public class Chatbot {

    private static String mBotName;
    private static String mAuthToken;
    private static String mChannelName;
    private static String mBotId;
    private static SlackSession mSession;

    public static void main(String[] args) {
        loadConfig();
        connect();
        mSession.addMessagePostedListener((SlackMessagePosted e, SlackSession s) -> {
            if (e.getChannel().getName().equals(mChannelName) && e.getMessageContent().contains(mBotId)) {
                //String msg = evt.getMessageContent();
                sayHello(e, s);
            }
        });
    }

    private static void sayHello(SlackMessagePosted event, SlackSession session) {
        session.sendMessage(event.getChannel(), "Hello " + event.getSender().getRealName() + ".");
    }

    private static void connect() {
        mSession = SlackSessionFactory.createWebSocketSlackSession(mAuthToken);
        try {
            mSession.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBotId = "<@" + mSession.findUserByUserName(mBotName).getId() + ">";
    }

    private static void loadConfig() {
        //botName = p.getProperty("botName");
        //authToken = p.getProperty("authToken");
        //channelName = p.getProperty("channelName");
        mBotName = "quanty";
        mAuthToken = "xoxb-280947785975-kZzzNnhqeZjppmzdRxQ6MKGV";
        mChannelName = "general";
    }
}
