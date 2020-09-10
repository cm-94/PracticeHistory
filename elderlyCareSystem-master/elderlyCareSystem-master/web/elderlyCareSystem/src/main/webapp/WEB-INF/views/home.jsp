<!DOCTYPE html5>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language = "java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page session="true" %>
<html>
<head>
	<title>Home</title>
</head>
<style>
tbody{
font-size: 13px;}
</style>
<body>
<%@ include file="header.jsp" %>

<div class="main">
  <div class="main-inner">
    <div class="container">
      <div class="row">
        <div class="span6">
          
          <!-- /widget -->
          <div class="widget widget-nopad">
          	
            	<div class="widget-header"> <i class="icon-list-alt"></i>
              		<h3> Calendar</h3>
            	</div>
            	<!-- /widget-header -->
            	<div class="widget-content">
              		<div id='calendar'></div>
            	</div>
            <!-- /widget-content --> 
          
          </div>
          
          
          
          <!-- /widget --> 
        </div>
        <!-- /span6 -->
        <div class="span6">
          
          <!-- /widget -->
          
          <div class="widget widget-table action-table">
            <div class="widget-header"> <i class="icon-th-list"></i>
              <h3>List</h3>
            </div>
            <!-- /widget-header -->
            <div class="widget-content">
              <table class="table table-striped table-bordered">
                <thead>
                  <tr>
                    <th> 이름 </th>
                    <th>생년월일</th>
                    <th> 주소</th>
                    <th> 전화번호</th>
                    
                    <th class="td-actions"> status</th>
                  </tr>
                </thead>
                <tbody id = "eld-table">
                  
                
                </tbody>
              </table>
            </div>
            <!-- /widget-content --> 
          </div>
          
          
          <!-- /widget -->
        </div>
        <!-- /span6 --> 
      </div>
      <!-- /row --> 
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
                            About Free Admin Template</h4>
                        <ul>
                            <li><a href="javascript:;">EGrappler.com</a></li>
                            <li><a href="javascript:;">Web Development Resources</a></li>
                            <li><a href="javascript:;">Responsive HTML5 Portfolio Templates</a></li>
                            <li><a href="javascript:;">Free Resources and Scripts</a></li>
                        </ul>
                    </div>
                    <!-- /span3 -->
                    <div class="span3">
                        <h4>
                            Support</h4>
                        <ul>
                            <li><a href="javascript:;">Frequently Asked Questions</a></li>
                            <li><a href="javascript:;">Ask a Question</a></li>
                            <li><a href="javascript:;">Video Tutorial</a></li>
                            <li><a href="javascript:;">Feedback</a></li>
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
                            <li><a href="">Open Source jQuery Plugins</a></li>
                            <li><a href="">HTML5 Responsive Tempaltes</a></li>
                            <li><a href="">Free Contact Form Plugin</a></li>
                            <li><a href="">Flat UI PSD</a></li>
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
        <div class="span12"> &copy; 2013 <a href="#">Bootstrap Responsive Admin Template</a>. </div>
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
<script>     


$(document).ready(function() {
	var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    
    var eList = [];
    var temp = null;
    var ev = new Object();
    $.getJSON('users/calendar', function(data){
    	temp = data;
    	
    
	var calendar = $('#calendar').fullCalendar({
		
    	header: {
    		left: 'prev,next today',
        	center: 'title',
        	right: 'month,agendaWeek,agendaDay'
		},
    	selectable: true,
    	selectHelper: true,
    	select: function(start, end, allDay) {
            var title = prompt('Event Title:');
            if (title) {
            	var etimezoneOffset = new Date(end).getTimezoneOffset()*60000;
            	var stimezoneOffset = new Date(start).getTimezoneOffset()*60000;
            	var calJson = {
                    	title: title,
                    	start: (new Date(start-stimezoneOffset)).toISOString(),
                    	end: (new Date(end-etimezoneOffset)).toISOString(),
                    	allDay: allDay
                    };
            	$.ajax({
            		url:'users/calendar',
            		type: 'POST',
            		contentType: 'application/json;charset=utf-8',
            		data: JSON.stringify(calJson),
            		dataType: 'json',
            		success: function(response){
            			calendar.fullCalendar('renderEvent', calJson, true);
            		}
            	});
            	
            	
            	
            	
            	//calendar.fullCalendar('renderEvent',calJson,true);
            	// make the event "stick" (true?))
            	
            }
            calendar.fullCalendar('unselect');
          },
          editable: true,
          events: temp
        });
    });
        var html1 = '', html2 = '';
     	$.getJSON('/elderlycare/devices', function(data){
     		$.each(data, function(index, item){
     			html1+="<li><a href='#' onclick='selectDev("+item.ekey+"); return false;'>"
	 			//html1 +="<li><a href = 'devices/"+item.ekey+"'>";
     			html1 +=item.ename+"</a></li>";
     		
	 			html2+="<tr>";
	 			html2 +="<td>"+item.ename+"</td>";
	 			html2+="<td>"+item.ebirth+"</td>";
	 			html2 +="<td>"+item.eaddr+"</td>";
	 			html2 +="<td>"+item.etel+"</td>";
	 			console.log(item.stat);
	 			if(item.stat== 1)
	 				html2+= '<td class="td-actions"><a class="btn btn-small btn-success"><i class="btn-icon-only icon-ok"> </i></a></td>';
	 			else
	 				html2+='<td class="td-actions"> <a class="btn btn-danger btn-small"><i class="btn-icon-only icon-remove"> </i></a></td>';
	 			html2+="</tr>";
	 		});
	 		$('#eld-list').html(html1);
	 		$('#eld-table').html(html2);
	 	});
      });
    </script><!-- /Calendar -->
</body>
</html>