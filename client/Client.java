
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    static boolean connected = false;
    static Socket socket = null;
    static DataInputStream inStream;
    static DataOutputStream outStream;
    static BufferedReader br;


    public static void main(String[] args) {
        System.out.println("Running GUI");

        JFrame frame = new JFrame("Bulletin Board");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 400);


        addComponentsToFrame(frame);
        //frame.pack();
        frame.setVisible(true);

    }

    public static void addComponentsToFrame(JFrame frame) {
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();//this is for the frame
        GridBagConstraints p = new GridBagConstraints();//this is for in panels if needed.
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.weightx = 1.0;
        c.weighty = 2.0;
        c.gridx = 0;

        //IPfeild Portfeild, disconnectbutton
        JPanel connectPanel = new JPanel();


        JTextField IPFeild = new JTextField("IP Address:", 16);
        JTextField portFeild = new JTextField("Port Number:", 16);
        JButton connectButton = new JButton("Connect");
        JButton disconnectButton = new JButton("Disonnect");


        connectPanel.add(IPFeild);
        connectPanel.add(portFeild);
        connectPanel.add(connectButton);
        connectPanel.add(disconnectButton);


        //post panel
        JPanel postPanel = new JPanel();

        JTextField Xfeild = new JTextField("X", 10);

        JTextField Yfeild = new JTextField("Y", 10);

        JTextField Wfeild = new JTextField("Width", 10);

        JTextField Hfeild = new JTextField("Height", 10);

        JTextField Cfeild = new JTextField("Color", 10);

        postPanel.add(Xfeild);
        postPanel.add(Yfeild);
        postPanel.add(Wfeild);
        postPanel.add(Hfeild);
        postPanel.add(Cfeild);

//message panel
        JPanel messagePanel = new JPanel();

        JTextField messagefeild = new JTextField("Enter Message Here", 40);


        JButton postButton = new JButton("Post");

        messagePanel.add(messagefeild);
        messagePanel.add(postButton);
        //get panel
        JPanel getPanel = new JPanel();

        JTextField getfeild = new JTextField("Use contains= refersTo= or color= to find post by location, message or color", 40);

        JButton getButton = new JButton("Get");

        getPanel.add(getfeild);
        getPanel.add(getButton);
        //pin panel
        JPanel pinPanel = new JPanel();
        JTextField pinxfeild = new JTextField("X", 5);
        JTextField pinyfeild = new JTextField("Y", 5);

        JButton pinButton = new JButton("Pin");
        JButton unpinButton = new JButton("Unpin");

        JButton shakeButton = new JButton("Shake");
        JButton clearButton = new JButton("Clear");

        JButton rulesButton = new JButton("Rules");

        pinPanel.add(pinxfeild);
        pinPanel.add(pinyfeild);
        pinPanel.add(pinButton);
        pinPanel.add(unpinButton);
        pinPanel.add(shakeButton);
        pinPanel.add(clearButton);
        pinPanel.add(rulesButton);

//adding to frame section---------------------------------------
        c.fill = GridBagConstraints.HORIZONTAL;


        c.gridy = 0;
        frame.add(connectPanel, c);
        c.gridy = 1;
        frame.add(postPanel, c);
        c.gridy = 2;
        frame.add(messagePanel, c);
        c.gridy = 3;
        frame.add(getPanel, c);
        c.gridy = 4;
        frame.add(pinPanel, c);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Connect detected");

                if(connected){
                    JOptionPane.showMessageDialog(null, "Already Connected");
                } else {
                    try {
                        String IP = IPFeild.getText();
                        int port = Integer.parseInt(portFeild.getText());
                        socket = new Socket(IP, port);
                        inStream = new DataInputStream(socket.getInputStream());
                        outStream = new DataOutputStream(socket.getOutputStream());
                        br = new BufferedReader(new InputStreamReader(System.in));
                        String clientMessage = "", serverMessage = "";
                        JOptionPane.showMessageDialog(null, "Connection Successful");
                        connected = true;
                    }catch (Exception err){
                        System.out.println(err);
                        JOptionPane.showMessageDialog(null, err);
                    }
                }
            }

        });
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!connected) {
                    JOptionPane.showMessageDialog(null, "Not Currently Connected");
                } else {

                    try {
                        outStream.writeUTF("DISCONNECT");
                        outStream.flush();
                        outStream.close();
                        socket.close();
                        JOptionPane.showMessageDialog(null, "Disconnected from server");
                        connected = false;
                    }catch(Exception err){
                        System.out.println(err);
                        JOptionPane.showMessageDialog(null, err);

                    }
                }
            }
        });
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!connected) {
                    JOptionPane.showMessageDialog(null, "Not Currently Connected");
                } else {


                    String postMessage = messagefeild.getText();
                    String x = Xfeild.getText();
                    String y = Yfeild.getText();
                    String width = Wfeild.getText();
                    String height = Hfeild.getText();
                    String color = Cfeild.getText();
                    String finalMsg = String.format("POST %s %s %s %s %s %s", x, y, width, height, color, postMessage);
                    System.out.println(finalMsg);
                    try {
                        outStream.writeUTF(finalMsg);
                        outStream.flush();
                        String serverMessage = inStream.readUTF();
                        System.out.println(serverMessage);
                        JOptionPane.showMessageDialog(null, serverMessage);
                    } catch (Exception err) {
                        System.out.println(err);
                        JOptionPane.showMessageDialog(null, err);
                    }
                }
            }
        });
        getButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!connected) {
                    JOptionPane.showMessageDialog(null, "Not Currently Connected");
                } else {
                    String getReq = getfeild.getText();
                    try {
                        outStream.writeUTF("GET " + getReq);
                        outStream.flush();
                        Thread.sleep(500);
                        String serverMessage = inStream.readUTF();
                        System.out.println(serverMessage);
                        JOptionPane.showMessageDialog(null, serverMessage);

                    }catch(Exception err){
                        System.out.println(err);
                        JOptionPane.showMessageDialog(null, err);
                    }
                }

            }
        });
        pinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!connected) {
                    JOptionPane.showMessageDialog(null, "Not Currently Connected");
                } else {
                    String pinX = pinxfeild.getText();
                    String pinY = pinyfeild.getText();
                    String pinReq = ("PIN " + pinX + " " + pinY);
                    try {
                        outStream.writeUTF(pinReq);
                        outStream.flush();
                        String serverMessage = inStream.readUTF();
                        JOptionPane.showMessageDialog(null, serverMessage);
                    }catch(Exception err){
                        System.out.println(err);
                    }
                }
            }
        });
        unpinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!connected) {
                    JOptionPane.showMessageDialog(null, "Not Currently Connected");
                } else {
                    String unpinX = pinxfeild.getText();
                    String unpinY = pinyfeild.getText();
                    String unpinReq = ("UNPIN " + unpinX + " " + unpinY);
                    try{
                        outStream.writeUTF(unpinReq);
                        outStream.flush();
                        String serverMessage = inStream.readUTF();
                        JOptionPane.showMessageDialog(null, serverMessage);
                    }catch(Exception err){
                        System.out.println(err);
                    }
                }

            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!connected) {
                    JOptionPane.showMessageDialog(null, "Not Currently Connected");
                } else {
                    try{
                        outStream.writeUTF("CLEAR");
                        outStream.flush();
                        String serverMessage = inStream.readUTF();
                        JOptionPane.showMessageDialog(null, serverMessage);
                    }catch(Exception err){
                        System.out.println(err);
                        JOptionPane.showMessageDialog(null, err);
                    }
                }

            }
        });
        shakeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!connected) {
                    JOptionPane.showMessageDialog(null, "Not Currently Connected");
                } else {
                    try{
                        outStream.writeUTF("SHAKE");
                        outStream.flush();
                        String serverMessage = inStream.readUTF();
                        JOptionPane.showMessageDialog(null, serverMessage);
                    }catch(Exception err){
                        System.out.println(err);
                        JOptionPane.showMessageDialog(null, err);
                    }
                }

            }
        });
        rulesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String rules = "Rules:\nDo not include \"IP Address:\", \"Port Number:\", etc. in your input\nFormat get statements like <PINS> or <color=red refersTo=\"insert message\" contains=5 5>\n"+
                        "get statements can be listed in any order\nOne request cannot contain two examples of the same parameter (<contains=4 4 contains=8 4> for example";
                JOptionPane.showMessageDialog(null, rules);


            }
        });
    }




}
