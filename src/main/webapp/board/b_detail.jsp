<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세정보</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>
<style>
	table {
		border-collapse:collapse;
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
		border:none;
		cursor:pointer;
		padding:10px;
		display:inline-block;
	}
</style>
<body>
	<h1>상세페이지</h1>

	<table>
	<tr>
		<td>번호</td>
		<td>${board.b_idx }</td>
	</tr>
	<tr>
		<td>내용</td>
		<td>${board.b_content }</td>
	</tr>
	<tr>
		<td>조회수</td>
		<td>${board.b_views }</td>
	</tr>
	<tr>
		<td>작성자</td>
		<td>${board.b_writer }</td>
	</tr>
	<tr>
		<td>작성일자</td>
		<td>${board.b_date }</td>
	</tr>
	
	<tr style="height:50px;">
			<td style="border:none;">
				<a href="/lcomp1/board-update.do?b_idx=${board.b_idx}" style="width:70%;font-weight:700;background-color:#818181;color:#fff;">수정</a>
			</td>
			<td style="border:none;">
				<a href="/lcomp1/board-delete.do?b_idx=${board.b_idx}" style="width:70%;font-weight:700;background-color:red;color:#fff;">삭제</a>
			</td>
		</tr>
	</table>
	<a href="/lcomp1/board-reply-insert.do?b_group=${board.b_group}&b_order=${board.b_order}&b_depth=${board.b_depth}">답글 등록</a>
	
<%--		
	<h4> >> 댓글 등록 </h4>
	<form action="comment-original-insert-process.do" name="comment" method="post">
		<input type = "hidden" name="b_idx" value="${board.b_idx}">
 		<input type = "hidden" name="c_group" value="${comment.c_group}">
		<input type = "hidden" name="c_order" value="${comment.c_order}">
		<input type = "hidden" name="c_depth" value="${comment.c_depth}">	
		내용 : <input type="text" name="c_content">
		
		<input type="submit" value="등록하기">	
	</form>
--%>	
	<button type="button" class="o_btnComment"> 댓글 등록 </button>
		
		<div style="display: none;">
			<textarea rows="3" cols="50" ></textarea>	
			<button type="button" class="o_btnComment-register">등록</button>
			<button type="button" class="o_btnComment-cancel">취소</button>
		</div>
	
	<h3>댓글 목록</h3>
		<table id="commentList">
			<tr>
				<th>No</th>
				<th>내용</th>
				<th>작성일자</th>		
			</tr>
			
			<c:forEach items="${commentList}" var="comment" varStatus="status">
				
				<tr>
					<td>${comment.c_idx}</td>
					<c:choose>
						<c:when test="${comment.c_depth > 0}">
							<td style="text-align: left;">
							
							<c:forEach var="i" begin="1" end="${comment.c_depth}" step="1">
									&nbsp;&nbsp;
							</c:forEach>
							ㄴ${comment.c_content}
							</td>
						</c:when>
						<c:when test = "${comment.c_depth == 0 }">
							<td style="text-align: left;">${comment.c_content}</td>
						</c:when>
					</c:choose>
				
					<td>${comment.c_date}</td>
				
					
					<td>
						<button type="button" class="btnComment">댓글</button>
						<button type="button" class="btnComment-Update">수정</button>
						<button type="button" class="btnComment-Delete" c_group="${comment.c_group}" c_order="${comment.c_order}" c_depth="${comment.c_depth}">삭제</button>						
					</td>			
				</tr>
						
				<tr style="display: none;">		<%-- 대댓글 등록창 --%>
					<td>
						<div>
							<textarea rows="3" cols="50" ></textarea>	
							<button type="button" class="btnComment-register" c_group="${comment.c_group}" c_order="${comment.c_order}" c_depth="${comment.c_depth}">등록</button>
							<button type="button" class="btnComment-cancel">취소</button>
						</div>	
					</td>	
				</tr>
				
				<tr style="display: none;">		<%-- 대댓글 수정창 --%>
					<td>
						<div>
							<textarea rows="3" cols="50"> ${comment.c_content } </textarea>
							<button type="button" class="btnComment-Update-register" c_group="${comment.c_group}" c_order="${comment.c_order}" c_depth="${comment.c_depth}">등록</button>
							<button type="button" class="btnComment-Update-cancel">취소</button>
						</div>	
					</td>
				</tr>
				
			</c:forEach>
		</table>

<script>

