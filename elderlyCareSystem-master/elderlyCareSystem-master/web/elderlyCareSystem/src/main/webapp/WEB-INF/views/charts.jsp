<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Datas</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <style>
    #graph{
    	font: 12px san-serif;
    	background:white;
    }
    #graph .axis {
  		font: 11px sans-serif;
	}

	#graph .axis path,
	#graph .axis line {
  		fill: none;
  		stroke: #ccc;
  		shape-rendering: crispEdges;
	}

	/* Lines */
	#graph .line {
  		fill: none;
  		stroke:gray;
  		stroke-width: 2px;
	}

	#graph {
    	clear:both;
	}	
	
	#temp {
	    color: #fd843b;
	}
	#humid {
	    color: steelblue;
	}
	#datetime td{
	    float:clear;
	    line-height: 20px;
	    font-size: 18px;
	    color: #050505;
	    font-weight: bold;
	    text-align:center;
	}
	#datetime table {
	    width:100%;
	    height:100%;
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
            <div class="widget-header"> <i class="icon-list-alt"></i>
              <h3> Current Status</h3>
            </div>
            <!-- /widget-header -->
            <div class="widget-content">
              <div class="widget big-stats-container">
                <div class="widget-content">
                  <h6 class="bigstats" id="curstats"></h6>
                  <div id="big_stats" class="cf" id = "cur-data">
                  
                      
                    <div class="stat"> <span class="value" id = "cur-temp"></span> </div>
                
                    
                    <div class="stat"><span class="value"id = "cur-humid"></span> </div>
             
                    
                    <div class="stat">  <span class="value"id = "cur-epulse"></span> </div>
              
                    
                    <div class="stat">  <span class="value"id = "cur-estep"></span> </div>
               
                </div>
                <!-- /widget-content --> 
                
              </div>
            </div>
          </div>
          </div>
 <div class="widget">
                            <div class="widget-header">
                                <i class="icon-bar-chart"></i>
                                <h3>HT graph</h3>
                                <h6>gray : 온도 /  blue : 습도</h6>
                            </div>
                            <div class="widget-content">
                            
                            
 								<div id="graph"></div>
 							</div>
                            <!-- /widget-content -->
                        </div>
                        
                    
                        
                        
                    </div>
                    </div>
                    <!-- /span6 -->
                    <div class="span6">
                   <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script src="http://d3js.org/d3.v3.min.js"></script> 
    
 
                        
            
                        <div class="widget">
                            <div class="widget-header">
                                <i class="icon-bar-chart"></i>
                                <h3>
                                    Pulse graph</h3>
                                    <h6 class ="latest-date"></h6>
                            </div>
                            <!-- /widget-header -->
                            <div class="widget-content">
                                <canvas id="bar-chart" class="chart-holder" width="538" height="250">
                                </canvas>
                                <!-- /bar-chart -->
                            </div>
                            <!-- /widget-content -->
                        </div>
                        <!-- /widget -->
                        <div class="widget">
                            <div class="widget-header">
                                <i class="icon-bar-chart"></i>
                                <h3>
                                    Step accumulate graph</h3>
                                    <h6 class ="latest-date"></h6>
                            </div>
                            <!-- /widget-header -->
                            <div class="widget-content">
                                <canvas id="area-chart" class="chart-holder" width="538" height="250">
                                </canvas>
                                <!-- /line-chart -->
                            </div>
                            <!-- /widget-content -->
                        </div>
                        </div>
                        
                    </div>
                    <!-- /span6 -->
                </div>
                <!-- /row -->
                
            
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
<script>
$(function(){
	var ekey = sessionStorage.getItem("ekey");
  	//var eld = sessionStorage.getItem('elderly');//'<c:out value = '${edto.ekey}'/>';
	$.getJSON(ekey, function(data){
    	var curstats = "name : "+data.ename;
    	curstats += "	|	birth : "+data.ebirth;
    	$('#curstats').html(curstats);
    	
	});
  	var html = '';
  	$.getJSON(ekey+'/curdata', function(data){
     		
		//$.each(data, function(index, item){
		//html += "<div class='stat'> <i class='icon-asterisk'></i> <span class='value'>";
		html= "<h3>온도</h3>"+data.temp+"℃";
		//html+= "</span> </div>";
		$('#cur-temp').html(html);
     			
		//html += "<div class='stat'> <i class='icon-tint'></i> <span class='value'>";
   		html= "<h3>습도</h3>"+data.humid+"%";
   		//html+= "</span> </div>";
   		$('#cur-humid').html(html);
	     			
   		//html += "<div class='stat'> <i class='icon-heart'></i> <span class='value'>";
    	html= "<h3>심박수</h3>"+data.epulse;
     	//html+= "</span> </div>";
     	$('#cur-epulse').html(html);
		     		
     	//html += "<div class='stat'> <i class='icon-shopping-cart'></i> <span class='value'>";
	    html= "<h3>걸음 수</h3>"+data.estep;
	    //html+= "</span> </div>";
	    $('#cur-estep').html(html);
   		//});
   		//$('#cur-data').html(html);
	}); 
    /*
    온습도 그래프 출처  https://github.com/haydenpark/haydenpark.github.io/tree/master/double-axes 
    */
	// size and margins vary as the mode changes (desktop or mobile)
    //mode = $("body").attr("mode");
    //num  = $("body").attr("num"); 
	//num = "2000";
    //if (mode=="desktop"){
        //$("body").addClass("desktop");
    var num_ticks = 6;
    var full_width = 540;
    var full_height = 500;
    var margin = {top: 30, right: 50, bottom: 40, left: 50};
    /* } else {
        $("body").addClass("mobile");
        var num_ticks = 3;
        var full_width = 300;
        var full_height = 280;
        var margin = {top: 20, right: 40, bottom: 30, left: 40};
    } */
    var width = full_width - margin.left - margin.right;
    var height = full_height- margin.top - margin.bottom;

    // scale for x, y axes
    // set domains later with tsv data
    var x = d3.time.scale()
        .range([0, width]);
    var yTemp = d3.scale.linear()
        .range([height, 0]);
    var yHumid = d3.scale.linear()
        .range([height, 0]);

    // x axis for datetime
    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom")
            .ticks(num_ticks)
            .tickFormat(d3.time.format('%m/%d %H:%M'))
            .tickPadding(8);

    // 2 y axes for temperature and humidity, respectivelyt
    var yAxisTemp = d3.svg.axis()
	        .scale(yTemp)
            .tickFormat(function(d) { return  d3.format(',f')(d) + '\u2103'})
            .orient("left");
    var yAxisHumid = d3.svg.axis()
            .scale(yHumid)
            .tickFormat(function(d) { return  d3.format(',f')(d) + '%'})
            .orient("right");

    // datetime formatter
    var iso = d3.time.format.utc("%Y-%m-%d %H:%M:%S%Z");
    var date_format= d3.time.format("%Y-%m-%d");
    var time_format= d3.time.format("%H:%M");

    var svg = d3.select("#graph").append("svg")
             .attr("preserveAspectRatio", "xMinYMin meet")
             .attr("width", full_width)
             .attr("height", full_height)
             .append("g")
             .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    // Load in data and draw line graph
    //var tsv_url = "sample_data.tsv";
    //d3.tsv(tsv_url, function(error, data) {
     $.getJSON(ekey+'/htdatas', function(data){   
     	 // preprocess tsv data
        data.forEach(function(d) {
        	d.timestamp = iso.parse(d.measuredtime+"+0900");
        	d.date = date_format(d.timestamp);
        	d.time = time_format(d.timestamp);
        	d.temp = parseFloat(d.temp);
        	d.humid = parseFloat(d.humid);
    	});

    // update current temp, humid
    //$("#temp").text(data[0].temp + '\u2103');
    //$("#humid").text(data[0].humid + "%");
    //$("#date").text(data[0].date);
    //$("#time").text(data[0].time);

    // domain for y axes
    	var temp_margin = 3.0, humid_margin = 5.0;
    	var temp_min    = d3.min(data, function(d){return d.temp-temp_margin;});
    	var temp_max    = d3.max(data, function(d){return d.temp+temp_margin;});
    	var humid_min   = d3.min(data, function(d){return d.humid-humid_margin;});
    	var humid_max   = d3.max(data, function(d){return d.humid+humid_margin;});
	
	    x.domain(d3.extent(data, function(d){return d.timestamp;}));
    	yTemp.domain([temp_min, temp_max]);
    	yHumid.domain([humid_min, humid_max]);
	
	    // bind data with line
    	var lineTemp = d3.svg.line()
    	        .interpolate("basis")
    	        .x(function(d) { return x(d.timestamp); })
    	        .y(function(d) { return yTemp(d.temp); });
    	     
    	var lineHumid = d3.svg.line()
    	        .interpolate("basis")
    	        .x(function(d) { return x(d.timestamp); })
    	        .y(function(d) { return yHumid(d.humid); });
	
	             // Draw the x axis
	    svg.append("g")
	            .attr("class", "x axis")
	            .attr("transform", "translate(0,"+height+")")
	            .call(xAxis);
	    // Draw the y axis
	    svg.append("g")
	            .attr("class", "y axis")
	            .call(yAxisTemp);
	    svg.append("g")
	            .attr("class", "y axis")
	            .attr("transform", "translate("+width+",0)") // right side
            .call(yAxisHumid);

    // Draw the line
    	svg.append("path")
    	        .datum(data)
    	        .attr("class", "line")
    	        .attr("d", lineTemp);
    	svg.append("path")
    	        .datum(data)
    	        .attr("class", "line")
    	        .style("stroke", "steelblue")
    	        .attr("d", lineHumid);
    	}); 
    	 
    $.getJSON(ekey+'/banddatas', function(dd){
    	
  		var label = '"labels": [';
  		var datas = ' "datasets": [{"fillColor": "rgba(151,187,205,0.5)",'
  			+'"strokeColor": "rgba(151,187,205,1)",'
		    +'"data": [';
		var steps = ' "datasets": [{"fillColor": "rgba(151,187,205,0.5)",'
  			+'"strokeColor": "rgba(151,187,205,1)",'
		    +'"data": [';
 		$.each(dd,function(index, item){
 			item.timestamp = iso.parse(item.measuredtime+"+0900");
 			item.date = date_format(item.timestamp);
 	        item.time = time_format(item.timestamp);
 	        label += '"'+item.time+'",';
 	        datas += parseInt(item.epulse)+',';
 	        steps += parseInt(item.estep)+',';
 		});
 		$('h6.latest-date').html(dd[0].measuredtime);
 		
		label = label.substr(0, label.length-1);
		datas = datas.substr(0, datas.length-1);
		steps = steps.substr(0, steps.length-1);

		label+='],';
 		datas +=']}]';
 		steps +=']}]';
 		var barChartData = JSON.parse('{'+label+datas+'}');
 		barChartData.labels.reverse();
 		barChartData.datasets[0].data.reverse();
 		
 		var lineChartData = JSON.parse('{'+label+steps+'}');
 		lineChartData.labels.reverse();
 		lineChartData.datasets[0].data.reverse();
 		 /* var barChartData = {
		            labels: [],
		            datasets: [
						{
						    fillColor: "rgba(151,187,205,0.5)",
						    strokeColor: "rgba(151,187,205,1)",
						    data: []
						}
						]
		        };
 		barChartData.labels.push(dd[0].time);
 		barChartData.labels.push(dd[1].time);
 		barChartData.datasets[0].data.push(dd[0].epluse);
 		barChartData.datasets[0].data.push(dd[1].epluse);
 		console.log(barChartData.datasets[0].data[0]); */
		var myBar = new Chart(document.getElementById("area-chart").getContext("2d")).Bar(lineChartData);
		var myLine = new Chart(document.getElementById("bar-chart").getContext("2d")).Line(barChartData);
		
     });
    
    //$.getJSON(ekey, function(data){
    //	var curstats = '';

    //})
    
    
});

       /*  var lineChartData = {
            labels: ["1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"],
            datasets: [
				{
				    fillColor: "rgba(220,220,220,0.5)",
				    strokeColor: "rgba(220,220,220,1)",
				    pointColor: "rgba(220,220,220,1)",
				    pointStrokeColor: "#fff",
				    data: [65, 59, 90, 81, 56, 55, 40]
				},
				{
				    fillColor: "rgba(151,187,205,0.5)",
				    strokeColor: "rgba(151,187,205,1)",
				    pointColor: "rgba(151,187,205,1)",
				    pointStrokeColor: "#fff",
				    data: [28, 48, 40, 19, 96, 27, 100]
				}
			]

        }
 */
        


        



				
				
	</script>
</body>
</html>
