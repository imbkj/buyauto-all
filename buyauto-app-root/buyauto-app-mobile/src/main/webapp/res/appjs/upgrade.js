$(function(){
	$("#submit_button").click(function(){
		var sendDate = {
	    		   "token":$("#token").val(),
	    		   "msgId":$("#msgId").val(),
	    	   }
		console.log(sendDate);
		$.ajax({
	    	   url:g_requestContextPath+'/h/l/msg/subButton',
	    	   type:'post',
	    	   dataType:'json',
	    	   async:false,
	    	   data:sendDate,
	    	   success:function(data){
	    		    console.log(data);
	    		    sendtips(data);
	    	   },
	    	  error:function(){
	    		   console.log('非法参数')
	    	   }
	      });
	});
});

function sendtips(code){
	   if(code =="200"){
		   location.href=g_requestContextPath+"/h/l/msg/jumpApp";/*提交成功调回APP*/
	   };
	   if(code =="601"){
		   $(".pop").show();
		   $(".popMiddle p").text("用户未登录");
	   };
	   if(code =="910"){
		   $(".pop").show();
		   $(".popMiddle p").text("身份证信息未填写完整");
	   };
	   if(code =="911"){
		   $(".pop").show();
		   $(".popMiddle p").text("借记卡信息未填写完整");
	   };
	   if(code =="912"){
		   $(".pop").show();
		   $(".popMiddle p").text("信用卡信息未填写完整");
	   };
	   return false;
}

$(".popBottom").click(function(){
	 $(".pop").hide();
})
