package edul.compiler.expression.varify;



public class Verify_edu {
	
	 // 'x' 이전의 식이 유효한지 검사한다.
	public int recursive_x(char []string, int i)
	{
		if(i >= 0)
		{
			if (('0' <= string[i] && string[i] <= '9') ) // x 이전에 문자가 0~9사이 문자 인지 체크
			{
				i--;
				
				if(i == -1) return 1;
			
				recursive_x(string, i);
			}

			else if (string[i] == '+' || string[i] == '-' || string[i] == '*' || string[i] == '/' || i < 0) // string[0]에서 string[-1]될 때(ex : x+1같은 첫번째 항 고려) 또는, +,-,*,/ 문자 만날 때( 두번째 이상 부터 고려)
			{
				return 1;
			}

			else
			{
				if(string[i] == '=') return 1;
				
				else
				{
					System.out.println("계수의 입력이 잘못되었습니다. 프로그램을 종료합니다." ); // 예외처리 발생
					System.exit(0); // 종료 
				}
			}
		}
		return 1;
	}

	// '^' 다음 식이 유효한지 검사한다.
	public int recursive_next(char []string, int i)
	{
		if(i < len)
		{
			if ('0' <= string[i] && string[i] <= '9')
		
			{
				i++;
				recursive_next(string, i);
			}

			else if (string[i] == '+' || string[i] == '-' || string[i] == '*' || string[i] == '/' || string[i] == '\0') // + 문자 만나거나( 두번째 이상 부터 고려), 쓰레기값 만나거나 (마지막항 고려)
			{

				if (string[i - 1] == '^')
				{
					System.out.println((i+1) + "번째 값 : " + string[i] + "이 이상이 있습니다. 프로그램을 종료합니다."); 
					System.exit(0);
				}
				return 1;
			}

			else
			{
				if(string[i] == '=') return 1;
				
				System.out.println((i+1) + "번째의 차수의 입력이 잘못되었습니다. 프로그램을 종료합니다."); // 예외처리 발생
				System.exit(0); // 종료 
			}
		}
		return 1;
	}

	//Parsing을 통해 식이 옳은지 판별한다.
	public void passer(char []string)
	{
		int i = 0;
		len = string.length;

		// Parsing Start
		for (i = j; i < len; i++)
		{
			// 예외처리
			System.out.println("String :: " + string[i]); 
			if (string[i] != 'x' && string[i] != '^' && string[i] != '+'  && string[i] != '-'  && string[i] != '*'  && string[i] != '/' && !('0' <= string[i] && string[i] <= '9')) //올바른 식이 아닐 경우
			{
				if (string[i] == '=')
				{
					if(i == 0)
					{	System.out.println("양 변에 식이 있는지 확인해주세요. 프로그램을 종료합니다."); System.exit(0); }
					else if(i+1 >= len)
					{	System.out.println("우변에 식이 있는지 확인해주세요. 프로그램을 종료합니다."); System.exit(0); }
					
					j = i + 1;
					notify = 1;
					System.out.println("좌변 식은 이상이 없습니다.");  
					passer(string);// ^다음 차수가 숫자형 문자인지 아닌지 체크
					System.out.println("우변 식은 이상이 없습니다.");  
					break;
				}
				
				System.out.println((i+1) + "번째 " + string[i] +"값이 올바르지 않습니다. 프로그램을 종료합니다."); // 예외처리 발생
				System.exit(0);
			}

			if (string[i] == '^' || string[i] == '+' || string[i] == '-' || string[i] == '*' || string[i] == '/') // 식에서 ^,+,-,*,/ 가 중복해서 나오는 경우
			{
				if(i-1 > 0)
				{
					if (string[i - 1] == '^' || string[i - 1] == '+' || string[i - 1] == '-' || string[i - 1] == '*' || string[i - 1] == '/')
					{
						System.out.println((i+1) + "번째" + string[i] + "값이 올바르지 않습니다. 프로그램을 종료합니다.");  // 예외처리 발생
						System.exit(0);
					}
				}
			}

			// 검사
			if (string[i] == 'x')
			{
				recursive_x(string, i - 1); // x 이전에 무슨 값이 나오는지 검사한다.

				if(i+1 < len)
				{
					if (string[i + 1] == '=')
					{
						if(i+2 >= len)
						{	System.out.println("우변에 식이 있는지 확인해주세요. 프로그램을 종료합니다."); System.exit(0); }
						
						j = i + 2;
						notify = 1;
						System.out.println("좌변 식은 이상이 없습니다.");  
						passer(string);// ^다음 차수가 숫자형 문자인지 아닌지 체크
						System.out.println("우변 식은 이상이 없습니다.");  
						break;
					}
					
					else if (string[i + 1] == '^')
					{
						recursive_next(string, i + 2);// ^다음 차수가 숫자형 문자인지 아닌지 체크
					}
				}
				
			}

		}
		if(notify ==0)
		System.out.println("이 식은 이상이 없습니다.");  
	}
	public static int len;
	public static int j = 0;
	public static int notify = 0; // println에 이용
}