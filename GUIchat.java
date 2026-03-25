package chatapplication;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import java.util.Date;



public class GUIchat {

    private int currentFriendID = 0 ;
    private String myPhone;
    private int myUserId;

    private JTextArea chatArea;

    CardLayout card;
    JPanel mainPanel;
    JPanel loginPanel;
    JPanel chatPanel;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    GUIchat() {

        card = new CardLayout();
        mainPanel = new JPanel(card);

        createLoginPanel();


        mainPanel.add(loginPanel, "login");

    }

    private void createLoginPanel() {

        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(Color.darkGray);
        JButton signin = new JButton("Sign IN");
        JButton signup = new JButton("Sign UP");

        signin.setAlignmentX(Component.CENTER_ALIGNMENT);
        signup.setAlignmentX(Component.CENTER_ALIGNMENT);

        signin.addActionListener(e -> {

            String[] loginData = showLoginDialog();

            if (loginData != null) {

                String phn = loginData[0];
                String pin = loginData[1];

                boolean success = QueryCaller.login(phn, pin);

                if(success){

                    myPhone = phn;
                    myUserId = QueryCaller.getUserId(phn);

                    try {
                        socket = new Socket("localhost", 5000);

                        in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));

                        out = new PrintWriter(socket.getOutputStream(), true);


                        out.println(myUserId);

                        // start listening thread
                        new Thread(() -> receiveMessages()).start();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    createChatPanel(myUserId);
                    mainPanel.add(chatPanel, "chat");
                    card.show(mainPanel, "chat");
                }else{
                    JOptionPane.showMessageDialog(null,"Invalid phone or pin");
                }
            }

        });

