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
	<h1>기기 등록</h1>
	<form id = "registration-form" action ="form" method = "POST">
	<h3>노인정보</h3>
	<div>
	<label for = "ename">name: </label>
	<input type = "text" name = "ename" id = "ename">
	</div>
	<div>
	<label for = "ebirth">birth: </label>
	<input type = "date" name = "ebirth" id = "ebirth">
	</div>
	<div>
	<label for = "etel">tel: </label>
	<input type = "text" name = "etel" id = "etel">
	</div>
	<div>
	<label for = "eaddr">address: </label>
	<input type = "text" name = "eaddr" id = "eaddr">
	</div>
	<h3>기기정보</h3>
	<div>
	<label for = "homeiot">homeiot: </label>
	<input type = "text" name = "homeiot" id = "homeiot">
	</div>
	
	<div>
	<button type = "submit" class = "btn btn-default">기기등록</button>
	</div>
	</form>
</body>

<script type = "text/javascript" src = "/elderlycare/resources/jquery-3.5.1.js"></script>
<script type = "text/javascript">
$(function(){
	$('#registration-form').submit(function(event){
		event.preventDefault();
		var data = {ename:$('#ename').val(),
					ebirth:$('#ebirth').val(),
					etel:$('#etel').val(),
					eaddr:$('#eaddr').val(),
					homeIoT:$('#homeiot').val()
					};
	$.ajax({
			type: 'POST',
			url: 'form',
			dataType: 'json',
			contentType: 'application/json',
			data:JSON.stringify(data),
			success : function(){
				alert("등록 성공");
			}
		});
	});
});
</script>
</html>