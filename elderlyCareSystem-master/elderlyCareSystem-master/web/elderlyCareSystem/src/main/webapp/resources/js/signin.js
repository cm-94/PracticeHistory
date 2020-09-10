$(function () {
	
	/*jQuery.support.placeholder = false;
	test = document.createElement('input');
	if('placeholder' in test) jQuery.support.placeholder = true;
	
	if (!$.support.placeholder) {
		
		$('.field').find ('label').show ();
		
	}*/
	$('#login-form').submit(function(event){
		event.preventDefault();
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
					window.location.replace('');
				}else
					alert("아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.");
			},                      
			error   : function(response){
				alert(response.uid);
			}
		});
	});
});