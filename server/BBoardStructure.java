

import java.util.ArrayList;

public class BBoardStructure {
    private String[] colors;
    private int height;
    private int width;
    private ArrayList<post> posts = new ArrayList<>();
    public BBoardStructure(int height, int width, String[] colors){
        this.height = height;
        this.width = width;
        this.colors = colors;
    }
    public void boardInfo(){
        System.out.println("Board height: " + height);
        System.out.println("Board width: " + width);
        System.out.println("Permitted colors: ");
        int i;
        for(i = 0;i<colors.length;i++){
            System.out.println(colors[i]);
        }
    }
    public void setHeight(int height){
        this.height = height;
    }
    public boolean addPost(post Post){
        int i;
        boolean posted = true;
        //posts are totally allowed to overlap. even have all identical x/y/width/height values
        /*
        if(this.posts!=null) {
            for (i = 0; i < posts.size(); i++) {
                if (Post.getCoords() == posts.get(i).getCoords()) {
                    posted = false;
                }
            }
        }
        */
        if(posted) {
            this.posts.add(Post);
        }
        return posted;
    }
    public String query(int x, int y, String color, String refersTo){
        int vx;
        int vy;
        String vcolor;
        String vrefersTo;
        String queryReturn = "";
        int i;
        ArrayList<post> toReturn = new ArrayList<>();
        for(i = 0;i<posts.size();i++){
            post cpost = posts.get(i);
            if(((x == cpost.getX() && y == cpost.getY()) || (x == -1 && y == -1)) && (color.equals(cpost.getColor()) ||
                    color.equals("")) && (cpost.getStatus().contains(refersTo) || refersTo.equals(""))){
                if(x == -1 && y == -1 && color.equals("") && refersTo.equals("")){
                    queryReturn = "No Valid Parameters Given";
                } else {
                    queryReturn = queryReturn + cpost.toString();
                }

            }
        }

        return queryReturn;
    }
    public String queryPins(){
        int i;
        String pinsReturn = "";
        ArrayList<post> pinned = new ArrayList<>();
        for(i = 0;i<posts.size();i++){
            if (posts.get(i).getPins().size() >= 1){
                pinsReturn = pinsReturn + posts.get(i).toString();
            }
        }
        if(pinsReturn.equals("")){
            return "No posts pinned";
        }
        return pinsReturn;
    }
    public String shakePins(){
        int i;
        String pinskilled = "Pins removed: \n";
        for(i = 0;i<posts.size();i++){
            if(posts.get(i).getPins().size() ==0){
                pinskilled = pinskilled + posts.get(i).toString();
                posts.remove(i);
                i = 0;
            }
        }
        return pinskilled;
    }
    public String clearPin(){
        posts.clear();
        return "Posts have been cleared";
    }

    public int getHeight(){
        return this.height;
    }
    public int getWidth(){
        return this.width;
    }
    public String[] getColors(){
        return this.colors;
    }

    public int postNumber(){
        return this.posts.size();
    }

    public ArrayList<post> getPosts(){
        return this.posts;
    }

}