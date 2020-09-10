<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<body>
<%@ include file="../header.jsp" %>
	<h1>JOIN</h1>
	<form id = "join-form" action ="join" method = "POST">
	<div>
	<label for = "uid">id: </label>
	<input type = "text" name = "uid" id = "uid">
	</div>
	<div>
	<label for = "upwd">password: </label>
	<input type = "password" name = "upwd" id = "upwd">
	</div>
	<div>
	<label for = "uname">name: </label>
	<input type = "text" name = "uname" id = "uname">
	</div>
	<div>
	<label for = "utel">tel: </label>
	<input type = "text" name = "utel" id = "utel">
	</div>
	<div>
	<label for = "uemail">email: </label>
	<input type = "text" name = "uemail" id = "uemail">
	</div>
	<br>
	<div>
	<label for = "ename">elderly name: </label>
	<input type = "text" name = "ename" id = "ename">
	</div>
	<div>
	<label for = "ebirth">elderly birth: </label>
	<input type = "date" name = "ebirth" id = "ebirth">
	</div>
	<div>
	<button type = "submit" class = "btn btn-default">가입 신청</button>
	</div>
	</form>
</body>

<script type = "text/javascript" src = "<c:url value = '/resources/js/jquery-3.5.1.js'/>"></script>
<script type = "text/javascript">
$(function(){
	$('#join-form').submit(function(event){
		event.preventDefault();
		
		//var data = {"uid": "staff101058", "upwd": "staff101058"};
		var data = {uid: $("#uid").val(),
				upwd: $('#upwd').val(),
				uname: $('#uname').val(),
				utel: $('#utel').val(),
				uemail: $('#uemail').val(),
				ename : $('#ename').val(),
				ebirth : $('#ebirth').val()
		};

		$.ajax({
				type : 'POST',                            
				url : 'join',                         
				contentType : 'application/json',            
				data : JSON.stringify(data),            
				success : function(response){
					$(location).attr("href", "${contextPath}/");
				},                      
				error   : function(){
					alert("error");
				}
		});
		
	});
	
	
});

</script>
</html>