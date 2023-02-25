<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		
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
						<textarea rows="2" cols="80" ></textarea>	
						<button type="button" class="btnComment-register" c_group="${comment.c_group}" c_order="${comment.c_order}" c_depth="${comment.c_depth}">등록</button>
						<button type="button" class="btnComment-cancel">취소</button>
					</div>	
				</td>	
			</tr>
				
			<tr style="display: none;">		<%-- 대댓글 수정창 --%>
				<td>
					<div>
						<textarea rows="2" cols="80"> ${comment.c_content } </textarea>
						<button type="button" class="btnComment-Update-register" c_group="${comment.c_group}" c_order="${comment.c_order}" c_depth="${comment.c_depth}">등록</button>
						<button type="button" class="btnComment-Update-cancel">취소</button>
					</div>	
				</td>
			</tr>
				
		</c:forEach>