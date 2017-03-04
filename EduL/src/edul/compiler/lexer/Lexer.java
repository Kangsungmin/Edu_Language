package edul.compiler.lexer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edul.compiler.common.Tuple;

public class Lexer {
	HashMap<String, String> keyword;
	HashMap<String, String> operator;
	HashMap<String, String> separator;
	
	Integer index_pointer;
	Integer line_index;
	ArrayList<Tuple<String, Object>> scanned;
	public Lexer() {
		// TODO Auto-generated constructor stub
		/*
		 * keyword와 operator와 separator를 HashMap에 넣어둠으로써 접근하여 확인하기 용이해짐.
		 */
//		System.out.println("DEBUG::This is constructor");
		keyword = new HashMap<>();
		operator = new HashMap<>();
		separator = new HashMap<>();

		keyword.put("bool", "BOOLEAN");
		keyword.put("char", "CHAR");
		keyword.put("else", "ELSE");
		keyword.put("false", "FALSE");
		keyword.put("float", "FLOAT");
		keyword.put("if", "IF");
		keyword.put("int", "INT");
		keyword.put("edu", "MAIN");
		keyword.put("true", "TRUE");
		keyword.put("loop", "LOOP");
		
		keyword.put("expr", "EXPR");
		
		keyword.put("verify", "VERIFY");
		keyword.put("solve", "SOLVE");
		keyword.put("graph", "GRAPH");
		keyword.put("calc", "CALC");//verify->solve->graph까지.

		operator.put("=", "ASSIGN");
		operator.put("||", "OR");
		operator.put("&&", "AND");
		operator.put("==", "EQUAL");
		operator.put("!=", "NOT_EQUAL");
		operator.put("<", "<");
		operator.put("<=", "<=");
		operator.put(">", ">");
		operator.put(">=", ">=");
		operator.put("+", "PLUS");
		operator.put("-", "MINUS");
		operator.put("*", "MULT");
		operator.put("/", "DIV");
		operator.put("%", "MOD");
		operator.put("!", "NOT");

		separator.put(":", ":");
		separator.put(";", ";");
		separator.put("{", "{");
		separator.put("}", "}");
		separator.put("(", "(");
		separator.put(")", ")");
		separator.put(".", ".");
		separator.put(",", ",");
		separator.put("[", "[");
		separator.put("]", "]");


	}
	public ArrayList<Tuple<String, Object>> scan_code_piece(String piece) throws Exception{
		ArrayList<Tuple<String, Object>> scanned_piece = new ArrayList<>();
		index_pointer = 0;
		/*
		 * 나누어진 코드 조각조각을 분석하는 method
		 */
		
		for(;index_pointer<piece.length();index_pointer++){
			
			char ch = piece.charAt(index_pointer);
		
			Tuple<String, Object> result = null;
			/*
			 * 첫 시작char가 letter냐 digit냐 ,operator냐 separator냐에 따라 분석 method가 다름.
			 */
			if(isLetter(ch)){
				result = p_letter_scanner(piece);//letter가 첫 char였을 때 scanner
			}else if(isDigit(ch)){
				
				result = p_digit_scanner(piece);//digit이 첫 char였을 때 scanner
			}else if(isOperatorPiece(ch)){
		
				result = p_operator_scanner(piece);//operator가 첫 char였을 때 scanner
			}else if(isSeparator(ch)){
				result = p_separator_scanner(piece);//separator가 첫 char였을 때 scanner
			}else if(ch=='\''){
				result = p_char_scanner(piece);// ' 가 첫 char였을 때 scanner
			}else if(ch=='\"'){
				result = p_expr_scanner(piece);// " 가 첫 char였을 때 scanner
			}
			if(result!=null)
				scanned_piece.add(result);
	
		}
		
		return scanned_piece;
	}
	public ArrayList<Tuple<String, Object>> scan_code(List<String> code_list) throws Exception{
		/*
		 * ReadFile class를 이용해서 불러온 코드라인들을 분석하는 method
		 */
		ArrayList<Tuple<String, Object>> result = new ArrayList<>();
		line_index = 0;
		for(String line : code_list){
			
			Tuple<String, Object> line_info = new Tuple<String, Object>("LINE", line_index+1);
			result.add(line_info);//새로운 줄이 나왔을 때 결과배열(result)에 line [number]를 넣어준다.
			
			result.addAll(scan_code_piece(parseForLexer(line)));
			
			line_index++;//line index를 증가시켜줌
		}
		
		return result;
	}
	public static String parseForLexer(String str) throws IOException{
		/*
		 * 빈칸, 주석제거 (regular expression 이용)
		 */
		String _str = str.replaceAll("\\s+"," ");
		String __str = _str.replaceAll("//.*", "");

		return __str;
	}
	/*
	 * 첫 char의 종류에 따라 스캔 method를 나누어 놓았다.
	 * p_letter_scanner
	 * p_digit_scanner
	 * p_operator_scanner
	 * p_separator_scanner
	 * p_char_scanner
	 */
	public Tuple<String, Object> p_letter_scanner(String piece){
		Tuple<String, Object> result = null;
		String token = ""+piece.charAt(index_pointer);
		
		for(int i = index_pointer+1;i<piece.length();i++){
			
			char ch = piece.charAt(i);
			if(isLetter(ch)||isDigit(ch)){
				token+=ch;
				index_pointer++;
			}else{

				break;
			}
			
		}
		if(isKeyword(token)){
			result = new Tuple<String, Object>(keyword.get(token), token);
		}else{
			result = new Tuple<String, Object>("Id", token);
		}
		return result;
	}
	
