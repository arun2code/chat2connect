package net.weibeld.qs.chatbot;

class Chatbot {

    public static void main(String[] args) {
        SlackManager slack = SlackManager.getInstance();
        EasyCCGManager easyccg = EasyCCGManager.getInstance();
        slack.connect();
        easyccg.connect();
        slack.addMessageListener((String msg, String sender) -> {
            String sentence = getPayload(msg);
            String response = easyccg.parse(getPayload(msg));
            slack.send(response);
        });
    }

    private static String getPayload(String msg) {
        int delim = msg.indexOf(":");
        return msg.substring(delim + 1);
    }

}
