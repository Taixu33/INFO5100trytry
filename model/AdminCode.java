package model;

/**
 * - 密钥类
 * @author Administrator
 *
 */
public class AdminCode {
	private int id;
	private String code;
	private int count;


	public AdminCode() {
	}
	
	

	public AdminCode(int id, String code, int count) {
		super();
		this.id = id;
		this.code = code;
		this.count = count;
	}
    

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
}
