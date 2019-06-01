var pageNumber = 1;
var pageSize = 5;
var state = "";
var stateAfter = "";
var searchTitle = '';
var prefix = null;
var upload01;
var upload02;
var upload03;
var isok = true;
var arr01 = [100,101,102,103,104,105,106,108,109,110,200,401,402,403,404,405];
var arr02 = [104,105,106,107,108,109,110,200,401,402,403,404,405];
var arr03 = [100,103,104,105,106,107,108,109,110,200,401,402,404,405];
var arr04 = [100,101,102,103,106,107,108,109,110,200,401,402,404,405];

$(function(){
	upload01 = new imageUploads($("#imageInput01"),true);
	upload01.init("image");
	upload02 = new imageUploads($("#imageInput02"),true);
	upload02.init("image");
	upload03 = new imageUploads($("#imageInput03"),true);
	upload03.init("image");
	//点击order-deleat按钮
	$('.order-deleat').click(function(){
		$('.boom').hide();
		$('.pro01').hide();
		$('.pro02').hide();
	})
	//点击check框确定按钮
	$('.check_btn').click(function(){
		$('.boom').hide();
		$('.check_pro').hide();
	})
	
	//图片前缀
	$.getJSON(g_requestContextPath + "/getShowAddress",function(data){
		prefix = data;
	})
	
	//点击确定弹框隐藏01
	$('#proBtn01').click(function(){
		if(isok){
			var parm = '';
			var fileObj = upload01.getNewFile();
			var fileObjOld = upload01.getOldFile();
	        
	        var fileObjLen = parseInt(fileObjOld.length) + parseInt(fileObj.length);
	        if(fileObjLen<1){
	        	hs = 1;
	        	$('.Errors').show();
        		$('.Errors').find('span').html('凭证不能为空')
        		return;
	        }
	        if(fileObjLen > 5){
	        	hs = 1;
	        	$('.Errors').show();
        		$('.Errors').find('span').html('凭证最多上传5张')
	        	return;
	        }else if(fileObjLen < 6){
	        	hs = 0;
	        	isok = false;
	        	$('.pro01').hide();	        	
	        } 
	        //console.log(fileObj);
	      //获取图片信息
	    	var fileValue01 = JSON.stringify(upload01.getNewFile());
	    	var fileValue02 = JSON.stringify(upload01.getOldFile());
	    	
	    	//var orderId = $()
	        
	        //parm += "&file="+fileValue;
	        parm = {
	        	'depositBackImageH':fileValue01,
	        	'depositBackImageOld':fileValue02,
	        	'orderId':code,
	        	'type':type
	        };
	        $.ajax({ 
	        	url:g_requestContextPath+"/h/l/orders/saveOrdersImg",
	        	dataType:"json",
	        	type:"post",
	        	async:true,
	        	data:parm,
	        	success:function(data){
	        		$('.Errors').show();
	        		$('.Errors').find('span').html('凭证上传成功');
	        		orderChg01();
	        	},
	        	error:function(){
	        		isok = true;
	        	}
	        });
		}
	})
	//点击确定弹框隐藏02
	$('#proBtn02').click(function(){
		if(isok){
			var parm = '';
			//console.log(parm);
			var fileObj = upload02.getNewFile();
			var fileObjOld = upload02.getOldFile();
	        var fileLen = parseInt(fileObjOld.length) + parseInt(fileObj.length);
	        if(fileLen<1){
	        	hs = 1;
	        	$('.Errors').show();
        		$('.Errors').find('span').html('凭证不能为空')
        		return;
	        }
	        if(fileLen > 5){
	        	hs = 1;
	        	$('.Errors').show();
        		$('.Errors').find('span').html('凭证最多上传5张')
	        	return;
	        }else if(fileLen < 6){
	        	hs = 0;
	        	isok = false;
	        	$('.pro02').hide();
	        } 
	        //console.log(fileObj);
	      //获取图片信息
	    	var fileValue01 = JSON.stringify(upload02.getNewFile());
	    	var fileValue02 = JSON.stringify(upload02.getOldFile());
	    	
	    	//var orderId = $()
	        //console.log(fileValue01);
	        //parm += "&file="+fileValue;
	        parm = {
	        	'depositBackImageH':fileValue01,
	        	'depositBackImageOld':fileValue02,
	        	'orderId':code,
	        	'type':type
	        };
	       // console.log(parm);
	        $.ajax({ 
	        	url:g_requestContextPath+"/h/l/orders/saveOrdersImg",
	        	dataType:"json",
	        	type:"post",
	        	async:true,
	        	data:parm,
	        	success:function(data){
	        		$('.Errors').show();
	        		$('.Errors').find('span').html('凭证上传成功');
	        		orderChg02();
	        		
	        	},
	        	error:function(){
	        		isok = true;
	        	}
	        });
		}
	})
	
	
	//点击确定弹框隐藏03 上传三证
	$('#proBtn03').click(function(){
		if(isok){
			var parm = '';
			var fileObj = upload03.getNewFile();
			var fileObjOld = upload03.getOldFile();
	        
	        var fileObjLen = parseInt(fileObjOld.length) + parseInt(fileObj.length);
	         if(fileObjLen<1){
	        	hs = 1;
	        	$('.Errors').show();
        		$('.Errors').find('span').html('凭证不能为空')
        		return;
	        }
	        if(fileObjLen > 3){
	        	hs = 1;
	        	$('.Errors').show();
        		$('.Errors').find('span').html('凭证最多上传3张')
	        	return;
	        }else if(fileObjLen < 4){
	        	hs = 0;
	        	isok = false;
	        	$('.pro03').hide();	        	
	        } 
	        //console.log(fileObj);
	      //获取图片信息
	    	var fileValue01 = JSON.stringify(upload03.getNewFile());
	    	var fileValue02 = JSON.stringify(upload03.getOldFile());
	    	
	    	//var orderId = $()
	        
	        //parm += "&file="+fileValue;
	        parm = {
	        	'depositBackImageH':fileValue01,
	        	'depositBackImageOld':fileValue02,
	        	'orderId':code,
	        	'type':4
	        };
	        $.ajax({ 
	        	url:g_requestContextPath+"/h/l/orders/saveOrdersImg",
	        	dataType:"json",
	        	type:"post",
	        	async:true,
	        	data:parm,
	        	success:function(data){
	        		$('.Errors').show();
	        		$('.Errors').find('span').html('凭证上传成功');
	        		//orderChg01();
	        		getInfo()
	        	},
	        	error:function(){
	        		isok = true;
	        	}
	        });
		}
	})
	
	
	
	
	//点击提示框确定按钮
	$('.delBtn').click(function(){
		if(hs == '0'){
			$('.boom').hide();
			$('.Errors').hide();
		}else if(hs == '1'){
			$('.Errors').hide();
			hs = 0;
			return;
		}
	})
	//点击order-deleat按钮
	$('.order-deleat').click(function(){
		$('.boom').hide();
		$('.pro01').hide();
		$('.pro02').hide();
		$('.pro03').hide();
	})
	//遮罩层
	$('.boom').css({
		'width':$(window).width(),
		'height':$(window).height()
	})
	//取消订单保存按钮
	$(".order_affirm").click(function(){
		var calcelReason = $("#cancelOrderReason").val();
		if (calcelReason == "" || calcelReason ==null) {
			alert("请选择取消理由！");
		}else{
			$.post(g_requestContextPath + "/h/l/orders/cancelOrder",
					{"orderId":$("#cancelOrderId").val(),"cancelReason":calcelReason},
					function(data){
						//console.log(data);
						if(data == 1){
							//取消申请已提交
							getPageInfo();
						}else{
							//错误
						}
					}
			);
			$(".abolish_order").hide();
		}
	})
	//确认收车保存按钮
	$(".affirm").click(function(){
		changeOrdersStatus($("#orderId").val(),200,null);
		$(".popup").hide();
	})
	
	//图片前缀
	$.getJSON(g_requestContextPath + "/getShowAddress",function(data){
		prefix = data;
	})
	
    //初始化订单列表
    getInfo();
    //点击获取不同状态的列表
    $('#indent_mn li').click(function(){
    	$(this).addClass('active').siblings().removeClass('active');
    	//console.log($(this).attr('state'));
    	state = $(this).attr('state');
    	stateAfter = state;
    	pageNumber = 1;
    	searchTitle = $(".search").val() == "" ? null : searchTitle = $(".search").val();
    	getInfo();
    })
    
    //搜索
    $(".hunt").click(function(){
    	searchTitle = $(".search").val() == "" ? null : searchTitle = $(".search").val();
    	state = stateAfter;
    	pageNumber = 1;
 		getInfo();
 	})
 	
	//分页
	$('.page').click(function(e){
		 var e = e || window.event;
         var target = e.target || e.srcElement;
         //console.log(target.id)
         if(target.id == "preBtn"){
        	 var id = $('.current').attr('id');
             id = id - 1;
             if(id < 1){
                 id = 1;
             }
             pageNumber = id;
             //console.log(pageNumber)
             $('.ctPage ul li[id='+id+']').addClass('current').siblings().removeClass('current');
             if(length > 5 && id > 2 && id < length - 1){
                 $('.ctPage ul').css({
                     'left': -39 * (id - 3) + 'px'
                 })
             }
            getPageInfo();
         }else if(target.id == "nextBtn"){
        	 var id = $('.current').attr('id');
             id = parseInt(id) + 1;
             if(id > length){
                 id = length;
             }
             pageNumber = id;
             //console.log(pageNumber)
             $('.ctPage ul li[id='+id+']').addClass('current').siblings().removeClass('current');
             if(length > 5 && id > 3 && id < length - 1){
                 $('.ctPage ul').css({
                     'left': -39 * (id - 3) + 'px'
                 })
             }
              
            getPageInfo();
            
         }else if(target.id == ''){
        	 return;
         }else{
        	 var id = target.id;
             pageNumber = id;
            // console.log(pageNumber) 
             $('.page ul li[id="'+ id +'"]').addClass('current').siblings().removeClass('current');
             if(length > 5 && id > 3 && id < length - 2){
                 $('.ctPage ul').css({
                     'left': -39 * (id - 3) + 'px'
                 })
             }else if(length > 5 && id < 4){
                 $('.ctPage ul').css({
                     'left': 0
                 })
             }else if(length > 5 && id > length - 3){
                 $('.ctPage ul').css({
                     "left":-39 * (length - 5) + 'px'
                 })
             }
            getPageInfo();
         }
	})
	
})

