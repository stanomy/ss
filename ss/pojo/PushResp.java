package pojo;

public class PushResp {

	private String code;
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private String s1;//
	private String s2;//
	private String s3;//成交额 w

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

	
	public String getS3() {
		return s3;
	}

	public void setS3(String s3) {
		this.s3 = s3;
	}

	public PushResp() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PushResp(String code, String s1, String s2, String s3, String key) {
		super();
		this.code = code;
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
		this.name = key;
	}

}