        signup.addActionListener(e -> {

            String[] signupData = showSignupDialog();

            if (signupData != null) {

                String name  = signupData[0];
                String phone_no = signupData[1];

                String new_pin = JOptionPane.showInputDialog("Set new pin");
                System.out.println("name : "+ name + "phone_no. = " + phone_no +"pin :" + new_pin);
                boolean created = QueryCaller.signup(name,phone_no,new_pin);

                if(created){
                    JOptionPane.showMessageDialog(null,"Account created");

                }else{
                    JOptionPane.showMessageDialog(null,"Phone number already exists");
                }

            }
        });

        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(signin);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(signup);
        loginPanel.add(Box.createVerticalGlue());
    }

    private  void createChatPanel(int userID) {

        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(Color.darkGray);
        JPanel friendPanel = new JPanel(new BorderLayout());
        friendPanel.setPreferredSize(new Dimension(150, 0));
        friendPanel.setBackground(Color.white);
        DefaultListModel<Integer> model = new DefaultListModel<>();


        ResultSet result = QueryCaller.getFriendList(userID);

        try {

            while(result.next()){

                model.addElement(result.getInt("friend_id"));

            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        JList<Integer> friendList = new JList<>(model);

        friendList.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                currentFriendID = friendList.getSelectedValue() ;
                System.out.println("current friend id :" + currentFriendID);
                chatArea.setText("") ;

                int friendId = currentFriendID ;

                String chatId = QueryCaller.getOrCreateConversation(myUserId,friendId);

                ResultSet rs = QueryCaller.loadMessages(chatId);

                try{

                    String SupDate = "";
                    while(rs.next()){
                        LocalDate nowdate = LocalDate.now() ;
                        int sender = rs.getInt("sender_id");
                        String msg = rs.getString("message");
                        LocalDate date = rs.getDate("sent_at").toLocalDate();;
                        Date time = rs.getTime("sent_at") ;
                        DayOfWeek day = date.getDayOfWeek();
                        String sDate = date.toString() ;
                        String snDate = nowdate.toString() ;


                        if(snDate.equals(sDate)&&!(SupDate.equals(sDate))){
                            chatArea.append("------------------------today--------------------------------\n");
                            SupDate = sDate ;
                        }
                        else if(!(snDate.equals(sDate))&&!(SupDate.equals(sDate))) {
                                chatArea.append("-----------------------------" + sDate +"( "+ day +" )---------------------------\n");
                                SupDate = sDate ;
                        }
                        if(sender == myUserId)
                            chatArea.append("Me: " + msg + "  "+ "  sent at :....................." + time +"\n");
                        else
                            chatArea.append(currentFriendID + ": " + msg + "      "+ "    sent at :..................." + time +"\n");
                    }
                    rs.close() ;

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        JButton addfriend = new JButton("Add Friend");

        friendPanel.add(new JScrollPane(friendList), BorderLayout.CENTER);
        friendPanel.add(addfriend , BorderLayout.NORTH);

        JPanel chatAreaPanel = new JPanel(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(Color.BLACK);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setForeground(Color.YELLOW);

        JPanel southPanel = new JPanel(new FlowLayout());

        JTextField messageField = new JTextField();
        messageField.setPreferredSize(new Dimension(300,40));

        JButton send = new JButton("SEND");
        send.setPreferredSize(new Dimension(100,40));

        messageField.addActionListener(e -> send.doClick());

        southPanel.add(messageField);
        southPanel.add(send);

        chatAreaPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        chatAreaPanel.add(southPanel, BorderLayout.SOUTH);

        chatPanel.add(friendPanel, BorderLayout.WEST);
        chatPanel.add(chatAreaPanel, BorderLayout.CENTER);

        addfriend.addActionListener(e -> {
            String mobile_number = JOptionPane.showInputDialog("Enter friend's phone number : ");

           int id = QueryCaller.getUserId(mobile_number) ;
            if(id == -1){
                JOptionPane.showMessageDialog(null, "User not found");
                return;
            }
           int flag = 0;
           for(int i = 0 ; i<model.getSize(); i++){
               if(id == model.elementAt(i)) {
                   flag = 1 ;
                   break ;

               }
           }
           if(flag == 0){
               JOptionPane.showMessageDialog(null , "added to the friendlist ");
               QueryCaller.addFriend(myUserId , id);
               System.out.println(myUserId + " "+id);
               model.addElement(id);

           }
           else{
               JOptionPane.showMessageDialog(null , "already in the friendlist ");
           }
        });

        send.addActionListener(e -> {

            if(currentFriendID == 0 ){

                JOptionPane.showMessageDialog(null, "select the friend first");

            }else{

                String mesg = messageField.getText().trim();

                if (!mesg.isEmpty()) {

                    int friendId = currentFriendID ;

                    String chatId = QueryCaller.getOrCreateConversation(myUserId,friendId);
                    System.out.println("chatid : "+chatId +" myID : " +myUserId +" "+mesg);

                    out.println(currentFriendID + ":" + mesg);


                    QueryCaller.sendMessage(chatId,myUserId,mesg);

                    chatArea.append("Me: " + mesg + "\n");

                    messageField.setText("");

                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                }
            }

        });
    }

    public void showFrame() {

        JFrame frame = new JFrame("Chat Application");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);

        frame.add(mainPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private String[] showLoginDialog() {

        JTextField PhoneNum = new JTextField(15);
        JPasswordField pin = new JPasswordField(15);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("PhoneNum"));
        panel.add(PhoneNum);
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Pin"));
        panel.add(pin);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Login",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            String new_phn = PhoneNum.getText();
            String userPin = new String(pin.getPassword());

            if((!new_phn.isEmpty()) && (!userPin.isEmpty())){
                return new String[]{new_phn, userPin};
            }
            else{
                JOptionPane.showMessageDialog(null , "phone_num or pin cannot empty ");
            }
        }

        return null;
    }

    private String[] showSignupDialog() {

        JTextField name = new JTextField(15);
        JTextField phone_num = new JTextField(10);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Name :"));
        panel.add(name);
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Phone number : "));
        panel.add(phone_num);
        panel.add(Box.createVerticalStrut(10));

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "SignUp",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {

            String new_user = name.getText();
            String PhoneNum = phone_num.getText();

            if(!(new_user.isEmpty()) && !(PhoneNum.isEmpty())){
                return new String[]{new_user, PhoneNum};
            }
            else{
                JOptionPane.showMessageDialog(null , "all the fields are mandatory");
            }
        }

        return null;
    }
    private void receiveMessages() {

        try {

            String msg;

            while ((msg = in.readLine()) != null) {

                String[] parts = msg.split(":", 2);

                int sender = Integer.parseInt(parts[0]);
                String message = parts[1];

                SwingUtilities.invokeLater(() -> {
                    chatArea.append(sender + ": " + message + "  "+ LocalDate.now()+ "\n");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new GUIchat().showFrame();
    }
}