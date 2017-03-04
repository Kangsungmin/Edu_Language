package edul.compiler.expression.test;

import edul.compiler.expression.graph.Graph_edu;
import edul.compiler.expression.solve.Solve_edu;

/*
 *  x^2 + 2x + 2 = 1일때
 *  x = -1이라는 것을 찾아내 주는 Class 
 */

public class valueChecker {
	Solve_edu graph ;
	
 public void trans(char []string) // 이항 시키는 Method
 {

	 int j = 0; // 반복문 변수
	 int lenFront = 0, lenRear = 0; // 좌변, 우변 길이
	 len = string.length; // 전체 길이
	 
	 // 좌변, 우변의 길이를 파악하기 위한 코드
	 for(int i = 0 ; i < len; i++)
	 {
	   if(string[i] != '=')
	   {
		   lenFront = i+1; 	  
	   }
	   else if(string[i] == '=')
	   {
		   i = i + 1;
		   lenRear = len-i;
		   break;
	   }
	 }
	 
	 System.out.println("좌 :: "  + lenFront + " 우  :: " + lenRear);
	 out += "좌 :: "  + lenFront + " 우  :: " + lenRear+"\n";
	 // 좌변 우변 길이만큼 char 배열 크기 할당
	 char[] tmpFront = new char[lenFront];
	 char[] tmpRear = new char[lenRear];
	 
	 // 좌변, 우변 String형
	 String Front;
	 String Rear;
	 
	 // 좌변, 우변의 각 계수(2차,1차,상수)
	 int aFront, bFront, cFront;
	 int aRear, bRear, cRear;
	 
	 for(int i = j ; i < len; i++)
	 {
	   if(string[i] != '=')
	   {
		   tmpFront[i] = string[i]; 	  
	   }
	   
	   else if(string[i] == '=')
	   {
		   i++; 
		   j = i;
		   int k = 0;
		   
		   for(i = j; i < len ;i ++)
		   {
			   tmpRear[k] = string[i];
			   k++;
		   }
		   
		   break;
	   }
	 }

	 Front = new String(tmpFront);
	 Rear = new String(tmpRear);
	 
	 System.out.println("좌변 식 :: " + Front + " 우변 식 :: " + Rear);
	 out += "좌변 식 :: " + Front + " 우변 식 :: " + Rear+"\n";
	  graph =  new Solve_edu(Front,0);
	  aFront = graph.a;
	  bFront = graph.b;
	  cFront = graph.c;
	  System.out.println("좌변 계수 a :: " + aFront + " b :: " + bFront+ " c :: " + cFront );
	  out += "좌변 계수 a :: " + aFront + " b :: " + bFront+ " c :: " + cFront+"\n";
	  graph = new Solve_edu(Rear,0);
	  aRear = graph.a;
	  bRear = graph.b;
	  cRear = graph.c;
	  System.out.println("우변 계수 a :: " + aRear + " b :: " + bRear+ " c :: " + cRear );
	  out += "우변 계수 a :: " + aRear + " b :: " + bRear+ " c :: " + cRear +"\n";

	  // 좌변으로 모두 이항
	  aFront = aFront - aRear;
	  bFront = bFront - bRear;
	  cFront = cFront - cRear;

	  System.out.println("이항 후 계수 a :: " + aFront + " b :: " + bFront+ " c :: " + cFront );
	  out += "이항 후 계수 a :: " + aFront + " b :: " + bFront+ " c :: " + cFront+"\n";
	  String Final = new String();
	  
	  // 이항 후 식 제작
	  if(aFront == 0 && bFront == 0 && cFront == 0)
		  Final += "0";
	  
	  if(aFront != 0)
	  {
		  Final += aFront + "x^2";
	  }
	  
	  if(bFront != 0)
	  {
		  if(aFront == 0)
			  Final += bFront + "x";
		  
		  else if(bFront < 0)
			  Final += bFront + "x";	 
		  
		  else
			  Final += "+" + bFront + "x";
	  }
	  
	  if(cFront != 0)
	  {
		  if(cFront == 0)
			  Final +=  cFront;
		 		  
		  else if(cFront < 0)
			  Final += cFront;
		  
		  else
			  Final += "+" + cFront;
	  }
	
	  System.out.println(Final);
	  out += Final+"\n";

	  graph =  new Solve_edu(Final,1);
	  graph.solve();
	  Graph_edu graph2 = new Graph_edu(graph.getA(), graph.getB(), graph.getC(),1);
	  
	  
 }
 public String getResult(){
	 
	 return out;
 }
 public static String out="";
 public static int len;
}