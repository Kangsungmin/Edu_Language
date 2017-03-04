package edul.compiler.expression.solve;

public class Solve_edu {
	String in;
	public int a; // Y = aX^2 + bX + c
	public int b;
	public int c;

	public Solve_edu(String input, int check) {

		in = input;
		CalCoe(in); // 계수를 모두 결정함.
	}

	private void CalCoe(String input) {
		// TODO Auto-generated method stub
		String in;
		int position;
		in = input;
		in = in.replace(" ", ""); // 공백 제거
		position = in.indexOf("x^2");
		if (position == -1)
			a = 0;// 값이 없다면 -1 리턴이고 그렇다면 a = 0;
		else {
			a = Coe(in.substring(0, position)); // 계수 문자열 부분을 넘겨서 인티저로 리턴한다.
			in = in.substring(position + 3);// x^2이후 문자열 잘라냄.
		}// 값이 있을 때, position까지 문자열 잘라 낸 후 값 판단 (값 판단 메소드 따로 만들어서 호출)

		position = in.indexOf("x");
		if (position == -1)
			b = 0;
		else {
			b = Coe(in.substring(0, position)); // 계수(b)부분을 넘겨서 인티저로 리턴
			in = in.substring(position + 1);
		}
		if (in.length() == 0)
			c = 0;
		else {
			c = Coe(in); // 상수 문자열 부분을 넘겨서 인티저로 리턴한다.
		}
		// if(position ); // 문자열 처음부터 x까지 잘라낸다.
		// 값이 없다면 -1 리턴이고 그렇다면 b = 0;
		// 잘라낸 값이 양수인지 음수인지 판단 후 b에 저장
		// x이후 문자열 in 에 저장
		// 맨앞의 부호가 +/-인지 판단 후 c에 값 저장
	}
	public int getA(){
		return a;
	}
	public int getB(){
		return b;
	}
	public int getC(){
		return c;
	}

	private int Coe(String substring) {
		// TODO Auto-generated method stub
		int pos;
		int Val = 1;
		if (substring.length() == 0) {// 계수가 없다면
			return 1; // 해당 계수는 0
		}
		pos = substring.indexOf("+");

		if (pos == -1) {// '+'가 없다면
			pos = substring.indexOf("-");
			if (pos == -1) {// 부호가 없다면
				Val = Integer.parseInt(substring); // 부호가 없으므로 value를 정수로 변환
				return Val;
			}
			// -일때,

			substring = substring.substring(pos + 1);// 부호를 자르고 value만 취함
			if (substring.equals(""))
				return -1;
			else {
				Val = Integer.parseInt(substring); //
				return -Val;
			}
		}
		// /+일때,
		// 그런데 만약 +다음 아무것도 없으면
		if (pos + 1 == substring.length())
			return 1;
		else {
			substring = substring.substring(pos + 1);// 부호를 자르고 value만 취함
			Val = Integer.parseInt(substring);
			return +Val;
		}

	}// 계수 결정 메소드 끝.

