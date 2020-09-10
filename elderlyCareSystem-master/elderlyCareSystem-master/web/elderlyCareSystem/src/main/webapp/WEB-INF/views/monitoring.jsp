<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title> - Bootstrap Admin Template</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <style type = "text/css">
    #map{
    	height:300;
    	width: 250;
    }
    </style>
</head>
<body>
<%@ include file="header.jsp" %>
    <div class="main">
        <div class="main-inner">
            <div class="container">
             <div class="row">
                    <div class="span6">
                        <div class="widget widget-nopad">
 <div class="widget">
                            <div class="widget-header">
                                <i class="icon-bar-chart"></i>
                                <h3>Monitoring</h3>
                            </div>
                            <div class="widget-content" id = "streaming">
                            
                            
 								<iframe></iframe>
 							</div>
                            <!-- /widget-content -->
                        </div>
                        
                    

                </div>
                <!-- widget widget-nopad -->
                </div>
                <!-- span6 -->
                <div class = "span6">
                <div class = "widget">
                <div class = "widget-header">
                <i class = "icon-map-marker"></i>
                <h3>GPS</h3>
                </div>
                <div class = "widget-content" id = "map" style = "width:100%;height:350px;">
                
                </div>
                </div>
                </div>
                </div>
                <!-- row -->
            </div>
            <!-- /container -->
        </div>
        <!-- /main-inner -->
    </div>
    <!-- /main -->
    <div class="extra">
        <div class="extra-inner">
            <div class="container">
                <div class="row">
                    <div class="span3">
                        <h4>
                            Elderly health care and home monitoring</h4>
                        <ul>
                            <li><a href="javascript:;">Web site</a></li>
                            <li><a href="javascript:;">Mobile Application</a></li>
                            <li><a href="javascript:;">HomeIoT device</a></li>
                            <li><a href="javascript:;">Wearable band device</a></li>
                        </ul>
                    </div>
                    <!-- /span3 -->
                    <div class="span3">
                        <h4>
                            Reference</h4>
                        <ul>
                            <li><a href="https://github.com/haydenpark/haydenpark.github.io/tree/master/double-axes">HT graph</a></li>
                            <li><a href="https://www.egrappler.com/templatevamp-twitter-bootstrap-admin-template-now-available/
                            ">Free dash board template</a></li>
                            <li><a href="javascript:;">bla</a></li>
                            <li><a href="javascript:;">bla bla</a></li>
                        </ul>
                    </div>
                    <!-- /span3 -->
                    <div class="span3">
                        <h4>
                            Something Legal</h4>
                        <ul>
                            <li><a href="javascript:;">Read License</a></li>
                            <li><a href="javascript:;">Terms of Use</a></li>
                            <li><a href="javascript:;">Privacy Policy</a></li>
                        </ul>
                    </div>
                    <!-- /span3 -->
                    <div class="span3">
                        <h4>
                            Open Source jQuery Plugins</h4>
                        <ul>
                            <li><a href="#">Open Source jQuery Plugins</a></li>
                            <li><a href="#">HTML5 Responsive Tempaltes</a></li>
                            <li><a href="#">Free Contact Form Plugin</a></li>
                            <li><a href="#">Flat UI PSD</a></li>
                        </ul>
                    </div>
                    <!-- /span3 -->
                </div>
                <!-- /row -->
            </div>
            <!-- /container -->
        </div>
        <!-- /extra-inner -->
    </div>
    <!-- /extra -->
    <div class="footer">
        <div class="footer-inner">
            <div class="container">
                <div class="row">
                    <div class="span12">
                        &copy; 2013 <a href="#">Bootstrap Responsive Admin Template</a>.
                    </div>
                    <!-- /span12 -->
                </div>
                <!-- /row -->
            </div>
            <!-- /container -->
        </div>
        <!-- /footer-inner -->
    </div>
    <!-- /footer -->
    <!-- Le javascript
================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="<c:url value='/resources/js/jquery-1.7.2.min.js'/>"></script> 
<script src="<c:url value='/resources/js/excanvas.min.js'/>"></script> 
<script src="<c:url value='/resources/js/chart.min.js'/>" type="text/javascript"></script> 
<script src="<c:url value='/resources/js/bootstrap.js'/>"></script>
<script type="text/javascript" src="<c:url value='/resources/js/full-calendar/fullcalendar.min.js'/>"></script>

 
<script src="<c:url value='/resources/js/base.js'/>"></script> 

<script src="https://polyfill.io/v3/polyfill.min.js?features=default"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDT7sSTMO5sgyqu_1l0KuaIK_QAyv0U44c&callback=initMap&libraries=&v=weekly"
      defer
    ></script>
    
<script>
function initMap(){
	
}
$(function(){
	var ip = sessionStorage.getItem("ip");
	var ekey = sessionStorage.getItem("ekey");
	//var mapX = sessionStorage.getItem("mapX");
	//var mapY = sessionStorage.getItem("mapY");
	
	console.log(sessionStorage.getItem("ekey")+":"+ip);
	
	var url = "http://"+ip+":8090/?action=stream";
	
	var streaming = '<center><iframe src ="'+url+'" width="325" height="240">이 브라우저는 iframe을 지원하지 않습니다.</iframe></center>';
	$('#streaming').html(streaming);
	
	"use strict";
	$.getJSON('/elderlycare/devices/'+ekey+'/curdata', function(data){
		var pos = {lat: data.ealtitude,
				lng: data.elongitude};
		let map;
		//function initMap(){
			map = new google.maps.Map(document.getElementById("map"),{
				center : pos,
				zoom: 18
			});
			var marker = new google.maps.Marker({
				position:pos, map: map
			});
			var image = 'img/'
		//}
	});
	/* 
	$('#map').html(map);
	 */
});
</script>
</body>
</html>
