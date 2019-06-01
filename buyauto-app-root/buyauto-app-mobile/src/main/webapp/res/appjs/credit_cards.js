var upload;
var debitCards;
var filePath;
var msgId;
var token;

$(document).ready(function() {
	getImgFilePath();
	  $("#submit_button").bind('click',function(){
		  //var reCard =/^(4\d{12}(?:\d{3})?)$/; 
		  var creadit=$("#creditid").val();
	      if($("#creditid").val()=="") {
	    	     $(".pop").show();
				 $(".popMiddle p").text("请输入信用卡卡号");
	       	    return false;
            }
	     /* else if(!reCard.test(creadit)){
	    	  $(".pop").show();
			  $(".popMiddle p").text("请输入正确的信用卡卡号");
	      	  return false;
	      }*/
	      else if($("#creditname").val()==""){
	    	  $(".pop").show();
			  $(".popMiddle p").text("请输入信用卡卡行");
	      	  return false;
	      }

	      var fail = submitForm();
	      if(fail)return false;
	      sendsubmit();
	      msgId = $("#msgId").val();
	      token = $("#token").val();
	      var sendDate = {
	    		   "token":token,
	    		   "facade":$("#creditid").val(),
	    		   "content":$("#creditname").val(),
	    		   "cardFile":creditCards,
	    		   "type":"3",   
	    	   }
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
	    	  /*error:function(){
	    		   alert('非法参数')
	    	   }*/
	      })

   })
   
   
     upload= new imageUploads($("#fileimages"),true);//初始化file表单框//首页
	 var uploadFigure1 = new Array();
     var card = $("#card").val();
     if(card!=null&&card!=""){
    	 uploadFigure1.push({filePath:filePath,name:card});
    	 upload.init("image",uploadFigure1);
     }else{
    	 upload.init("image");  
     }
	  
});
 


	function submitForm(){
	    if(upload.getNewFile().length == 0){
			if(upload.getOldFile() ==null){
				 $(".pop").show();
				 $(".popMiddle p").text("请上传信用卡正面图片");
				 return true;
			}	
		}
	    return false;
	}  
			
			
	function sendsubmit(){
	   if(upload.getNewFile().length == 0){
			if(upload.getOldFile() ==null){
				
			}else if(upload.getOldFile().length != 0){
				creditCards=upload.getOldFile()[0].name;
			}else{
	            $("#fileimages").val();
			}	
		}else{
			creditCards=upload.getNewFile()[0].name;
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
