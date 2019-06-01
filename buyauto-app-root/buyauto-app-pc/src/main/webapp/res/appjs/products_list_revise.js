 //初始化file表单框
var upload1;//缩略图
var upload2;//封面图片
var upload3;//推荐图
var upload4;//详情展示图(多张)
//var upload5;//车辆三证
var inputhand;
var carType=0;//车辆类型
var priceRange=0; //价格区间
var carposition=0; //车辆状态
var draftSave=""//判断是否保存为草稿   空:新建 0草稿
var code;
var revis = true;//判断预览
var jump = false;
var ueEdit;
var jumpUrl=1;//1:跳列表 2：跳草稿列表


	  $(function(){
			   //遮罩层
		    	$('.boom').css({
		    		'width':$(window).width(),
		    		'height':$(window).height(),
		    	})
		        //车辆类型初始化
		        brandList();
		        
		        //校验表单
				$("form :input.required").each(function(){ 
		            var required = $("<strong class='high'></strong>");
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
		            //车辆品牌
		            if($(this).is("#startTimeFrame")){
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
		    });
		        
		     //点击重置按钮时，触发文本框的失去焦点事件
		     $("#submit_form").click(function(){
		            //trigger 事件执行完后，浏览器会为submit按钮获得焦点
		        	 $("form .required:input").trigger("blur"); 
		             var numError = $("form .onError").length;
		             if(numError==0){
		            	 submitForm();
		            	 jumpUrl=1;//提交成功跳列表
		                 sendSubmit();
		                 return false;
		             }
		       });
		        
			  //初始化ueditor
			  ueEdit=UE.getEditor('comContent',{
			   		autoHeightEnabled: false  //自动适应高度
			   		,initialFrameHeight : 320 //编辑器高度，默认320
			   	});
				
			   /*初始化表单控件*/
				upload1 = new imageUploads($("#imgInput"),true);//
				upload2 = new imageUploads($("#imageInput"),true);
				upload3 = new imageUploads($("#imgtInput"),true);
				upload4 = new imageUploads($("#imginfoInput"),true);
				//upload5 = new imageUploads($("#threeimgInput"),true);
				
				 //删除可选配置
				var inputhand = '<div class="ipt_txt">'+
						'<input type="hidden"  name="conf_id" />'+
						'<input type="text" placeholder="名称" name="conf_name" />'+
						'<input type="hidden" value="" name="conf_id"/>'+
						'<input type="text" placeholder="价格" name="conf_price"  id="conf_price" onkeyup="confy(this)"/>'+
						'<div class="del" id="delect" onclick="del(this)"></div>'+
						'</div>';
				$(".adds").click(function() {
					$(".rtmth").append(inputhand);
				});
				$(".del").bind("click",function(){
					$(this).parent().hide();
				});
				//获取id
				 var id = ($("#id").val())+"";
					$.ajax({
						  url:g_requestContextPath+"/h/l/supplier/getProductsById",
						  type:"post",
						  dataType:"json",
						  data:{
							  "id":id
						  },
						  async:false,
						  success:function(data){
							code = data.code;
							//console.log(data);
							if(code==200){
								bindDate(data.obj)
							}else{
								location.href=g_requestContextPath+"/error/404";
							}
						  }
					});
					
			    //点击确定(报错提示)
			    $('.delBtn').click(function(){
			    		$('.Errors').hide();
			    		$('.boom').hide();
			    		$("#btn_levelUp").removeAttr("disabled");
			    		/*提交成功 页面跳转*/
			    		if(code=="200" && jumpUrl==2){
			    			//window.location.href=g_requestContextPath+"/h/l/msg/mainPage";
			    			location.href=g_requestContextPath+"/h/l/supplier/recycleBin";//草稿
			    		}else if(code == "200" && jumpUrl==1){
			    			location.href=g_requestContextPath+"/h/l/supplier/toProductsList";//列表
			    		}
			    	})
	     });
    
		//车辆类型选择
		function brandList(){
			$.ajax({
	               url:g_requestContextPath+"/h/l/supplier/queryBrandList",
	               type:'post',
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
	        })
	        var brandid = $("#brandid").find("option:selected").val();
		};
	
	    //禁止输入数字
		function confy(obj){
			obj.value = obj.value.replace(/[^0-9-]+/,"");
		}
		function del(obj){
			$(obj).parent().hide();
			$(inputhand).hide();
		}
		$("#conf_price").click(function() {
			this.value = this.value.replace(/[^0-9-]+/, '');
		})

		 /*上传图片*/
		 function submitForm(){
		        var fileObj1 = upload1.getNewFile();
		        var fileObj2 = upload2.getNewFile();
		        var fileObj3 = upload3.getNewFile();
		        var fileObj4 = upload4.getNewFile();
		      //  var fileObj5 = upload5.getNewFile();
		        var fileLen1 = fileObj1.length;
		        var fileLen2 = fileObj2.length;
		        var fileLen3 = fileObj3.length;
		        var fileLen4 = fileObj4.length;
		       // var fileLen5 = fileObj5.length;
		        if(fileLen4 < 1){
		             $('.boom').show();
		             $('.Errors').show();
		             $('.Errors').find("span").html("请上传详情展示图(多张)");
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
					var indexImageH;
					var shrinkImageH;
					var recommendImageH;
					//封面图
					if(upload2.getNewFile().length == 0){
						if(upload2.getOldFile() ==null){
						}else if(upload2.getOldFile().length != 0){
							shrinkImageH=upload2.getOldFile()[0].name;
						}else{
						}	
					}else{
						shrinkImageH=upload2.getNewFile()[0].name;
					}
					//console.log("封面图",shrinkImageH);
					
					//缩略图
					if(upload1.getNewFile().length == 0){
						if(upload1.getOldFile() ==null){
			              //$("#indexImageH").val();
						}else if(upload1.getOldFile().length != 0){
							indexImageH = upload1.getOldFile()[0].name;
						}else{
						}	
					}else{
						indexImageH = upload1.getNewFile()[0].name;
					}
			
					//推荐图
					
					if(upload3.getNewFile().length == 0){
						if(upload3.getOldFile() ==null){
			               
						}else if(upload3.getOldFile().length != 0){
							recommendImageH = upload3.getOldFile()[0].name;
						}else{
			            //$("#recommendImageH").val();
						}	
					}else{
						recommendImageH = upload3.getNewFile()[0].name;
					}
					//详情展示图
					var detailedImageH = JSON.stringify(upload4.getNewFile());
					var detailedImageOld = JSON.stringify(upload4.getOldFile());
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
				    	   		"content" :ueEdit.getContent(),//富文本
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
								"shrinkImageH" :shrinkImageH,
								"recommendImageH" : recommendImageH,
								"detailedImageH" :detailedImageH,
								"detailedImageOld" :detailedImageOld,
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
			   		    		    return false;
			   		    	   }
			   		      })
			     };
     
		     //返回参数
		     function alertcode(data){
		    	 code = data.code;
		    	 if(code == "")return "";
		    	 if(code=="200" && revis){
		    		 /*保存草稿成功*/
		    		 $('.boom').show();
		    		 $('.Errors').show();
		    		 $('.Errors').find("span").html("您的信息提交成功");
		    		 jump = true;	
		    	 };
		    	 if(code=="200" && !revis){
		    		 /*预览前保存草稿*/
		    		 var id = data.obj.id;
		    		 //console.log("id:"+id);
		    		 window.open(g_requestContextPath+"/h/l/supplier/carInfo?productsId="+id);
		    	 };
		    	 if(code=="500"){
		    		 $('.boom').show();
		    		 $('.Errors').show();
		    		 $('.Errors').find("span").html("您的信息提交失败");
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
		    	 /*if(code=="617"){
		    		 $('.boom').show();
		    		 $('.Errors').show();
		    		 $('.Errors').find("span").html("您的车辆三证为空");
		    	 };	*/ 
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
		    	draftSave = "0";
		    	revis = true;
		    	jumpUrl=2;//提交成功跳草稿列表
		    	sendSubmit();
			 });
		    
		    //预览
		    $("#submit_revise").click(function(){
		    	draftSave = "0";
		    	revis = false;
		    	sendSubmit();
			 });
		
		    //参数ID   根据ID获取产品信息
		    function bindDate(data){
				    	$("#productnames").val(data.title);
				    	$("#brandid").find("option").each(function(i,v){
				    		var value = $(v).val();
				    		if(value == data.brandId){
				    			$(v).attr("selected","selected");
				    		}
				    	});
				    	carType=data.carType;//车辆类型
						priceRange=data.priceRange; //价格区间
						carposition=data.position; //车辆状态
						
				    	//车辆类型
				    	$("#carType").find("span").each(function(i,v){
				    		var status = $(v).data("status");
				    		if(status == data.carType){
				    			$(this).addClass("genre").siblings().removeClass("genre");
				    		}
				    	});
		
				       //价格区间
				       $("#priceRange").find("span").removeClass("range");
						var twoExh = $("#priceRange").find("span").siblings().get(data.priceRange);
						$(twoExh).addClass("range");
						
				    	//车辆状态
						$("#carposition").find("span").removeClass("nows");
						var threeExh = $("#carposition").find("span").siblings().get(data.position);
						$(threeExh).addClass("nows");
						
				    	//$("#brandid").val(data.scValue);
				    	$("#country").val(data.country);
				    	$("#deposit").val(data.deposit);
				    	$("#carModel").val(data.carModel);
				    	$("#inColor").val(data.inColor);
				    	$("#outColor").val(data.outColor);
				    	$("#engine").val(data.engine);
				    	$("#gearbox").val(data.gearbox);
				    	$("#startTimeFrame").val(getLocalTime(data.factoryDate));
				    	$("#basicConfigure").val(data.basicConfigure);
				    	$("#mustConfigure").val(data.mustConfigure);
				    	$("#name").val(data.optionalConfigure.name);
				    	$("#price").val(data.carPrice);
				    	$("#minprice").val(data.mustConfigurePrice);
				        $("#guideprice").val(data.chinaPrice);
				        $("#stock").val(data.stock);
				        /*可选配置*/
				        var content = data.content;
				        ueEdit.addListener("ready", function () {
				            ueEdit.setContent(data.content);
				        });
				        $.each(eval(data.optionalConfigure),function(k,v){
				        	var inputhand ='<div class="ipt_txt">';
				        	inputhand+='<input type="hidden"  name="conf_id" />';
				        	inputhand+='<input type="text" placeholder="名称" name="conf_name"  value="'+v.name+'"/>';
				        	inputhand+='<input type="hidden" value="'+v.id+'" name="conf_id"/>';
				        	inputhand+='<input type="text" placeholder="价格" name="conf_price" value="'+v.number+'" onkeyup="this.value=this.value.replace(/[^0-9-]+/,"");" />';
				        	inputhand+='<div class="del" id="delect" onclick="del(this)"></div>';
				        	inputhand+='</div>';
							$(".rtmth").html(inputhand);
						});
				        $("#carprice").val();  //可选配置 
				        var uploadFigure1 = new Array();
				        var uploadFigure2 = new Array();
				        var uploadFigure3 = new Array();
				        var uploadFigure4 = new Array();
				        //var uploadFigure5 = new Array();
				        /*产品图片*/
				        $.each(data.tProductsImage,function(k,v){
							if(v.type ==0){	
								//缩略
								uploadFigure1.push({filePath:data.imgfilePath,name:v.filePath});
							}
							if(v.type ==1){
								//封面
								uploadFigure2.push({filePath:data.imgfilePath,name:v.filePath});
							}
							if(v.type ==2){
								//推荐
								uploadFigure3.push({filePath:data.imgfilePath,name:v.filePath});
							}
							if(v.type ==3){
								/*多图上传需要提供preid*/
								uploadFigure4.push({filePath:data.imgfilePath,name:v.filePath,preId:v.fileName});
							}
							/*if(v.type ==4){
								车辆三证
								uploadFigure5.push({filePath:data.imgfilePath,name:v.filePath,preId:v.fileName});
							}*/
						})
				//		$("#detailedImageH").val(JSON.stringify(uploadFigure));//重新赋予多图路径
						upload1.init("image",uploadFigure1);//缩略图
						upload2.init("image",uploadFigure2);//封面图片
						upload3.init("image",uploadFigure3);//推荐图
						upload4.init("image",uploadFigure4);//详情展示图(多张)
						//upload5.init("image",uploadFigure5);////车辆三证
				    }
		     
		    //日期控件
		    function getLocalTime(nS) {  
					if(nS=="" ||nS == null || (nS+"").trim()=="") return "";
					var date = new Date(parseInt(nS));
					Y = date.getFullYear() + '-';
					M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
					D = date.getDate() + ' ';
					h = date.getHours() + ':';
					m = date.getMinutes() + ':';
					s = date.getSeconds(); 
					return Y+M+D+h+m+s; 
			  } 
    
    
  