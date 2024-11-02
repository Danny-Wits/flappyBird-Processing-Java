import java.util.*;

PImage backgroundImage; 
Queue<PipePair> pipeList = new ArrayDeque<>();
String fileLink = "assests/backgroundImage.png";
ArrayList<Bird> birds = new ArrayList<>();
ArrayList<Bird> birdsToADD = new ArrayList<>();
ArrayList<Bird> birdsToSUB = new ArrayList<>();
Pipe pipe;
float gap = 400;
int gapY=3;
float speed = 5;
boolean started=false;
boolean endScreen=false;
int initialBatch=50;
float learnRate=10;
PFont font ;
void settings(){
   size(displayWidth-300,displayHeight-180);
}
void setup() {
    addbirds(initialBatch);
    backgroundImage=loadImage(fileLink);
    backgroundImage.resize(width,height);   
}
void addbirds(int count ){
    for (int i = 0; i < count; ++i) {
        birds.add(new Bird());
     }
}
void addbirds(int count ,Bird parent ,Bird parent2,Bird parent3){
    if(learnRate>=0.1){
      learnRate-=0.05;
    }
    birds.add(parent);
    birds.add(parent2);
    birds.add(parent3);
    for (int i = 0; i < count; ++i) {
        birds.add(parent.child(0.1));
    }
      for (int i = 0; i < count/2; ++i) {
        birds.add(parent.child());
     }
      for (int i = 0; i < count/2; ++i) {
        birds.add(parent2.child(0.2));
     }
     for (int i = 0; i < count/3; ++i) {
        birds.add(parent3.child());
     }
}
int count = 0;
int dequeCount = -3 ;
int score = 0;
void draw() {
    background(backgroundImage); 
    // if(endScreen){
    //     textSize(70);
    //     fill(255);
    //     textAlign(CENTER,CENTER);
    //     text("GAME OVER \n YOUR SCORE :  "+(dequeCount+3)+"\n PRESS 'r' TO CONTINUE",width/2,200 );
    //     return;
    // }  
    // if(!started){
    //     bird.show();
    //     textSize(70);
    //     fill(255);
    //     textAlign(CENTER);
    //     text("PRESS ENTER TO \nSTART/RESTART\nRESUME/PAUSE",width/2, 200);
    //     return;
    // }
    if(count%(60*gapY)==0){
        addPipes();
        if(dequeCount >=0){
            pipeList.poll();
        }
        dequeCount++;
        count=0;
    }
    count++;
    for (PipePair pp : pipeList) {
        pp.move();
        pp.show(); 
    }
    
    for (Bird  bird : birds) {
    bird.physics();
    bird.network();
    bird.show();
   
    if(birds.size()==3){
        birdsToADD.add(birds.get(0).child());
        birdsToADD.add(birds.get(1).child());
        birdsToADD.add(birds.get(2).child());
     }
      if(bird.endGame(pipeList)){
        birdsToSUB.add(bird);
     }
    }
    if(birdsToADD.size()!=0){
    birds.addAll(birdsToADD);
    birdsToADD.clear();
    }
    if(birdsToSUB.size()!=0){
    for (Bird bird : birdsToSUB) {
        birds.remove(bird);
    }
    }
    if(birds.size()==0){
        addbirds(20,birdsToSUB.get(birdsToSUB.size()-1),birdsToSUB.get(birdsToSUB.size()-2),birdsToSUB.get(birdsToSUB.size()-2));
        addbirds(5);
        reset();

        count = 1;
        birdsToADD.clear();
        birdsToSUB.clear();
    }

  
    score = max(score,dequeCount+3);
    textAlign(CENTER,TOP);
    fill(255);
    textSize(50);
    text("Score : "+ score,width/2,30);
    // if (keyPressed==true) {
    //    if(key==' '){
    //     bird.flap();
    //    }
    // }

    
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
   pipeList.add(createPipePair());
}
PipePair createPipePair(){
    
    float partition = random(0,height-gap);
    Pipe p1 = new Pipe(partition-Pipe.sizeY,true);
    Pipe p2 = new Pipe(partition+gap,false);

    return new PipePair(p1,p2);
}
void reset(){
    // bird = new Bird();
    dequeCount=-3;
   // speed = 5;
  //  endScreen=false;
  // started=false;
    gapY=3;
    pipeList=new ArrayDeque<PipePair>();
   pipeList.add(createPipePair());
}