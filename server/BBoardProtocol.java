
import java.util.ArrayList;

public class BBoardProtocol {

    public static synchronized String parseMessage(String clientMessage, int clientNumber, BBoardStructure[] pb){
        String serverMessage = "RESPONSE defaultResponse";

        if(clientMessage.equals("DISCONNECT")) {
            serverMessage="Successfully Disconnected";
        }
        /*
        else if(clientMessage.equals("INFO")){
            System.out.println("Info function called");
            serverMessage=" Message understood";
            info(pb);
        }*/
        else if(clientMessage.length() > 4 && clientMessage.substring(0, 4).equals("POST")){
            serverMessage = post(clientMessage, pb);
        }
        else if(clientMessage.length() > 3 && clientMessage.substring(0, 3).equals("GET")){
            serverMessage = get(clientMessage, pb);
        }
        else if(clientMessage.length() > 3 && clientMessage.substring(0, 3).equals("PIN")){
            serverMessage = pin(clientNumber, clientMessage, pb);
        }
        else if(clientMessage.length() > 5 && clientMessage.substring(0, 5).equals("UNPIN")){
            serverMessage = unpin(clientNumber, clientMessage, pb);
        }
        else if(clientMessage.length() >= 5 && clientMessage.substring(0, 5).equals("SHAKE")){
            serverMessage = shake(pb);
        }
        else if(clientMessage.length() >= 5 && clientMessage.substring(0, 5).equals("CLEAR")){
            serverMessage = clear(pb);
        }
        else{
            serverMessage="RESPONSE InvalidMessageException";

        }

        return serverMessage;
    }

    public static void info( BBoardStructure[] pb){
        pb[0].boardInfo();
    }

    public static void changeHeight(BBoardStructure[] pb, int height){
        pb[0].setHeight(height);
    }

    public static boolean postToBoard(BBoardStructure[] pb, post Post){
        boolean toClient = pb[0].addPost(Post);
        return toClient;

    }
    public static boolean checkCoords(int x, int y, int width, int height, BBoardStructure[] pb){
        boolean valid = true;
        int maxh = pb[0].getHeight();
        int maxw = pb[0].getWidth();
        if (y < 0 || y + height > maxh){
            valid = false;
        }
        if (x < 0 || x + width > maxw){
            valid = false;
        }
        return valid;
    }

    public static boolean checkCoords(int x, int y, BBoardStructure[] pb){
        boolean valid = true;
        int maxh = pb[0].getHeight();
        int maxw = pb[0].getWidth();
        if (y < 0 || y > maxh){
            valid = false;
        }
        if (x < 0 || x > maxw){
            valid = false;
        }
        return valid;
    }

    public static boolean checkColor(String color, BBoardStructure[] pb){
        boolean valid = false;
        String[] colors = pb[0].getColors();
        for (String option : colors) {
            if (color.equals(option)) {
                valid = true;
            }
        }
        return valid;
    }

    public static boolean checkOverlap(post post, int x, int y){
        int[] dimensions = post.getDimesions();
        int[] coords = post.getCoords();
        boolean valid = true;

        if (y < coords[1] || y > coords[1] + dimensions[1]){
            valid = false;
        }
        if (x < coords[0] || x > coords[0] + dimensions[0]){
            valid = false;
        }
        return valid;
    }

    //public static boolean checkPinned(int x, int y, BBoardStructure[] pb){

