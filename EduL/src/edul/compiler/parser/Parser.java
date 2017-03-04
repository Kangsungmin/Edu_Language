package edul.compiler.parser;
import java.util.ArrayList;
import java.util.HashMap;

import edul.compiler.common.Tuple;
import edul.compiler.common.UnitNode;

public class Parser {
	private Integer index_pointer;
	private ArrayList<Tuple<String, Object>>tokens;
	
	private Integer line_index;
	
	
	//문법요소
	private HashMap<String, String> type;
	private HashMap<String, String> value;
	private HashMap<String, String> boolean_value;
	private HashMap<String, String> separator;
	private HashMap<String, String> keyword;
	private HashMap<String, String> operator;
	private HashMap<String, String> equOp;
	private HashMap<String, String> relOp;
	private HashMap<String, String> addOp;
	private HashMap<String, String> mulOp;
	private HashMap<String, String> unaryOp;
	
	public Parser(ArrayList<Tuple<String, Object>>tokens) {
		// TODO Auto-generated constructor stub
		index_pointer = -1;//token의 position
		
		this.tokens = tokens;
		this.type = new HashMap<>();
		this.value = new HashMap<>();
		this.boolean_value = new HashMap<>();
		this.separator = new HashMap<>();
		this.keyword = new HashMap<>();
		this.operator = new HashMap<>();
		this.equOp = new HashMap<>();
		this.relOp = new HashMap<>();
		this.addOp = new HashMap<>();
		this.mulOp = new HashMap<>();
		this.unaryOp = new HashMap<>();
		
		/*
		 * type
		 */
		type.put("INT", "int");
		type.put("BOOLEAN", "bool");
		type.put("CHAR", "char");
		type.put("FLOAT", "float");
		type.put("EXPR", "expr");
		/*
		 * value
		 */
		value.put("IntLiteral", "integer");
		value.put("FloatLiteral", "float");
		value.put("CharLiteral", "char");
		value.put("ExprLiteral", "expr");
		/*
		 * boolean
		 */
		boolean_value.put("TRUE", "true");
		boolean_value.put("FALSE", "false");
		/*
		 * separator
		 */
		separator.put("(", "(");
		separator.put(")", ")");
		separator.put("{", "{");
		separator.put("}", "}");
		separator.put(";", ";");
		separator.put("[", "[");
		separator.put("]", "]");
		separator.put(",", ",");
		/*
		 * keyword
		 */
		keyword.put("MAIN", "edu");
		keyword.put("IF", "if");
		keyword.put("LOOP", "loop");
		keyword.put("ELSE", "else");
		
		keyword.put("VERIFY", "verify");
		keyword.put("SOLVE","solve");
		keyword.put("GRAPH", "graph");
		keyword.put("CALC","calc");
		/*
		 * operator
		 */
		operator.put("ASSIGN", "=");
		operator.put("OR", "||");
		operator.put("AND","&&");
		/*
		 * equOp
		 */
		equOp.put("EQUAL", "==");
		equOp.put("NOT_EQUAL", "!=");
		/*
		 * relOp
		 */
		relOp.put("<", "<");
		relOp.put(">", ">");
		relOp.put("<=","<=");
		relOp.put(">=", ">=");
		/*
		 * addOp
		 */
		addOp.put("PLUS", "+");
		addOp.put("MINUS", "-");
		/*
		 * mulOp
		 */
		mulOp.put("MULT", "*");
		mulOp.put("DIV", "/");
		mulOp.put("MOD", "%");
		/*
		 * unaryOp
		 */
		unaryOp.put("MINUS", "-");
		unaryOp.put("NOT", "!");
	}
	
	public Tuple<String, Object> glimpseNextToken(){
		Tuple<String, Object> n_token = null;
		if(tokens.size()>index_pointer+1){
			n_token = tokens.get(index_pointer+1);
			
			if(n_token.getX()=="LINE"){
				line_index =  (Integer) n_token.getY();
//				System.out.println("DEBUG::glimpseNextToken-LINE="+line_index);
				index_pointer++;
				n_token = glimpseNextToken();
			}
			
			return n_token;
		}else{
			return n_token = new Tuple<String, Object>("End", "End");
		}
		
	}
	/*
	 * start parsing!
	 */
	public UnitNode startParsing() throws Exception{
		return Program(0);
	}
	/*
	 * Grammar function
	 */
	
