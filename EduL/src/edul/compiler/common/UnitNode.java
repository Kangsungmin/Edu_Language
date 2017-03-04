package edul.compiler.common;
import java.util.ArrayList;

public class UnitNode {
	private String type;
	private Tuple<String, Object> coreToken;
	private ArrayList<Object> info_seq;
	private Integer depth;
	
	public UnitNode() {
		// TODO Auto-generated constructor stub
		this.type = null;
		this.coreToken = null;
		this.info_seq = new ArrayList<>();
	}
	public void addSeq(Object obj){
		info_seq.add(obj);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Tuple<String, Object> getCoreToken() {
		return coreToken;
	}
	public void setCoreToken(Tuple<String, Object> coreToken) {
		this.coreToken = coreToken;
	}
	public ArrayList<Object> getInfo_seq() {
		return info_seq;
	}
	public void setInfo_seq(ArrayList<Object> info_seq) {
		this.info_seq = info_seq;
	}
	
	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result_str = "";
		for(int i =0 ; i<depth;i++){
//			result_str += "\t";
			result_str += "-";
		}
		result_str+=this.type+" = ";
		for(int i = 0 ; i<info_seq.size();i++){
			if(info_seq.get(i) instanceof UnitNode){
				result_str+='\n'+info_seq.get(i).toString();
			}else{
				result_str+='\n';
				for(int j = 0; j<=depth;j++){
//					result_str+='\t';
					result_str += "-";
				}
				result_str += info_seq.get(i);
			}
			
			result_str += " ";
		}
		return result_str;

	}
	
}
