<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 추가</title>
</head>
<body>

<h2>게시글 등록</h2>

<form action="board-insert-process.do" name="board" method="post" enctype="multipart/form-data">
	
	<p> 제목 : <input type="text" name="title"></p>
	<p> 내용 : </p> 
	<textarea name="content" rows="10" cols="50"></textarea> 
	<p> 파일 : <input type="file" name="b_fileName"> </p>
		
	<p> <input type="submit" value="등록하기"></p>
	
</form>



</body>
</html>