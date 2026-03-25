package chatapplication;

public class Multiuser extends Thread {
    public void run() {
        GUIchat.main(null );
    }

    public static void main(String[] args) {


        Multiuser user1 = new Multiuser();
        Multiuser user2 = new Multiuser();
        Multiuser user3 = new Multiuser();
        user1.start() ;
        user2.start();
        user3.start();
        ChatServer.main(null);
    }
}
