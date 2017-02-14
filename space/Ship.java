/*
 	***NOTE: Known issues, as you are shooting and going left, you cannot move upwards (spacebar + left arrow key)***
Controls:
Arrows Keys - Movement
Spacebar - Shooting
D - Teleport to a random spot
S - Bomb range of 300 (150 Left, 150 Right, 150 Upwards, 150 Downwards)

Instructions:
Shoot Green Aliens - 150 points
Shoot Purple Buffed Aliens - 150 points
Dropping Astronauts off - 150 points
Protect Astronauts (Bottom of screen), if they are abducted, carried to the top of the map by the green alien, it will be changed into a buffed Alien.
If the astronaut is dropped (So you kill the alien carrying it, it will fall you are able to catch it for bonus points, if it falls from a high height it will die upon hitting the ground)
When carrying an astronaut you can drop it off at the bottom of the screen for extra points.
Purple Aliens chase you, if all astronauts die, remaining aliens will randomly transform into buffed aliens (Purple ones)

Rewards:
At 5000 points you gain 1 bomb
At 7500 points you gain 1 life

*/
package space;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Ship extends Applet implements Runnable, KeyListener{
	int posHolder = 250,t_value;
	int score, lives = 3, bombs = 3, teleport = 3;
	int bombsS, livesS;
	int canHit;
	int x_pos = 250,y_pos=250,x_speed=1,y_speed, x_pos2 = 250,y_move, holdingAstro = 50, deadAstros = 0;
	int shootingDelay = 0;
	boolean left,right,up,down,lastUD, holding = false,BuffedAliens = false, areBullets = false;
	boolean shooting = false;
	boolean astrosAlive = true;
	boolean start = true;
	int lSpeed,rSpeed,uSpeed,dSpeed,direction;
	int lastD = 1;
	int buffAlienGen;
	int numOfA = 0;
	int numOfDead = 0;
	Astronaut [] astro = new Astronaut[10];//Array of Astronauts
	ArrayList <Object> aliens = new ArrayList();//Array of Aliens
	ArrayList <Object> aliens2 = new ArrayList();//Array of Buffed Aliens
	ArrayList <Object> bullets = new ArrayList();//Array of Bullets
	Alien enemy;
	Bullet bullet;
	BuffAlien enemy2;
	Image backImage;
	Image charImage;
	BufferedImage image=null;
	BufferedImage ai = null;

	Thread th;


	public void init() {
		this.setSize(500,500);
		addKeyListener(this);
		backImage = getImage (getCodeBase (), "space2.png");
		try
		{
			image = ImageIO.read(new File("space2.png"));
			ai = ImageIO.read(new File("Defender Graphics.png"));
		}
		catch (IOException z)
		{
			System.out.println("Problem loading background");
			System.out.println(z);
		}
	}

	public void start() {
		th = new Thread(this);
		th.start();
	}
	public void stop() {

	}

	public void destroy() {

	}

	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		while (true) { 
			if (lives <= 0) {//Restarting Game
				JOptionPane.showMessageDialog(null, "You lost! Final Score: " + score + "\nPress OK to restart!");
				reset();
			}
			if (numOfDead >= 50) {
				JOptionPane.showMessageDialog(null, "You Won! Final Score: " + score + "\nPress OK to restart!");
				reset();
			}
			if (start) {
				for (int z = 0; z < 10;z++){ //Spawning Astronauts
					astro[z] = new Astronaut((int)(Math.random()*500) + 1, 460,(int)(Math.random()*2) + 1, z);
				}
				start = false;
			}

			if (lives >= 1){
				if (canHit <= 19) {
					canHit += 1;
				}


				if (bombsS / 5000 == 1){//Checks if player earned an extra bomb
					bombsS -= 5000;
					bombs += 1;
				}
				if (livesS / 7500 == 1){//Checks if player earned an extra life
					livesS -= 7500;
					lives += 1;
				}
				if (shootingDelay >= 1){//Shooting Delay
					shootingDelay += 1;
					if (shootingDelay == 5){
						shootingDelay = 0;
					}
				}else if (shootingDelay == 0 && shooting){
					shootingDelay += 1;
					createBullet(x_pos,y_pos + 20,lastD,0,true);
				}

				if (y_pos >= 470 && holding) {//If astronaut was dropped off
					astro[holdingAstro].DropOff();
					score += 150;
					bombsS += 150;
					livesS += 150;
					holding = false;
					astro[holdingAstro].setY(460);
				}

				if (numOfA != 50){ //Spawning aliens
					if ((int)(Math.random()*30) + 1 == 2){
						numOfA += 1;
						aliens.add(enemy = new Alien((int)(Math.random()*1000),(int)(Math.random()*400),(int)(Math.random()*2) + 1,(int)(Math.random()*2) + 1));

					}
				}
				if (astrosAlive){
					for (int z = 0; z < 10;z++){ //Checking if astronauts are dead
						if (!astro[z].isDead()){
							deadAstros += 1;
						}
					}
					if (deadAstros >= 10){
						astrosAlive = false;
					}else {
						deadAstros = 0;
					}

				}else if (!astrosAlive){//Spawning buffed aliens random when all astronauts are dead
					if (aliens.size() > 0){
						buffAlienGen = (int)(Math.random()*aliens.size());
						Alien mover = (Alien)aliens.get(buffAlienGen);
						if((int)(Math.random()*30) + 1 == 1){
							createBuffAlien(mover.getX(),mover.getY());
							aliens.remove(buffAlienGen);
						}
					} 

				}
				for (int t = 0; t < bullets.size();t++){//Moving Bullets
					Bullet finder = (Bullet)bullets.get(t);
					if (finder.playerBullet()){
						finder.Move();
					}else {
						finder.MoveY();
						finder.MoveX();
					}
					if (finder.Expire()){
						bullets.remove(t);
					}

				}

				if (BuffedAliens){//Moving Buffed Aliens
					for (int t = 0; t < aliens2.size();t++){
						BuffAlien finder = (BuffAlien)aliens2.get(t);
						if (finder.Draw()){
							finder.MoveX(x_pos);
							finder.MoveY(y_pos);
							finder.FMove(x_speed);
						}
					}
				}


				for (int k = 0; k < 10;k++){ //Moving astronauts (Constant)
					if (!astro[k].isHeld()){
						if (astro[k].isFall()){
							astro[k].FallMove();
						}else{
							astro[k].Move();
						}
					}
					astro[k].FMove(x_speed);
				}

				if (right){ //Determining speed to the right and left
					if (rSpeed > -20){
						rSpeed -= 1;
						if (lSpeed > 0){
							lSpeed -=1;
						}

					}
					x_speed = lSpeed + rSpeed;
				}
				if (left){//Determining speed to the left and right
					if (lSpeed < 20){
						lSpeed += 1;
						if (rSpeed < 0){
							rSpeed += 1;
						}

					}
					x_speed = lSpeed + rSpeed;
				}
				
				if (down){//Determining speed downward
					if (dSpeed < 10){
						dSpeed += 1;
						y_speed = dSpeed;
					}	
				}
				if (up){//Determining speed upwards
					if (uSpeed > -10){
						uSpeed -= 1;
						y_speed = uSpeed;

					}	
				}
				if(!(down && up && left && right)) {//reducing Speed

					if (rSpeed <= -1 && !right){
						rSpeed += 1;
					}

					if (lSpeed >= 1 && !left){
						lSpeed -= 1;
					}

					if (uSpeed <= -1 && !up){
						uSpeed += 1;
					}

					if (dSpeed >= 1&& !down){
						dSpeed -= 1;
					}

					y_speed = dSpeed + uSpeed;
					if (y_pos + y_speed <= 10){
						y_pos = 10;
					}else if (y_pos + y_speed >= 460){
						y_pos = 460;
					}
					x_speed = lSpeed + rSpeed;
				}

				for (int t = 0; t < bullets.size();t++){//Aliens Bullets
					Bullet finder = (Bullet)bullets.get(t);
					if (!finder.playerBullet()){
						if ((x_pos <= finder.getX() && x_pos + 40 >= finder.getX() && y_pos <= finder.getY() && y_pos + 30 >= finder.getY() && canHit >= 20) || (x_pos <= finder.getX() + 5 && x_pos + 40 >= finder.getX() + 5 && y_pos <= finder.getY() + 5 && y_pos + 30 >= finder.getY() + 5 && canHit >= 20)){
							if (holding){	
								astro[holdingAstro].Falling();
							}
							lives -= 1;
							canHit = 0;	
							bullets.remove(t);
							JOptionPane.showMessageDialog(null, "Shot By Alien. Press Ok To Respawn!");
							respawn();
						}	
					}

				}

				for (int o = 0; o < aliens2.size();o++){//Crashing into BuffAliens & Shooting BuffAliens
					BuffAlien mover = (BuffAlien)aliens2.get(o);
					if (mover.Draw()){
						if ((mover.getX() <= x_pos && mover.getX()+ 40 >= x_pos && mover.getY() <= y_pos && mover.getY() + 35 >= y_pos && canHit >= 20) || (mover.getX() <= x_pos + 40 && mover.getX()+ 40 >= x_pos + 40 && mover.getY() <= y_pos + 30 && mover.getY() + 35 >= y_pos + 30 && canHit >= 20)){
							lives -= 1;
							canHit = 0;

							if (holding) {
								holding = false;
								astro[holdingAstro].Falling();
							}
							mover.Dead();
							aliens2.remove(o);
							numOfDead += 1;
							JOptionPane.showMessageDialog(null, "Crashed Into A Mutant. Press Ok To Respawn!");
							respawn();
						}else{
							for (int t = 0; t < bullets.size();t++){ // Shooting Buffed Aliens
								Bullet finder = (Bullet)bullets.get(t);
								if (finder.playerBullet()){
									if ((mover.getX() <= finder.getX() && mover.getX()+ 40 >= finder.getX() && mover.getY() <= finder.getY() && mover.getY() + 35 >= finder.getY() && canHit >= 20) || (mover.getX() <= finder.getX() + 30 && mover.getX()+ 40 >= finder.getX() + 30 && mover.getY() <= finder.getY() + 2 && mover.getY() + 35 >= finder.getY() + 2 && canHit >= 20)){
										aliens2.remove(o);
										numOfDead += 1;
										bullets.remove(t);
										score += 150;
										bombsS += 150;
										livesS += 150;
									}
								}

							}
						}
					}
				}


				for (int o = 0; o < aliens.size();o++){//Crashing into Aliens & Shooting Aliens
					Alien mover = (Alien)aliens.get(o);
					if (mover.Draw()){
						if ((mover.getX() <= x_pos && mover.getX()+ 40 >= x_pos && mover.getY() <= y_pos && mover.getY() + 35 >= y_pos && canHit >= 20) || (mover.getX() <= x_pos + 40 && mover.getX()+ 40 >= x_pos + 40 && mover.getY() <= y_pos + 30 && mover.getY() + 35 >= y_pos + 30 && canHit >= 20)){
							lives -= 1;
							canHit = 0;
							if (holding) {
								holding = false;
								astro[holdingAstro].Falling();
							}
							if (mover.isHolding()){
								astro[mover.HoldingAstro()].Falling();
								mover.Dropping();
							}
							mover.Alive();
							aliens.remove(o);
							numOfDead += 1;
							JOptionPane.showMessageDialog(null, "Crashed Into An Alien. Press Ok To Respawn!");
							respawn();

						}else {//Shooting Aliens
							for (int t = 0; t < bullets.size();t++){
								Bullet finder = (Bullet)bullets.get(t);
								if (finder.playerBullet()){
									if ((mover.getX() <= finder.getX() && mover.getX()+ 40 >= finder.getX() && mover.getY() <= finder.getY() && mover.getY() + 35 >= finder.getY()) || (mover.getX() <= finder.getX() + 30 && mover.getX()+ 40 >= finder.getX() + 30 && mover.getY() <= finder.getY() + 2 && mover.getY() + 35 >= finder.getY() + 2) || (mover.getX() <= finder.getX() + 15 && mover.getX()+ 40 >= finder.getX() + 15 && mover.getY() <= finder.getY() + 1 && mover.getY() + 35 >= finder.getY() + 1)){
										if (mover.isHolding()){
											astro[mover.HoldingAstro()].Falling();
											mover.Dropping();
										}
										mover.Alive();
										aliens.remove(o);
										numOfDead += 1;
										score += 150;
										bombsS += 150;
										livesS += 150;
										bullets.remove(t);
									}	
								}

							}
						}
						for (int h = 0; h < 10;h++){ //Catching Astronauts
							if ((astro[h].getX() <= x_pos && astro[h].getX()+ 10 >= x_pos && astro[h].getY() <= y_pos && astro[h].getY() + 30 >= y_pos && astro[h].CanCatch()) || (astro[h].getX() <= x_pos + 40 && astro[h].getX()+ 10 >= x_pos + 40 && astro[h].getY() <= y_pos + 30 && astro[h].getY() + 30 >= y_pos + 30 && astro[h].CanCatch())){
								if (!astro[h].isCaught() && !holding) {
									holding = true;
									astro[h].Caught();	
									holdingAstro = h;
								}
							}
						}
						for (int h = 0; h < 10;h++){ //Shooting Astronauts
							for (int t = 0; t < bullets.size();t++){
								Bullet finder = (Bullet)bullets.get(t);
								if (finder.playerBullet()){
									if (astro[h].isDead()){
										if ((astro[h].getX() <= finder.getX() && astro[h].getX()+ 10 >= finder.getX() && astro[h].getY() <= finder.getY() && astro[h].getY() + 30 >= finder.getY()) || (astro[h].getX() <= finder.getX() + 30 && astro[h].getX()+ 10 >= finder.getX() + 30 && astro[h].getY() <= finder.getY() + 2 && astro[h].getY() + 30 >= finder.getY() + 2) || (astro[h].getX() <= finder.getX() + 15 && astro[h].getX()+ 10 >= finder.getX() + 15 && astro[h].getY() <= finder.getY() + 1 && astro[h].getY() + 30 >= finder.getY() + 1)){
											if (astro[h].isHeld()){
												for (int u = 0; u < aliens.size();u++){//Checking Who Is Holding It
													Alien checking = (Alien)aliens.get(u);
													if (checking.HoldingAstro() == h){
														checking.Dropping();
													}
												}

											}
											astro[h].Dead();
											bullets.remove(t);
										}
									}
								}


							}


						}
						//Aliens shooting range
						if ((x_pos + 40 >= mover.getX() && x_pos - 40 <= mover.getX() && y_pos + 30 >= mover.getY()&& y_pos - 30 <= mover.getY()) || (x_pos + 40 >= mover.getX() + 40 && x_pos - 40 <= mover.getX() + 40 && y_pos + 30 >= mover.getY() + 35 && y_pos - 30 <= mover.getY() + 35)){
							if ((int)(Math.random()*50) + 1 == 1){
								createBullet(mover.getX(),mover.getY(), (mover.getX() + 15 - x_pos + 5),(mover.getY() + 15 - y_pos + 5),false);
							}


						}

						for (int k = 0; k < 10;k++){//Abducting Astronauts
							if ((mover.getX() <= astro[k].getX() && mover.getX()+ 40 >= astro[k].getX() && mover.getY() <= astro[k].getY() && mover.getY() + 35 >= astro[k].getY()) || (mover.getX() <= astro[k].getX() + 10 && mover.getX()+ 40 >= astro[k].getX() + 10 && mover.getY() <= astro[k].getY() + 30 && mover.getY() + 35 >= astro[k].getY() + 30)){
								if (!astro[k].isHeld() && !astro[k].isCaught()){
									if (!mover.isHolding() && mover.Draw()){
										astro[k].Held();
										mover.Holding(k);
									}
								}
							}
						}

						if (mover.getY() <= 10 && mover.isHolding()){//Calling to create a buffed alien
							createBuffAlien(mover.getX(),mover.getY());
							astro[mover.HoldingAstro()].Dead();
							mover.Alive();
							aliens.remove(o);
						}
						mover.MoveX();//Moving Aliens
						mover.ChangeX((int)(Math.random()*100) + 1);
						mover.ChangeY((int)(Math.random()*100) + 1);
						mover.MoveY();
						mover.FMove(x_speed);
					}	
				}
				repaint();
				try { 
					Thread.sleep(50);
				} catch (InterruptedException ex) {
				}
			}
		}
	}
	public void createBuffAlien(int x, int y){//Creating Buffed Aliens
		aliens2.add(enemy2 = new BuffAlien(x, y));
		BuffedAliens = true;
		for (int t = 0; t < aliens2.size();t++){
			BuffAlien finder = (BuffAlien)aliens2.get(t);
			finder.Alive();
		}
	}
	public void createBullet(int x, int y, int d, int dV, boolean type){
		if (type == false){
			bullets.add(bullet = new Bullet(x,y,d,dV, false));//Creating Alien Bullets
		}else {
			bullets.add(bullet = new Bullet(x,y,d,dV, true));//Creating Ship Bullets
		}

	}
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		if (posHolder - x_pos <0){//reseting Position
			posHolder = 1000;
		}
		if (x_pos2 - 250 < 0 && x_pos2 + 250 <= 0 ){//Scrolling of astronauts
			x_pos2 += 1000;
			for (int i= 0; i < 10;i++){
				astro[i].resetNeg();
			}
		}else if (x_pos2 - 250 >= 1000 && x_pos2 + 250 > 1000 ){
			x_pos2 -= 1000;
			for (int i= 0; i < 10;i++){
				astro[i].resetPos();
			}
		}

		if (x_pos2 - 250 < 0){//Scrolling
			g.drawImage(image, 0,0,500,500,x_pos2 + 750,0,x_pos2 + 1250,500, this);
		}else if (x_pos2 + 250 > 1000){
			g.drawImage(image, 0,0,500,500,x_pos2 - 1250,0,x_pos2 -750,500, this);
		}
		g.drawImage(image, 0,0,500,500,x_pos2 - 250,0,250 + x_pos2,500, this);


		if ((x_speed < 0 && x_pos <= 20) || (x_speed > 0 && x_pos >= 440) ){
			if (x_speed < 0) {//Ship Facing Right
				g.drawImage(ai,  x_pos,y_pos += y_speed,  x_pos + 40,y_pos + 30  ,  0,55  ,  60,80, this);
			}else if (x_speed > 0){//Ship Facing Left
				g.drawImage(ai,  x_pos,y_pos += y_speed,  x_pos + 40,y_pos + 30  ,  60,55  ,  0,80, this);
			}else {
				if (lastD == 1) {//Ship Facing Right
					g.drawImage(ai,  x_pos,y_pos += y_speed,  x_pos + 40,y_pos + 30  ,  0,55  ,  60,80, this);
				}else {//Ship Facing Left
					g.drawImage(ai,  x_pos,y_pos += y_speed,  x_pos + 40,y_pos + 30  ,  60,55  ,  0,80, this);
				}
			}

			x_pos2 -= x_speed;
		}else {
			if (x_speed < 0) {//Ship Facing Right
				g.drawImage(ai,  x_pos += x_speed,y_pos += y_speed,  x_pos + 40,y_pos + 30  ,  0,55  ,  60,80, this);
			}else if (x_speed > 0){//Ship Facing Left
				g.drawImage(ai,  x_pos += x_speed,y_pos += y_speed,  x_pos + 40,y_pos + 30  ,  60,55  ,  0,80, this);
			}else {
				if (lastD == 1) {//Ship Facing Right
					g.drawImage(ai,  x_pos += x_speed,y_pos += y_speed,  x_pos + 40,y_pos + 30  ,  0,55  ,  60,80, this);
				}else {//Ship Facing Left
					g.drawImage(ai,  x_pos += x_speed,y_pos += y_speed,  x_pos + 40,y_pos + 30  ,  60,55  ,  0,80, this);
				}
			}
			x_pos2 -= x_speed;
		}

		Font myFont = new Font("Arial", Font.BOLD, 20);//Top Left Text (Score, Lives, Bombs, teleporting)
		g.setFont(myFont);
		g.drawString("Score: " + score ,5,70);
		g.drawString("Lives: " + lives ,5,50);
		Font myFont2 = new Font("Arial", Font.BOLD, 15);

		g.setFont(myFont2);
		g.drawString("Teleport: " + teleport ,5,15);
		g.drawString("Bombs: " + bombs ,5,30);

		for (int z = 0; z < 10;z++){//Drawing Astronauts Walking
			if (astro[z].isDead()){
				if (!astro[z].isHeld() && !astro[z].isCaught()){
					g.drawImage(ai,  astro[z].getX(),astro[z].getY() ,  10 + astro[z].getX(),30 + astro[z].getY()  ,  170,10  ,  180,40, this);
				}else if (astro[z].isHeld()){//Drawing Astronauts Under Aliens
					for (int o = 0; o < aliens.size();o++){
						Alien locator = (Alien)aliens.get(o);
						if ( z == locator.HoldingAstro() && locator.Draw()){
							astro[z].setX(locator.getX() + 15);
							astro[z].setY(locator.getY() + 30);
							g.drawImage(ai,  astro[z].getX(),astro[z].getY() ,  10 + astro[z].getX(),30 + astro[z].getY()  ,  170,10  ,  180,40, this);
						}
					}
				}else if (astro[z].isCaught()){//Drawing Astronauts Under Ship
					astro[z].setX(x_pos + 15);
					astro[z].setY(y_pos + 25);
					g.drawImage(ai,  astro[z].getX(),astro[z].getY() ,  10 + astro[z].getX(),30 + astro[z].getY()  ,  170,10  ,  180,40, this);
				}
			}

		}


		for (int o = 0; o < aliens.size();o++){//Drawing Aliens
			Alien locator = (Alien)aliens.get(o);

			if (locator.Draw()){
				g.drawImage(ai,  locator.getX(),locator.getY()  ,  40 + locator.getX(),35 + locator.getY()  ,  45,10  ,  85,45, this);
			}
		}
		for (int o = 0; o < bullets.size();o++){//Drawing Bullets
			Bullet locator = (Bullet)bullets.get(o);

			if (locator.playerBullet()){
				g.fillRect(locator.getX(), locator.getY(), 30, 2);
			}else{
				g.fillRect(locator.getX(), locator.getY(), 5, 5);
			}
		}

		if (BuffedAliens){//Drawing Buffed Aliens
			for (int t = 0; t < aliens2.size();t++){
				BuffAlien finder = (BuffAlien)aliens2.get(t);
				if (finder.Draw()){			
					g.drawImage(ai,  finder.getX(),finder.getY()  ,  40 + finder.getX(),35 + finder.getY()  ,  0,10  ,  40,45, this);


				}
			}
		}


	}
	private Image dbImage;
	private Graphics dbg;

	/** Update - Method, implements double buffering */
	public void update(Graphics g) { // initialize buffer
		if (dbImage == null) {
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}

		// clear screen in background
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

		// draw elements in background
		dbg.setColor(getForeground());
		paint(dbg);

		// draw image on the screen
		g.drawImage(dbImage, 0, 0, this);
	}
	public static void main(String[] args) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 38 && up==false){//up key
			direction = 3;
			up = true;
		}
		if (e.getKeyCode() == 39&& right==false){//right key
			direction = 1;
			lastD = 1;
			right = true;
		}
		if (e.getKeyCode() == 40 && down==false){ //down key
			direction = 4;
			down = true;
		}
		
		if (e.getKeyCode() == 37 && left==false){//left key
			direction = 2;
			
			lastD = 2;
			left = true;
		}
		if (e.getKeyCode() == 32 && shooting==false){//spacebar key
			shooting = true;	
		}
		if (e.getKeyCode() == 83){ //s key - Bomb
			if (bombs >= 1){
				bombs -= 1;
				for (int o = 0; o < aliens.size();o++){//Bomb Hitting  Aliens
					Alien mover = (Alien)aliens.get(o);
					if ((x_pos + 150 >= mover.getX() && x_pos - 150 <= mover.getX() && y_pos + 150 >= mover.getY()&& y_pos - 150 <= mover.getY()) || (x_pos + 150 >= mover.getX() + 40 && x_pos - 150 <= mover.getX() + 40 && y_pos + 150 >= mover.getY() + 35 && y_pos - 150 <= mover.getY() + 35)){
						if (mover.isHolding()){
							astro[mover.HoldingAstro()].Falling();
							mover.Dropping();
						}
						score += 150;
						bombsS += 150;
						livesS += 150;
						aliens.remove(o);
						numOfDead += 1;
					}
				}
				for (int o = 0; o < aliens2.size();o++){//Bomb Hitting Buffed Aliens
					BuffAlien mover = (BuffAlien)aliens2.get(o);
					if ((x_pos + 150 >= mover.getX() && x_pos - 150 <= mover.getX() && y_pos + 150 >= mover.getY()&& y_pos - 150 <= mover.getY()) || (x_pos + 150 >= mover.getX() + 40 && x_pos - 150 <= mover.getX() + 40 && y_pos + 150 >= mover.getY() + 35 && y_pos - 150 <= mover.getY() + 35)){
						aliens2.remove(o);
						numOfDead += 1;
					}
				}

			}

		}
		if (e.getKeyCode() == 68){//d - Teleport
			if (teleport >= 1){
				teleport -= 1;	
				t_value = (int)(Math.random()*460) + 40;
				x_pos = t_value;
				x_pos2 = x_pos - 250;
				for (int o = 0; o < aliens.size();o++){ //Alien Position Adjustment
					Alien mover = (Alien)aliens.get(o);
					mover.teleported(t_value);
				}
				for (int o = 0; o < aliens2.size();o++){//Buffed Aliens Position Adjustment
					BuffAlien mover = (BuffAlien)aliens2.get(o);
					mover.teleported(t_value);
				}

				for (int t = 0; t < bullets.size();t++){//Bullets Position Adjustment
					Bullet finder = (Bullet)bullets.get(t);
					finder.teleported(t_value);
				}
				for (int z = 0; z < 10;z++){ //Astronaut Position Adjustment
					astro[z].teleported(t_value);
				}
				y_pos = (int)(Math.random()*460) + 40;
			}
		}

	}
	public void respawn() {
		x_pos2 = 0;
		lSpeed = 0;
		rSpeed = 0;
		uSpeed = 0;
		dSpeed = 0;
		y_speed = 0;
		x_speed = 0;
		right = false;
		left = false;
		up = false;
		down = false;
		shooting = false;
		x_pos = 250;
		y_pos = 235;
	}
	public void reset() {
		numOfA = 0;
		numOfDead = 0;
		holding = false;
		buffAlienGen = 0;
		lastD = 1;
		astrosAlive = true;
		BuffedAliens = false;
		areBullets = false;
		deadAstros = 0;
		holdingAstro = 50;
		y_move = 0;
		canHit = 0;
		bombs = 3;
		teleport = 3;
		lives = 3;
		score = 0;
		bombsS = 0;
		livesS = 0;
		posHolder = 250;
		t_value = 0;
		start = true;
		for (int o = 0; o < aliens.size();o++){ //Alien Position Adjustment
			aliens.remove(o);
		}
		for (int o = 0; o < aliens2.size();o++){//Buffed Aliens Position Adjustment
			aliens2.remove(o);
		}

		for (int t = 0; t < bullets.size();t++){//Bullets Position Adjustment
			bullets.remove(t);
		}
		aliens = new ArrayList();//Array of Aliens
		aliens2 = new ArrayList();//Array of Buffed Aliens
		bullets = new ArrayList();//Array of Bullets
		lives = 3;
		respawn();

		respawn();
	}

	@Override
	public void keyReleased(KeyEvent e) {//Releasing Keys
		// TODO Auto-generated method stub
		if (e.getKeyCode() == 39){//right	
			right = false;
		}
		if (e.getKeyCode() == 40){ //down
			down = false;
			lastUD = true;
		}
		if (e.getKeyCode() == 38){//up
			up = false;
			
			lastUD = false;
		}
		if (e.getKeyCode() == 37){//left
			left = false;
		}
		if (e.getKeyCode() == 32){//spacebar
			shooting = false;
		}
		direction = 0;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
