<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>update</title>
</head>
<body>

<h2>회원 정보 수정</h2>
<form action="board-update-process.do" name="board" method="post">
	<input type = "hidden" name = "b_idx" value = "${board.b_idx }">
	<p> 제목 : <input type="text" name="title" value = ${board.b_title }></p>
	<p> 내용 : <input type="text" name="content" value = ${board.b_content }></p>
	<p> 조회수 : <input type="text" name="views" value = ${board.b_views }></p>
	<p> 작성자 : <input type="text" name="writer" value = ${board.b_writer }></p>
	<p> 작성일자 : <input type="text" name="date" value = ${board.b_date }></p>
	
	<p> <input type="submit" value="수정하기"></p>
	<a href="/lcomp1/board-list.do"> 목록으로 </a>
</form>

</body>
</html>