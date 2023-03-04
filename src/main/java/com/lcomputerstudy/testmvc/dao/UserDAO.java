package com.lcomputerstudy.testmvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.lcomputerstudy.testmvc.database.DBConnection;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.User;

public class UserDAO {
	
	private static UserDAO dao = null;
		
	private UserDAO() {
		
	}
	
	public static UserDAO getInstance() {	
		if(dao == null) {
			dao = new UserDAO();
		}
		return dao;
	}
	
	public ArrayList<User> getUsers(Pagination pagination) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<User> list = null;
		int pageNum = pagination.getPageNum();
		
		try {
			conn = DBConnection.getConnection();
			// String query = "select * from user limit ?,3";
			String query = new StringBuilder()
					.append("SELECT         @ROWNUM := @ROWNUM - 1 AS ROWNUM,\n")
					.append("               ta.*\n")
					.append("FROM           user ta\n")
					.append("INNER JOIN      (SELECT @rownum := (SELECT COUNT(*)-?+1 FROM user ta)) tb ON 1=1 \n")
					.append("ORDER BY u_idx DESC\n")
					.append("LIMIT          ?, ?\n")
					.toString();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, pageNum);
			pstmt.setInt(2, pageNum);
			pstmt.setInt(3, Pagination.perPage);
			rs = pstmt.executeQuery();
			list = new ArrayList<User>();
			
			while(rs.next()) {
				User user = new User();
				user.setRownum(rs.getInt("ROWNUM"));
				user.setU_idx(rs.getInt("u_idx"));
				user.setU_id(rs.getString("u_id"));
				user.setU_name(rs.getString("u_name"));
				user.setU_tel(rs.getString("u_tel"));
				user.setU_telArr(user.getU_tel().split("-"));	//전화번호 3개로 쪼개기
				user.setU_age(rs.getString("u_age"));
				user.setU_level(rs.getInt("u_level"));	         // level추가
				
				list.add(user);
			}
		} catch (Exception e) {
			e.printStackTrace();		//예외 설정을 하자!!!
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public void insertUser(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "insert into user(u_id, u_pw, u_name, u_tel, u_age, u_level) values(?,?,?,?,?,1)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getU_id());
			pstmt.setString(2, user.getU_pw());
			pstmt.setString(3, user.getU_name());
			pstmt.setString(4, user.getU_tel());
			pstmt.setString(5, user.getU_age());
			pstmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("SQLException : " + ex.getMessage());
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public User detailUser(User user) {		// 01-18	상세보기
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User resultUser = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = "select * from user where u_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, user.getU_idx());	//첫번째 물음표에 user.getU_idx가 들어간다.
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				resultUser = new User();
				resultUser.setU_idx(Integer.parseInt(rs.getString("u_idx")));
				resultUser.setU_id(rs.getString("u_id"));
				resultUser.setU_name(rs.getString("u_name"));
				resultUser.setU_tel(rs.getString("u_tel"));
				resultUser.setU_telArr(resultUser.getU_tel().split("-"));		// 배열이 필요한 이유는 u_tel에 저장되어 있는 것을 3군데로 나누기 위한 배열 즉 담은 공간 3개 바구니를 만든것과 같은 것이다.
				resultUser.setU_pw(rs.getString("u_pw"));
				resultUser.setU_age(rs.getString("u_age"));
		   	  	resultUser.setU_level(rs.getInt("u_level"));		//나중에 지우기 level 보여주는거
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultUser;
	}
	
	public void updateUser(User user) {		// 01-19	업데이트
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE user SET u_id = ?, u_pw = ?, u_name = ?, u_tel = ?, u_age = ? where u_idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, user.getU_id());
			pstmt.setString(2, user.getU_pw());
			pstmt.setString(3, user.getU_name());
			pstmt.setString(4, user.getU_tel());
			pstmt.setString(5, user.getU_age());
			pstmt.setString(6, String.valueOf(user.getU_idx()));
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	

	
	public User deleteUser(User user) {		// 01-19	삭제
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User resultUser = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = "delete from user where u_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, user.getU_idx());
			rs = pstmt.executeQuery();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return resultUser;
	}
	
	public int getUsersCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		try {
			conn = DBConnection.getConnection();
			String query = "SELECT COUNT(*) count FROM user ";
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("count");
			}
		} catch (Exception e) {
			
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
		
	}
	
	public User loginUser(String id, String pw) {		// login
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			conn = DBConnection.getConnection();
			String sql = "SELECT * FROM user WHERE u_id = ? AND u_pw = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				user = new User();
				user.setU_idx(rs.getInt("u_idx"));
				user.setU_pw(rs.getString("u_pw"));
				user.setU_id(rs.getString("u_id"));
				user.setU_name(rs.getString("u_name"));
			}
		} catch (Exception ex) {
			System.out.println("SQLException : " + ex.getMessage());
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}	// end of loginUser
	
	public void adminOnUser(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE user SET u_level = 9 where u_idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getU_idx());
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
	}
	
	public void adminOffUser(User user) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE user SET u_level = 1 where u_idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getU_idx());
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
	}
	
	
	

}