package net.weibeld.qs.chatbot;

class Chatbot {

    public static void main(String[] args) {
        SlackManager slack = SlackManager.getInstance();
        slack.connect();
        slack.addMessageListener((String msg, String sender) -> {
            slack.send("Hello " + sender);
        });
    }

}
