package edul.compiler.expression.solve;

public class GCD {
	public int gcd(int max, int min)
	{
	 if (min == 0) 
	 {
	  return max;
	 } 
	 
	 else 
	 {
	  return gcd(min, max % min);
	 }
	}


	public int InputGCD(int a, int b)
	{
		int result = 0;

		if(a >= b) // gcd함수가 max,min이니 순서 위치 바꾸어준다. 
			result = result + gcd(a,b);       
	
		else
			result = result + gcd(b,a);
	   
		return result;
	}
}

