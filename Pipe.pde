public class Pipe {
    public float x = width;
    public float y = height/2;
    public static final float sizeX =100;
    public static final float sizeY =1000;
    public PImage sprite ;
    Pipe(float y ,boolean fliped){
        this.y=y;
        sprite = loadImage((fliped)?"assests/pipeR.png":"assests/pipe.png");
    }
    void move(){
        x-=speed;
    }
    void show(){
        image(sprite,x,y,100,1000);
    } 
    boolean isOutOfBound(){
        return x<=-sizeX;
    }

    public String toString(){
        return "P";
    }
}
