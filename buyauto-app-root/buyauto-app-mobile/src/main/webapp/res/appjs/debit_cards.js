var upload1;
var debitCards;
var filePath;
var msgId;
var token;
$(document).ready(function() {
	  getImgFilePath();
	  $("#submit_button").bind('click',function(){
			  //var pattern = /^([1-9]{1})(\d{14}|\d{18})$/;
		      if($("#username").val()=="") {
		    	    $(".pop").show();
				    $(".popMiddle p").text("请输入借记卡卡号");
		       	    return false;
	            }
		      /*else if(!pattern.test($("#username").val())){
		    	  $(".pop").show();
				  $(".popMiddle p").text("请输入正确的借记卡卡号");
		      	  return false;
		      }*/
		      else if($("#idcard").val()==""){
		      	  $(".pop").show();
				  $(".popMiddle p").text("请输入借记卡卡行");
		      	  return false;
		      }
		      var fail = submitForm();
		      if(fail)return false;
		      sendsubmit();
		      msgId = $("#msgId").val();
		      token = $("#token").val();
		      var sendDate = {
		    		   "token":token,
		    		   "facade":$("#username").val(),
		    		   "content":$("#idcard").val(),
		    		   "cardFile":debitCards,
		    		   "type":"2",   
		    	   };
		      console.log(sendDate);
		      $.ajax({
		    	   url:g_requestContextPath+'/h/l/msg/subInfo',
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
		      })
      
     });
	 var uploadFigure1 = new Array();
	 upload1 = new imageUploads($("#fileimg"),true);//初始化file表单框
     var card = $("#card").val();
     if(card!=null&&card!=""){
    	 uploadFigure1.push({filePath:filePath,name:card});
    	 upload1.init("image",uploadFigure1);
     }else{
    	 upload1.init("image");
     }
	  
	 
});

	function submitForm(){
		    if(upload1.getNewFile().length == 0){
				if(upload1.getOldFile() ==null){
					 $(".pop").show();
					 $(".popMiddle p").text("请上传借记卡正面图片");
					 return true;
				}	
			}
		    return false;
	   }  




   function sendsubmit(){
		   if(upload1.getNewFile().length == 0){
				if(upload1.getOldFile() ==null){
	             //$("#fileimg").val();
				}else if(upload1.getOldFile().length != 0){
					debitCards=upload1.getOldFile()[0].name;
				}else{
	                $("#fileimg").val();
				}	
			}else{
				debitCards=upload1.getNewFile()[0].name;
			}
		    //console.log(debitCards);
   }
   
   
   
   function sendtips(code){
		   if(code =="200"){
			   location.href=g_requestContextPath+"/h/l/msg/personalInfo?token="+token+"&msgId="+msgId;
		   };
		   if(code =="601"){
			   $(".pop").show();
			   $(".popMiddle p").text("用户未登录");
			   
		   };
		   if(code =="905"){
			   $(".pop").show();
			   $(".popMiddle p").text("卡号为空");
		   };
		   if(code =="906"){
			   $(".pop").show();
			   $(".popMiddle p").text("开户行为空");
		   };
		   if(code =="907"){
			   $(".pop").show();
			   $(".popMiddle p").text("银行卡照片为空");
			   
		   };
		   if(code =="909"){
			   $(".pop").show();
			   $(".popMiddle p").text("类型错误");
		   };
		   return false;
	   }
 


   /*获取图片前缀地址*/	
	function getImgFilePath(){
		$.ajax({
			 url:g_requestContextPath+'/getShowAddress',
	    	   type:'post',
	    	   dataType:'json',
	    	   async:false,
	    	   data:{},
	    	   success:function(data){
	    		    filePath = data.detail;
	    	   },
	    	   error:function(){
	    		   //alert('非法参数')
	    	   }
		});
	}	
	
	$(".popBottom").click(function(){
		 $(".pop").hide();
	})

 

