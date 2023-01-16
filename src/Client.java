//import ir.alamdari.battleship.model.Player;
//
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.SocketException;
//import java.util.Scanner;
//
//public class Client {
//    public static void main(String[] args) throws Exception {
//        //get the localhost IP address, if ir.alamdari.battleship.server is running on some other IP, you need to use that
//        InetAddress host = InetAddress.getLocalHost();
//        Socket socket = null;
//        ObjectOutputStream oos = null;
//        ObjectInputStream ois = null;
//        Scanner scanner = new Scanner(System.in);
//            //establish socket connection to ir.alamdari.battleship.server
//            try{
//        while (true){
//            //write to socket using ObjectOutputStream
//            socket = new Socket(host.getHostName(), 9876);
//            socket.setKeepAlive(true);
//            oos = new ObjectOutputStream(socket.getOutputStream());
//
//            System.out.println("Sending request to Socket Server");
//
//            oos.writeObject(new Player(9090,"my ip","ali"));
//            //read the ir.alamdari.battleship.server response message
//            ois = new ObjectInputStream(socket.getInputStream());
//            Player message = (Player) ois.readObject();
//            System.out.println("Message: " + message.toString());
//
//            //close resources
//        }}catch (SocketException exception){
//                System.out.println(exception.getMessage());
//                exception.printStackTrace();
//            }
//
//    }
//}