	public UnitNode Program(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
		glimpseNextToken();//첫 라인을 가져오기 위함.
		
		node.setType("Program");
		
		match("keyword","MAIN");

		match("separator","{");
		node.addSeq(Declarations(depth));
		
		node.addSeq(Statements(depth));

		match("separator", "}");
		return node;
	}
	public UnitNode Declarations(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
		
		node.setType("Declarations");
		while(type.get(glimpseNextToken().getX())!=null){
			node.addSeq(Declaration(depth));
		}
		return node;
	}
	public UnitNode Declaration(Integer depth) throws Exception{
		/*
		 * 타입이 core, (Id,#id#)..., optional array이냐?->(ARR,#size#)...
		 */
		
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
		
		node.setType("Declaration");
		node.addSeq(match("type"));
		
		Tuple<String, Object> tmp_token = match("id");
		node.addSeq(tmp_token);
		if(glimpseNextToken().getX()=="["){
			match("separator","[");
			Tuple tmp_tup = match("value","IntLiteral");
			node.addSeq(new Tuple<Object, String>("ARR",tmp_tup.getY().toString()));
			match("separator","]");
		}
		while(glimpseNextToken().getX()==","){
			match("separator",",");
			tmp_token = match("id");
			node.addSeq(tmp_token);
			if(glimpseNextToken().getX()=="["){
				match("separator","[");
				Tuple tmp_tup = match("value","IntLiteral");
				node.addSeq(new Tuple<Object, String>("ARR",tmp_tup.getY().toString()));//ARR은 배열을 뜻함
				match("separator","]");
			}
		}
		match("separator",";");
		return node;
	}
	public UnitNode Statements(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
				
		node.setType("Statements");
		while(isStatement()){
			node.addSeq(Statement(depth));
		}
		return node;
	}
	public Boolean isStatement() throws Exception{
		Boolean result = true;
		if(glimpseNextToken().getX()=="LOOP"||glimpseNextToken().getX()=="IF"||
				glimpseNextToken().getX()==";"||glimpseNextToken().getX()=="{"||glimpseNextToken().getX()=="Id"
				||glimpseNextToken().getX()=="SOLVE"||glimpseNextToken().getX()=="VERIFY"||glimpseNextToken().getX()=="GRAPH"||glimpseNextToken().getX()=="CALC"){
				//Block//LoopStatement//IfStatement//Assignment
		}else{
			result = false;
		}
		return result;
	}
	public UnitNode Statement(Integer depth) throws Exception{
		/*
		 * statement 판단기준이 복잡하므로 따로 statement 판단, get하는 함수 제작
		 */
		UnitNode result = null;
		if(glimpseNextToken().getX()==";"){//;
			result = new UnitNode();
			//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
			result.setDepth(depth);
			depth++;
			result.setType("Skip");
			match("separator",";");//skip!
		}else if(glimpseNextToken().getX()=="{"){//Block
			result = Block(depth);
		}else if(glimpseNextToken().getX()=="Id"){//Assignment
			result = Assignment(depth);
		}else if(glimpseNextToken().getX()=="IF"){//IfStatement
			result = IfStatement(depth);
		}else if(glimpseNextToken().getX()=="LOOP"){//LoopStatement
			result = LoopStatement(depth);
		}else if(glimpseNextToken().getX()=="VERIFY"){//VerifyStatement
			result = VerifyStatement(depth);
		}else if(glimpseNextToken().getX()=="SOLVE"){//SolveStatement

			result = SolveStatement(depth);
		}else if(glimpseNextToken().getX()=="GRAPH"){//GraphStatement
			result = GraphStatement(depth);
		}else if(glimpseNextToken().getX()=="CALC"){//CalcStatement
			result = CalcStatement(depth);
		}else{
			/**
			 * TODO:경고창 필요 
			 */
			throw new Exception("Line:"+line_index+"//Syntax Error::invalid Statement");
		}
		return result;
	}

