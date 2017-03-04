package edul.compiler.generator;
import java.util.ArrayList;

import edul.compiler.common.Tuple;
import edul.compiler.common.UnitNode;
import edul.compiler.expression.graph.Graph_edu;
import edul.compiler.expression.solve.Solve_edu;
import edul.compiler.expression.test.valueChecker;
import edul.compiler.expression.varify.Verify_edu;
import edul.compiler.typechecker.DeclInfo;
import edul.compiler.typechecker.TypeChecker;

public class Generator {
	UnitNode tree;
	int block_count = 1;
	int var_count = 1;
	int as_count = 1; //�썑�뿉 �떖蹂쇳뀒�씠釉붿뿉�꽌 鍮꾧탳�븯�룄濡� �븿
	boolean looptag = false;
	boolean arrtag = false;
	boolean asstag = false;
	boolean stitag = false;
	static int demand = 0;
	TypeChecker tc;

	String out = "";
	String check_result = "";
	String X;//식을 담는 임시 문자열
	Solve_edu coe;
	Graph_edu draw;
	valueChecker val_check;
	Verify_edu verify = new Verify_edu();
	
	//�뒪�깮�꽔�뼱�빞 �븿
	public Generator(UnitNode parsedtree){ //�듃由щ�� 諛쏆븘�샂

		tree = parsedtree;
		tc = new TypeChecker();

		//System.out.println(tc.getInfo_table_map().get("decl_table").get(0)+"0踰덉㎏ �슂�냼");

		System.out.println("============코드 생성============");

		translate(tree,tree.getInfo_seq().size());//蹂��솚 �룞�떆 異쒕젰
		//      System.out.println("           end");
		out += "           end";
		System.out.println("============코드 종료============");
	}

	public String gen(){
		return out;
	}
	public String getCheckResult(){
		return check_result;
	}


