 //初始化file表单框
var upload1;
var upload2;
var upload3;
var upload4;
/*var upload5;*/
var inputhand;
var hs = 0;
var carType=0;//车辆类型
var priceRange=0; //价格区间
var carposition=0; //车辆状态
var draftSave=""//判断是否保存为草稿   空新建 有值为草稿
var revis = true;//判断预览

	$(function(){
		//form校验
			$("form :input.required").each(function(){ 
	            //创建元素
	            var required = $("<strong class='high'></strong>");
	            //将它追加到文档中
	            $(this).parent().append(required);
	        });
			
	        $("form :input").blur(function(){
	            var parent = $(this).parent();
	            parent.find(".msg").remove();
	            //产品名称
	            if($(this).is("#productnames")){
	            	
	                if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入产品名称";
	                    //class='msg onError' 中间的空格是层叠样式的格式
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                    $(this).focus(); 
	                    		
	                    
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }           
	            }
	            //车辆品牌
	            if($(this).is("#brandid")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入车辆品牌";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //出厂时间
	            /*if($(this).is("#startTimeFrame")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入出厂时间";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }*/
	          //商品价格
	            if($(this).is("#price")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入商品价格";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //必选总价
	            if($(this).is("#minprice")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入必选总价";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //国内指导价
	            if($(this).is("#guideprice")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = "请输入国内指导价";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	            //库存
	            if($(this).is("#stock")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入库存";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	            //原产地
	            if($(this).is("#country")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入原产地";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //定金比例
	            var reg = /^((?!0)\d{1,2}|100)$/;
	            if($(this).is("#deposit")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入定金比例";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	            	else if(!reg.test($.trim(this.value))){
	            		var errorMsg = "定金比例输入有误";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	            	}
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //型号
	            if($(this).is("#carModel")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入型号";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //车身内部颜色
	            if($(this).is("#inColor")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入车身内部颜色";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //车身车身外观颜色
	            if($(this).is("#outColor")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入车身外观颜色";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //发动机
	            if($(this).is("#engine")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入发动机型号";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //变速箱
	            if($(this).is("#gearbox")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入变速箱型号";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //基础配置
	            if($(this).is("#basicConfigure")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入基础配置";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	          //必选配置
	            if($(this).is("#mustConfigure")){
	            	if($.trim(this.value) == ""){
	                    var errorMsg = " 请输入必选配置";
	                    parent.append("<span class='msg onError'>" + errorMsg + "</span>"); 
	                }
	                else{
	                    var okMsg="";
	                    parent.find(".high").remove();
	                    parent.append("<span class='msg onSuccess'>" + okMsg + "</span>");
	                }  
	            }
	            
	        });
	
	        //点击重置按钮时，触发文本框的失去焦点事件
	        $("#submit_form").click(function(){
	        	 $("form .required:input").trigger("blur"); 
	             var numError = $("form .onError").length;
	             if(numError==0){
	            	 submitForm();
	                 sendSubmit();
	                 return false;
	             }
	        });
	        
		        //上传图片初始化
				upload1 = new imageUploads($("#imgInput"),true);//缩略
				upload1.init("image");	
				upload2 = new imageUploads($("#imageInput"),true);//封面
				upload2.init("image");
				upload3 = new imageUploads($("#imgtInput"),true);
				upload3.init("image");
				upload4 = new imageUploads($("#imginfoInput"),true);
				upload4.init("image");
				/*upload5 = new imageUploads($("#threeimgInput"),true);
				upload5.init("image");*/
				
				//删除可选配置
				var inputhand = '<div class="ipt_txt">'+
						'<input type="hidden"  name="conf_id" />'+
						'<input type="text" placeholder="名称" name="conf_name" />'+
						'<input type="hidden" value="" name="conf_id"/>'+
						'<input type="text" placeholder="价格" name="conf_price" onkeyup="confy(this)" />'+
						'<div class="del" id="delect" onclick="del(this)"></div>'+
						'</div>';
				
				$(".adds").click(function() {
					$(".rtmth").append(inputhand);
				});
				$(".del").bind("click",function(){
					$(this).parent().hide();
				});	
				//获取车辆品牌
				 var brandid = $("#brandid").find("option:selected").val();  
				 $.ajax({
		               url:g_requestContextPath+"/h/l/supplier/queryBrandList",
		               type:'get',
		               data:{},
		               dataType:'json',
		               async:false,
		               success:function(data){
		                    if(data){
		                    	var option='<option value="" selected="selected">-请选择-</option>';
		                    	for(var i=0 ; i<data.length; i++){
		                            option+='<option value="'+data[i].id+'">'+data[i].scValue+'</option>';
		                        }
		                    	$("#brandid").html(option);
		                    }
		               } 
		        });
	            
		        //遮罩层
		    	$('.boom').css({
		    		'width':$(window).width(),
		    		'height':$(window).height(),
		    	})
		    	
		    	//点击确定(报错提示)
		    	$('.delBtn').click(function(){
		    		$('.Errors').hide();
		    		$('.boom').hide();
		    		$("#btn_levelUp").removeAttr("disabled");
		    	})
	    });
	    //禁止输入中文
		function confy(obj){
			obj.value = obj.value.replace(/[^0-9-]+/,"");  
		}
		function del(obj){
			$(obj).parent().hide();
			$(inputhand).hide();
		}

		//上传图片提示	
		function submitForm(){
		        var fileObj1 = upload1.getNewFile();
		        var fileObj2 = upload2.getNewFile();
		        var fileObj3 = upload3.getNewFile();
		        var fileObj4 = upload4.getNewFile();
		        //var fileObj5 = upload5.getNewFile();
		        var fileLen1 = fileObj1.length;
		        var fileLen2 = fileObj2.length;
		        var fileLen3 = fileObj3.length;
		        var fileLen4 = fileObj4.length;
		        //var fileLen5 = fileObj5.length;
		        if(fileLen4 <1){
		            $('.boom').show();
		            $('.Errors').show();
		            $('.Errors').find("span").html("请上传详情展示图");
		            $("#btn_levelUp").removeAttr("disabled");
		            return false;
		       }
		}  
     
	    //获取可选配置 json
		function sendSubmit(){ 
			//可选配置
			var infoArray = new Array();
			$(".ipt_txt").each(function(k,v){
				infoArray.push({id:$(v).find("[name=conf_id]").val(),name: $(v).find("[name=conf_name]").val() ,number: $(v).find("[name=conf_price]").val()});		
			});
	       //console.log("可选配置",infoArray);
			var indexImageH;
			var shrinkImageH;
			var recommendImageH;
			//缩略图
			if(upload1.getNewFile().length == 0){
				if(upload1.getOldFile() ==null){
	        //$("#shrinkImageH").val();
				}else if(upload1.getOldFile().length != 0){
					shrinkImageH=upload1.getOldFile()[0].name;
				}else{
	          $("#shrinkImageH").val();
				}	
			}else{
				shrinkImageH=upload1.getNewFile()[0].name;
			}
			
			//封面图
			if(upload2.getNewFile().length == 0){
				if(upload2.getOldFile() ==null){
	              //$("#indexImageH").val();
				}else if(upload2.getOldFile().length != 0){
					indexImageH = upload2.getOldFile()[0].name;
				}else{
	              //$("#indexImageH").val();
				}	
			}else{
				indexImageH = upload2.getNewFile()[0].name;
			}
	
			//推荐图
			if(upload3.getNewFile().length == 0){
				if(upload3.getOldFile() ==null){
	               //$("#recommendImageH").val();
				}else if(upload3.getOldFile().length != 0){
					recommendImageH = upload3.getOldFile()[0].name;
				}else{
	            //$("#recommendImageH").val();
				}	
			}else{
				recommendImageH = upload3.getNewFile()[0].name;
			}
	
			//详情展示图
			if(upload4.getNewFile().length == 0){
				if(upload4.getOldFile() ==null){
	               //$("#recommendImageH").val();
				}else if(upload4.getOldFile().length != 0){
					detailedImageH = upload3.getOldFile()[0].name;
				}else{
	            //$("#recommendImageH").val();
				}	
			}else{
				detailedImageH = upload4.getNewFile()[0].name;
			}
			var detailedImageH = JSON.stringify(upload4.getNewFile());
			var detailedImageOld = JSON.stringify(upload4.getOldFile());
			//
			//console.log("detailedImageH",detailedImageH);
			//console.log("detailedImageOld",detailedImageOld);
			//车辆三证
			//var productFileImg = JSON.stringify(upload5.getNewFile());
			//var productFileImgOld = JSON.stringify(upload5.getOldFile());
			
			var sendData = {
		    		    "draftSave":draftSave,//判断是否保存为草稿  
		    		    "id" : $('#id').val(),    //主键
		    	   		"title":$("#productnames").val(),
		    	   		"carType" :carType,   //车辆类型
		    	   		"priceRange" :priceRange,  //价格区间
		    	   		"position" :carposition,  //车辆状态
		    	   		"basicConfigure" :$('#basicConfigure').val(),//基础配置
		    	   		"mustConfigure" :$('#mustConfigure').val(),//必选配置
		    	   		"optionalConfigure":JSON.stringify(infoArray),//可选配置
		    	   		"carPrice" :$('#price').val(),//商品价格
		    	   		"mustConfigurePrice" :$('#minprice').val(),//必选总价(元)
		    	   		"stock" :$('#stock').val(),    //库存
		    	   		"content" :ue.getContent(),//富文本
		    	   		"factoryDate":$("#startTimeFrame").val(), //出厂日期
		    	   		"country":$("#country").val(),//原产地
		    	   		"brandId":$("#brandid").find("option:selected").val(),//品牌ID
						"deposit":$("#deposit").val(),//定金支付比例
						"carModel":$("#carModel").val(), //车辆型号
						"outColor":$("#outColor").val(),//车体外观颜色
						"inColor":$("#inColor").val(),//车身内部颜色
						"engine":$("#engine").val(),//发动机
						"gearbox":$("#gearbox").val(),//变速箱
						"chinaPrice":$("#guideprice").val(),//国内指导价
						//"personal":$("#personal").val(),//个人客户加价 ?
						//"sales":$("#sales").val(),//个人销售加价   ?
						//"distributor":$("#distributor").val()//经销商加价?
						"indexImageH" : indexImageH,    
						"shrinkImageH" : shrinkImageH,          //缩略图
						//"shrinkImageH" :shrinkImageH,
						"recommendImageH" : recommendImageH,
						"detailedImageH" :detailedImageH,
						"indexImageOld" :detailedImageOld,
						//"productFileImg":productFileImg,
						//"productFileImgOld":productFileImgOld
		    	 }
	   	    	 $.ajax({
	   		    	   url:g_requestContextPath+"/h/l/supplier/saveProductsList",
	   		    	   type:"post",
	   		    	   data:sendData,
	   		    	   dataType:"json",
	   		    	   async:false,
	   		    	   success:function(data){
	   		    		    //console.log(data);
	   		    		    alertcode(data);
	   		    		    $("#submit_daftbox").removeAttr("disabled");
	   		    		    return false;
	   		    	   },
	   		    	error : function() {
	   					$("#captital_submit").removeAttr("disabled");
	   				}
	   		      })
	          };
	      //列表返回参
	     function alertcode(data){
	    	 var code = data.code;
	    	 if (code=="")return false;
	    	 if(code=="200" && revis){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的信息提交成功");
	    		 var t = 3;
	    		 var timer  = setInterval(function(){
	    			 t--;
	    			 if(t<=0){
	    				 clearInterval(timer);
	    				 location.href=g_requestContextPath+"/h/l/supplier/toProductsList";  
	    			 }
	    		 },1000);
	    	 };
	    	 if(code=="200" && !revis){
	    		 /*预览前保存草稿*/
	    		 var id = data.obj.id;
	    		 //console.log("预览前保存草稿id:"+id);
	    		 $("#id").val(id);
	    		 var url = g_requestContextPath+"/h/l/supplier/carInfo?productsId="+id;
	    		 //console.log(url)
	    		 window.open(url);
	    	 };
	    	 
	    	 if(code=="500"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("请把信息填写完整");
	    	 };
	    	 if(code=="601"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的标题为空");
	    	 };
	    	 if(code=="602"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的品牌ID为空");
	    	 };
	    	 if(code=="603"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的车辆为空");
	    	 };
	    	 if(code=="604"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的价格区间为空");
	    	 };
	    	 if(code=="605"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的车辆类型为空");
	    	 };
	    	 if(code=="606"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的定金比例为空");
	    	 };
	    	 if(code=="607"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的出厂日期为空");
	    	 };
	    	 if(code=="608"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的裸车价格为空");
	    	 };
	    	 if(code=="609"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的必须总价为空");
	    	 };
	    	 if(code=="610"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的国内指导价为空");
	    	 };
	    	 if(code=="611"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的库存为空");
	    	 };
	    	 if(code=="612"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的缩略图为空");
	    	 };
	    	 if(code=="613"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的封面图为空");
	    	 };
	    	 if(code=="614"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的详情图为空");
	    	 };
	    	 if(code=="615"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的推荐图为空");
	    	 };
	    	 if(code=="616"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的产品正文为空");
	    	 };	 
	    	 if(code=="617"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("您的车辆三证为空");
	    	 };	 
	    	 if(code=="618"){
	    		 $('.boom').show();
	    		 $('.Errors').show();
	    		 $('.Errors').find("span").html("未查到该产品");
	    	 };	 
	    	 return false;
	     }

	    //类型选择
	    $("#carType span").click(function(){
	         $(this).addClass("genre").siblings().removeClass("genre");
	         carType = $(this).data("status");
	
	    });
	    $("#priceRange span").click(function(){
	       $(this).addClass("range").siblings().removeClass("range");
	        priceRange = $(this).data("status");
	     });
	    $("#carposition span").click(function(){
	       $(this).addClass("nows").siblings().removeClass("nows");
	        carposition = $(this).data("status");
	     })
	     
	     //保存到草稿箱
	    $("#submit_daftbox").click(function(){
	    	draftSave = "0";//保存草稿
	    	revis = true;
	    	sendSubmit();
	    	$("#submit_daftbox").attr("disabled","ture");
		 });
	     //预览
	    $("#submit_revise").click(function(){
	    	draftSave = "0";
		    	revis = false;
		    	sendSubmit();
		});
 

     
     