	public UnitNode Block(Integer depth) throws Exception{
		/*
		 * Block -> {statements}
		 */
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		
		node.setType("Block");
		match("separator", "{");
		node.addSeq(Statements(depth));
		match("separator", "}");
		return node;
	}
	public UnitNode IfStatement(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
				
		node.setType("Conditional");
		match("keyword","IF");
		match("separator","(");
		node.addSeq(Expression(depth));
		match("separator",")");
		node.addSeq(Statement(depth));
		if(glimpseNextToken().getX()=="ELSE"){
			node.addSeq(match("keyword","ELSE"));
			node.addSeq(Statement(depth));
		}
		return node;
	}
	public UnitNode LoopStatement(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Loop");
		match("keyword","LOOP");
		match("separator","(");
		node.addSeq(Expression(depth));
		match("separator",")");
		node.addSeq(Statement(depth));
		return node;
	}
	public UnitNode VerifyStatement(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
		
		node.setType("Verify");
		
		match("keyword","VERIFY");
		
		match("separator","(");
		if(glimpseNextToken().getX().equals("Id")){
			/**
			 * 해당 case의 경우 generate 할 때, 변수형을 expr인지 확인을 꼭 해주어야 한다.
			 */
			node.addSeq(match("id"));
			if(glimpseNextToken().getX()=="["){
				match("separator","[");
				node.addSeq(Expression(depth));
				match("separator","]");
			}
		}else{
			node.addSeq(match("value", "ExprLiteral"));
		}
		
		match("separator",")");
		
		match("separator",";");
		
		return node;
	}
	public UnitNode SolveStatement(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
		
		node.setType("Solve");
		match("keyword","SOLVE");
		match("separator","(");
		if(glimpseNextToken().getX().equals("Id")){
			/**
			 * 해당 case의 경우 generate 할 때, 변수형을 expr인지 확인을 꼭 해주어야 한다.
			 */
			node.addSeq(match("id"));
			if(glimpseNextToken().getX()=="["){
				match("separator","[");
				node.addSeq(Expression(depth));
				match("separator","]");
			}
		}else{
			node.addSeq(match("value", "ExprLiteral"));
		}
		match("separator",")");
		match("separator",";");
		return node;
	}
	public UnitNode GraphStatement(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
		
		node.setType("Graph");
		match("keyword","GRAPH");
		match("separator","(");
		if(glimpseNextToken().getX().equals("Id")){
			/**
			 * 해당 case의 경우 generate 할 때, 변수형을 expr인지 확인을 꼭 해주어야 한다.
			 */
			node.addSeq(match("id"));
			if(glimpseNextToken().getX()=="["){
				match("separator","[");
				node.addSeq(Expression(depth));
				match("separator","]");
			}
		}else{
			node.addSeq(match("value", "ExprLiteral"));
		}
		match("separator",")");
		match("separator",";");
		return node;
	}
	public UnitNode CalcStatement(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;
		
		node.setType("Calc");
		match("keyword","CALC");
		match("separator","(");
		if(glimpseNextToken().getX().equals("Id")){
			/**
			 * 해당 case의 경우 generate 할 때, 변수형을 expr인지 확인을 꼭 해주어야 한다.
			 */
			node.addSeq(match("id"));
			if(glimpseNextToken().getX()=="["){
				match("separator","[");
				node.addSeq(Expression(depth));
				match("separator","]");
			}
		}else{
			node.addSeq(match("value", "ExprLiteral"));
		}
		match("separator",")");
		match("separator",";");
		return node;
	}
	public UnitNode Assignment(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Assignment");
		
		node.addSeq(match("id"));
		if(glimpseNextToken().getX()=="["){
			match("separator","[");
			node.addSeq(Expression(depth));
			match("separator","]");
		}
		Tuple<String, Object>core_t = match("operator","ASSIGN");
		node.setCoreToken(core_t);
		node.addSeq(Expression(depth));
		match("separator",";");
		return node;
	}
	public UnitNode Expression(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Expression");
		node.addSeq(Conjunction(depth));
		while(glimpseNextToken().getX()=="OR"){
			match("operator","OR");
			node.addSeq(Conjunction(depth));
		}
		return node;
	}
	public UnitNode Conjunction(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Conjunction");
		node.addSeq(Equality(depth));
		while(glimpseNextToken().getX()=="AND"){
			match("operator","AND");
			node.addSeq(Equality(depth));
			break;
		}
		return node;
	}
	public UnitNode Equality(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Equality");
		node.addSeq(Relation(depth));
		if(equOp.get(glimpseNextToken().getX())!=null){
			node.setCoreToken(match("equOp"));
			node.addSeq(Relation(depth));
		}
		return node;
	}
	public UnitNode Relation(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Relation");
		node.addSeq(Addition(depth));
		if(relOp.get(glimpseNextToken().getX())!=null){
			node.addSeq(match("relOp"));
			node.addSeq(Addition(depth));
		}
		return node;
	}
	public UnitNode Addition(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Addition");
		node.addSeq(Term(depth));
		while(addOp.get(glimpseNextToken().getX())!=null){
			node.addSeq(match("addOp"));
			node.addSeq(Term(depth));
		}
		return node;
	}
	public UnitNode Term(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Term");
		node.addSeq(Factor(depth));
		while(mulOp.get(glimpseNextToken().getX())!=null){
			node.addSeq(match("mulOp"));
			node.addSeq(Factor(depth));
		}
		return node;
	}
	public UnitNode Factor(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Factor");
		if(unaryOp.get(glimpseNextToken().getX())!=null){
			node.addSeq(match("unaryOp"));
		}
		node.addSeq(Primary(depth));
		return node;
	}
	public UnitNode Primary(Integer depth) throws Exception{
		UnitNode node = new UnitNode();
		//노드의 깊이 정보를 입력 (tree 구조 생성시 필요)
		node.setDepth(depth);
		depth++;

		node.setType("Primary");
		if(glimpseNextToken().getX()=="Id"){
			node.addSeq(match("id"));
			if(glimpseNextToken().getX()=="["){
				match("separator","[");
				node.addSeq(Expression(depth));
				match("separator","]");
			}
		}else if(value.get(glimpseNextToken().getX())!=null){//Literal except Boolean
			node.addSeq(match("value"));
		}else if(boolean_value.get(glimpseNextToken().getX())!=null){//Boolean Literal
			node.addSeq(match("boolean_value"));
		}else if(glimpseNextToken().getX()=="("){//(Expression)
			match("separator","(");
			node.addSeq(Expression(depth));
			match("separator",")");
		}else if(type.get(glimpseNextToken().getX())!=null){//Type(Expression)
			node.addSeq(match("type"));
			match("separator","(");
			node.addSeq(Expression(depth));
			match("separator",")");
		}else{
			//에러..;;
			/**
			 * TODO:경고창 필요 
			 */
			throw new Exception("Line:"+line_index+"//Syntax Error::invalid Primary");
		}
		return node;
	}
	/*
	 * Matching Function
	 */
	public Tuple<String, Object> match(String g_elem) throws Exception{
		Boolean result = false;
		Tuple<String, Object> token = getNextToken();
		switch (g_elem) {
		case "type":
			if(type.get(token.getX())!=null){
				result = true;
			}
			break;
		case "value":
			if(value.get(token.getX())!=null){
				result = true;
			}
			break;
		case "boolean_value":
			if(boolean_value.get(token.getX())!=null){
				result = true;
			}
			break;
		case "keyword":
			if(keyword.get(token.getX())!=null){
				result = true;
			}
			break;
		case "separator":
			if(separator.get(token.getX())!=null){
				result = true;
			}
			break;
		case "operator":
			if(operator.get(token.getX())!=null){
				result = true;
			}
			break;
		case "equOp":
			if(equOp.get(token.getX())!=null){
				result = true;
			}
			break;
		case "relOp":
			if(relOp.get(token.getX())!=null){
				result = true;
			}
			break;
		case "addOp":
			if(addOp.get(token.getX())!=null){
				result = true;
			}
			break;
		case "mulOp":
			if(mulOp.get(token.getX())!=null){
				result = true;
			}
			break;
		case "unaryOp":
			if(unaryOp.get(token.getX())!=null){
				result = true;
			}
			break;
		case "id":

			if(token.getX().equals("Id")){
				
				result = true;
			}
			break;
		default:
			break;
		}
		if(!result){
			/**
			 * TODO:경고창 필요 
			 */
			throw new Exception("Line:"+line_index+"//Syntax Error::Matching("+g_elem+")");
		}
		return token;
	}
	public Tuple<String, Object> match(String g_elem, String detailVal) throws Exception{
		Tuple<String, Object> token = match(g_elem);
		if(token.getX()!=detailVal){
			/**
			 * TODO:경고창 필요 
			 */
			throw new Exception("Line:"+line_index+"Syntax Error::Matching("+g_elem+" "+detailVal+")");
		}
		return token;
		
	}
	public Tuple<String, Object> getNextToken(){
		index_pointer++;
		Tuple<String, Object> n_token = null;
		if(index_pointer<tokens.size()){
			n_token = tokens.get(index_pointer);

			//		if(n_token.getX()=="LINE"){
			//			line_index =  (Integer) n_token.getY();
			//			System.out.println("DEBUG::getNextToken-LINE="+line_index);
			//			n_token = getNextToken();
			//		}
			//		
			return n_token;
		}else{
			return n_token = new Tuple<String, Object>("End", "End");
		}
	}
}
