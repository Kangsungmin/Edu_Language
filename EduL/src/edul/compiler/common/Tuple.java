package edul.compiler.common;

public class Tuple <X,Y>{
	public final X x; 
	public final Y y; 
	public Tuple(X x, Y y) { 
		this.x = x; 
		this.y = y; 
	} 
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "( "+x+", "+y+")";
	}
	public X getX(){
		return x;
	}
	public Y getY(){
		return y;
	}
	public Tuple<Y, X> getSwapped(){
		Tuple <Y,X> swapped = new Tuple<Y, X>(y, x);
		return swapped;
	}
}