//修改订单状态
function changeOrdersStatus(id,status,oldStatus){
	$.post(g_requestContextPath + "/h/l/orders/orderChg",
		{"id":id,"status":status,"oldStatus":oldStatus,"orderPayStatus":null},
		function(data){
			if(data==1){
				getPageInfo();
			}else{
				//错误信息
			}
		}
	);
}


function orderChg01(){
	 var parm = {
		'id':code,
		'orderPayStatus':'10',
		'oldStatus':null,
		'status':'102'
	 }
	 $.ajax({ 
     	url:g_requestContextPath+"/h/l/orders/orderChg",
     	dataType:"json",
     	type:"post",
     	async:true,
     	data:parm,
     	success:function(data){
    		getInfo();
     	},
     	error:function(){
     		 //
     	}
     });
}

function orderChg02(){
	 var parm = {
		'id':code,
		'orderPayStatus':'11',
		'oldStatus':null,
		'status':'105'
	 }
	 $.ajax({ 
    	url:g_requestContextPath+"/h/l/orders/orderChg",
    	dataType:"json",
    	type:"post",
    	async:true,
    	data:parm,
    	success:function(data){
    		getInfo();
    	},
    	error:function(){
    		 //
    	}
    });
}

//动态插入页码
function pageChange(page){
     $('.ctPage ul').html('');
     $('.ctPage ul').css({
         'left': "0"
     })
     for(var i = 1;i < page;i++){
         $('.ctPage ul').append('<li id="'+i+'">'+i+'</li>')
     }
     $('.ctPage ul li:eq(0)').addClass('current'); 
     length = $('.ctPage ul li').length;
     var wid = length * 40 - 10;
     $('.ctPage ul').css({'width':wid + 'px'})
 }

