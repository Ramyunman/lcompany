package com.lcomputerstudy.testmvc.vo;

import com.lcomputerstudy.testmvc.service.BoardService;

public class Pagination {
	private int amount;				// 데이터 총 개수
	private int page;				// 현재 페이지 번호
	private int pageNum;			// amount / page 현재 페이지에 보여줄 데이터의 시작 인덱스
	private int startPage;			// pagination의 시작페이지 번호(ex1,6,11)
	private int endPage;			// pagination의 끝페이지 번호(ex5,10,15)
	private int lastPage;			// (amount/화면에 표시할 갯수), 마지막 페이지 번호
	private int prevPage;			// pagination의 이전 목록(이전 페이지 번호)
	private int nextPage;			// pagination의 다음 목록(다음 페이지 번호)
	public static final int pageUnit=5;		// 한번에 볼러 올 pagination 수
	public static final int perPage=10;		// 한번에 불러 올 amount 수
	private Search search;			//추가
	
	public Pagination() {
		this.page = 1;
	}
	public void init() {	// Pagination 객체를 초기화한다.
		pageNum = (page-1)*perPage;		//amount - pageNum이 나오는 숫자에서부터 rownum이 시작돼 user 인덱스를 불러온다.
		startPage = ((page-1) / pageUnit) * pageUnit + 1;  // ex) 1,6,11...
		lastPage = (int)Math.ceil(amount / (float)perPage);	//제일 마지막페이지
		endPage = startPage + pageUnit - 1;		// ex) 5,10,15...
		endPage = endPage < lastPage ? endPage : lastPage;		//정보가 얼마 없을때를 대비
		prevPage = (startPage - pageUnit);		//5개 나오는 페이지에서 제일 첫번째꺼 ex)1,6,11...에서 6이면 prevPage는 1
		nextPage = (startPage + pageUnit);		//5개 나오는 페이지에서 제일 첫번째꺼 ex)1,6,11...에서 6이면 nextPage는 11
	}
		
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getLastPage() {
		return lastPage;
	}
	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}
	public int getPrevPage() {
		return prevPage;
	}
	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public int getPageunit() {
		return pageUnit;
	}
	public int getPerpage() {
		return perPage;
	}
	public Search getSearch() {
		return search;
	}
	public void setSearch(Search search) {
		this.search = search;
	}
	
/*	public void update() {	// 추가
		BoardService boardService = BoardService.getInstance();
		amount = boardService.getBoardsamount(search);	// 검색 조건에 맞는 데이터 개수를 계산
		init();	// 필드 값들을 업데이트
	}
*/	
}