<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
  
<head>
    <meta charset="utf-8">
    <title>Bootstrap Admin Template</title>

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
			
			<a class="brand" href="${contextPath }/">
				노인 건강 모니터링 시스템				
			</a>		
			
			<div class="nav-collapse">
				<ul class="nav pull-right">
					
					<li class="">						
						<a href="${contextPath }/" class="">
							<i class="icon-chevron-left"></i>
							Back to Homepage
						</a>
						
					</li>
				</ul>
				
			</div><!--/.nav-collapse -->	
	
		</div> <!-- /container -->
		
	</div> <!-- /navbar-inner -->
	
</div> <!-- /navbar -->



<div class="account-container register">
	
	<div class="content clearfix">
		
		<form id = "#registration-form" action="form" method="post">
		
			<h1>Device User Registration</h1>			
			
			<div class="login-fields">
				
				<p>Device User:</p>
				
				<div class="field">
					<label for="ename">Elderly Name:</label>
					<input type="text" id="ename" name="ename" value="" placeholder="Elderly Name" class="login" />
				</div> <!-- /field -->
				
				<div class="field">
					<label for="ebirth">Elderly Birthday:</label>	
					<input type="date" id="ebirth" name="ebirth" value="" placeholder="Elderly Birthday" class="login" />
				</div> <!-- /field -->
				
				
				<div class="field">
					<label for="etel">Elderly Telephone:</label>
					<input type="text" id="etel" name="etel" value="" placeholder="Elderly Telephone" class="login"/>
				</div> <!-- /field -->
				
				<div class="field">
					<label for="eaddr">Elderly Address:</label>
					<input type="text" id="eaddr" name="eaddr" value="" placeholder="Elderly Address" class="login"/>
				</div> <!-- /field -->
				<br>
				<div class="field">
					<label for="homeIoT">Device IP:</label>
					<input type="text" id="homeIoT" name="homeIoT" value="" placeholder="Device IP" class="login"/>
				</div> <!-- /field -->
				
			</div> <!-- /login-fields -->
			
			<div class="login-actions">
				
				
									
				<button class="button btn btn-primary btn-large">Register</button>
				
			</div> <!-- .actions -->
			
		</form>
		
	</div> <!-- /content -->
	
</div> <!-- /account-container -->




<script src="<c:url value='/resources/js/jquery-1.7.2.min.js'/>"></script>
<script src="<c:url value='/resources/js/bootstrap.js'/>"></script>


</body>
<script>
/*
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
				window.location.replace('/elderlycare/');
			}
		});
	});
});
*/
</script>
 </html>