    public static String post(String message, BBoardStructure[] pb){
        String response = "";
        message = message.replace("POST ","");
        String[] postReq = message.split(" ");
        int j;
        int x = Integer.parseInt(postReq[0]);
        int y = Integer.parseInt(postReq[1]);
        int width = Integer.parseInt(postReq[2]);
        int height = Integer.parseInt(postReq[3]);
        String color = postReq[4];
        int i;
        String status="";
        for(i = 5; i<postReq.length;i++){
            status = status + postReq[i] + " ";
        }
        for(j = 0;j< postReq.length;j++){
            System.out.println(postReq[j]);
        }
        boolean check = checkCoords(x, y, width, height, pb);
        if (!check){
            response = "RESPONSE OutOfBoundsException";
        }
        else {
            check = checkColor(color, pb);
            if (!check){
                response = "RESPONSE InvalidColorException";
            }
            else {
                post newPost = new post(x, y, width, height, color, status);
                check = BBoardProtocol.postToBoard(pb, newPost);
                if (!check){
                    response = "RESPONSE ErrorUnknown";
                }
                else {
                    response = "RESPONSE SuccessfulPost";
                }
            }
        }
        return response;
    }
    public static String get(String message, BBoardStructure[] pb){
        int x = -1;
        int y = -1;
        String sx = "";
        String sy = "";
        String color = "";
        String refersTo = "";

        boolean stringEnd = false;
        boolean invalid = false;

        try {
            message = message.replace("GET ", "");
            if (message.equals("PINS")){
                return pb[0].queryPins();
            }

            String temp = message;
            String[] parts = temp.split(" ");
            int max = parts.length;
            int i = 0;
            int j = 0;
            while (i < max){
                if(parts[i].startsWith("contains=")){
                    parts[i] = parts[i].replace("contains=", "");
                    try{
                        x = Integer.parseInt(parts[i]);
                    } catch(NumberFormatException nfe) {
                        invalid = true;
                        break;
                    }
                    try{
                        y = Integer.parseInt(parts[i+1]);
                    } catch(NumberFormatException nfe) {
                        invalid = true;
                        break;
                    }
                    i++;
                } else if (parts[i].startsWith("color=")){
                    parts[i]=parts[i].replace("color=", "");
                    color = parts[i];
                } else if (parts[i].startsWith("refersTo=\"")){
                    refersTo=parts[i].replace("refersTo=\"", "");
                    refersTo=refersTo.replace("\"", "");
                    stringEnd = false;
                    String rf="";
                    try{
                        while (stringEnd == false){
                            for(j = 0; j < parts[i].length(); j++){
                                if (parts[i].charAt(j) == '"'){
                                    stringEnd = true;
                                } else {
                                    rf = rf + parts[i].substring(j, j+1);
                                }
                            }
                            i++;
                        }
                    } catch(Exception e){
                        invalid = true;
                    }
                    /*
                     */
                }
                i++;
            }



            if(invalid){
                return "Invalid Input";
            }

            System.out.println("QUERYSTR: x:" + x + " y:"+ y + " color:" + color + " refersTo:" + refersTo);
            String queryStr = pb[0].query(x, y, color, refersTo);
            return queryStr;
        }catch(Exception e){
            return e.toString();
        }

    }
    public static String pin(int clientnumber, String message, BBoardStructure[] pb) {
        String response = "RESPONSE ERROR";
        message = message.replace("PIN ","");
        String[] postReq = message.split(" ");
        int x = Integer.parseInt(postReq[0]);//dont need to check if theres enough arguments cause the GUI will always include 2
        int y = Integer.parseInt(postReq[1]);
        boolean pinit = false;
        int successes = 0;
        boolean success = false;
        boolean postfound = false;
        ArrayList<post> posts = pb[0].getPosts();

        //first check if the coordinates are in bounds
        //then go through the list of posts, find which are at these coords
        //alter the pins arraylists of all of those posts
        boolean check = checkCoords(x, y, pb);
        if (!check){
            response = "Given Co-ordinates Are Out Of Bounds\nPin Unsuccessful";
        }
        else {
            //for every post

            for (int i = 0; i < pb[0].postNumber(); i++) {

                pinit = checkOverlap(posts.get(i), x, y);
                //System.out.println("for post at " + posts.get(i).getX() + ", " + posts.get(i).getY() + ", pinit = " + pinit);
                if (pinit) {
                    postfound = true;
                    success = posts.get(i).setPin(x, y, clientnumber);
                    if (success){
                        successes++;
                    }
                    success = false;

                }
                //pinit = false; //not necessary
            }

            if (postfound == false){
                response = "There is No Note at " + x + "," + y + "\nPin Unsuccessful";
            } else if (successes == 0){
                response = "There Is Already a Pin at " + x + "," + y + "\nPin Unsuccessful";
            } else if (successes == 1){
                response = successes + " Note Successfully Pinned at " + x + "," + y + "!";
            } else {
                response = successes + " Notes Successfully Pinned at " + x + "," + y + "!";
            }

        }
        return response;
    }
    public static String unpin(int clientnumber, String message, BBoardStructure[] pb) {

        message = message.replace("UNPIN ","");
        String[] postReq = message.split(" ");
        int x = Integer.parseInt(postReq[0]);//dont need to check if theres enough arguments cause the GUI will always include 2
        int y = Integer.parseInt(postReq[1]);
        int unpins = 0;

        // first go through the list of posts
        // call removePin
        // go through list of pins
        // find which have pins at these coords and from this clientnumber
        // remove the pin
        String response = "There is No Pin at " + x + "," + y + "\nUnpin Unsuccessful";
        for (int i = 0; i < pb[0].postNumber(); i++){//for every post
            unpins += pb[0].getPosts().get(i).removePin(x, y, clientnumber);
        }
        if(unpins == 1){
            response = unpins + " Pin Successfully Unpinned at " + x + "," + y;
        } else if(unpins > 1){
            response = unpins + " Pins Successfully Unpinned at " + x + "," + y;
        }

        return response;
    }
    public static String shake(BBoardStructure[] pb){
        return pb[0].shakePins();
    }
    public static String clear(BBoardStructure[] pb){
        return pb[0].clearPin();
    }


}