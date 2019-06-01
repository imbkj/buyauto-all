$(function(){
	if($('#position').val() == '2' || $('#position').val() == '0' ){
		$('#addrs').html('系统首页');
		$('.back').attr({'href':g_requestContextPath+"/h/l/user/leaderIndex"})
	}else{
		$('#addrs').html('首页');
		$('.back').attr({'href':g_requestContextPath+"/"})
	}
	var time = 5;	
 	var timer = setInterval(function(){
 		time--;
 		$('#time').html(time);
 		if(time<1){
 			clearInterval(timer);
	 		if($('#position').val() == '2' || $('#position').val() == '0' ){
				location.href = g_requestContextPath+"/h/l/user/leaderIndex";	
			}else{
				location.href = g_requestContextPath+"/";
			}
 		}
 	},1000)
 	
 	$('#back').click(function(){
 		if($('#position').val() == '2' || $('#position').val() == '0' ){
			location.href = g_requestContextPath+"/h/l/user/leaderIndex";	
		}else{
			location.href = g_requestContextPath+"/";
		}
 	})
})