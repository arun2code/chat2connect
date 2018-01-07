package net.weibeld.qs.chatbot;

import java.io.IOException;
import java.net.URISyntaxException;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

class EasyCCGManager {

    private static EasyCCGManager instance;
    private final static String QUEUE = System.getenv("CHATBOT_TO_EASYCCG");

    private RpcClient rpcClient;

    protected EasyCCGManager() {}

    public static EasyCCGManager getInstance() {
        if (instance == null) instance = new EasyCCGManager();
        return instance;
    }

    /**
     * Establish a connection to the EasyCCG parser.
     */
    public void connect() {

        // Establish connection to RabbitMQ server
        String uri = System.getenv("CLOUDAMQP_URL");
        if (uri == null) uri = "amqp://guest:guest@localhost";
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(uri);
            Channel channel = factory.newConnection().createChannel();
            rpcClient = new RpcClient(channel, "", QUEUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Submit a sentence to parse to the EasyCCG parser and return the parse
     * tree as a string.
     */
    public String parse(String sentence) {;
        RpcClient.Response reply = null;
        try {
            reply = rpcClient.doCall(null, Util.string2bytes(sentence));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Util.bytes2string(reply.getBody());
    }

}
