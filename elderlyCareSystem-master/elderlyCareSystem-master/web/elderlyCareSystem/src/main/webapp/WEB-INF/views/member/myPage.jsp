<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ include file="../header.jsp" %>
	<h1>my page</h1>
	<table>
	<tr>
	<td>아이디</td><td>${mdto.id }</td>
	</tr>
	<tr>
	<td>이름</td><td>${mdto.name }</td>
	</tr>
	<tr>
	<td>연락처</td><td>${mdto.tel }</td>
	</tr>
	<tr>
	<td>이메일</td><td>${mdto.email }</td>
	</tr>
	</table>
	<a href = "">정보 수정</a>
</body>
</html>