//初始化获取数据函数
function getInfo(){
	//console.log(pageNumber)
	$.ajax({
		url: g_requestContextPath + "/h/l/orders/myOrders",
		type: "post",
		data:{
			"page":pageNumber,
			"rows":pageSize,
			"searchTitle":searchTitle,
			"state":state
		},
		success:function(data){
			//console.log(data);
			$('.car_indent_deta').html('');
			var total = data.total;
			if(total == '0'){
				$('.order_ser').show();
				$('.indent_con').hide();
			}else{
				$('.order_ser').hide();
				$('.indent_con').show();
			}
			
			if(total > 5){
				$('.page').show();
			}else{
				$('.page').hide();
			}
			var pageNum = Math.ceil(total/pageSize);
			pageChange(pageNum+1);
			var carIndent = '';
			for(var i=0;i<data.rows.length;i++){
				carIndent+= '<a target="_blank" href="'+ g_requestContextPath + '/h/l/orders/orderDetial/' + data.rows[i].id + '">';
				carIndent+= 	'<div class="indent_deta" id="'+data.rows[i].id +'">';
				carIndent+=				'<div class="code"><span>'+ new Date(data.rows[i].createDate).format("yyyy-MM-dd hh:mm:ss") +'</span>订单号码&nbsp;<span name="code">'+data.rows[i].id+'</span></div>';
				carIndent+=				'<ul class="cl" name="cl">';
				carIndent+=					'<li class="fl cl">';
				carIndent+=						'<div class="fl img"><img src="'+prefix+''+data.rows[i].productImage.filePath+'" width="170" height="119"/></div>';
				carIndent+=						'<div class="fl mat">';
				carIndent+=							'<div id="name_wapper">'+ data.rows[i].product.title +'</div>';
				carIndent+=							'<div>麦卡价：￥'+thousandBitSeparator(parseInt(data.rows[i].carPrice))+'</div>';
				carIndent+=							'<div class="cft">车身 '+data.rows[i].product.outColor+'  内饰 '+data.rows[i].product.inColor+'</div>';
				carIndent+=							'<div class="wapper">';
				carIndent+=								'<span class="fls"><h5 class="address">'+data.rows[i].product.country+'</h5></span>';
				carIndent+=								'<span class="frs"><h5 class="now">' + change(data.rows[i].product.position) + '</h5></span>';
				carIndent+=							'</div>';
				carIndent+=						'</div>';
				carIndent+=					'</li>';
				carIndent+=					'<input type="hidden" name="status"  value="'+ (data.rows[i].status)+'">';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].product.mustConfigurePrice + data.rows[i].premiums ))+'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].configurePrice))+'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].amount))+'</li>';
				carIndent+=					'<li class="fl status" name="status">'+getOrderState(data.rows[i].status)+'</li>';
				carIndent+=                 '</a>';
				carIndent+=					'<li class="fl flex">';
				if(data.rows[i].status == 101 || data.rows[i].status == 104|| data.rows[i].status == 402 || data.rows[i].status == 403 ){
					carIndent+=		'<span class="take_car upload" id="'+ data.rows[i].status +'" data-id="'+data.rows[i].id+'">上传凭证</span>';
				}
				if(data.rows[i].payStatus == 11  ||  data.rows[i].payStatus == 10){
					carIndent+=		'<span class="take_car check" id="'+ data.rows[i].status +'" data-id="'+data.rows[i].id+'">查看凭证</span>';	
				}
				if(data.rows[i].status == 107){
					carIndent+=		'<span class="take_car cancel" id="'+ data.rows[i].status +'" data-id="'+data.rows[i].id+'">确认收车</span>';
				}
			
					//carIndent+=						'<span class="take_car confirm" id="'+ data.rows[i].status +'" data-id="'+data.rows[i].id+'" data-oldstatus="'+data.rows[i].status+'">取消订单</span>';
				
				carIndent+=					'</li>';
				carIndent+=				'</ul>';
				carIndent+=			'</div>';
			}
				
				$(".car_indent_deta").html(carIndent);
				/*for(var i=0;i<arr01.length;i++){
					$('.cancel[id="'+ arr01[i] +'"]').hide();
				}
				for(var i=0;i<arr02.length;i++){
					$('.confirm[id="'+ arr02[i] +'"]').hide();
				}
				for(var i=0;i<arr03.length;i++){
					$('.upload[id='+ arr03[i] +']').hide();
				 };
				 for(var i=0;i<arr04.length;i++){
					$('.check[id='+ arr04[i] +']').hide();
				 };*/
				
				
				//点击上传三证
				$('.certificate').click(function(){
					//订单号
					//code = $(this).parents('.indent_deta').find('span[name="code"]').html();
					alert("---");
					//获取已有的三证图片
					isok = true;
					uploadDepositFigure=new Array();
			 		$.ajax({
		       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
		       			type:'post',
		       			data:{"orderId":code,"type":4},
		       			success:function(data){
		       				console.log(data,"data");
		       				$.each(data,function(k,v){
		       					if(v.name==""){
		       						upload03.init("image");
		       						uploadDepositFigure.push();
		       					}else{
		       						uploadDepositFigure.push({filePath:prefix,name:v.filePath,preId:v.fileName});
		       					}
		       				});
		       				//$("#depositBackImageH").val(JSON.stringify(uploadDepositFigure));//重新赋予多图路径
		       				upload03.init("image",uploadDepositFigure);
		       			}
		       		});
			 		$('.boom').show();
			 		$('.pro03').show();
					
				});
				
				
				//点击上传凭证
				$('.upload').click(function(){
						//订单号
						code = $(this).parents('.indent_deta').find('span[name="code"]').html();
						//订单状态
						//type = $(this).parents('.indent_deta').find('input[name="status"]').val();
						type = $(this).parents('.indent_deta').find('li[name="status"]').html();
						console.log(code,"code");
						console.log(type)
						type = typeChange(type);
						console.log(type,"type");
						//console.log(type)
						isok = true;
						uploadDepositFigure=new Array();
						//获取图片(查看已经上传的支付凭证)
			       		$.ajax({
			       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
			       			type:'post',
			       			data:{"orderId":code,"type":type},
			       			success:function(data){
			       				console.log(data);
			       				$.each(data,function(k,v){
			       					if(v.name==""){
			       						upload01.init("image");
			       						upload02.init("image");
			       						uploadDepositFigure.push();
			       					}else{
			       						uploadDepositFigure.push({filePath:prefix,name:v.filePath,preId:v.fileName});
			       					}
			       				});
			       				//$("#depositBackImageH").val(JSON.stringify(uploadDepositFigure));//重新赋予多图路径
			       				if(type == '0'){
			       					upload01.init("image",uploadDepositFigure);
			       				}else if(type == '1'){
			       					upload02.init("image",uploadDepositFigure);
			       				}
			       			}
			       		});
			       		
			       		$('.boom').show();
						//console.log(type)
						if(type == '0'){
							$('.pro01').show();
						}else if(type == '1'){
							$('.pro02').show();
						}
						
					})
					//点击查看凭证
			$('.check').click(function(){
				$('.check_pro').css({'height':'340px'});
				$('.tk').hide();
				var imgArr01 = [];
				var imgArr02 = [];
				$('.check01').html('');
				$('.check02').html('');
				$('.check_pro').show();
				$('.boom').show();
				code = $(this).parents('.indent_deta').find('span[name="code"]').html();
				//type = $(this).parents('.indent_deta').find('input[name="status"]').val();
				type = $(this).parents('.list_info').find('li[name="state"]').html();
				type = typeChange02(type);
				var parm = {
						"orderId":code,
						"type":type
				};
				$.ajax({ 
			    	url:g_requestContextPath+"/h/l/orders/getOrdersImages",
			    	dataType:"json",
			    	type:"post",
			    	async:true,
			    	data:parm,
			    	success:function(data){
			    		//console.log(data)
		    			for(var i = 0;i < data.length;i++){
		    				//console.log(prefix +''+ data[0].filePath);
		    				if(data[i].type == '0'){
		    					imgArr01.push(prefix + data[i].filePath+"");
		    					$('.check01').append('<li><a target="_blank" href="'+ g_requestContextPath+'/h/l/orders/picDetails?id=0"><img src="'+ prefix + data[i].filePath +'"></a></li>')
		    				}else if(data[i].type == '1'){
		    					imgArr02.push(prefix + data[i].filePath+"");
		    					$('.check02').append('<li><a target="_blank" href="'+ g_requestContextPath+'/h/l/orders/picDetails?id=1"><img src="'+ prefix + data[i].filePath +'"></a></li>')
		    				}
		    				 
		    			}	
		    			if(imgArr02.length == '0'){
		    				$('.check_pro').css({'height':'160px'});
		    				$('.tk').hide();
		    				$('.wk').hide();
		    				$('.dj').show();
		    			}else{
		    				$('.check_pro').css({'height':'340px'});
		    				$('.tk').hide();
		    				$('.dj').show();
		    				$('.wk').show();
		    			}
		    			localStorage.setItem('img01',imgArr01);
		    			localStorage.setItem('img02',imgArr02);
			    	},
			    	error:function(){
			    		//
			    	}
			    });
			})
				 
				$(".take_car").click(function(){
					if($(this).text() == "确认收车"){
						$("#orderId").val($(this).data("id"));
						$(".popup").show();
					}else if($(this).text() == "取消订单"){
						$("#cancelOrderId").val($(this).data("id"));
						$("#cancelOrderOldStatus").val($(this).data("oldstatus"));
						$("#cancelOrderReason option:eq(0)").prop("selected",true);
						$(".abolish_order").show();
					}
				});
				
				$(".abolish").click(function(){
					$(".popup").hide();
				})
				
				$(".order_abolish").click(function(){
					$(".abolish_order").hide();
				})
				
			
			
		},
		error:function(data){
			//报错信息
		}
	})
}