	public void translate(UnitNode object, int size){//�뙆�꽌�듃由щ�� �닚�쉶�븯硫댁꽌 �쟻�젅�븳 肄붾뱶�옉�꽦 硫붿냼�뱶瑜� �샇異쒗븳�떎.
		ArrayList<Object> Tlist = object.getInfo_seq();
		String type;


		///////////////�썑�쐞�닚�쉶濡� 蹂��솚//////////////////
		if( object.getType().equals("Addition") || object.getType().equals("Term")
				|| object.getType().equals("Relation")){ //留뚯빟 Addition �쑀�떅�씠�씪硫�
			//ArrayList<Object> tempL = new ArrayList<Object>();
			Object tempO;
			for(int i =0; i<Tlist.size(); i++){ //Term遺��꽣 紐⑤몢 �꽔�뒗�떎.
				if(i == 0)continue;
				else if(i % 2 == 0){
					tempO = Tlist.get(i);   
					Tlist.set(i, Tlist.get(i-1)); //i�뿉 i+1���엯
					Tlist.set(i-1, tempO);
				}
			}
		}
		if(object.getType().equals("Program")) 
		{   
			w_decl();
			w_bgn(tc.getInfo_table_map().get("decl_table").size());
		}

		//System.out.println("由ъ뒪�듃 �궗�씠利�" + size);
		for(int j = 0 ; j<size; j++){
			//System.out.println("for 吏꾩엯 吏곹썑 i媛�"+j);
			if(Tlist.get(j) instanceof UnitNode){
				UnitNode temp = (UnitNode)Tlist.get(j);
				Tuple flag;
				switch(temp.getType()){ //�듃由щ�� �닚�쉶�븯硫댁꽌 �쑀�떅�끂�뱶�뿉 ���븳 ���엯寃��궗瑜� �떆�뻾.

				case "Declaration" : 
					//w_sym(1,var_count,1); var_count++; 
					break;
				case "Assignment" : //Assignment�떆�옉�엫�쓣 �븣由�
					flag = (Tuple) temp.getInfo_seq().get(0); //��寃� �끂�뱶 媛��졇�샂
					if(tc.getDeclInfoById((String)flag.getY()).getSize() > 1) asstag = true; //��寃잙끂�뱶媛� 諛곗뿴�엫�쓣 �씤吏�
					translate(temp,temp.getInfo_seq().size());
					//str�븯�뒗 ��寃잛쓽 seq瑜� 援ы븳�떎.
					//�떎�쓬 �끂�뱶�쓽 泥ル쾲吏�(�꽣誘몃꼸�끂�뱶)瑜� 媛��졇�샂
					if(stitag == true) {w_sti(); stitag = false;} //諛곗뿴�뿉 ���옣 �쐞�빐 sti異쒕젰
					else
						w_str(1, tc.getDeclInfoById((String)flag.getY()).getSeq()); //�꽣誘몃꼸 �끂�뱶�쓽 seq瑜� 媛��졇�삩�떎.
					//Assignment�걹
					break;//assignment�뿉 ���븳 �썑�쐞�닚�쉶
				case "Addition" : translate(temp,temp.getInfo_seq().size()); 
				break;
				case "Loop" : if(looptag == false){w_Loopstart();}looptag = true; 
				demand++; translate(temp,temp.getInfo_seq().size());
				w_Loopend(); w_Loopnext(); break;
				case "Conditional" :demand++; block_count--; translate(temp,temp.getInfo_seq().size());
				w_Loopnext(); break;
				case "Expression" : translate(temp,temp.getInfo_seq().size());
				w_fjp(); break;
				case "Graph" : 
					flag = (Tuple) temp.getInfo_seq().get(0);
					X = (String)flag.getY(); //식이나 Id 변수이름이 넘어온다.
					if("ExprLiteral" == flag.getX())//자식노드가 식이라면
					{
						 
						X = X.substring(1, X.length()-1);
						char[] tempstring = new char[X.length()];
						for(int i=0;i<X.length();i++) tempstring[i] = X.charAt(i);
						verify.passer(tempstring); //식 검사한다.
						coe = new Solve_edu(X, 1);
						draw = new Graph_edu(coe.getA(),coe.getB(),coe.getC(),1);//그래프 그린다.
						translate(temp,temp.getInfo_seq().size());
					}
					else{//변수이름일 때, 변수를 심볼테이블에서 찾는다.
						System.out.println(tc.getDeclInfoById(X)+"입니다");
					}
					break;
				case "Solve" :
					flag = (Tuple) temp.getInfo_seq().get(0);
					X = (String)flag.getY(); //식이나 Id 변수이름이 넘어온다.
					if("ExprLiteral" == flag.getX())//자식노드가 식이라면
					{
						 
						X = X.substring(1, X.length()-1);
						char[] tempstring = new char[X.length()];
						for(int i=0;i<X.length();i++) tempstring[i] = X.charAt(i);
						verify.passer(tempstring); //식 검사한다.
						val_check = new valueChecker();
						val_check.trans(tempstring);
						check_result +=val_check.getResult();
						translate(temp,temp.getInfo_seq().size());
					}
					else
						translate(temp,temp.getInfo_seq().size());
					break;
				
				default : translate(temp,temp.getInfo_seq().size()); break;
				}
				//translate(temp,temp.getInfo_seq().size());
			}
			else if(Tlist.get(j) instanceof Tuple)//�꽣誘몃꼸 �끂�뱶瑜� 留뚮궗�쓣 �븣,
			{
				Tuple terminal = (Tuple) Tlist.get(j);

				//System.out.println( terminal.toString());
				switch((String) terminal.getX()){
				case "Id" : 
					if(tc.getDeclInfoById((String)terminal.getY()) != null){
						//�쁽�옱 �븘�씠�뵒媛� 諛곗뿴�씠�씪硫�(�궗�씠利덇� 1蹂대떎 �겕硫�) 諛곗뿴 �뀒洹몃�� true濡� �븳�떎.
						if(tc.getDeclInfoById((String)terminal.getY()).getSize() > 1){
							arrtag = true;
						}
						int SEQ = tc.getDeclInfoById((String)terminal.getY()).getSeq();
						//Id瑜� 留뚮굹硫� Id�쓽 蹂��닔紐낆쓣 �빐�돩留듭뿉�꽌 寃��깋�븯�뿬, 紐� 踰덉㎏ �룷吏��뀡�씤吏� int 濡� 李얜뒗�떎.
						w_lod(1,SEQ);
					}else{System.out.println("OUT OF SCOPE!");}break;

				case "IntLiteral" : //諛곗뿴�궡遺� 媛믪씪 寃쎌슦(諛곗뿴�깭洹멸� true)�씪�븣, 諛곗뿴 Ucode異쒕젰
					w_ldc((int)terminal.getY());
					if(arrtag == true){ //諛곗뿴�븞�쓽 �씤�뜳�뒪�씪硫�
						w_lda(1,1);
						w_add();
						if(asstag == false) //��寃잛씠 �븘�땺�븣�뒗 ldi異쒕젰�빐�빞 �븿
							w_ldi();
						else
						{
							asstag = false;////��寃� 諛곗뿴�씪 寃쎌슦 ldi異쒕젰 �뒪�궢 �썑 留덉�留됱뿉 sti異쒕젰 �빐�빞 �븿
							stitag = true;
						}
						arrtag = false;
					}
					break;
				case "CharLiteral" : w_ldc(66); break;
				case "TRUE" :w_ldc(1); break;
				case "FALSE" :w_ldc(0); break;
				case "PLUS" : w_add(); break;
				case "MULT" : w_mult(); break;
				case "MINUS" : w_sub(); break;
				case "DIV" : w_div(); break;
				case "<" : w_lt(); break;
				case ">" : w_gt(); break;
				case "<=" : w_le(); break;
				case ">=" : w_ge(); break;
				case "ExprLiteral" : 
					System.out.println(terminal.getY());//연산식 노드
					break;

				}
			}
			//System.out.println(j+"---------------i媛�" );
		}

	}
	private void w_decl() {
		// TODO Auto-generated method stub
		for(int i= 0; i<tc.getInfo_table_map().get("decl_table").size();i++ )
		{
			out += "           sym 1 "+tc.getInfo_table_map().get("decl_table").get(i).getSeq()
					+" "+tc.getInfo_table_map().get("decl_table").get(i).getSize()+"\n";
			//         System.out.println("           sym 1 "+tc.getInfo_table_map().get("decl_table").get(i).getSeq()
			//               +" "+tc.getInfo_table_map().get("decl_table").get(i).getSize());
		}
	}
	private void w_Loopnext() {
		// TODO Auto-generated method stub
		block_count++;
		String code = new String();
		code = "$$"+block_count+"        nop"+"\n";
		//      System.out.print(code);
		out += code;
	}

