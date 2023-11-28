package model;

public class Book {
	private Integer bookid;
	private String bookname;
	private String author;
	private Long num;
	private String borrow; //是否可以借阅
	private Integer quantity;
	private String location;//存放位置
	
	public Book() {
		super();
	}

	public Book(String bookname, String author, Long num, String borrow, Integer quantity, String location){
		super();
		this.bookname = bookname;
		this.author = author;
		this.num = num;
		this.borrow = borrow;
		this.quantity = quantity;
		this.location = location;
	}

	public Book(Integer bookid, String bookname, String author, Long num, String borrow, Integer quantity, String location) {
		super();
		this.bookid = bookid;
		this.bookname = bookname;
		this.author = author;
		this.num = num;
		this.borrow = borrow;
		this.quantity = quantity;
		this.location = location;
	}


	public Integer getBookid() {
		return this.bookid;
	}

	public void setBookid(Integer bookid) {
		this.bookid = bookid;
	}

	public String getBookname() {
		return this.bookname;
	}

	public void setBookname(String bookname) {
		this.bookname = bookname;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getNum() {
		return this.num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getBorrow() {
		return this.borrow;
	}

	public void setBorrow(String borrow) {
		this.borrow = borrow;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
