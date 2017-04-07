package pojo;

public class PushResp {

	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	private String s1;
	private String s2;
	public String getS1() {
		return s1;
	}
	public void setS1(String s1) {
		this.s1 = s1;
	}
	public String getS2() {
		return s2;
	}
	public void setS2(String s2) {
		this.s2 = s2;
	}
	public PushResp() {
		super();
	}
	public PushResp(String code, String s1, String s2) {
		super();
		this.code = code;
		this.s1 = s1;
		this.s2 = s2;
	}
	
}
