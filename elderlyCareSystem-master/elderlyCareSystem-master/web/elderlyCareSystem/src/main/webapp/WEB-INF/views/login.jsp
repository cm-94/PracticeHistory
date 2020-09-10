<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
  <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <meta charset="utf-8">
    <title>Login</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"> 
    
<link href="<c:url value='/resources/css/bootstrap.min.css' />" rel="stylesheet" type="text/css" />
<link href="<c:url value='/resources/css/bootstrap-responsive.min.css' />" rel="stylesheet" type="text/css" />

<link href="<c:url value='/resources/css/font-awesome.css' />" rel="stylesheet">
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600" rel="stylesheet">
    
<link href="<c:url value='/resources/css/style.css' />" rel="stylesheet" type="text/css">
<link href="<c:url value='/resources/css/pages/signin.css' />" rel="stylesheet" type="text/css">

</head>

<body>
	<c:set var = "contextPath" value = "<%=request.getContextPath() %>"></c:set>
	
	
	<div class="navbar navbar-fixed-top">
	
	<div class="navbar-inner">
		
		<div class="container">
			
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</a>
			
			<a class="brand" href="">
				노인 건강 모니터링 시스템				
			</a>		
			
			<div class="nav-collapse">
				<ul class="nav pull-right">
					
					<li class="">						
						<a href="${contextPath }/users/join" class="">
							Don't have an account?
						</a>
						
					</li>
					
					
				</ul>
				
			</div><!--/.nav-collapse -->	
	
		</div> <!-- /container -->
		
	</div> <!-- /navbar-inner -->
	
</div> <!-- /navbar -->



<div class="account-container">
	
	<div class="content clearfix">
		
		<form action="login" id = "login-form" method="post">
		
			<h1>Member Login</h1>		
			
			<div class="login-fields">
				
				<p>Please provide your details</p>
				
				<div class="field">
					<label for="uid">Username</label>
					<input type="text" id="uid" name="uid" value="" placeholder="ID" class="login username-field" />
				</div> <!-- /field -->
				
				<div class="field">
					<label for="upwd">Password:</label>
					<input type="password" id="upwd" name="upwd" value="" placeholder="Password" class="login password-field"/>
				</div> <!-- /password -->
				
			</div> <!-- /login-fields -->
			
			<div class="login-actions">
				
				<span class="login-checkbox">
					<input id="Field" name="Field" type="checkbox" class="field login-checkbox" value="First Choice" tabindex="4" />
					<label class="choice" for="Field">Keep me signed in</label>
				</span>
									
				<button class="button btn btn-success btn-large">Sign In</button>
				
			</div> <!-- .actions -->
			
			
			
		</form>
		
	</div> <!-- /content -->
	
</div> <!-- /account-container -->



<div class="login-extra">
	<a href="#">Reset Password</a>
</div> <!-- /login-extra -->


<script src="<c:url value='/resources/js/jquery-1.7.2.min.js' />"></script>
<script src="<c:url value='/resources/js/bootstrap.js' />"></script>
<script type = "text/javascript">
$(function(){
	$('#login-form').submit(function(event){
		event.preventDefault();
		
		//var data = {"uid": "staff101058", "upwd": "staff101058"};
		var data = {uid: $("#uid").val(), upwd: $('#upwd').val()};

		$.ajax({
				type : 'POST',                            
				url : 'login',                        
				dataType : 'json',                          
				contentType : 'application/json',            
				data : JSON.stringify(data),            
				success : function(response){
					if(response.result){
						alert(response.uid+'님 환영합니다~');
						window.location.replace('/elderlycare/');
					}else
						alert("아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.");
				},                      
				error   : function(response){
					alert(response.uid);
				}
		});
	});
});
	


</script>
<!--  <script src="<c:url value='/resources/js/signin.js' />"></script>-->

</body>

</html>
