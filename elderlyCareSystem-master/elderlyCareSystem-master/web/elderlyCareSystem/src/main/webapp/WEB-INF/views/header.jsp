<!DOCTYPE html5>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page session="true" %>
<html>
<head>
	<title></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<link href="<c:url value='/resources/css/bootstrap.min.css'/>" rel="stylesheet">
<link href="<c:url value='/resources/css/bootstrap-responsive.min.css'/>" rel="stylesheet">
<link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600"
        rel="stylesheet">
<link href="<c:url value='/resources/css/font-awesome.css'/>" rel="stylesheet">
<link href="<c:url value='/resources/css/style.css'/>" rel="stylesheet">
<link href="<c:url value='/resources/css/pages/dashboard.css'/>" rel="stylesheet">
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
 
<script type = "text/javascript" charset = "utf-8">
    //sessionStorage.setItem("ctx", "${pageContext.request.contextPath}");
    //var ctx = sessionStorage.getItem("ctx");
    </script>   
</head>
<body>
<c:set var = "contextPath" value = "<%=request.getContextPath() %>"></c:set>
<!--  
<c:if test = "${empty uid }">
<%//response.sendRedirect(request.getContextPath()+"/users/login"); %>
</c:if>
-->
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span
                    class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span> </a><a class="brand" href="${contextPath}/">노인 건강 모니터링 시스템 </a>
      <div class="nav-collapse">
        <ul class="nav pull-right">
        <c:if test ="${auth ge 0 }">
          <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-cog"></i> 
                            List
                             <b class="caret"></b></a>
            <ul class="dropdown-menu" id = "eld-list">
              <li>
            </ul>
          </li>
          </c:if>
          <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i
                            class="icon-user"></i> ${ uid} <b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="javascript:;">Profile</a></li>
              <li><a href="#" onclick="logout(); return false;">Logout</a></li>
            </ul>
          </li>
        </ul>
        
      </div>
      <!--/.nav-collapse --> 
    </div>
    <!-- /container --> 
  </div>
  <!-- /navbar-inner --> 
</div>
<!-- /navbar -->
<div class="subnavbar">
  <div class="subnavbar-inner">
    <div class="container">
      <ul class="mainnav">
        <li id = "home-btn"><a href="${contextPath }/" onclick = "navBtnActive('#home-btn');"><i class="icon-dashboard"></i><span>Home</span> </a> </li>
        <li id = "datas-btn"><a href="${contextPath }/devices/datas" onclick = "navBtnActive('#datas-btn');"><i class="icon-bar-chart"></i><span>Datas</span> </a> </li>
        <li id = "monitoring-btn"><a href="${contextPath }/devices/monitoring" onclick = "navBtnActive('#monitoring-btn');"><i class="icon-facetime-video"></i><span>Home monitoring</span> </a></li>
<c:if test = '${auth eq 1}' >
      <li class="dropdown"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-long-arrow-down"></i><span>Drops</span> <b class="caret"></b></a>
          <ul class="dropdown-menu">
            <li><a href="${contextPath }/devices/form">기기 등록</a></li>
            <li><a href="${contextPath}/users/approval">가입 승인</a></li>
          </ul>
        </li>
        </c:if>

        
      </ul>
    </div>
    <!-- /container --> 
  </div>
  <!-- /subnavbar-inner --> 
</div>
<!-- /subnavbar -->
</body>


<script src="<c:url value='/resources/js/jquery-1.7.2.min.js'/>"></script> 

<script>
	function logout(){
		$.getJSON('/elderlycare/users/logout', function(data){
			sessionStorage.clear();
			window.location.replace('');
		});
	}
	function selectDev(key, ip){
		sessionStorage.setItem("ekey", key);
		sessionStorage.setItem("ip", ip);
		window.location.replace('');
	}
	function navBtnActive(btn){
		/* 
		var btn = '';
		switch(loc){
		case 'elderlycare/':
			btn = '#home-btn'; break;
		case 'elderlycare/devices/datas':
			btn = '#datas-btn'; break;
		case 'elderlycare/devices/monitoring':
			btn = '#monitoring-btn'; break;
		}
		 */
		//$('li.active').removeClass('active');
 		$(btn).addClass('active');
	}
	 $(document).ready(function() {
		//navBtnActive($(location).attr('pathname'));
	 	
		 var btn = '';
			switch($(location).attr('pathname')){
			case 'elderlycare/':
				btn = '#home-btn'; break;
			case 'elderlycare/devices/datas':
				btn = '#datas-btn'; break;
			case 'elderlycare/devices/monitoring':
				btn = '#monitoring-btn'; break;
			}
			
			//$('li.active').removeClass('active');
	 		$(btn).addClass('active');
		
		var html = '';
	 	$.getJSON('/elderlycare/devices', function(data){
	 		$.each(data, function(index, item){
	 			//html +="<li><a href = 'devices/"+item.ekey+"'>";
	 			var param = JSON.stringify(item)
	 			//console.log(param);
	 			//html+="<li><a href='#' onclick='selectDev("+item.ekey+", \""+item.homeIoT.toString() +"\"); return false;'>";
	 			
	 			html+="<li><a href='#' onclick='selectDev(";
	 			html+=item.ekey+", ";
	 			html+='"'+item.homeIoT+'"';
	 			html+="); return false;'>";
	 			html +=item.ename+"</a></li>";
	 			
	 			console.log(html);
	 		});
	 		$('#eld-list').html(html);
	 	});
	 	//sessionStorage.getItem('ekey', ekey);
	 	/* 
	 	$('#datas-btn').click(function(){
	 		console.log("clicked");
	 		$('li.activate').removeClass('activate');
	 		$(this).addClass('activate');
	 	});
	 	 */
	 });
</script>
</html>