	public Tuple<String, Object> p_digit_scanner(String piece){
		Tuple<String, Object> result = null;
		String token = ""+piece.charAt(index_pointer);
		Boolean float_flag = false;
		for(int i = index_pointer+1;i<piece.length();i++){
			char ch = piece.charAt(i);
			index_pointer++;
			if(isDigit(ch)){
				token+=ch;
			}else if(ch=='.'){
				token+=ch;
				float_flag = true;
			}else{
				index_pointer--;
				break;
			}
		}
		if(float_flag){
			result = new Tuple<String, Object>("FloatLiteral", Float.parseFloat(token));
		}else{
			result = new Tuple<String, Object>("IntLiteral", Integer.parseInt(token));
		}
		
		return result;
	}
	
	public Tuple<String, Object> p_operator_scanner(String piece) throws Exception{
		Tuple<String, Object> result = null;
		String token = ""+piece.charAt(index_pointer);

		for(int i = index_pointer+1;i<piece.length();i++){
			
			char ch = piece.charAt(i);
			if(isOperatorPiece(ch)){
				token+=ch;
				index_pointer++;
			}else{

				break;
			}
			
		}
		if(isOperator(token)){
			result = new Tuple<String, Object>(operator.get(token), token);
		}else{
			/**
			 * TODO:경고창 필요 
			 */
			throw new Exception("올바르지 않은 연산자!:::\'"+token+"\'");
		}
		
		return result;
	}
	
	public Tuple<String, Object> p_separator_scanner(String piece){
		Tuple<String, Object> result = null;
		String token = ""+piece.charAt(index_pointer);
		
		result = new Tuple<String, Object>(separator.get(token), "");
		return result;
	}
	
	public Tuple<String, Object> p_char_scanner(String piece) throws Exception{
		Tuple<String, Object> result = null;
		String token = ""+piece.charAt(index_pointer);
		Boolean isClosed = false;
		Integer charCount = 0; //''안에 들어가는 글자수를 카운트 -> 1개를 넘어가면 오류!
		for(int i = index_pointer+1 ;i<piece.length();i++){
			index_pointer++;
			char ch = piece.charAt(i);
			if(piece.charAt(i)=='\''){
				token += ch;
				isClosed = true;
				break;
			}else{
				token += ch;
				charCount++;
				if(charCount>1){
					/**
					 * TODO:경고창 필요 
					 */
					throw new Exception("char형일 경우 \'\'안의 글자 수는 1개이하 이어야 합니다.");
				}
			}
			
		}
		if(!isClosed){
			/*
			 * 작은 따옴표가 닫히지 않았을 경우 오류
			 */
			/**
			 * TODO:경고창 필요 
			 */
			throw new Exception("Quotation is not closed");
		}
		result = new Tuple<String, Object>("CharLiteral", token);
		return result;
	}
	public Tuple<String, Object> p_expr_scanner(String piece) throws Exception{
		Tuple<String, Object> result = null;
		String token = ""+piece.charAt(index_pointer);
		Boolean isClosed = false;

		for(int i = index_pointer+1 ;i<piece.length();i++){
			index_pointer++;
			char ch = piece.charAt(i);
			if(piece.charAt(i)=='\"'){
				token += ch;
				isClosed = true;
				break;
			}else{
				token += ch;
			}
			
		}
		if(!isClosed){
			/*
			 * 쌍 따옴표가 닫히지 않았을 경우 오류
			 */
			/**
			 * TODO:경고창 필요 
			 */
			throw new Exception("Quotation is not closed");
		}
		result = new Tuple<String, Object>("ExprLiteral", token);
		return result;
	}
	/*
	 * 	Basic Methods
	 *  isLetter
	 *  isDigit
	 *  isOperator1
	 *  isOperator2
	 *  -operator는 길이가 1혹은2 이므로 첫char일때 두번째char일때의 판단기준을 달리하여 만들었다.
	 *  isSeparator
	 *  isKeyword
	 */
	public Boolean isLetter(char ch){
		Boolean result = false;
		if(ch>='a'&&ch<='z'){
			result = true;
		}else if(ch>='A'&&ch<='Z'){
			result = true;
		}else if(ch=='$'||ch=='@'||ch=='_'){
			result = true;
		}
		return result;
	}
	public Boolean isDigit(char ch){
		Boolean result = false;
		if(ch>='0'&&ch<='9'){
			result = true;
		}
		return result;
	}
	public Boolean isOperatorPiece(char ch){
		/*
		 * operator 조각 check!
		 */
		Boolean result = false;
		if(operator.get(String.valueOf(ch))!=null){
			result = true;
		}
		return result;
	}
	
	
	public Boolean isSeparator(char ch){
		Boolean result = false;
		if(separator.get(String.valueOf(ch))!=null){//separator hashmap에서 확인
			result = true;
		}
		return result;
	}
	public Boolean isKeyword(String str){
		Boolean result = false;
		System.out.println("DEBUG::"+keyword);
		if(keyword.get(str)!=null){//keyword hashmap에서 확인
			result = true;
		}
		return result;
	}
	public Boolean isOperator(String str){
		Boolean result = false;
		if(operator.get(str)!=null){
			result = true;
		}
		return result;
	}
	
}
