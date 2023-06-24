package zad1;

import javax.jms.*;
import javax.naming.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;



public class ChatUser {

    private String nick;
    private TopicConnectionFactory topicConnectionFactory;
    private Topic topic;
    private BufferedReader bufferedReader;
    private  JTextArea chatArea;
    private JTextField inputField;
    private TopicConnection topicConnection;
    private TopicSession topicSession;
    private TopicPublisher topicPublisher;

    public ChatUser(String nick) {
        this.nick = nick;
    }

    public void run() {
        JFrame frame = new JFrame(nick +" chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        panel.add(chatArea, BorderLayout.CENTER);

        inputField = new JTextField();
        panel.add(inputField, BorderLayout.SOUTH);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    try {
                        sendMessage(nick, message);
                    } catch (JMSException ex) {
                        ex.printStackTrace();
                    }
                    inputField.setText("");
                }
            }
        });
        panel.add(sendButton, BorderLayout.EAST);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

        Hashtable<String, String> properties = setJndi();
        chatArea.append("Chat of user: " + nick + "\n");
        try {
            TopicSubscriber tsubscriber = setup(properties);
            topicThread(tsubscriber);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close() {
        try {
            if (topicConnection != null) {
                topicConnection.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
    }

    private void topicThread(TopicSubscriber tsubscriber) throws JMSException, IOException, InterruptedException {
        String msg = "";
        topicConnection.start();

        do {
            if (!msg.isEmpty()) {
                sendMessage(nick, msg);
            }
            Thread.sleep(100);
            getMessage(tsubscriber, nick);
        } while (!"bye".equals(msg = bufferedReader.readLine()));

        chatArea.append("User left the chat\n");
        topicConnection.close();
    }

    private TopicSubscriber setup(Hashtable<String, String> properties)
            throws NamingException, JMSException {
        Context context = new InitialContext(properties);
        topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
        topicConnection = topicConnectionFactory.createTopicConnection();
        topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        topic = (Topic) context.lookup("topic1");
        topicPublisher = topicSession.createPublisher(topic);
        TopicSubscriber tsubscriber = topicSession.createSubscriber(topic);
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        return tsubscriber;
    }

    private Hashtable<String, String> setJndi() {
        Hashtable<String, String> properties = new Hashtable<>();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.exolab.jms.jndi.InitialContextFactory");
        properties.put(Context.PROVIDER_URL, "tcp://localhost:3035/");
        return properties;
    }

    private void sendMessage(String nick, String msg) throws JMSException {
        topicPublisher.publish(topicSession.createTextMessage(nick + " [" + getTime() + "]: " + msg));
    }

    private  void getMessage(TopicSubscriber topicSubscriber, String nick) throws JMSException {
        topicSubscriber.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    chatArea.append(textMessage.getText() + "\n");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }

    public static void main(String[] args) {
        ChatUser chatUser = new ChatUser("jakub");
        chatUser.run();
    }
}