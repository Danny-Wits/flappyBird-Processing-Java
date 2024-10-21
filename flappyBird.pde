import java.util.ArrayDeque;
import java.util.Queue;

PImage backgroundImage; 
Queue<Pipe> pipeList = new ArrayDeque<>();
String fileLink = "assests/backgroundImage.png";
Bird bird ;
Pipe pipe;
float gap = 350;
int gapY=3;
float speed = 5;
boolean started=false;
boolean endScreen=false;
PFont font ;
void settings(){
 
   size(displayWidth-100,displayHeight-140);
}
void setup() {
    bird = new Bird();
    backgroundImage=loadImage(fileLink);
    backgroundImage.resize(width,height);
    
}

int count = 0;
int dequeCount = -3 ;
void draw() {
    background(backgroundImage); 
    if(endScreen){
        textSize(70);
        fill(255);
        textAlign(CENTER,CENTER);
        text("GAME OVER \n YOUR SCORE :  "+(dequeCount+3)+"\n PRESS 'r' TO CONTINUE",width/2,200 );
        return;
    }  
    if(!started){
        bird.show();
        textSize(70);
        fill(255);
        textAlign(CENTER);
        text("PRESS ENTER TO \nSTART/RESTART\nRESUME/PAUSE",width/2, 200);
        return;
    }
    if(count%(60*gapY)==0){
        addPipes();
        if(dequeCount >=0){
            pipeList.poll();
            pipeList.poll();
        }
        if(dequeCount%5==0 && speed<20){            
            speed+=5;
            if(gapY>1){
                gapY-=1;
            }
           
        }
        dequeCount++;
        count=0;
    }
    count++;
    for (Pipe p1 : pipeList) {
        p1.move();
        p1.show(); 
    }
    
    bird.physics();
    bird.show();
    textAlign(CENTER,TOP);
    fill(255);
    text("Score : "+(dequeCount+3),width/2,30);
    if (keyPressed==true) {
       if(key==' '){
        bird.flap();
       }
    }

    if(bird.endGame(pipeList)){
        endScreen = true;
    }
}
void keyPressed() {
  
        if (keyCode == ENTER) {
            started=!started;
        }
       
       else if(endScreen){
         if (key == 'r'){
            reset();
       }
       }
        
}

void addPipes(){
   pipeList.addAll(createPipePair());
}
ArrayList<Pipe> createPipePair(){
    
    float partition = random(0,height-gap);
    Pipe p1 = new Pipe(partition-Pipe.sizeY,true);
    Pipe p2 = new Pipe(partition+gap,false);
    ArrayList<Pipe> pipePair = new ArrayList<Pipe>();
    pipePair.add(p1);
    pipePair.add(p2);
    return pipePair;
}
void reset(){
    bird = new Bird();
    dequeCount=-3;
    speed = 5;
    endScreen=false;
    started=false;
    gapY=3;
    pipeList=new ArrayDeque<Pipe>();
}