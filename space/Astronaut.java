package space;

public class Astronaut {
	private int x_pos, y_pos, d_value,asNum;
	private int dropH;
	private boolean alive = true, holding = false, fall = false, caught = false;
	public Astronaut(int x, int y, int direction, int num) {
		setValues(x,y, direction, num);
	}
	public void setValues (int x, int y,int direction, int num){
		x_pos = x;
		y_pos = y;
		asNum = num;
	if (direction == 1){
		d_value = -1;
	}else {
		d_value = 1;
	}
		
	}
	public int Number(){//Astronaut number
		return asNum;
	}
	public int getX (){
		return x_pos;
	}
	public int getY (){
		return y_pos;
	}
	public void setX (int newX){
		x_pos = newX;
	}
	public void setY (int newY){
		y_pos = newY;
	}
	public void Move(){
		x_pos += d_value;
		if (x_pos > 1000){
			x_pos -= 1000;
		}else if (x_pos < 0){
			x_pos += 1000;
		}
	}
	public void FallMove(){//Movement when falling
		if (y_pos + 35 >= 500){
			if (dropH <= 200){
				Dead();
			}else {
				y_pos = 460;
			}
			
			fall = false;
		}else {
			y_pos += 5;
		}
	}
	public void FMove(int fValue){//Movement when ship moves
		x_pos += fValue;
	}
	public void resetPos(){//Reseting position positively
		x_pos += 1000;
	}
	public void resetNeg(){//Reseting position negatively
		x_pos -= 1000;
	}
	public boolean CanCatch(){//Checks if player can catch
		return fall;
		
	}
	public boolean isCaught() {//Checks if caught
		return caught;
	}
	public void  Caught() {//Setting to caught
		caught = true;
	}
	public void DropOff() {//Reseting when dropped off
		caught = false;
		fall = false;
	}
	public void Held(){//Setting held
		holding = true;
	}
	public boolean isHeld(){//Checking if it is held by alien
		return holding;
	}
	public boolean isFall(){//returning if falling
		return fall;
	}
	public void Falling(){//setting value to falling
		dropH = y_pos;
		fall = true;
		holding = false;
		caught = false;
	}
	public boolean isDead(){//Checking live status
		return alive;
	}
	public void Dead(){//setting dead
		alive = false;
	}
	public int dropHeight(){//Return height of drop
		return dropH;
	}
	public void teleported(int value){//Teleporting
		x_pos += value;
	}
}