//翻页获取数据函数
function getPageInfo(){
	//console.log(pageNumber)
	$.ajax({
		url: g_requestContextPath + "/h/l/orders/myOrders",
		type: "post",
		data:{
			"page":pageNumber,
			"rows":pageSize,
			"searchTitle":searchTitle,
			"state":state
		},
		success:function(data){
			//console.log(data)
			$('.car_indent_deta').html('');
			//var pageNum = Math.ceil(row.length/pageSize);
			//pageChange(pageNum+1);
			var carIndent = '';
			for(var i=0;i<data.rows.length;i++){
				carIndent+= '<a target="_blank" href="'+ g_requestContextPath + '/h/l/orders/orderDetial/' + data.rows[i].id + '">';
				carIndent+= 	'<div class="indent_deta">';
				carIndent+=				'<div class="code"><span>'+new Date(data.rows[i].createDate).format("yyyy-MM-dd hh:mm:ss")+'</span>订单号码&nbsp; <span>'+data.rows[i].id+'</span></div>';
				carIndent+=				'<ul class="cl">';
				carIndent+=					'<li class="fl cl">';
				carIndent+=						'<div class="fl img"><img src="'+prefix+''+data.rows[i].productImage.filePath+'" width="170" height="119"/></div>';
				carIndent+=						'<div class="fl mat">';
				carIndent+=							'<div id="name_wapper">'+ data.rows[i].product.title + '</div>';
				carIndent+=							'<div>麦卡价：￥'+thousandBitSeparator(parseInt(data.rows[i].carPrice))+'</div>';
				carIndent+=							'<div class="cft">车身 '+data.rows[i].product.outColor+'  内饰 '+data.rows[i].product.inColor+'</div>';
				carIndent+=							'<div class="wapper">';
				carIndent+=								'<span class="fls"><h5 class="address">'+data.rows[i].product.country+'</h5></span>';
				carIndent+=								'<span class="frs"><h5 class="now">' + change(data.rows[i].product.position) + '</h5></span>';
				carIndent+=							'</div>';
				carIndent+=						'</div>';
				carIndent+=					'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].product.mustConfigurePrice + data.rows[i].premiums))+'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].configurePrice))+'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].amount))+'</li>';
				carIndent+=					'<li class="fl status" name="status">'+getOrderState(data.rows[i].status)+'</li>';
				carIndent+=                 '</a>';
				carIndent+=					'<li class="fl flex">';
				if(data.rows[i].status == 101 || data.rows[i].status == 104|| data.rows[i].status == 402 || data.rows[i].status == 403 ){
					carIndent+=		'<span class="take_car upload" id="'+ data.rows[i].status +'" data-id="'+data.rows[i].id+'">上传凭证</span>';
				}
				if(data.rows[i].payStatus == 11  ||  data.rows[i].payStatus == 10){
					carIndent+=		'<span class="take_car check" id="'+ data.rows[i].status +'" data-id="'+data.rows[i].id+'">查看凭证</span>';	
				}
				if(data.rows[i].status == 107){
					carIndent+=		'<span class="take_car cancel" id="'+ data.rows[i].status +'" data-id="'+data.rows[i].id+'">确认收车</span>';
				}
			
					//carIndent+=						'<span class="take_car confirm" id="'+ data.rows[i].status +'" data-id="'+data.rows[i].id+'" data-oldstatus="'+data.rows[i].status+'">取消订单</span>';
				
				carIndent+=					'</li>';
				carIndent+=				'</ul>';
				carIndent+=			'</div>';
			};	
				
			
				$(".car_indent_deta").html(carIndent);
				for(var i=0;i<arr01.length;i++){
					$('.cancel[id="'+ arr01[i] +'"]').hide();
				}
				for(var i=0;i<arr02.length;i++){
					$('.confirm[id="'+ arr02[i] +'"]').hide();
				}
				for(var i=0;i<arr03.length;i++){
					 $('.upload[id='+ arr03[i] +']').hide();
				 };
				 for(var i=0;i<arr04.length;i++){
					 $('.check[id='+ arr04[i] +']').hide();
				 };
				//点击上传凭证
					$('.upload').click(function(){
						//订单号
						code = $(this).parents('.indent_deta').find('span[name="code"]').html();	
						//订单状态
						type = $(this).parents('.indent_deta').find('li[name="status"]').html();
						//console.log(type)
						type = typeChange(type);
						//console.log(type)
						isok = true;
						uploadDepositFigure=new Array();
						//获取图片(查看已经上传的支付凭证)
			       		$.ajax({
			       			url:g_requestContextPath + "",
			       			type:'post',
			       			data:{"orderId":code,"type":type},
			       			success:function(data){
			       				//console.log(data);
			       				$.each(data,function(k,v){
			       					if(v.name==""){
			       						upload01.init("image");
			       						upload02.init("image");
			       						uploadDepositFigure.push();
			       					}else{
			       						uploadDepositFigure.push({filePath:prefix,name:v.filePath,preId:v.fileName});
			       					}
			       				});
			       				//$("#depositBackImageH").val(JSON.stringify(uploadDepositFigure));//重新赋予多图路径
			       				if(type == '0'){
			       					upload01.init("image",uploadDepositFigure);
			       				}else if(type == '1'){
			       					upload02.init("image",uploadDepositFigure);
			       				}
			       			}
			       		});
			       		
			       		$('.boom').show();
						//console.log(type)
						if(type == '0'){
							$('.pro01').show();
						}else if(type == '1'){
							$('.pro02').show();
						}
						
					})
				 		//点击查看凭证
			$('.check').click(function(){
				$('.check_pro').css({'height':'340px'});
				$('.tk').hide();
				var imgArr01 = [];
				var imgArr02 = [];
				$('.check01').html('');
				$('.check02').html('');
				$('.check_pro').show();
				$('.boom').show();
				code = $(this).parents('.list_info').find('li[name="code"]').html();
				type = $(this).parents('.list_info').find('li[name="state"]').html();
				type = typeChange02(type);
				var parm = {
						"orderId":code,
						"type":type
				};
				$.ajax({ 
			    	url:g_requestContextPath+"/h/l/orders/getOrdersImages",
			    	dataType:"json",
			    	type:"post",
			    	async:true,
			    	data:parm,
			    	success:function(data){
			    		//console.log(data)
		    			for(var i = 0;i < data.length;i++){
		    				//console.log(prefix +''+ data[0].filePath);
		    				if(data[i].type == '0'){
		    					imgArr01.push(prefix + data[i].filePath+"");
		    					$('.check01').append('<li><a target="_blank" href="'+ g_requestContextPath+'/h/l/orders/picDetails?id=0"><img src="'+ prefix + data[i].filePath +'"></a></li>')
		    				}else if(data[i].type == '1'){
		    					imgArr02.push(prefix + data[i].filePath+"");
		    					$('.check02').append('<li><a target="_blank" href="'+ g_requestContextPath+'/h/l/orders/picDetails?id=1"><img src="'+ prefix + data[i].filePath +'"></a></li>')
		    				}
		    				 
		    			}	
		    			if(imgArr02.length == '0'){
		    				$('.check_pro').css({'height':'160px'});
		    				$('.tk').hide();
		    				$('.wk').hide();
		    				$('.dj').show();
		    			}else{
		    				$('.check_pro').css({'height':'340px'});
		    				$('.tk').hide();
		    				$('.dj').show();
		    				$('.wk').show();
		    			}
		    			localStorage.setItem('img01',imgArr01);
		    			localStorage.setItem('img02',imgArr02);
			    	},
			    	error:function(){
			    		//
			    	}
			    });
			})
				 
				 
				$(".take_car").click(function(){
					if($(this).text() == "确认收车"){
						$("#orderId").val($(this).data("id"));
						$(".popup").show();
					}else if($(this).text() == "取消订单"){
						$("#cancelOrderId").val($(this).data("id"));
						$("#cancelOrderOldStatus").val($(this).data("oldstatus"));
						$("#cancelOrderReason option:eq(0)").prop("selected",true);
						$(".abolish_order").show();
					}
				})
				
				$(".abolish").click(function(){
					$(".popup").hide();
				})
				
				
				$(".order_abolish").click(function(){
					$(".abolish_order").hide();
				})
			
			
		},
		error:function(data){
			//报错信息
		}
	})
}

