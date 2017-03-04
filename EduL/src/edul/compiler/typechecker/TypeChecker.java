package edul.compiler.typechecker;

import java.util.ArrayList;
import java.util.HashMap;

import edul.compiler.common.Tuple;
import edul.compiler.common.UnitNode;

public class TypeChecker {
	private static HashMap<String, ArrayList<DeclInfo>> info_table_map;
	private static ArrayList<DeclInfo> decl_arr;
	private static UnitNode parsedUnitNode;
	
	
	
	public TypeChecker() {
		/**
		 * 이미 테이블이 완성된 부분을 사용하려는 것이기 때문에 초기화 필요 x
		 */
		// TODO Auto-generated constructor stub
	}
	
	public TypeChecker(UnitNode parsed) {
		// TODO Auto-generated constructor stub
		/**
		 * parse된 UnitNode가 넘어올 경우 
		 * 모든 요소를 초기화 한다.
		 */
		parsedUnitNode = parsed;
		info_table_map = new HashMap<>();
		decl_arr = new ArrayList<>();
	}
	
	
	public void check() throws NumberFormatException, Exception{
		/**
		 * 가장처음 UnitNode를 순회하면서 table을 채우고 싶을 때, 실행 하면 된다.
		 */
		tracker(parsedUnitNode);
		info_table_map.put("decl_table", decl_arr);
	}
	
	private void fillDeclTable(UnitNode unitnode) throws NumberFormatException, Exception{
		/**
		 * Declartion을 이용하여 declaration table을 채워준다.
		 */
		ArrayList<Object> info_seq = unitnode.getInfo_seq();

		String type = (String)((Tuple)info_seq.get(0)).getX();
		int size = 1;
		int scope = 1;//하나의 함수 안에서만 실행되므로 scope는 1로 고정 
		/**
		 * int,float,bool,char 별로 사이즈가 다를경우 각각의 사이즈를 지정해줄 것!
		 */
		switch (type) {
		case "INT":

			break;
		case "FLOAT":

			break;
		case "BOOLEAN":

			break;

		case "CHAR":
			
			break;

		default:

			break;
		}
		for(int i = 1;i<info_seq.size();i++){
			DeclInfo info = new DeclInfo();
			info.setId((String)((Tuple)info_seq.get(i)).getY());
			Boolean result = isDeclared(info.getId());
			if(result){
				/**
				 * TODO:경고창 필요 
				 */
				throw new Exception("변수 선언 중복 id : "+info.getId());
			}
			if((i+1)<info_seq.size()&&(((Tuple)info_seq.get(i+1)).getX().toString()).equals("ARR")){
				Tuple tmp = (Tuple)info_seq.get(i+1);
				int tmp_size = size*Integer.parseInt((String)tmp.getY());
				info.setSize(tmp_size);
				
				i++;

			}else{
				info.setSize(size);
			}
			info.setBase_size(size);
			info.setType(type);
			info.setScope(scope);
			/**
			 * seq -> 메모리의 몇번째에 와야하는가? 
			 *(block  seq  size)
			 * sym 1  1  10
			 * sym 1  11  1
			 * sym 1  12  2
			 * sym 1  14  1
			 */
			info.setSeq(getPosition());
			insertDeclTable(info);
		}
	}
	
	private void insertDeclTable(DeclInfo info){
		/**
		 * Declaration Table에 변수의 정보를 넣어준다.
		 */
		decl_arr.add(info);
	}
	private void tracker(UnitNode unitnode) throws NumberFormatException, Exception{
		/**
		 * parsed tree를 순회
		 */
		ArrayList<Object> info_seq = unitnode.getInfo_seq();
		for(int i =0;i<info_seq.size();i++){
			Object info = info_seq.get(i);
			if(info instanceof UnitNode){

				switch (((UnitNode) info).getType()) {
				case "Declaration":
					fillDeclTable((UnitNode)info);
					break;
				case "Assignment":
					
					break;
				default:
					break;
				}
				tracker((UnitNode) info);
			}else if(info instanceof Tuple){
				if(((Tuple)info).getX().equals("Id")){
					if(!isDeclared((String)((Tuple)info).getY())){
						throw new Exception("선언되지 않은 변수!");
					}
				}
				return;
			}
		}
	}
	
	private Boolean isDeclared(String id) throws Exception{
		/**
		 * decl_arr에 해당 id정보가 저장 되어있나 확인. 
		 */
		Boolean result = false;
		DeclInfo decinfo = getDeclInfoById(id);
		if(decinfo!=null){
			result=true;
		}
		return result;
	}
	
	private int getPosition(){
		/**
		 * 적절한 seq를 반환해주는 함수
		 */
		if(decl_arr.size()==0){
			return 1;
		}else{
			DeclInfo last = decl_arr.get(decl_arr.size()-1);
			return last.getSeq()+last.getSize();
		}
	}
	
	public DeclInfo getDeclInfoById(String id){
		/**
		 * id를 이용해서 decl_arr에 있는 DeclInfo를 반환해주는 함수
		 * 만약 값이 없다면 null이 반환.
		 */
		DeclInfo result = null;
		for(int i =0;i<decl_arr.size();i++){
			result = decl_arr.get(i);
			if(result.getId().equals(id)){
				break;
			}else{
				result = null;
			}
		}
		return result;
	}
	
	
	
	
	/**
	 * getter, setter.
	 * @return
	 */
	public HashMap<String, ArrayList<DeclInfo>> getInfo_table_map() {
		return info_table_map;
	}
	public void setInfo_table_map(HashMap<String, ArrayList<DeclInfo>> info_table_map) {
		this.info_table_map = info_table_map;
	}
	
	
	
}
