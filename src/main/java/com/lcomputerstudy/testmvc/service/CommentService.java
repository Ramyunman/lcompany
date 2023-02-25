package com.lcomputerstudy.testmvc.service;

import java.util.ArrayList;

import com.lcomputerstudy.testmvc.dao.CommentDAO;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Comment;

public class CommentService {
	
	private static CommentService service = null;
	private static CommentDAO dao = null;
	
	private CommentService() {
		
	}
	
	public static CommentService getInstance() {
		if(service == null) {
			service = new CommentService();
			dao = CommentDAO.getInstance();
		}
		return service;
	}
	
	public ArrayList<Comment> getComments(int b_idx) {		//댓글 목록
		return dao.getComments(b_idx);
	}
	public void insertComment(Comment comment) {			//원댓글 등록
		dao.insertComment(comment);
	}
	public void commentInComments(Comment comment) {		//대댓글 달기
		dao.commentInComments(comment);	
	}
	public void updateComment(Comment comment) {			//댓글 수정
		dao.updateComment(comment); 
	}												
	public void deleteComment(Comment comment) {			//댓글 삭제
		dao.deleteComment(comment);
	}
	
	
	
}