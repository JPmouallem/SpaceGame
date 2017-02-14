package space;

public class Bullet {
	int x_pos,y_pos, d_value,dv_value;
	boolean created = false;
	boolean player = true;
	int moved = 0;
	public Bullet(int x, int y, int direction,int v_direction, boolean type){
		created = true;
		setValues(x,y,direction,v_direction, type);
	}
	public void setValues (int x, int y, int direction,int v_direction, boolean type){//Setting bullet values
		player = type;
		if (player){
			y_pos = y;
			d_value = direction;
			if (direction == 1){
				x_pos = x + 40;
			}else if (direction == 2){
				x_pos = x - 30;
			}
		}else {
			if (v_direction > 0){
				dv_value = -1;
				y_pos = y - 5;
			}else  if (v_direction < 0){
				dv_value = 1;
				y_pos = y + 30;
			}
			if (direction > 0){
				d_value = -1;
				x_pos = x - 5;
			}else  if (direction < 0){
				d_value = 1;
				x_pos = x + 30;
			}
		}
		
	}
	public int getX(){
		return x_pos;
	}
	public int getY(){
		return y_pos;
	}
	public boolean playerBullet(){//Checking who created the bullet (Player or Alien)
		return player;
	}
	public void Move(){
		if (d_value == 1){
			x_pos += 20;
		}else if (d_value == 2){
			x_pos -= 20;
		}
	}
	public void MoveY(){
		if (dv_value == -1){
			y_pos -= 5;
		}else if (dv_value == 1){
			y_pos += 5;
		}else {
			y_pos += 5;
		}
	}
	public void MoveX(){
		if (d_value == 1){
			x_pos += 10;
		}else if(d_value == -1){
			x_pos -= 10;
		}else {
			x_pos += 10;
		}
	}
	public boolean Expire(){//Bullets expiring
		moved += 1;
		if (moved >= 20){
			return true;
		}
		return false;
	}
	public void teleported(int value){//Teleporting Bullet
		x_pos += value;
	}
}
