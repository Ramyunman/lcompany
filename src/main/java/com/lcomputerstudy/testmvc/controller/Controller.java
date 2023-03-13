package com.lcomputerstudy.testmvc.controller;

import java.io.IOException;

import java.util.ArrayList;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import com.lcomputerstudy.testmvc.service.BoardService;
import com.lcomputerstudy.testmvc.service.CommentService;
import com.lcomputerstudy.testmvc.service.UserService;
import com.lcomputerstudy.testmvc.vo.Board;
import com.lcomputerstudy.testmvc.vo.Comment;
import com.lcomputerstudy.testmvc.vo.Pagination;
import com.lcomputerstudy.testmvc.vo.Search;
import com.lcomputerstudy.testmvc.vo.User;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("*.do")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());
		String view = null;
		String id = null;		//login
		String pw = null;		//login
		HttpSession session = null;		//login
		command = checkSession(request, response, command);		//권한
		
		UserService userService = null; 
		BoardService boardService = null;		//board 추가
		CommentService commentService = null;
		User user = null;	
		Board board = null;			//board 추가
		Comment comment = null;		//comment(댓글)추가
		ArrayList<Comment> commentList = null;
		
		int page = 1;
		int count = 0;
	    int b_idx = 0;		//comment
		Pagination pagination = null;
			
		switch (command) {
			case "/user-list.do":
				String reqPage = request.getParameter("page");
				if (reqPage != null) 
					page = Integer.parseInt(reqPage);
					
				userService = UserService.getInstance();
				count = userService.getUsersCount();
				
				pagination = new Pagination();
				pagination.setPage(page);
				pagination.setAmount(count);
				pagination.init();
				
				ArrayList<User> list = userService.getUsers(pagination);
				
				request.setAttribute("list", list);
				request.setAttribute("pagination", pagination);
				
				view = "user/list";
				break;
				
			case "/user-insert.do":			//보여주기
				view = "user/insert";
				break;
				
			case "/user-insert-process.do":		//실제 저장하는 코드 -> 있는 이유 : 이게 있어야 저장이 된다. 위에 하나만 있으면 시작하자마자 바로 저장이 되어서 입력할수가 없다.
				user = new User();
				user.setU_id(request.getParameter("id"));
				user.setU_pw(request.getParameter("password"));
				user.setU_name(request.getParameter("name"));
				user.setU_tel(request.getParameter("tel1") + "-" + request.getParameter("tel2") + "-" + request.getParameter("tel3"));
				user.setU_age(request.getParameter("age"));
				userService = UserService.getInstance();
				userService.insertUser(user);
				view = "user/insert-result";
				break;
				
			case "/user-detail.do":		//01-18
				user = new User();		//user라는 저장공간을 만들어 놓은 것이다. 새로운 인스턴스를 만든게 아니라.
				user.setU_idx(Integer.parseInt(request.getParameter("u_idx")));
				userService = UserService.getInstance();
				user = userService.detailUser(user);
				request.setAttribute("user", user);
				view = "user/detail";
				break;
				
			case "/user-update.do":		//01-19
				user = new User();		//user라는 저장공간을 만들어 놓은 것이다. 새로운 인스턴스를 만든게 아니라.
				user.setU_idx(Integer.parseInt(request.getParameter("u_idx")));
				userService = UserService.getInstance();
				user = userService.detailUser(user);
				request.setAttribute("user", user);
				view = "user/update";
				break;
			
			case "/user-update-process.do":		//01-19
				user = new User();
				user.setU_id(request.getParameter("id"));
				user.setU_pw(request.getParameter("password"));
				user.setU_name(request.getParameter("name"));
				user.setU_tel(request.getParameter("tel1") + "-" + request.getParameter("tel2") + "-" + request.getParameter("tel3"));
				user.setU_telArr(user.getU_tel().split("-"));	//1
				user.setU_age(request.getParameter("age"));
				user.setU_idx(Integer.parseInt(request.getParameter("u_idx")));
				userService = UserService.getInstance();
				userService.updateUser(user);
				view = "user/update-result";
				break; 
				
			case "/user-delete.do":		//01-19
				user = new User();
				user.setU_idx(Integer.parseInt(request.getParameter("u_idx")));
				userService = UserService.getInstance();
				user = userService.deleteUser(user);
				view = "user/delete";
				break;
			case "/user-login.do":		//login
				view = "user/login";
				break;
			case "/user-login-process.do":		//login
				// 세션을 생성
				session = request.getSession();
				
				// 로그인 파라미터 받아오기
				id = request.getParameter("login_id");
				pw = request.getParameter("login_password");
				
				// 로그인 서비스를 이용하여 사용자 정보 조회
				userService = UserService.getInstance();
				user = userService.loginUser(id,pw);
				
				if(user != null) {
					// 세선에 사용자 정보 저장
					session.setAttribute("user", user);
//					session.setAttribute("u_idx", user.getU_idx());	// 세선에 u_idx 저장
//					session.setAttribute("u_level", user.getU_level());	// u_level 값을 세션에 저장
//					session.setAttribute("u_id", user.getU_id());
					view = "user/login-result";
				} else {
					view = "user/login-fail";
				}
				
				break;
			case "/logout.do":
				session = request.getSession();
				session.invalidate();		//세션 무효화
				view = "user/login";
				break;
			case "/access-denied.do":
				view = "user/access-denied";
				break;
			case "/user-adminOn.do":	//level만 1 -> 9 로 업데이트
				user = new User();
				user.setU_idx(Integer.parseInt(request.getParameter("u_idx")));
				user.setU_level(Integer.parseInt(request.getParameter("u_level")));
				userService = UserService.getInstance();
				userService.adminOnUser(user);
				view = "user/u_list";
				break;
			case "/user-adminOff.do":	//level만 9 -> 1 로 업데이트
				user = new User();
				user.setU_idx(Integer.parseInt(request.getParameter("u_idx")));
				user.setU_level(Integer.parseInt(request.getParameter("u_level")));
				userService = UserService.getInstance();
				userService.adminOffUser(user);
				view = "user/u_list";
				break;
			////////////////////////////board/////////////////////////////////
			case "/board-list.do":
				
				String reqPage2 = request.getParameter("page");
				if (reqPage2 != null) 
					page = Integer.parseInt(reqPage2);
				
				boardService = BoardService.getInstance(); 
							
				Search search = new Search();	
				search.setTcw(request.getParameter("tcw"));
				search.setSearchbox(request.getParameter("searchbox"));
				count = boardService.getBoardsCount(search);
				
				pagination = new Pagination();		
				pagination.setSearch(search);
				pagination.setPage(page);			//페이지가 나옴
				pagination.setAmount(count);		//총데이터 개수 나옴
				pagination.init();					//init() 메소드를 실행
									
				ArrayList<Board> list2 = boardService.getBoards(pagination);
				boardService.getBoards(pagination);
				
				request.setAttribute("list", list2);
				request.setAttribute("pagination", pagination);
				
				view = "board/b_list";
				break;
				
			case "/board-insert.do":			//보여주기
				view = "board/b_insert";
				break;
				
			case "/board-insert-process.do":		
				try {
					String rootPath = System.getProperty("user.home");
					String savePath = rootPath + "/Documents/work12/lcompany/src/main/webapp/upload";
				
					int maxSize = 10 * 1024 * 1024; // 최대 업로드 파일 크기를 10MB로 제한한다.
					String encoding = "UTF-8";
					MultipartRequest multi = new MultipartRequest(request, savePath, maxSize, encoding, new DefaultFileRenamePolicy());
				
					//Board 객체 생성
					board = new Board();
				
					// Board 객체에 파일 이름 및 파일 경로 설정
					String b_fileName = multi.getFilesystemName("b_fileName");
					if (b_fileName != null) {
						board.setB_fileName(b_fileName);
						board.setB_filePath(savePath + "/" + b_fileName);	// 파일 경로 설정 
					} else {
						board.setB_fileName("");		//업로드된 파일이 없는 경우
						board.setB_filePath("");		//업도르된 파일이 없는 경우
					}
											
					// 나머지 Board 객체 설정
					board.setB_title(multi.getParameter("title"));
					board.setB_content(multi.getParameter("content"));
					
				
					// BoardService를 통해 Board 객체를 DB에 저장
					//session = request.getSession();	//세션 생성 코드
					user = (User)request.getSession().getAttribute("user");
					board.setUser(user);
					
					// BoardService를 통해 Board객체를 DB에 저장
					boardService = BoardService.getInstance();
					boardService.insertBoard(board);
				
					// 결과 페이지로 이동
					view = "board/b_insert-result";
				} catch (Exception e) {
					e.printStackTrace();
					// 에러 페이지로 이동
					view = "error.jsp";
				}
				break;
				
			case "/board-detail.do":		// board 상세정보
				board = new Board();		//board라는 저장공간을 만들어 놓은 것이다. 새로운 인스턴스를 만든게 아니라.
				b_idx = Integer.parseInt(request.getParameter("b_idx"));
				board.setB_idx(b_idx);
				boardService = BoardService.getInstance();
				board = boardService.detailBoard(board);
				request.setAttribute("board", board);
								
				commentService = CommentService.getInstance();		//comment
				commentList = commentService.getComments(b_idx);
				request.setAttribute("commentList", commentList);
				view = "board/b_detail";				
				break;
				
			case "/board-update.do":		//01-19
				board = new Board();		//board라는 저장공간을 만들어 놓은 것이다. 새로운 인스턴스를 만든게 아니라.
				board.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				boardService = BoardService.getInstance();
				board = boardService.detailBoard(board);
				request.setAttribute("board", board);
				view = "board/b_update";
				break;
			
			case "/board-update-process.do":		//01-19
				board = new Board();
				board.setB_title(request.getParameter("title"));
				board.setB_content(request.getParameter("content"));
				board.setB_views(Integer.parseInt(request.getParameter("views")));
				board.setB_writer(request.getParameter("writer"));
				board.setB_date(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day"));
				board.setB_dateArr(board.getB_date().split("-"));	//1
				board.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				boardService = BoardService.getInstance();
				boardService.updateBoard(board);
				view = "board/b_update-result";
				break; 
				
			case "/board-delete.do":		//01-19
				board = new Board();
				board.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				boardService = BoardService.getInstance();
				board = boardService.deleteBoard(board);
				view = "board/b_delete";
				break;
				
			case "/board-reply-insert.do":			//답글 보기
				board = new Board();
				board.setB_group(Integer.parseInt(request.getParameter("b_group")));
				board.setB_order(Integer.parseInt(request.getParameter("b_order")));
				board.setB_depth(Integer.parseInt(request.getParameter("b_depth")));
				
				request.setAttribute("board", board);	
				view = "board/b_reply-insert";
				break;
				
			case "/board-reply-insert-process.do":		
				session = request.getSession();	//세션 생성 코드
				board = new Board();
				board.setB_title(request.getParameter("title"));
				board.setB_content(request.getParameter("content"));
				board.setB_writer(request.getParameter("writer"));
				board.setB_group(Integer.parseInt(request.getParameter("b_group")));
				board.setB_order(Integer.parseInt(request.getParameter("b_order")));
				board.setB_depth(Integer.parseInt(request.getParameter("b_depth")));
				
				boardService = BoardService.getInstance();
				boardService.replyInsert(board,session);
				view = "board/b_reply-insert-result";
				break;
				
			/////////////////comment						
			case "/comment-original-insert-process.do":		// b_detail에 댓글 달기
				session = request.getSession();	//세션 생성 코드
				comment = new Comment();
				comment.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				comment.setC_content(request.getParameter("c_content"));
			
				commentService = CommentService.getInstance();
				commentService.insertComment(comment,session);
				
				commentList = commentService.getComments(comment.getB_idx());		//받아왔던 목록을 다시 넘겨줌.
				request.setAttribute("commentList", commentList);					//jsp에게로 commenList를 넘겨줌
				view = "board/c_list";
				break;
		
			case "/comment-commentInComments.do":	// b_detail 댓글 목록의 댓글에 댓글 달기
				session = request.getSession();	//세션 생성 코드
				comment = new Comment();
				comment.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				comment.setC_content(request.getParameter("c_content"));
				comment.setC_group(Integer.parseInt(request.getParameter("c_group")));
				comment.setC_order(Integer.parseInt(request.getParameter("c_order")));
				comment.setC_depth(Integer.parseInt(request.getParameter("c_depth")));
				
				commentService = CommentService.getInstance();
				commentService.commentInComments(comment,session);
				
				commentList = commentService.getComments(comment.getB_idx());		//받아왔던 목록을 다시 넘겨줌.
				request.setAttribute("commentList", commentList);					//jsp에게로 commenList를 넘겨줌
				view = "board/c_list";
				break;
			
			case "/comment-updateComment.do":		// b_detail에 있는 댓글 수정
				comment = new Comment();
				comment.setC_content(request.getParameter("c_content"));
				comment.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				comment.setC_group(Integer.parseInt(request.getParameter("c_group")));
				comment.setC_order(Integer.parseInt(request.getParameter("c_order")));
				comment.setC_depth(Integer.parseInt(request.getParameter("c_depth")));
				
				commentService = CommentService.getInstance();
				commentService.updateComment(comment);
				
				commentList = commentService.getComments(comment.getB_idx());
				request.setAttribute("commentList", commentList);
				view = "board/c_list";
				break;
				
			case "/comment-deleteComment.do":		// b_detail에 있는 댓글 삭제
				comment = new Comment();
				comment.setB_idx(Integer.parseInt(request.getParameter("b_idx")));
				comment.setC_group(Integer.parseInt(request.getParameter("c_group")));
				comment.setC_order(Integer.parseInt(request.getParameter("c_order")));
				comment.setC_depth(Integer.parseInt(request.getParameter("c_depth")));
								
				commentService = CommentService.getInstance();
				commentService.deleteComment(comment);
				
				commentList = commentService.getComments(comment.getB_idx());
				request.setAttribute("commentList", commentList);
				view = "board/c_list";
				break;
				
		}
		
		RequestDispatcher rd = request.getRequestDispatcher(view + ".jsp");
		rd.forward(request, response);
	}

	String checkSession(HttpServletRequest request, HttpServletResponse response, String command) {		//권한
		HttpSession session = request.getSession();
		
		String[] authList = {
				"/user-list.do"
				,"/user-insert.do"
				, "/user-insert-process.do"
				//, "/user-detail.do"
				//, "/user-edit.do"
				//, "/user-edit-process.do"
				,"/logout.do"
		};
		
		for (String item : authList) {
			if (item.equals(command)) {
				if (session.getAttribute("user") == null) {
					command = "/access-denied.do";
				}
			}
		}
		return command;
	}
	

}