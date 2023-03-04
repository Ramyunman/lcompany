<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="userList">
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
</div>	
	