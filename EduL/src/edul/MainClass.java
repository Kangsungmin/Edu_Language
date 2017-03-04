package edul;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import edul.compiler.common.Tuple;
import edul.compiler.common.UnitNode;
import edul.compiler.lexer.Lexer;
import edul.compiler.parser.Parser;
import edul.view.LabelFrame;

public class MainClass {
	public static void main(String[] args) throws Exception {
		HashMap<String, String> keyword = new HashMap<>();
		HashMap<String, String> operator = new HashMap<>();
		HashMap<String, String> separator = new HashMap<>();
		
		keyword.put("bool", "BOOLEAN");
		keyword.put("char", "CHAR");
		keyword.put("else", "ELSE");
		keyword.put("false", "FALSE");
		keyword.put("float", "FLOAT");
		keyword.put("if", "IF");
		keyword.put("int", "INT");
		keyword.put("main", "MAIN");
		keyword.put("true", "TRUE");
		keyword.put("while", "WHILE");
		
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
		
		LabelFrame labelFrame = new LabelFrame();
		labelFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		labelFrame.setSize(500, 600);
		labelFrame.setVisible(true);

//		File file_ = new File("text.c");
//		List<String> code = ReadFile.readFile(file_.getAbsolutePath());
//		for(String line : code){
//			System.out.println(line);
//		}
//		Lexer lexer = new Lexer(keyword,operator,separator);
//		ArrayList<Tuple<String, Object>> scanned = lexer.scan_code(code);
//		System.out.println("======Scanned========");
//		for(int i = 0 ; i<scanned.size();i++){
//			System.out.println(scanned.get(i));
//		}
//		Parser parser = new Parser(scanned);
//		System.out.println("=======Parsed========");
//		UnitNode parsedTree = parser.startParsing();
//		

//		NodeTreeView.viewParsedTree(parsedTree);
		
		
		
	}
}
