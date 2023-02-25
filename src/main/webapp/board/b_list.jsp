<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
</head>
<style>
	h1 {
		text-align:center;
	}
	table {
		border-collapse:collapse;
		margin:40px auto;
	}
	table tr th {
		font-weight:700;
	}
	table tr td, table tr th {
		border:1px solid #818181;
		width:200px;
		text-align:center;
	}
	a {
		text-decoration:none;
		color:#000;
		font-weight:700;
	}
	ul {
		width:600px;
		height:50px;
		margin:10px auto;	
	}
	li {
		list-style:none;
		width:50px;
		line-height:50px;
		border:1px solid #ededed;
		float:left;
		text-align:center;
		margin:0 5px;
		border-radius:5px;
	}
	form {
		text-align:center;
		margin:50px
	}
</style>
<body>
<h1>게시글 목록</h1>
	<table>
		<tr>
			<td colspan="3">전체 게시글 수: ${pagination.userCount }</td>
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>내용</th>
		</tr>
		<c:forEach items="${list}" var="board" varStatus="status">
			<tr>
				<td><a href="/lcomp1/board-detail.do?b_idx=${board.b_idx}">${board.rownum}</a></td>
				<c:choose>
					<c:when test="${board.b_depth > 0  }">
						<td style="text-align: left;"> 
						
						<c:forEach var="i" begin="1" end="${board.b_depth}" step="1">
													&nbsp;&nbsp;
						</c:forEach>
						ㄴ${board.b_title}
						</td>
					</c:when>
					<c:when test = "${board.b_depth == 0 }">
						<td style="text-align: left;">${board.b_title}</td>
					</c:when>
				</c:choose>
					
				<td>${board.b_content}</td>
			
				
			</tr>
		</c:forEach>	
	</table>
	
<!-- 아래부터 pagination -->
	<div>
		<ul>
			<c:choose>
				<c:when test="${ pagination.prevPage < 0 }">
					<li style="display:none;">					
						<span>◀</span>	
					</li>
				</c:when>
				<c:when test="${ pagination.prevPage <= pagination.endPage }">
					<li>					
						<a href="board-list.do?page=${pagination.prevPage}">
							◀
						</a>
					</li>
				</c:when>
				
			</c:choose>
			<c:forEach var ="i" begin="${pagination.startPage}" end="${pagination.endPage}" step="1">
			
				<c:choose>
					<c:when test="${ pagination.page == i }">		<%-- 현재페이지가 i와 같다면 회색으로 나오게 한다. --%>
					
						<li style="background-color:#ededed;">
							<span>${i}</span>
						</li>
					</c:when>
					<c:when test="${ pagination.page != i }">		<%-- 현재페이지가 i와 다르다면 링크를 걸게 한다. --%>
						<li>
							<a href="board-list.do?page=${i}">${i}</a>
						</li>
					</c:when>
				</c:choose>
			</c:forEach>
			<c:choose>
				<c:when test="${ pagination.nextPage <= pagination.lastPage }">
					<li style="">	
						<a href="board-list.do?page=${pagination.nextPage}">▶</a>
					</li>
				</c:when>
			</c:choose>
		</ul>
	</div>
	
	<form action="board-list.do" name="search" method="get">
		<select name="tcw">
			<option value="none"> 선택 </option>
			<option value="title"> 제목 </option>
			<option value="content"> 내용 </option>
			<option value="writer"> 작성자 </option>
		</select>
		<input type="text" name="searchbox" placeholder="입력하세요!">
		<input type="submit" value="검색하기">
	</form>
	
</body>

</html>