//判断车的状态
function change(carPosition){
    switch(carPosition)
    {
    case 0:
      return "现车"
      break;
    case 1:
      return "预定车"
      break;
    case 2:
      return "在途车"
      break;
    }
}

//判断订单的状态
function getOrderState(orderStatus){
	switch(orderStatus)
    {
    	case 110:
	      return "待系统确认"
	      break;
    	case 100:
	      return "确认中"
	      break;
	    case 101:
	      return "待支付定金"
	      break;
	    case 102:
	      return "定金待审核"
	      break;
	    case 103:
	      return "待发货"
          break;
	    case 104:
	      return "待支付尾款"
          break;
	    case 105:
	      return "尾款待审核"
          break;
	    case 106:
	      return "待上传三证"
          break;
	    case 107:
	      return "等待交车"
	      break;
	    case 108:
	      return "订单取消待审核"
          break;
	    case 200:
	      return "订单完成"
          break;
	    case 401:
	      return "库存确认未通过"
          break;
	    case 402:
	      return "定金审核未通过"
          break;
	    case 403:
	      return "尾款审核未通过"
          break;
	    case 404:
	      return "订单取消"
          break;
	    case 405:
	      return "订单关闭"
          break;
	    case 109:
	      return "订单取消审核未通过"
          break;
    }
}

function typeChange(date){
	switch(date)
    {
    case "待支付定金":
      return "0"
      break;
    case "定金待审核":
        return "0"
        break;
    case "定金审核未通过":
        return "0"
        break;
    case "待支付尾款":
        return "1"
        break;
    case "尾款审核未通过":
        return "1"
        break;
    case "尾款待审核":
      return "1"
      break;
    }
}

function typeChange02(date){
	switch(date)
    {
    case "定金待审核":
      return "0"
      break;
    case "待支付尾款":
      return "0"
      break;
    default : return null
    break;
  }
}