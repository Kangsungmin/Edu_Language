package edul.compiler.typechecker;

public class DeclInfo {
	private String type;
	private String id;
	private int size;
	private int base_size;
	private int scope;
	private int seq;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getBase_size() {
		return base_size;
	}
	public void setBase_size(int base_size) {
		this.base_size = base_size;
	}
	@Override
	public String toString() {
		return "DeclInfo [type=" + type + ", id=" + id + ", size=" + size + ", base_size=" + base_size + ", scope="
				+ scope + ", seq=" + seq + "]\n";
	}
	
	
	
	
}
