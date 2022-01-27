
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class ServerClientThread extends Thread {
    Socket clientSocket;
    int clientNumber;
    BBoardStructure[] pb;
    public ServerClientThread(Socket inSocket, int ccount, BBoardStructure pb[]){
        this.clientSocket = inSocket;
        clientNumber = ccount;
        this.pb = pb;
    }

    public void run(){
        try{
            DataInputStream inStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());
            String clientMessage="", serverMessage="";
            while(true){
                clientMessage=inStream.readUTF();
                System.out.println("From Client-" +clientNumber+ ": message is :"+clientMessage);

                serverMessage = parseMessage(clientMessage, clientNumber, pb);

                if(serverMessage.equals("RESPONSE SuccessfulDisconnect")){
                    break;
                }

                outStream.writeUTF(serverMessage);
                outStream.flush();


            }
            inStream.close();
            outStream.close();
            clientSocket.close();
        }catch(Exception ex){
            System.out.println(ex);
        }finally{
            System.out.println("Client -" + clientNumber + " exit!! ");
        }
    }

}



