public class Bird  {
    public float position = height/2;
    public float positionX=150;
    public float flapStrength=15;
    private float defaultGravity=2;
    public float gravityStrength =defaultGravity;
    public PImage sprite = loadImage("assests/bird.png");
    public float size = 128;
    void flap(){
        position-=flapStrength;
        gravityStrength=defaultGravity;
    }
    void physics(){
        position+=gravityStrength;
        gravityStrength+=0.2;
    }
    void show(){
        image(sprite, positionX, position, size, size);
    }
    boolean endGame(Queue<Pipe>pipeList){
        return outOfBound()||collisionCheck(pipeList);
    }
    boolean outOfBound(){
        if(position+size>=height) return true;
        if(position<=0 ) return true;
        return false;
    }
    boolean collisionCheck(Queue<Pipe>pipeList){
       for (Pipe p : pipeList) {
            if(collisionCheck( p)){
                return true;
            }
       }
       return false;
    }
    boolean collisionCheck(Pipe p ){
        return positionX + size >p.x  && positionX<p.x+Pipe.sizeX&& position+size>p.y && position<p.y+Pipe.sizeY;
    }

}
