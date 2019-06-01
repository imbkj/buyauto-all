var upload1;
var upload2;
var filePath;
var msgId;
var token;
$(document).ready(function() {
	    $(".bg").css({
	    		"width":$(window).width(),
	            "height":$(window).height(),
	    });


	    getImgFilePath();
		//表单校验
		$("#submit_button").bind('click',function(){
			  var isidCard = /(^\d{15}$)|(^\d{17}([0-9]|X)$)/;
		    if($("#username").val()=="") {
		    	    $(".pop").show();
		    	    $(".popMiddle p").text("请输入真实姓名");
		     	    return false;
		     	    
		      }
		    if($("#idcard").val()=="") {
		    	     $(".pop").show();
			    	 $(".popMiddle p").text("请输入身份证号码");
		     	    return false;
		     	    
		      }
		    else if(!isidCard.test($("#idcard").val())){
		  	      $(".pop").show();
			      $(".popMiddle p").text("请输入正确的身份证号码");
		    	  return false;
		    }
		    
		    var fail = submitForm();
		    if(fail)return false;
		    sendsubmit();
		    token = $("#token").val();
		    msgId = $("#msgId").val();
		    $.ajax({
		    	   url:g_requestContextPath+'/h/l/msg/subInfo',
		    	   type:'post',
		    	   dataType:'json',
		    	   async:false,
		    	   data:{
		    		   "token":token,
		    		   "facade":$("#username").val(),
		    		   "content":$("#idcard").val(),
		    		   "cardFile":creditCards,
		    		   "cardbFile":cardbFile,
		    		   "type":"1",   
		    	   },
		    	   success:function(data){
		    		    console.log(data);
		    		    sendtips(data);
		    	   },
		    	  error:function(){
		    		  // alert('非法参数')
		    	   }
		      })
	
		});
	
	upload1 = new imageUploads($("#fileimgs"),true);//初始化file表单框
	upload2 = new imageUploads($("#filecards"),true);//初始化file表单框
 
	/*如果有数据显示*/
	var uploadFigure1 = new Array();
    var uploadFigure2 = new Array();
    var file1 = $(".content").data("card");
    var file2 = $(".content").data("cardb");
    if(file1!=""){
    	uploadFigure1.push({filePath:filePath,name:file1});
    	upload1.init("image",uploadFigure1);
    }else{
    	upload1.init("image");
    }
    if(file2!=""){
    	uploadFigure2.push({filePath:filePath,name:file2});
    	upload2.init("image",uploadFigure2);
    }else{
    	upload2.init("image");
    }
	
  
});
       



    function submitForm(){
		    if(upload1.getNewFile().length == 0){
				if(upload1.getOldFile() ==null){
					 $(".pop").show();
				     $(".popMiddle p").text("请上传身份证正面图片");
					 return true;
				}	
			}
		    if(upload2.getNewFile().length == 0){
				if(upload2.getOldFile() ==null){
					 $(".pop").show();
				     $(".popMiddle p").text("请上传身份证反面图片");
					 return true;
				}	
			}
		    return false;
		}

		
	function sendsubmit(){
			   //身份证正面
			   if(upload1.getNewFile().length == 0){
					if(upload1.getOldFile() ==null){
			         //$("#fileimgs").val();
					}else if(upload1.getOldFile().length != 0){
						creditCards=upload1.getOldFile()[0].name;
					}else{
			            $("#fileimgs").val();
					}	
				}else{
					creditCards=upload1.getNewFile()[0].name;
				}
			    //console.log(debitCards);
			   
			    //身份证反面
			   if(upload2.getNewFile().length == 0){
					if(upload2.getOldFile() ==null){
			         //$("#fileimg").val();
					}else if(upload2.getOldFile().length != 0){
						cardbFile=upload2.getOldFile()[0].name;
					}else{
			            $("#filecards").val();
					}	
				}else{
					cardbFile=upload2.getNewFile()[0].name;
				}
			   
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
			   if(code =="908"){
				   $(".pop").show();
				   $(".popMiddle p").text("身份证反面文件");
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
