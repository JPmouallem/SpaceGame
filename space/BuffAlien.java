package space;

public class BuffAlien {
	int x_pos, y_pos, d_value,asNum,dv_value, astroHeld = 50;
	boolean alive = false;
	public BuffAlien(int x, int y) {
		setValues(x,y);
	}
	public void setValues (int x, int y){
		x_pos = x;
		y_pos = y;
	}
	public int getX(){
		return x_pos;
	}
	public int getY(){
		return y_pos;
	}
	public void MoveX(int x){
		if (x + 10 >= x_pos && x - 10 <= x_pos){
			d_value = 0;
		}else if (x_pos < x){
			d_value = 10;
		}else if (x_pos > x){
			d_value = -10;
		}
		x_pos += d_value;
		if (x_pos > 1000){
			x_pos -= 1000;
		}else if (x_pos < 0){
			x_pos += 1000;
		}
	}
	public void MoveY(int y){
		if ((y_pos + dv_value) >= 0 && (y_pos + dv_value) <= 470){
			if (y + 5 >= y_pos && y - 5 <= y_pos){
				dv_value = 0;
			}else if (y_pos < y){
				dv_value = 5;
			}else  if (y_pos > y){
				dv_value = -5;
			}
			y_pos += dv_value;
		}else {
			y_pos = 460;
		}
	}
	public void FMove(int fValue){//Moving when ship moves
		x_pos += fValue;
	}
	public void Dead(){
		alive = false;
	}
	public void Alive(){
		alive = true;
	}
	public boolean Draw(){//Should draw
		return alive;
	}
	public void teleported(int value){//Teleporting
		x_pos += value;
	}
}
