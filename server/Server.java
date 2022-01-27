

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server {
    public static void main(String[] args) throws IOException {
//example arguments: 4444 200 300 green blue
        int ccount;
        int  i = 0;
        int portNumber;
        int width;
        int height;

        if (args.length < 3) {
            //System.err.println("Not enough arguments given");
            ccount = 0;
            portNumber = 4444;
            width = 500;
            height = 500;
        }
        else {
            //declaring variables
            ccount = 0;// client count
            portNumber = Integer.parseInt(args[0]);
            width = Integer.parseInt(args[1]);
            height = Integer.parseInt(args[2]);
            //String color = args[3];
        }
        List<String> boardColors = new ArrayList<>();
        if(args.length > 3) {
            for (i = 3; i < args.length; i++) {
                boardColors.add(args[i]);
            }
        }
        else{
            boardColors.add("red");
            boardColors.add("green");
            boardColors.add("blue");
        }
        String[] colors = new String[boardColors.size()];
        colors = boardColors.toArray(colors);

        //create the bulletin board
        new BBoardStructure(height, width, colors);

        //^this adds all arguments to the proper variables

        try {//try with resources denotes the following as resources
            BBoardStructure board = new BBoardStructure(height, width, colors);
            board.boardInfo();
            BBoardStructure pb[] = {board};//pb is the "pointer" to the board. it is a single element array. altering pb will alter the original board (its pass by reference)
            ServerSocket serverSocket = new ServerSocket(portNumber);//makes a server socket
            System.out.println("Server Online....");
            while (true) {
                ccount++;
                Socket clientSocket = serverSocket.accept();//accepts a client to listen to
                //serverSocket is for listening for new clients, clientSocket is for listening to that specific client
                ServerClientThread sct = new ServerClientThread(clientSocket, ccount, pb);
                sct.start();
            }
        } catch (IOException e) {
            System.out.println("error during connection");
            System.exit(1);
        }

    }

}