package chatapplication;

import java.sql.*;

public class QueryCaller {

    private static Connection conn;

    private static PreparedStatement signupQuery ;
    private static PreparedStatement loginQuery;
    private static PreparedStatement getUserIdQuery;

    private static PreparedStatement createConversationQuery;
    private static PreparedStatement checkConversationQuery;

    private static PreparedStatement sendMessageQuery;
    private static PreparedStatement loadMessagesQuery;
    private static PreparedStatement getFriendListQuery;
    private static PreparedStatement getPhoneNumberofFriends ;
    private static PreparedStatement add_friend ;

    static {

        try {

            conn = Dbconnection.getConnection();

            signupQuery = conn.prepareStatement(
                    "INSERT INTO users(name,phone_number,pin) VALUES(?,?,?)"
            );

            loginQuery = conn.prepareStatement(
                    "SELECT * FROM users WHERE phone_number=? AND pin=?"
            );

            getUserIdQuery = conn.prepareStatement(
                    "SELECT user_id FROM users WHERE phone_number=?"
            );

            createConversationQuery = conn.prepareStatement(
                    "INSERT INTO conversations(chat_id,user1,user2) VALUES(?,?,?)"
            );

            checkConversationQuery = conn.prepareStatement(
                    "SELECT chat_id FROM conversations WHERE chat_id=?"
            );

            sendMessageQuery = conn.prepareStatement(
                    "INSERT INTO messages(chat_id,sender_id,message) VALUES(?,?,?)"
            );

            loadMessagesQuery = conn.prepareStatement(
                    "SELECT sender_id,message,sent_at FROM messages WHERE chat_id=? ORDER BY sent_at"
            );
            getFriendListQuery = conn.prepareStatement("select friend_id from friends where user_id = ?") ;
            getPhoneNumberofFriends = conn.prepareStatement("select phone_number from users where user_id = ?") ;
            add_friend = conn.prepareStatement(
                    "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SIGNUP
    public static boolean signup(String name, String phone, String pin) {

        try {

            signupQuery.setString(1, name);
            signupQuery.setString(2, phone);
            signupQuery.setString(3, pin);

            return signupQuery.executeUpdate() > 0;

        } catch (SQLException e) {

            return false;
        }
    }

    // LOGIN
    public static boolean login(String phone, String pin) {

        try {

            loginQuery.setString(1, phone);
            loginQuery.setString(2, pin);

            ResultSet rs = loginQuery.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            return false;
        }
    }

    // GET USER ID FROM PHONE
    public static int getUserId(String phone) {

        try {

            getUserIdQuery.setString(1, phone);

            ResultSet rs = getUserIdQuery.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    // GENERATE CHAT ID
    public static String generateChatId(int u1, int u2) {

        if (u1 < u2)
            return u1 + "_" + u2;
        else
            return u2 + "_" + u1;
    }

    // CREATE CONVERSATION IF NOT EXISTS
    public static String getOrCreateConversation(int user1, int user2) {

        String chatId = generateChatId(user1, user2);

        try {

            checkConversationQuery.setString(1, chatId);

            ResultSet rs = checkConversationQuery.executeQuery();

            if (!rs.next()) {

                createConversationQuery.setString(1, chatId);
                createConversationQuery.setInt(2, user1);
                createConversationQuery.setInt(3, user2);

                createConversationQuery.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chatId;
    }

    // SEND MESSAGE
    public static void sendMessage(String chatId, int senderId, String message) {

        try {

            sendMessageQuery.setString(1, chatId);
            sendMessageQuery.setInt(2, senderId);
            sendMessageQuery.setString(3, message);

            sendMessageQuery.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void addFriend(int userId, int friendId) {

        try {

            conn.setAutoCommit(false);

            add_friend.setInt(1, userId);
            add_friend.setInt(2, friendId);
            add_friend.executeUpdate();

            add_friend.setInt(1, friendId);
            add_friend.setInt(2, userId);
            add_friend.executeUpdate();

            conn.commit();

        } catch (SQLException e) {

            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
        }
    }

    public static ResultSet loadMessages(String chatId) {

        try {

            loadMessagesQuery.setString(1, chatId);

            return loadMessagesQuery.executeQuery();

        } catch (SQLException e) {

            return null;
        }
    }
    public static ResultSet getFriendList(int user_id) {

        try {

            getFriendListQuery.setInt(1, user_id);


            return getFriendListQuery.executeQuery() ;

        } catch (SQLException e) {

            return null ;
        }
    }
//    public static ResultSet getphone_numberofFriends(int[] Id ) {
//
//        try {
//
//            getPhoneNumberofFriends.set(1, Id);
//
//
//            return getFriendListQuery.executeQuery() ;
//
//        } catch (SQLException e) {
//
//            return null ;
//        }
//    }


}