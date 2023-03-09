package com.lcomputerstudy.testmvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.lcomputerstudy.testmvc.database.DBConnection;
import com.lcomputerstudy.testmvc.vo.Comment;
import com.lcomputerstudy.testmvc.vo.User;

public class CommentDAO {
	
	private static CommentDAO dao = null;
	
	private CommentDAO() {
		
	}
	
	public static CommentDAO getInstance() {
		if(dao == null) {
			dao = new CommentDAO();
		}
		return dao;
	}
	
	public ArrayList<Comment> getComments(int b_idx) {		// 댓글 리스트
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Comment> commentList = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = new StringBuilder()
					.append("SELECT        ta.*, \n")
					.append("              tb.u_name, u_level \n")
					.append("FROM          comment ta \n")
					.append("LEFT JOIN     user tb ON ta.u_idx = tb.u_idx \n")
					.append("WHERE 		   b_idx = ? \n")
					.append("ORDER BY c_group DESC, c_order ASC, c_depth ASC")
					.toString();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, b_idx);
			rs = pstmt.executeQuery();
			commentList = new ArrayList<Comment>();
			
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setC_idx(rs.getInt("c_idx"));
				comment.setC_content(rs.getString("c_content"));
				comment.setC_date(rs.getString("c_date"));
				comment.setB_idx(rs.getInt("b_idx"));
				comment.setC_group(rs.getInt("c_group"));
				comment.setC_order(rs.getInt("c_order"));
				comment.setC_depth(rs.getInt("c_depth"));
								
				commentList.add(comment);
				
				User user = new User();
				user.setU_name(rs.getString("u_name"));
				user.setU_level(rs.getInt("u_level"));
				comment.setUser(user);
				
			}
		} catch (Exception e) {
			e.printStackTrace();		
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return commentList;
	}
	
	public void insertComment(Comment comment, HttpSession session) {		// 댓글 목록에 원댓글 넣기
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();	
			String sql = "INSERT INTO comment(c_content, c_date, b_idx, u_idx, c_group, c_order, c_depth) values (?,now(),?,?,0,1,0)";		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comment.getC_content());
			pstmt.setInt(2, comment.getB_idx());
			pstmt.setInt(3, (int)session.getAttribute("u_idx"));
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement("UPDATE comment SET c_group = last_insert_id() WHERE c_idx = last_insert_id()");
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
	
	public void commentInComments(Comment comment, HttpSession session) {	// 댓글 목록 안에 있는 댓글에 댓글달기(대댓글 달기)
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE comment SET c_order = c_order+1 where c_group = ? and c_order > ?";	// 원글에 계속 댓글을 다는게 아니라 새로이 원글에 댓글을 달때
			pstmt = conn.prepareStatement(sql);															// 더 큰쪽이 1이 더해지면서 뒤로 밀려나면서 새로 하나 들어온다.
			pstmt.setInt(1, comment.getC_group());
			pstmt.setInt(2, comment.getC_order());
			pstmt.executeUpdate();
			pstmt.close();
			
			sql = "INSERT INTO comment(c_content, c_date, b_idx, u_idx, c_group, c_order, c_depth) values (?,now(),?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comment.getC_content());
			pstmt.setInt(2, comment.getB_idx());
			pstmt.setInt(3, (int)session.getAttribute("u_idx"));
			pstmt.setInt(4, comment.getC_group());
			pstmt.setInt(5, comment.getC_order() + 1);
			pstmt.setInt(6, comment.getC_depth() + 1);
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
	
	public void updateComment(Comment comment) {		//댓글 또는 대댓글 수정
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE comment SET c_content = ? where b_idx = ? and c_group = ? and c_order = ? and c_depth = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comment.getC_content());
			pstmt.setInt(2, comment.getB_idx());
			pstmt.setInt(3, comment.getC_group());
			pstmt.setInt(4, comment.getC_order());
			pstmt.setInt(5, comment.getC_depth());
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

	public void deleteComment(Comment comment) {			//댓글 또는 대댓글 삭제
		Connection conn = null;
		PreparedStatement pstmt = null;
				
		try {
			conn = DBConnection.getConnection();
			String query = "DELETE from comment where b_idx = ? and c_group = ? and c_order = ? and c_depth = ?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, comment.getB_idx());
			pstmt.setInt(2, comment.getC_group());
			pstmt.setInt(3, comment.getC_order());
			pstmt.setInt(4, comment.getC_depth());
			pstmt.executeUpdate();
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	


}