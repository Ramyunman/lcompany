package com.lcomputerstudy.testmvc.vo;

public class Board {
	private int b_idx;
	private String b_title;
	private String b_content;
	private int b_views;
	private String b_writer;
	private String b_date;
	private String[] b_dateArr;
	private int rownum;
	private int u_idx;		// u_idx 추가
	private int b_group;	// 답글
	private int b_order;	// 답글
	private int b_depth;	// 답글
	private User user;
	private String b_fileName;	// 파일 추가
	private String b_filePath;	// 파일 경로 추가
	
	public int getB_idx() {
		return b_idx;
	}
	public void setB_idx(int b_idx) {
		this.b_idx = b_idx;
	}
	public String getB_title() {
		return b_title;
	}
	public void setB_title(String b_title) {
		this.b_title = b_title;
	}
	public String getB_content() {
		return b_content;
	}
	public void setB_content(String b_content) {
		this.b_content = b_content;
	}
	public int getB_views() {
		return b_views;
	}
	public void setB_views(int b_views) {
		this.b_views = b_views;
	}
	public String getB_writer() {
		return b_writer;
	}
	public void setB_writer(String b_writer) {
		this.b_writer = b_writer;
	}
	public String getB_date() {
		return b_date;
	}
	public void setB_date(String b_date) {
		this.b_date = b_date;
	}
	public String[] getB_dateArr() {
		return b_dateArr;
	}
	public void setB_dateArr(String[] b_dateArr) {
		this.b_dateArr = b_dateArr;
	}
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public int getB_group() {
		return b_group;
	}
	public void setB_group(int b_group) {
		this.b_group = b_group;
	}
	public int getB_order() {
		return b_order;
	}
	public void setB_order(int b_order) {
		this.b_order = b_order;
	}
	public int getB_depth() {
		return b_depth;
	}
	public void setB_depth(int b_depth) {
		this.b_depth = b_depth;
	}
	public int getU_idx() {
		return u_idx;
	}
	public void setU_idx(int u_idx) {
		this.u_idx = u_idx;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getB_fileName() {
		return b_fileName;
	}
	public void setB_fileName(String b_fileName) {
		this.b_fileName = b_fileName;
	}
	public String getB_filePath() {
		return b_filePath;
	}
	public void setB_filePath(String b_filePath) {
		this.b_filePath = b_filePath;
	}

		
}