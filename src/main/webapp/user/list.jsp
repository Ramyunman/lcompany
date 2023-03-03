<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원목록2</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
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
</style>
<body>
<h1>회원 목록</h1>
	<table>
		<tr>
			<td colspan="3">전체 회원 수: ${pagination.amount}</td>
		<tr>
			<th>No</th>
			<th>ID</th>
			<th>이름</th>
		</tr>
		<c:forEach items="${list}" var="user" varStatus="status">
			<tr>
				<td><a href="/lcompany/user-detail.do?u_idx=${user.u_idx}">${user.rownum}</a></td>
				<td>${user.u_id}</td>
				<td>${user.u_name}</td>				
				<td>
					<div>
						<button type="button" class="adminOn">관리자On</button>
					</div>
					<div style="display:none;">
						<button type="button" class="adminOff">관리자Off</button>	
					</div>			
				</td>
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
						<a href="user-list.do?page=${pagination.prevPage}">
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
							<a href="user-list.do?page=${i}">${i}</a>
						</li>
					</c:when>
				</c:choose>
			</c:forEach>
			<c:choose>
				<c:when test="${ pagination.nextPage <= pagination.lastPage }">
					<li style="">	
						<a href="user-list.do?page=${pagination.nextPage}">▶</a>
					</li>
				</c:when>
			</c:choose>
		</ul>
	</div>

<script>
$(document).on('click','adminOn',function () {		//권한 On 버튼 click : u_level이 1 -> 9
	let uIdx = $(this).closest('tr').find('a').attr('href').split('=')[1];	// 클릭한 버튼이 속한 회원의 u_idx 값을 가져옵니다.
	let uLevel = 9;		// 업데이트할 u_level 값을 지정합니다.
	
	$.ajax({
		method : 'POST',
		url : "/lcompany/user-adminOn.do",
		data : { 
			u_idx: uIdx,
			u_level:uLevel
		},
		success: function (data) {
			$('.adminOn').hide();		
			$('.adminOff').show();	
			console.log('관리자가 되었습니다.');
		},
		error: function (xhr, status, error) {
			console.log('관리자On 버튼에 error 발생!');	
		}
	});
});

$(document).on('click','adminOn',function () {		//권한 Off 버튼 click : u_level이 9 -> 1
	let uIdx = $(this).closest('tr').find('a').attr('href').split('=')[1];	// 클릭한 버튼이 속한 회원의 u_idx 값을 가져옵니다.
	let uLevel = 1;		// 업데이트할 u_level 값을 지정합니다.
	
	$.ajax({
		method : 'POST',
		url : "/lcompany/user-adminOff.do",
		data : { 
			u_idx: uIdx,
			u_level:uLevel
		},
		success: function (data) {
			$('.adminOff').hide();		
			$('.adminOn').show();
			console.log('일반회원이 되었습니다.');
		},
		error: function (xhr, status, error) {
			console.log('관리자Off 버튼 error 발생!');	
		}
	});
});
</script>
	
</body>

</html>