$(document).on('click', '.o_btnComment', function () {				//원댓글 달기 버튼		
	console.log('원댓글 달기 버튼')
	$(this).next().css('display','');
});	

$(document).on('click', '.o_btnComment-register', function () {		//원댓글 등록 버튼		
	let bIdx = '${board.b_idx}';
	let cContent = $(this).prev('textarea').val();
	
	$.ajax({
		method : 'POST',
		url : "/lcomp1/comment-original-insert-process.do",
		data : { b_idx:bIdx, c_content:cContent }
	})
	.done(function( msg ) {
		console.log(msg);
	   	$('#commentList').html(msg);
	});
	console.log('원댓글 등록 버튼')
	$(this).parent().css('display','none');
});	

$(document).on('click', '.o_btnComment-cancel', function () {		//원댓글 취소 버튼		
	console.log('원댓글 취소 버튼')
	$(this).parent().css('display','none');
});	

$(document).on('click', '.btnComment', function () {				//대댓글 달기 버튼
	console.log('대댓글 달기 버튼');
	$(this).parent().parent().next().css('display', '');	
});

$(document).on('click', '.btnComment-register', function (){		//대댓글 등록 버튼
	let bIdx = '${board.b_idx}';
	let cContent = $(this).prev('textarea').val();
	let cGroup = $(this).attr('c_group');
	let cOrder = $(this).attr('c_order');
	let cDepth = $(this).attr('c_depth');
	
	/*
	console.log('cGroup: '+cGroup);
	console.log('cOrder: '+cOrder);
	console.log('cDepth: '+cDepth);
	return false;
	*/
	
	$.ajax({
		  method: "POST",
		  url: "/lcomp1/comment-commentInComments.do",
		  data: { b_idx:bIdx, c_content:cContent, c_group:cGroup, c_order:cOrder, c_depth:cDepth }
	})
	.done(function( msg ) {
		console.log(msg);
	   	$('#commentList').html(msg);
	});
	console.log('대댓글 등록 버튼');
});

$(document).on('click', '.btnComment-cancel', function () {				//대댓글 등록 취소 버튼
	console.log('대댓글 등록 취소 버튼');	
	$(this).parent().parent().parent().css('display', 'none');
});
	
$(document).on('click', '.btnComment-Update', function () {				//대댓글 수정 버튼
	console.log('대댓글 수정 버튼 ');
	$(this).parent().parent().next().next().css('display', '');	
	$(this).parent().parent().css('display', 'none');		
});	

$(document).on('click', '.btnComment-Update-register', function () {	//대댓글 수정 등록 버튼
	let bIdx = '${board.b_idx}';
	let cContent = $(this).prev('textarea').val();
	let cGroup = $(this).attr('c_group');
	let cOrder = $(this).attr('c_order');
	let cDepth = $(this).attr('c_depth');
	
	$.ajax({
		  method: "POST",
		  url: "/lcomp1/comment-updateComment.do",
		  data: { b_idx:bIdx, c_content:cContent, c_group:cGroup, c_order:cOrder, c_depth:cDepth }
	})
	.done(function( msg ) {
		console.log(msg);
	   	$('#commentList').html(msg);
	});
	console.log('대댓글 수정 등록 버튼');	
});	

$(document).on('click', '.btnComment-Update-cancel', function () {		//대댓글 수정 취소 버튼
	console.log('대댓글 수정 취소 버튼');	
	$(this).parent().parent().parent().prev().prev().css('display','');
	$(this).parent().parent().parent().css('display', 'none');
});

$(document).on('click', '.btnComment-Delete', function () {				//대댓글 삭제 버튼
	let bIdx = '${board.b_idx}';
	let cGroup = $(this).attr('c_group');
	let cOrder = $(this).attr('c_order');
	let cDepth = $(this).attr('c_depth');
	
	$.ajax({
		method: "POST",
		url: "/lcomp1/comment-deleteComment.do",
		data: { b_idx:bIdx, c_group:cGroup, c_order:cOrder, c_depth:cDepth }
	})
	.done(function( msg ) {
		console.log(msg);
		$('#commentList').html(msg);
	});
	console.log('대댓글 삭제 버튼');
});
	


/*	let cgroup = $(this).attr('cgroup');
	
	$.ajax({
		  method: "POST",
		  url: "commentReply.do",
		  data: { c_group:cgorup , c_order: , c_depth: }
	})
	.done(function( msg ) {
	   	alert( "Data Saved: " + msg );
	});
*/


</script>
</body>
</html>