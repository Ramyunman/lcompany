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
	<p> 내용 : <textarea name="content" rows="10" cols="50">${board.b_content}</textarea></p>

	<p> <input type="submit" value="수정하기"></p>
	<a href="/lcompany/board-list.do"> 목록으로 </a>
</form>

</body>
</html>