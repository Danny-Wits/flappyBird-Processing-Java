PImage backgroundImage; 
String fileLink = "assests/backgroundImage.png";
Bird bird ;
boolean started=false;
void settings(){
   size(displayWidth-100,displayHeight-140);
}
void setup() {
    bird = new Bird();
    backgroundImage=loadImage(fileLink);
    backgroundImage.resize(width,height);
}


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
    bird.physics();
    bird.show();

    if (keyPressed==true) {
       if(key==' '){
        bird.flap();
       }
    }
    if(bird.endGame()){
        textSize(70);
        fill(255,0,0);
        textAlign(CENTER,CENTER);
        text("GAME OVER ",width/2,height/2 );
        bird = new Bird();
        started =false;
    }
}
void keyPressed() {
       
       if (keyCode == ENTER) {
            started=!started;
        }
}