	public void solve() {

		// 2차식
		if (a != 0) {
			ans1 = (-b + Math.sqrt(b * b - 4 * a * c)) / 2 / a;
			ans2 = (-b - Math.sqrt(b * b - 4 * a * c)) / 2 / a;

			// 근 존재 안함
			if (b * b - 4 * a * c < 0) {
				System.out.println("근이 존재하지 않습니다.");
			}

			// 완전 제곱식일 때
			else if (ans1 == ans2) {
				System.out.println("ans: " + ans1 + " (제곱근)");
				System.out.println("=====완전 제곱식 인수분해 과정=====");

				if (ans1 == 0) {
					System.out.println("인수분해 - 최종 :: " + in);
				}

				else if (ans1 != 0) {
					System.out.println("인수분해 - 1 :: " + in);
					if (a != 1) {
						// a(bx^2+cx+d) 꼴
						System.out.printf("인수분해 - 2 :: %d(x^2 %+.1fx %+.1f)\n",
								a, (double) b / a, (double) c / a);
						// a(x-b)^2 꼴
						System.out.printf("인수분해 - 최종 :: %d(x %+.1f)^2\n", a,
								-ans1);
					} else if (a == 1) {
						// (x-a)^2 꼴
						System.out.printf("인수분해 - 최종 :: (x %+.1f)^2\n", -ans1);
					}
				}
			}

			// 완전 제곱식이 아닐 때
			else {
				System.out.println("ans: " + ans1 + ", " + ans2);
				System.out.println("=====인수분해 과정=====");
				System.out.println("인수분해 - 1 :: " + in);

				// 약분 전 분자 분모 빼내기
				double tmp1 = ans1 * a; // 약분 전 ans1 분자
				double tmp2 = ans2 * a; // 약분 전 ans2 분자

				// 최대공약수 구하기
				GCD getGCD = new GCD();
				int gcd1 = getGCD.InputGCD((int) tmp1, a);
				int gcd2 = getGCD.InputGCD((int) tmp2, a);
				if (gcd1 < 0)
					gcd1 = -gcd1; // 최대공약수는 무조건 양수
				if (gcd2 < 0)
					gcd2 = -gcd2; // 최대공약수는 무조건 양수

				// 약분 후 분자 분모 빼내기
				tmp1 = tmp1 / gcd1; // 약분 후 ans1 분자
				int tmp1_under = a / gcd1; // 약분 후 ans1 분모
				tmp2 = tmp2 / gcd2; // 약분 후 ans2 분자
				int tmp2_under = a / gcd2; // 약분 후 ans2 분모

				if (tmp1_under != 1 && tmp1_under != -1 && tmp2_under != 1
						&& tmp2_under != -1) {

					if (a / (tmp1_under * tmp2_under) == 1)
						System.out.printf(
								"인수분해 - 2 :: (%dx %+.1f)(%dx %+.1f)\n",
								tmp1_under, -tmp1, tmp2_under, -tmp2);

					else if (a / (tmp1_under * tmp2_under) == -1) {
						System.out.printf(
								"인수분해 - 2 :: -(%dx %+.1f)(%dx %+.1f)\n",
								tmp1_under, -tmp1, tmp2_under, -tmp2);

					}

					else
						System.out.printf(
								"인수분해 - 2 :: %.1f(%dx %+.1f)(%dx %+.1f)\n",
								(double) a / (tmp1_under * tmp2_under),
								tmp1_under, -tmp1, tmp2_under, -tmp2);
				}

				else if (tmp1_under != 1 && tmp1_under != -1) {
					if (a / tmp1_under == 1) {
						System.out.printf(
								"인수분해 - 2 :: (%dx %+.1f)(x %+.1f/%d)\n",
								tmp1_under, -tmp1, -tmp2, tmp2_under);

					}

					else if (a / tmp1_under == -1) {
						System.out.printf(
								"인수분해 - 2 :: -(%dx %+.1f)(x %+.1f/%d)\n",
								tmp1_under, -tmp1, -tmp2, tmp2_under);
					}

					else
						System.out.printf(
								"인수분해 - 2 :: %.1f(%dx %+.1f)(x %+.1f/%d)\n",
								(double) a / tmp1_under, tmp1_under, -tmp1,
								-tmp2, tmp2_under);
				}

				else if (tmp2_under != 1 && tmp2_under != -1) {
					if (a / tmp2_under == 1) {
						System.out.printf("인수분해 - 2 :: (x %+.1f)(%dx %+.1f)\n",
								-tmp1, tmp2_under, -tmp2);
					}

					else if (a / tmp2_under == -1) {
						System.out.printf(
								"인수분해 - 2 :: -(x %+.1f)(%dx %+.1f)\n", -tmp1,
								tmp2_under, -tmp2);

					}

					else
						System.out.printf(
								"인수분해 - 2 :: %.1f(x %+.1f)(%dx %+.1f)\n",
								(double) a / tmp2_under, -tmp1, tmp2_under,
								-tmp2);
				}

				// 최종식
				if (ans1 % 1 == 0 && ans2 % 1 != 0) { // ans1 정수 ans2 유리수
					if (a == 1) { // a=1
						if (tmp2 > 0 && tmp2_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: (x %+.1f)(x -%.1f/%d)\n",
									-ans1, tmp2, tmp2_under);
						} else if (tmp2 < 0 && tmp2_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: (x %+.1f)(x -%.1f/%d)\n",
									-ans1, -tmp2, -tmp2_under);
						} else if (tmp2 > 0 && tmp2_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: (x %+.1f)(x +%.1f/%d)\n",
									-ans1, tmp2, -tmp2_under);
						} else if (tmp2 < 0 && tmp2_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: (x %+.1f)(x +%.1f/%d)\n",
									-ans1, -tmp2, tmp2_under);
						}
					} else if (a == -1) { // a=-1
						if (tmp2 > 0 && tmp2_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: -(x %+.1f)(x -%.1f/%d)\n",
									-ans1, tmp2, tmp2_under);
						} else if (tmp2 < 0 && tmp2_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: -(x %+.1f)(x -%.1f/%d)\n",
									-ans1, -tmp2, -tmp2_under);
						} else if (tmp2 > 0 && tmp2_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: -(x %+.1f)(x +%.1f/%d)\n",
									-ans1, tmp2, -tmp2_under);
						} else if (tmp2 < 0 && tmp2_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: -(x %+.1f)(x +%.1f/%d)\n",
									-ans1, -tmp2, tmp2_under);
						}
					} else { // a!=1 , a!=-1
						if (tmp2 > 0 && tmp2_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: %d(x %+.1f)(x -%.1f/%d)\n",
									a, -ans1, tmp2, tmp2_under);
						} else if (tmp2 < 0 && tmp2_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: %d(x %+.1f)(x -%.1f/%d)\n",
									a, -ans1, -tmp2, -tmp2_under);
						} else if (tmp2 > 0 && tmp2_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: %d(x %+.1f)(x +%.1f/%d)\n",
									a, -ans1, tmp2, -tmp2_under);
						} else if (tmp2 < 0 && tmp2_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: %d(x %+.1f)(x +%.1f/%d)\n",
									a, -ans1, -tmp2, tmp2_under);
						}
					}
				} else if (ans1 % 1 != 0 && ans2 % 1 == 0) { // ans1 유리수
																// ans2 정수
					if (a == 1) { // a=1
						if (tmp1 > 0 && tmp1_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: (x -%.1f/%d)(x %+.1f)\n",
									tmp1, tmp1_under, -ans2);
						} else if (tmp1 < 0 && tmp1_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: (x -%.1f/%d)(x %+.1f)\n",
									-tmp1, -tmp1_under, -ans2);
						} else if (tmp1 > 0 && tmp1_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: (x +%.1f/%d)(x %+.1f)\n",
									tmp1, -tmp1_under, -ans2);
						} else if (tmp1 < 0 && tmp1_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: (x +%.1f/%d)(x %+.1f)\n",
									-tmp1, tmp1_under, -ans2);
						}
					} else if (a == -1) { // a=-1
						if (tmp1 > 0 && tmp1_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: -(x -%.1f/%d)(x %+.1f)\n",
									tmp1, tmp1_under, -ans2);
						} else if (tmp1 < 0 && tmp1_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: -(x -%.1f/%d)(x %+.1f)\n",
									-tmp1, -tmp1_under, -ans2);
						} else if (tmp1 > 0 && tmp1_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: -(x +%.1f/%d)(x %+.1f)\n",
									tmp1, -tmp1_under, -ans2);
						} else if (tmp1 < 0 && tmp1_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: -(x +%.1f/%d)(x %+.1f)\n",
									-tmp1, tmp1_under, -ans2);
						}
					} else { // a!=1 , a!=-1
						if (tmp1 > 0 && tmp1_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: %d(x -%.1f/%d)(x %+.1f)\n",
									a, tmp1, tmp1_under, -ans2);
						} else if (tmp1 < 0 && tmp1_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: %d(x -%.1f/%d)(x %+.1f)\n",
									a, -tmp1, -tmp1_under, -ans2);
						} else if (tmp1 > 0 && tmp1_under < 0) {
							System.out.printf(
									"인수분해 - 최종 :: %d(x +%.1f/%d)(x %+.1f)\n",
									a, tmp1, -tmp1_under, -ans2);
						} else if (tmp1 < 0 && tmp1_under > 0) {
							System.out.printf(
									"인수분해 - 최종 :: %d(x +%.1f/%d)(x %+.1f)\n",
									a, -tmp1, tmp1_under, -ans2);
						}
					}
				} else if (ans1 % 1 == 0 && ans2 % 1 == 0) { // ans1 정수 ans2
																// 정수
					if (a == 1) { // a=1
						System.out.printf("인수분해 - 최종 :: (x %+.1f)(x %+.1f)\n",
								-ans1, -ans2);
					} // a=-1
					else if (a == -1) {
						System.out.printf("인수분해 - 최종 :: -(x %+.1f)(x %+.1f)\n",
								-ans1, -ans2);
					} // a!=1 && a!=-1
					else {
						System.out.printf(
								"인수분해 - 최종 :: %d(x %+.1f)(x %+.1f)\n", a,
								-ans1, -ans2);
					}
				} else { // ans1 유리수 ans2 유리수
					if (ans1 > 0 && ans2 > 0) {
						if (a == 1) { // a=1
							System.out.printf(
									"인수분해 - 최종 :: (x -%.1f/%d)(x -%.1f/%d)\n",
									Math.abs(tmp1), Math.abs(tmp1_under),
									Math.abs(tmp2), Math.abs(tmp2_under));
						} // a=-1
						else if (a == -1) {
							System.out.printf(
									"인수분해 - 최종 :: -(x -%.1f/%d)(x -%.1f/%d)\n",
									Math.abs(tmp1), Math.abs(tmp1_under),
									Math.abs(tmp2), Math.abs(tmp2_under));
						} // a!=1 && a!=-1
						else {
							System.out
									.printf("인수분해 - 최종 :: %d(x -%.1f/%d)(x -%.1f/%d)\n",
											a, Math.abs(tmp1),
											Math.abs(tmp1_under),
											Math.abs(tmp2),
											Math.abs(tmp2_under));
						}

					} else if (ans1 < 0 && ans2 < 0) {
						if (a == 1) { // a=1
							System.out.printf(
									"인수분해 - 최종 :: (x +%.1f/%d)(x +%.1f/%d)\n",
									Math.abs(tmp1), Math.abs(tmp1_under),
									Math.abs(tmp2), Math.abs(tmp2_under));
						} // a=-1
						else if (a == -1) {
							System.out.printf(
									"인수분해 - 최종 :: -(x +%.1f/%d)(x +%.1f/%d)\n",
									Math.abs(tmp1), Math.abs(tmp1_under),
									Math.abs(tmp2), Math.abs(tmp2_under));
						} // a!=1 && a!=-1
						else {
							System.out
									.printf("인수분해 - 최종 :: %d(x +%.1f/%d)(x +%.1f/%d)\n",
											a, Math.abs(tmp1),
											Math.abs(tmp1_under),
											Math.abs(tmp2),
											Math.abs(tmp2_under));
						}

					} else if (ans1 > 0 && ans2 < 0) {
						if (a == 1) { // a=1
							System.out.printf(
									"인수분해 - 최종 :: (x -%.1f/%d)(x +%.1f/%d)\n",
									Math.abs(tmp1), Math.abs(tmp1_under),
									Math.abs(tmp2), Math.abs(tmp2_under));
						} // a=-1
						else if (a == -1) {
							System.out.printf(
									"인수분해 - 최종 :: -(x -%.1f/%d)(x +%.1f/%d)\n",
									Math.abs(tmp1), Math.abs(tmp1_under),
									Math.abs(tmp2), Math.abs(tmp2_under));
						} // a!=1 && a!=-1
						else {
							System.out
									.printf("인수분해 - 최종 :: %d(x -%.1f/%d)(x +%.1f/%d)\n",
											a, Math.abs(tmp1),
											Math.abs(tmp1_under),
											Math.abs(tmp2),
											Math.abs(tmp2_under));
						}

					} else if (ans1 < 0 && ans2 > 0) {
						if (a == 1) { // a=1
							System.out.printf(
									"인수분해 - 최종 :: (x +%.1f/%d)(x -%.1f/%d)\n",
									Math.abs(tmp1), Math.abs(tmp1_under),
									Math.abs(tmp2), Math.abs(tmp2_under));
						} // a=-1
						else if (a == -1) {
							System.out.printf(
									"인수분해 - 최종 :: -(x +%.1f/%d)(x -%.1f/%d)\n",
									Math.abs(tmp1), Math.abs(tmp1_under),
									Math.abs(tmp2), Math.abs(tmp2_under));
						} // a!=1 && a!=-1
						else {
							System.out
									.printf("인수분해 - 최종 :: %d(x +%.1f/%d)(x -%.1f/%d)\n",
											a, Math.abs(tmp1),
											Math.abs(tmp1_under),
											Math.abs(tmp2),
											Math.abs(tmp2_under));
						}

					}
				}

			}
		}
		if (a == 0) {
			ans3 = (double) -c / b;
			System.out.println("ans: " + ans3);

			if (ans3 == 0) {
				System.out.println("인수분해 식 - 최종 :: " + in);
			}

			else {

				if (b == 1) {
					System.out.println("인수분해 식 - 최종 :: " + in);
				} else if (b == -1) {
					System.out.println("인수분해 식 - 1 :: " + in);
					System.out.printf("인수분해 식- 최종 :: -(x %+.1f\n", -ans3);
				} else {
					System.out.println("인수분해 식 - 1 :: " + in);
					System.out.printf("인수분해 식 - 최종 :: %d(x %+.1f)\n", b, -ans3);
				}
			}
		}
		
	}

	// public static int a,b,c; //Y = aX^2 + bX + c
	public static double ans1, ans2, ans3;
}