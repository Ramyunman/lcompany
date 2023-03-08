package com.lcomputerstudy.testmvc.service;

import java.util.ArrayList;


import com.lcomputerstudy.testmvc.dao.BoardDAO;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.Search;

public class BoardService {
	
	private static BoardService service = null;
	private static BoardDAO dao = null;
	
	private BoardService() {
		
	}
	
	public static BoardService getInstance() {
		if(service == null) {
			service = new BoardService();
			dao = BoardDAO.getInstance();
		}
		return service;
	}
	
	public ArrayList<Board> getBoards(Pagination pagination) {
		return dao.getBoards(pagination);
	}
	public void insertBoard(Board board) {
		dao.insertBoard(board);
	}
	public Board detailBoard(Board board) {
		return dao.detailBoard(board);
	}
	public Board deleteBoard(Board board) {
		return dao.deleteBoard(board);
	}
	public void updateBoard(Board board) {
		dao.updateBoard(board);
	}
	public int getBoardsCount(Search search) {
		return dao.getBoardsCount(search);
	}
	public void replyInsert(Board board) {		//답글
		dao.replyInsert(board);
	}
	

}