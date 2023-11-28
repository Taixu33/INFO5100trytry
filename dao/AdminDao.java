package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Admin;
import model.Book;
import utils.JDBCUtil;

public class AdminDao extends UserDao {
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	
	
	/*
	 * 通过账号、密码查询管理员
	 */
	public Admin findAllByAdminAndPasswd(String userName, String passWord) {
		Admin admin = null;
		try {
			String sql = "select * from admin where username=? and password= ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setString(2, passWord);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Integer id = rs.getInt("adminID");
				String username = rs.getString("username");
				String password = rs.getString("password");
				
				admin = new Admin(id, username, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCUtil.release(rs, ps, conn);
		}
		
		return admin;
	}


	/**
	 * - 添加图书
	 * @param book
	 */
	public String addBook(Book book) {
		String msg = "";
		try {
			String sql = "insert into book (bookname,author,number,borrow,quantity,location) values (?,?,?,?,?,?)";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, book.getBookname());
			ps.setString(2, book.getAuthor());
			ps.setLong(3, book.getNum());
			ps.setString(4, book.getBorrow());
			ps.setInt(5,book.getQuantity());
			ps.setString(6, book.getLocation());
			
			int flag = ps.executeUpdate();
			if(flag == 1) {
				msg = "Successfully Added!";
			}else {
				msg = "Failed to Add!";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.release(ps, conn);
		}
		return msg;
	}

	/**
	 *  - 根据图书编号删除图书
	 */
	public String deleteByNum(String num) {
		String msg = "";
		try {
			String sql = "delete from book where number = ?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, num);
			int flag = ps.executeUpdate();
			if (flag == 1) {
				msg = "Successfully Deleted!";
			}else {
				msg = "Failed to Delete";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCUtil.release(ps, conn);
		}
		return msg;
	}

	/**
	 * - 修改图书
	 * @param book
	 */
	public String updateBook(Book book) {
		String msg = "";
		try {
			String sql = "update book set bookname=?, author=?, number=?, borrow=?, quantity=?, location=? where bookid=?";
			conn = JDBCUtil.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, book.getBookname());
			ps.setString(2, book.getAuthor());
			ps.setString(3, book.getNum().toString());
			ps.setString(4, book.getBorrow());
			ps.setString(5,book.getQuantity().toString());
			ps.setString(6, book.getLocation());
			ps.setString(7, book.getBookid().toString());
			
			int flag = ps.executeUpdate();
			if (flag == 1) {
				msg = "Successfully Updated!";
			}else {
				msg = "Failed to Update!";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCUtil.release(ps, conn);
		}
		return msg;
	}
}
