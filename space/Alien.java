package space;

public class Alien {
	int x_pos, y_pos, d_value,asNum,dv_value, astroHeld = 50;
	boolean alive = true, holding = false;
	public Alien(int x, int y, int direction,int directionV) {
		setValues(x,y, direction,directionV);
	}
	public void setValues (int x, int y,int direction, int directionV){
		x_pos = x;
		y_pos = y;
	if (direction == 1){
		d_value = -5;
	}else {
		d_value = 5;
	}
	if (directionV == 1){
		dv_value = -2;
	}else {
		dv_value = 2;
	}
		
	}
	public int getX(){
		return x_pos;
	}
	public int getY(){
		return y_pos;
	}
	public void MoveX(){
		x_pos += d_value;
		if (x_pos > 1000){
			x_pos -= 1000;
		}else if (x_pos < 0){
			x_pos += 1000;
		}
	}
	
	public void MoveY(){
		if ((y_pos + dv_value) > 0 && (y_pos + dv_value) < 490){
			y_pos += dv_value;
		}
	}
	public void Holding(int astroNum){//Holding astronaut
		holding = true;
		astroHeld = astroNum;
	}
	public boolean isHolding(){//Checks if holding
		return holding;
	}
	public int HoldingAstro(){//returns astronaut being held
		return astroHeld;
	}
	public void Dropping(){//Dropping astronaut
		holding = false;
	}
	public void ChangeX(int num){
		if (num <= 1){
			d_value *= -1;
		}
	}
	public void ChangeY(int num){
		if (num <= 2){
			dv_value *= -1;
		}
		if (holding){
			dv_value = -1;
		}
	}
	public void FMove(int fValue){//Movement as ship moves
		x_pos += fValue;
	}
	
	public void Alive(){
		alive = false;
	}
	public boolean Draw(){
		return alive;
	}
	public void teleported(int value){//Teleport
		x_pos += value;
	}
	
}
