package com.lcomputerstudy.testmvc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.lcomputerstudy.testmvc.database.DBConnection;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.Search;
import com.lcomputerstudy.testmvc.vo.User;


public class BoardDAO {
	
	private static BoardDAO dao = null;
	
	private BoardDAO() {
		
	}
	
	public static BoardDAO getInstance() {
		if(dao == null) {
			dao = new BoardDAO();
		}
		return dao;
	}
	
	public ArrayList<Board> getBoards(Pagination pagination) {		// board-list
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<Board> list = null;
		int pageNum = pagination.getPageNum();
		Search search = pagination.getSearch();		//pagination에서 search를 가져오기
		
		String where = "";
		if(search != null) {	
			String tcw = search.getTcw();
			switch (tcw != null ? tcw : "none") {
				case "title":
					where = "WHERE b_title like ? \n";
					break;
				case "content":
					where = "WHERE b_content like ? \n";
					break;
				case "writer":
					where = "WHERE u_name like ? \n";
					break;
			} 
		}
		
		try {
			conn = DBConnection.getConnection();				
			String query = new StringBuilder()		//쿼리 수정
					.append("SELECT         @ROWNUM := @ROWNUM - 1 AS ROWNUM, b.*, u.u_name \n")
					.append("FROM           board b \n")
					.append("INNER JOIN     user u ON b.u_idx = u.u_idx \n")
					.append("INNER JOIN     (SELECT @rownum := (SELECT COUNT(*)-?+1 FROM board b)) tc ON 1=1 \n")
					.append(where)
					.append("ORDER BY b_group DESC, b_order ASC\n")
					.append("LIMIT          ?, ? \n")
					.toString();
			
			pstmt = conn.prepareStatement(query);
			
			String channel = search.getTcw();
			switch (channel != null ? channel : "none") {
				case "title" :
					pstmt.setInt(1, pageNum);
					pstmt.setString(2, "%"+search.getSearchbox()+"%");		//추가
					pstmt.setInt(3, pageNum);
					pstmt.setInt(4, Pagination.perPage);
					break;
				case "content" :
					pstmt.setInt(1, pageNum);
					pstmt.setString(2, "%"+search.getSearchbox()+"%");		//추가
					pstmt.setInt(3, pageNum);
					pstmt.setInt(4, Pagination.perPage);
					break;
				case "writer" :
					pstmt.setInt(1, pageNum);
					pstmt.setString(2, "%"+search.getSearchbox()+"%");		//추가
					pstmt.setInt(3, pageNum);
					pstmt.setInt(4, Pagination.perPage);
					break;
				case "none":
					pstmt.setInt(1, pageNum);
					pstmt.setInt(2, pageNum);
					pstmt.setInt(3, Pagination.perPage);
					break;
			} 
						
			rs = pstmt.executeQuery();
			list = new ArrayList<Board>();
			
			while(rs.next()) {
				Board board = new Board();
				int rownum = rs.getInt("ROWNUM");
				if (!rs.wasNull()) { // rs 객체가 null이 아닌 경우에만 getInt 메소드를 호출합니다.
					board.setRownum(rownum);
				}
				board.setB_idx(rs.getInt("b_idx"));
				board.setB_title(rs.getString("b_title"));
				board.setB_content(rs.getString("b_content"));
				board.setB_views(rs.getString("b_views"));
				board.setB_date(rs.getString("b_date"));
				board.setU_idx(rs.getInt("u_idx"));
				board.setB_group(rs.getInt("b_group"));
				board.setB_order(rs.getInt("b_order"));
				board.setB_depth(rs.getInt("b_depth"));
				
				list.add(board);
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
		
		return list;
	}
	
	public void insertBoard(Board board, HttpSession session) {		// 등록
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "insert into board(b_title, b_content, b_views, b_date, u_idx, b_group, b_order, b_depth) values (?,?,0,now(),?,0,1,0)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getB_title());
			pstmt.setString(2, board.getB_content());
			pstmt.setInt(3, (int)session.getAttribute("u_idx"));	
			pstmt.executeUpdate();
			pstmt.close();
			
			pstmt = conn.prepareStatement("UPDATE board SET b_group = last_insert_id() WHERE b_idx = last_insert_id()");
			pstmt.executeUpdate();
		
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public Board detailBoard(Board board) {		// 상세보기
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Board resultBoard = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = new StringBuilder()
					.append("SELECT        ta.*, \n")
					.append("              tb.u_name, u_level \n")
					.append("FROM          board ta \n")
					.append("LEFT JOIN     user tb ON ta.u_idx = tb.u_idx \n")
					.append("WHERE 		   b_idx = ?")
					.toString();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, board.getB_idx());	//첫번째 물음표에 board.getB_idx()가 들어간다.
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				resultBoard = new Board();
				resultBoard.setB_idx(Integer.parseInt(rs.getString("b_idx")));
				resultBoard.setB_title(rs.getString("b_title"));
				resultBoard.setB_content(rs.getString("b_content"));
				resultBoard.setB_views(rs.getString("b_views"));
				resultBoard.setB_date(rs.getString("b_date"));	
				resultBoard.setU_idx(rs.getInt("u_idx"));	// u_idx 추가
				resultBoard.setB_group(Integer.parseInt(rs.getString("b_group")));		
				resultBoard.setB_order(Integer.parseInt(rs.getString("b_order")));		
				resultBoard.setB_depth(Integer.parseInt(rs.getString("b_depth")));		
				
				User user = new User();
				user.setU_name(rs.getString("u_name"));
				user.setU_level(rs.getInt("u_level"));		//u_level이 보이게 하는것 추가
				resultBoard.setUser(user);
				
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
		return resultBoard;
	}
	
	public void updateBoard(Board board) {		// 수정
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "UPDATE board SET b_title = ?, b_content = ?, b_date = now() where b_idx = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getB_title());
			pstmt.setString(2, board.getB_content());
			pstmt.setInt(3, board.getB_idx());
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

	public Board deleteBoard(Board board) {		// 삭제
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Board resultBoard = null;
		
		try {
			conn = DBConnection.getConnection();
			String query = "delete from board where b_idx=?";
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, board.getB_idx());
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
		return resultBoard;
	}
	
	public int getBoardsCount(Search search) {		// 게시물 수를 알려준다.
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		try {
			conn = DBConnection.getConnection();
			String query = new StringBuilder()
					.append("SELECT        COUNT(*) count \n")
					.append("FROM          board b \n")
					.append("LEFT JOIN     user u ON b.u_idx = u.u_idx \n")
					.toString();
			pstmt = null;
			
			if(search != null) {
				String tcw = search.getTcw();			
				switch ((tcw != null) ? tcw : "none" ) {
					case "title" :
						query += "WHERE b_title LIKE ? ";
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, "%"+search.getSearchbox()+"%");
						
						break;
					case "content" :
						query += "WHERE b_content LIKE ? ";
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, "%"+search.getSearchbox()+"%");

						break;
					case "writer" :
						query += "WHERE u_name LIKE ? ";
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, "%"+search.getSearchbox()+"%");
						break;	
					default:
						query = "SELECT COUNT(*) count FROM board ";
						pstmt = conn.prepareStatement(query);
						break;											
				} 		
			} 			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				count = rs.getInt("count");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public void replyInsert(Board board, HttpSession session) {		// 답글 
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBConnection.getConnection();
			String sql = "update board set b_order = b_order+1 where b_group = ? and b_order > ?";	//원글에 계속 다는거 말고 새로 원글에 답글을 달때
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getB_group());
			pstmt.setInt(2, board.getB_order()); 	
			pstmt.executeUpdate();
			pstmt.close();
			
			sql = "insert into board(b_title, b_content, b_views, u_idx, b_date, b_group, b_order, b_depth) values (?,?,0,?,now(),?,?,?)";	//원글에 계속 다는거(사선으로)		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getB_title());
			pstmt.setString(2, board.getB_content());
			pstmt.setInt(3, (int)session.getAttribute("u_idx"));		//u_idx
			pstmt.setInt(4, board.getB_group());
			pstmt.setInt(5, board.getB_order() + 1);
			pstmt.setInt(6, board.getB_depth() + 1);
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