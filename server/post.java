

import java.util.ArrayList;
public class post {
    private int id;
    private int x;
    private int y;
    private int width;
    private int height;
    private String color;
    private String status;
    private ArrayList<Integer[]> pins = new ArrayList(0);

    public post(int x, int y, int width, int height, String color, String status){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.status = status;
        this.pins = pins;
    }

    public boolean setPin(int x, int y, int clientNumber){
        boolean pin = true;
        Integer[] temp = {};
        String response = "";

        for(int i = 0; i < this.pins.size(); i++){//for every pin, check for match
            temp = this.pins.get(i);
            if(temp[0].equals(x) && temp[1].equals(y) && temp[2].equals(clientNumber) ){//if theres a match, dont pin
                pin = false;
            }
        }

        //this is for adding a pin to the list of pins. no checks needed

        if(pin){
            temp = new Integer[]{x, y, clientNumber};
            this.pins.add(temp);
        }
        return pin;
    }
    public int removePin(int x, int y, int clientnumber){

        Integer[] temp = {};
        int unpins = 0;

        for (int j = 0; j < this.pins.size(); j++) {//for every pin
            temp = this.pins.get(j);
            if (temp[0].equals(x) && temp[1].equals(y) && temp[2].equals(clientnumber) ) {
                this.pins.remove(j);
                unpins++;
                break;
            }
        }
        return unpins;
    }
    public int[] getCoords(){
        int[] coords = {x, y};
        return coords;
    }
    public int[] getDimesions(){
        int[] dimensions = {width, height};
        return dimensions;
    }

    public String toString(){
        String retString = "Note | Coordinates: " + String.valueOf(this.x)  + "x " + String.valueOf(this.y) + "y "
                + "Dimensions: " + String.valueOf(this.width) + " by " + String.valueOf(this.height) + " Color: "
                + this.color + " Status: " + this.status + "\n";
        return retString;

    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public String getColor(){
        return this.color;
    }
    public String getStatus(){
        return this.status;
    }
    public ArrayList<Integer[]> getPins(){
        return this.pins;
    }


}
