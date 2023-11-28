package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import model.User;
import utils.JDBCUtil;

public class UserDao {
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	

	
	/*
	 * 通过账号、密码查询用户
	 */
	public User findAllByUserAndPasswd(String userName, String userPassword) {
		User user = null;
		Integer id = null;
		String username = null;
		String password = null;
		try {
			String sql = "select * from users where username=? and password=?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, userPassword);
			rs = ps.executeQuery();//执行sql查询，返回结果集
			
			//在使用rs.getxxx()之前，必须使用rs.next()，否则会发生错误
			if(rs.next()) {
				id = rs.getInt("userID");
				username = rs.getString("username");
				password = rs.getString("password");
				
				
				user = new User(id,username,password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return user;
	}
	
	/**
	 * 通过书名检索
	 */
	public List<Book> findBookByBookname(String bookname) {
		List<Book> books = new ArrayList<>();
		try {
			String sql = "select * from book where bookname = ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1,bookname);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				books.add(new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),rs.getString("borrow"),rs.getInt("quantity"),rs.getString("location")));
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}
	
	/**
	 * 通过作者检索
	 * 
	 */
	public List<Book> findBookByAuthor(String author) {
		List<Book> books = new ArrayList<>();
		try {
			String sql = "select * from book where author like ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+author+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {
				books.add(new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),rs.getString("borrow"),rs.getInt("quantity"),rs.getString("location")));
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}
	
	/**
	 * 通过编号检索
	 */
	public List<Book> findBookByNum(String num) {
		List<Book> books = new ArrayList<>();
		try {
			String sql = "select * from book where number = ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, num);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				books.add(new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),rs.getString("borrow"),rs.getInt("quantity"),rs.getString("location")));
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}
	
	public List<Book> findAllBook() {
		List<Book> books = new ArrayList<>();
		try {
			String sql = "select * from book";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				books.add(new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),rs.getString("borrow"),rs.getInt("quantity"),rs.getString("location")));
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}

	public List<Book> borrowBookByBookname(String bookname) {
		List<Book> books = new ArrayList<>();
		int count = 0;
		try {
			String sql = "select * from book where bookname = ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1,bookname);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("quantity")-1;
				if(count >= 0){

				Book temp = new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),count == 0? "N":"Y",count,rs.getString("location"));
				books.add(temp);

				updateAfterBorrow(temp);
				}else{
					books = null;
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}

		public List<Book> borrowBookByAuthor(String author) {
		List<Book> books = new ArrayList<>();
		int count = 0;
		try {
			String sql = "select * from book where author like ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+author+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {
                count = rs.getInt("quantity")-1;
				if(count >=0 ){
				Book temp = new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),count == 0? "N":"Y",count,rs.getString("location"));
				books.add(temp);

				updateAfterBorrow(temp);
				}else{
					books = null;
				}	
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}

	public List<Book> borrowBookByNum(String num) {
		List<Book> books = new ArrayList<>();
		int count = 0;
		try {
			String sql = "select * from book where number = ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, num);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("quantity")-1;
				if(count >=0 ){
				Book temp = new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),count == 0? "N":"Y",count,rs.getString("location"));
				books.add(temp);

				updateAfterBorrow(temp);
				}else{
					books = null;
				}	
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}

	public List<Book> returnBookByBookname(String bookname) {
		List<Book> books = new ArrayList<>();
		int count = 0;
		try {
			String sql = "select * from book where bookname = ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1,bookname);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("quantity")+1;
				Book temp = new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),count == 0? "N":"Y",count,rs.getString("location"));
				books.add(temp);

				updateAfterBorrow(temp);
				
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}

		public List<Book> returnBookByAuthor(String author) {
		List<Book> books = new ArrayList<>();
		int count = 0;
		try {
			String sql = "select * from book where author like ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, "%"+author+"%");
			rs = ps.executeQuery();
			
			while(rs.next()) {
                count = rs.getInt("quantity")+1;
				
				Book temp = new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),count == 0? "N":"Y",count,rs.getString("location"));
				books.add(temp);

				updateAfterBorrow(temp);
				
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}

	public List<Book> returnBookByNum(String num) {
		List<Book> books = new ArrayList<>();
		int count = 0;
		try {
			String sql = "select * from book where number = ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, num);
			rs = ps.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("quantity")+1;

				Book temp = new Book(rs.getInt("bookid"),rs.getString("bookname"),rs.getString("author"),
						rs.getLong("number"),count == 0? "N":"Y",count,rs.getString("location"));
				books.add(temp);
				updateAfterBorrow(temp);
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		return books;
	}

	public void updateAfterBorrow(Book book) {

		try {
			String sql = "update book set borrow=?, quantity=? where bookid=?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, book.getBorrow().toString());
			ps.setString(2,book.getQuantity().toString());
			ps.setString(3, book.getBookid().toString());
			
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCUtil.release(ps, conn);
		}

	}

	
}
