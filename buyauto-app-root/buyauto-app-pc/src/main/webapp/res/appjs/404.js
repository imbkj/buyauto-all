$(function(){
	var time = 5;	
 	var timer = setInterval(function(){
 		time--;
 		$('#time').html(time);
 		if(time<1){
 			clearInterval(timer);
 			location.href = g_requestContextPath+"/";
 		}
 	},1000)
}) 