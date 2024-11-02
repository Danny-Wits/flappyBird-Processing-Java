import java.util.Arrays;
public class Bird  {
    public float position = height/4;
    public float positionX=150;
    public float flapStrength=15;
    private float defaultGravity=2;
    public float gravityStrength =defaultGravity;
    public PImage sprite = loadImage("assests/bird.png");
    public float size = 128;
    public Matrix<Float> wm= new Matrix<>(1,5,0.1);
   
    public int score=0;
    Bird(){
        shuffleWeights();
    }
    Bird(Matrix<Float> wm){
        this.wm=wm;
    }
    void flap(){
        position-=flapStrength;
        gravityStrength=defaultGravity;
      
    }
    void network(){
        //input / feed forward 
        Matrix inputMatrix = new Matrix();
        inputMatrix.addRow(position/10);
        boolean shouldFlap =false;
        try {
        var pp = closestPipePair();
         inputMatrix.addRow(pp.getX()-positionX/10);
         inputMatrix.addRow(pp.getT()/10);
         inputMatrix.addRow(pp.getB()/10);
         inputMatrix.addRow(1F);
        
         shouldFlap = activation((Float)Matrix.multiplication(wm,inputMatrix).getValueAt(0,0,0));
        } catch (Exception e) {
           print(e.getMessage()); 
        }
        if(shouldFlap)
        {
            flap();
        //  print(inputMatrix+"\n");
         };
    }
    public Bird child(){
        var child =  new Bird(this.wm);
        child.shuffleWeights();
        return child;
    }
      public Bird child(float limit ){
        var child =  new Bird(this.wm);
        child.shuffleWeights(limit);
        return child;
    }
    boolean activation(Float x ){
       float f =sigmoid(x/100);
        // print (f+"\n");
        return f>0.98;
    }
    float sigmoid(float x ){
         return  (float)(1 / (1 + Math.exp(-(x))));
    }
    void shuffleWeights(){
        this.wm.setArray((Float[][])wm.map(e ->e + random(-learnRate, learnRate)));
        //  print(wm);
    }
    void shuffleWeights(float limit){
        this.wm.setArray((Float[][])wm.map(e ->e + random(-limit, limit)));
        //  print(wm);
    }
    PipePair closestPipePair()  throws Exception{
      for (PipePair pp : pipeList) {
          if(pp.getX()>this.positionX)
           return pp;  
      }
      throw new Exception("No closest pipe");
    }
    void physics(){
        position+=gravityStrength;
        gravityStrength+=0.2;
    }
    void show(){
        image(sprite, positionX, position, size, size);
    }
    boolean endGame(Queue<PipePair>pipeList){
        return outOfBound()||collisionCheck(pipeList);
    }
    boolean outOfBound(){
        if(position+size>=height) position=height-size;
        if(position<=0 ) position=0;
        shuffleWeights();
        return false;
    }
    boolean collisionCheck(Queue<PipePair>pipeList){
       for (PipePair p : pipeList) {
            if(collisionCheck(p.top)||collisionCheck(p.bottom)){
                return true;
            }
       }
       return false;
    }
    boolean collisionCheck(Pipe p ){
        return positionX + size >p.x  && positionX<p.x+Pipe.sizeX&& position+size>p.y && position<p.y+Pipe.sizeY;
    }

}
