<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type = "text/javascript" src = "/elderlycare/resources/jquery-3.5.1.js"></script>
<script type = "text/javascript">
$(function(){
	$('#modify-form').submit(function(event){
		//event.preventDefault();
		
		//var data = {"uid": "staff101058", "upwd": "staff101058"};
		var data = {uname: $('#uname').val(),
				utel: $('#utel').val(),
				uemail: $('#uemail').val()};

		$.ajax({
				type : 'put',                            
				url : 'join',                         
				contentType : 'application/json',            
				data : JSON.stringify(data),            
				success : function(response){
					alert(response);
				},                      
				error   : function(response){
					alert(response);
				}
		});
	});
});

</script>
</head>
<body>
	<h1>JOIN</h1>
	<form id = "modify-form" action ="${uid}" method = "POST">
	<div>
	id: ${uid }
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
	<input type = "hidden" name = "_method" value = "put"/>
	<button type = "submit">정보 수정</button>
	</form>
</body>
</html>