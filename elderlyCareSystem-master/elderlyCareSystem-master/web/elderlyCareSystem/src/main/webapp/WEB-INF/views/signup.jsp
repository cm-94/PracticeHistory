<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<html lang="en">
  
 <head>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
    <meta charset="utf-8">
    <title>Signup - Elderlycare</title>

	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"> 
    
<link href="<c:url value='/resources/css/bootstrap.min.css'/>" rel="stylesheet" type="text/css" />
<link href="<c:url value='/resources/css/bootstrap-responsive.min.css'/>" rel="stylesheet" type="text/css" />

<link href="<c:url value='/resources/css/font-awesome.css'/>" rel="stylesheet">
    <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600" rel="stylesheet">
    
<link href="<c:url value='/resources/css/style.css'/>" rel="stylesheet" type="text/css">
<link href="<c:url value='/resources/css/pages/signin.css'/>" rel="stylesheet" type="text/css">

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
						<a href="${contextPath}/users/login" class="">
							Already have an account? Login now
						</a>
						
					</li>
					
				</ul>
				
			</div><!--/.nav-collapse -->	
	
		</div> <!-- /container -->
		
	</div> <!-- /navbar-inner -->
	
</div> <!-- /navbar -->



<div class="account-container register">
	
	<div class="content clearfix">
		
		<form id = "join-form" action="join" method="post">
		
			<h1>Signup for an Account</h1>			
			
			<div class="login-fields">
				
				<p>Create your free account:</p>
				
				<div class="field">
					<label for="uid">ID :</label>
					<input type="text" id="uid" name="uid" value="" placeholder="ID" class="login" />
				</div> <!-- /field -->
				
				<div class="field">
					<label for="upwd">Password :</label>	
					<input type="password" id="upwd" name="upwd" value="" placeholder="Password" class="login" />
				</div> <!-- /field -->
				<!-- 
				<div class="field">
					<label for="confirm_password">Confirm Password:</label>
					<input type="password" id="confirm_password" name="confirm_password" value="" placeholder="Confirm Password" class="login"/>
				</div>
				--> 
				<!-- /field -->
				
				<div class="field">
					<label for="uname">Name:</label>
					<input type="text" id="uname" name="uname" value="" placeholder="Name" class="login"/>
				</div> <!-- /field -->
				
				<div class="field">
					<label for="utel">Telephone:</label>
					<input type="text" id="utel" name="utel" value="" placeholder="Telephone" class="login"/>
				</div> <!-- /field -->
				
				<div class="field">
					<label for="uemail">Email Address:</label>
					<input type="text" id="uemail" name="uemail" value="" placeholder="Email" class="login"/>
				</div> <!-- /field -->
				
				<br><br>
				<div class = "field">
					<label for="ename">Elderly Name:</label>
					<input type="text" id="ename" name="ename" value="" placeholder="Elderly Name" class="login"/>
				</div>
				<div class = "field">
					<label for="ebirth">Eldelry Birthday:</label>
					<input type="text" id="ebirth" name="ebirth" value="" placeholder="Elderly Birthday" class="login"/>
				</div>
				
				
			</div> <!-- /login-fields -->
			
			<div class="login-actions">
				
				<span class="login-checkbox">
					<input id="Field" name="Field" type="checkbox" class="field login-checkbox" value="First Choice" tabindex="4" />
					<label class="choice" for="Field">Agree with the Terms & Conditions.</label>
				</span>
									
				<button class="button btn btn-primary btn-large">Register</button>
				
			</div> <!-- .actions -->
			
		</form>
		
	</div> <!-- /content -->
	
</div> <!-- /account-container -->


<!-- Text Under Box -->
<div class="login-extra">
	Already have an account? <a href="/elderlycare/users/login">Login to your account</a>
</div> <!-- /login-extra -->


<script src="<c:url value='/resources/js/jquery-1.7.2.min.js'/>"></script>
<script src="<c:url value='/resources/js/bootstrap.js'/>"></script>

<script src="<c:url value='/resources/js/signin.js'/>"></script>

</body>
<script>
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