	private void w_le() {
		// TODO Auto-generated method stub
		String code = new String();
		code = "le"+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}
	private void w_ge() {
		// TODO Auto-generated method stub
		String code = new String();
		code = "ge"+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_lt() {
		// TODO Auto-generated method stub
		String code = new String();
		code = "lt"+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}
	private void w_gt() {
		// TODO Auto-generated method stub
		String code = new String();
		code = "gt"+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_fjp() { //�뒪�깮�뿉 �씡�뒪�봽�젅�뀡�뿉 ���븳 fjp異쒕젰 �슂援ш� �엳�쓣 �떆�뿉留� 異쒕젰�븳�떎.
		// TODO Auto-generated method stub
		if(demand != 0)
		{
			String code = new String();
			int temp = block_count+1;
			code = "fjp $$"+temp+"\n";
			//         System.out.print("           "+code);
			out += "           "+code;
			demand--;

		}
	}

	private void w_Loopend() {
		// TODO Auto-generated method stub
		String code = new String();
		code = "ujp $$"+block_count+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_Loopstart() {
		// TODO Auto-generated method stub
		block_count++;
		String code = new String();
		code = "$$"+block_count+"        nop"+"\n";
		//      System.out.print(code);
		out += code;
	}

	private void w_div() {
		// TODO Auto-generated method stub
		String code = new String();
		code = "div"+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_mult() {
		// TODO Auto-generated method stub
		String code = new String();
		code = "mult"+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_bgn(int var_num){
		String code = new String();
		code = "bgn "+var_num+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_sym(int b, int varcount, int size){
		String code = new String();
		code = "sym "+b+" "+varcount+" "+size+"\n";
		//      System.out.print("           "+code);
		out +="           "+code;
	}

	private void w_lod(int block, int target){
		String code = new String();
		code = "lod "+block+" "+target+"\n";
		//      System.out.print("           "+code);
		out +="           "+code;
	}

	private void w_lda(int block, int target){
		String code = new String();
		code = "lda "+block+" "+target+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_ldi(){
		String code = new String();
		code = "ldi\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_str(int block, int target){

		String code = new String();
		code = "str "+block+" "+target+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_sti(){

		String code = new String();
		code = "sti\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_add(){
		String code = new String();
		code = "add"+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_sub(){
		String code = new String();
		code = "sub"+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}

	private void w_ldc(int val){
		String code = new String();
		code = "ldc "+val+"\n";
		//      System.out.print("           "+code);
		out += "           "+code;
	}
}