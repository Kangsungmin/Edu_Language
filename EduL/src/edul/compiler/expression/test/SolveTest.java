package edul.compiler.expression.test;

import java.awt.Frame;
import java.util.Scanner;

import edul.compiler.expression.solve.Solve_edu;


public class SolveTest extends Frame{
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      
      //입력을 String으로 넣고 그 식을 그래프로 표현하기
	  int chk = 0;
      String input = new String();
      Scanner scan = new Scanner(System.in);
      char[] input_tmp;
      input = scan.nextLine(); // 수식 입력
      input_tmp = input.toCharArray(); // 수식을 String에서 Char[]로 변환
      

            
      for(int i = 0 ; i < input_tmp.length; i++)
      {
    	  if(input_tmp[i] == '=')
    	  { chk = 1; break;}	  
    	  
    	  else
    		  chk = 2;
      }
	  
      if(chk == 1)
      {
    	  valueChecker a = new valueChecker(); // 해 판별기 생성
    	  a.trans(input_tmp);
      }
      
      else if(chk == 2)
      {
	      Solve_edu b2 = new Solve_edu(input,1);
	      b2.solve();
	      //new Graph_edu(b2.getA(), b2.getB(), b2.getC(), 1);
      }
      
   }

}