import java.util.ArrayDeque;
import java.util.Queue;

PImage backgroundImage; 
Queue<Pipe> pipeList = new ArrayDeque<>();
String fileLink = "assests/backgroundImage.png";
Bird bird ;
Pipe pipe;
float gap = 300;
float gapY=3;
float speed = 5;
boolean started=false;
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
    if(!started){
        bird.show();
        textSize(70);
        fill(0);
        textAlign(CENTER);
        text("Press enter to start/resume/pause/restart",width/2, height/2);
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
        textSize(70);
        fill(255,0,0);
        textAlign(CENTER,CENTER);
        text("GAME OVER ",width/2,height/2 );
        bird = new Bird();
        started =false;
        dequeCount=-3;
        speed = 5;
        pipeList=new ArrayDeque<Pipe>();
    }
}
void keyPressed() {
       
       if (keyCode == ENTER) {
            started=!started;
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