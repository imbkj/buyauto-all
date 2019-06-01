var pageNumber = 1;
var pageSize = 5;
var state = 101;
var arr01 = [100,102,103,105,106,107,108,110,200,401,404,405];
var arr02 = [100,101,110,402,404,405];
var arr03 = [100,102,103,104,105,106,107,108,110,200,401,403,404,405];
var prefix = null;
var isok = true;
var code = null;
var type = null;
var upload01 = null;
var upload02 = null;
var uploadDepositFigure = null;
var time = [];
var timeZ = null;
var hs = 0;
$(function(){
	timeZ = (new Date()).getTime() - serverCurrentTime;
	//遮罩层
	$('.boom').css({
		'width':$(window).width(),
		'height':$(window).height()
	})
	upload01 = new imageUploads($("#imageInput01"),true);//初始化file表单框//首页
	upload01.init("image");	
	upload02 = new imageUploads($("#imageInput02"),true);//初始化file表单框//首页
	upload02.init("image");
	 //图片前缀
	$.getJSON(g_requestContextPath + "/getShowAddress",function(data){
		prefix = data;
	})
	//初始化
	getInfo();
	//切换状态更新列表
	 $('.order_state ul li').click(function(){
		 $(this).addClass('limit').siblings().removeClass('limit');
		 state = $(this).attr('id');
		 //console.log(state);
		 pageNumber = 1;
		 getInfo();
	 })
	 //翻页
	$('.page').click(function(e){
		 var e = e || window.event;
         var target = e.target || e.srcElement;
         if(target.id == "preBtn"){
        	 var id = $('.current').attr('id');
             id = id - 1;
             if(id < 1){
                 id = 1;
                 return;
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
                 return;
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
	
	
	//点击确定弹框隐藏01
	$('#proBtn01').click(function(){
		if(isok){
			var parm = '';
			var fileObj = upload01.getNewFile();
			var fileObjOld = upload01.getOldFile();
	        
	        var fileObjLen = parseInt(fileObjOld.length) + parseInt(fileObj.length);
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
	        		console.log(data)
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
	})
})


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
	$.ajax({
		url: g_requestContextPath + "/h/l/orders/getOrders",
		type: "post",
		dateType: "json",
		async: true,
		data:{
			"page":pageNumber,
			"rows":pageSize,
			"userId":"",
			"state":state
		},
		success:function(data){
			//console.log(data)
			$('#order_list').html('');
			var row = data.rows;
			var total = data.total;
			if(total == '0'){
				$('.order_ser').show();
				$('.order_list_title').hide();
			}else{
				$('.order_ser').hide();
				$('.order_list_title').show();
			}
			
			if(total > 5){
				$('.page').show();
			}else{
				$('.page').hide();
				$('.order_list').css({'marginBottom':'110px'})
			}
			var pageNum = Math.ceil(total/pageSize);
			pageChange(pageNum+1);
			var html = '';
			time = [];
			for(var i=0;i<row.length;i++){
				//console.log(row.length)
				//console.log( prefix)
				html += '<ul class="list_info">';
				html += '<li><ul class="info_time">';
				html += '<li>'+ new Date(data.rows[i].createDate).format("yyyy-MM-dd hh:mm:ss")+'</li>';
				html += '<li>订单号码：</li><li name="code">'+ row[i].id +'</li>';
				html += '<li><span name="state">' + State(row[i].status) + '</span></li></ul>';
				html += '<ul class="info">';
				html += '<a href="'+ g_requestContextPath + '/h/l/orders/orderInfo/' + row[i].id + '?id=2">';
				html += '<li><img src="'+ prefix + '' + row[i].productImage.filePath +'" alt=""></li>';
				html += '<li class="car_info"><span class="name">' + row[i].product.title +'</span>';
				html += '<span class="price">麦卡价：' + ("￥ "+thousandBitSeparator(parseInt(row[i].product.carPrice))) + '</span>';
				html += '<div><span class="addre add1"><h5>' + string(row[i].product.country) + '</h5></span>';
				html += '<span class="addre add2"><h5>' + change(row[i].product.position) + '</h5></span></div>';
				html += '<span class="Car">车身&nbsp;&nbsp;' + row[i].product.outColor  + '&nbsp;&nbsp;内饰&nbsp;&nbsp;' + row[i].product.inColor + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].deposit))) + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].amount))) + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].amount-row[i].deposit))) + '</span></li>';
				html += '<li class="inf"><span>' + row[i].customer + '</span></li>';
				html += '</a>';
				html += '<li class="now"><div>';
			if(operation){
				html += '<span class="lit upload" id="'+ row[i].status +'">上传凭证</span>';
				html += '<span class="lit check" id="'+ row[i].status +'">查看凭证</span>';
				html += '<span class="lit refund" id="'+ row[i].status +'" name="'+ row[i].payStatus +'">退款凭证</span>';
				//html += '<span class="time" id="'+ row[i].status  +'"></span>';
			}
				html += '</div></li></ul></li></ul>';
				//if(state == '101'){
					time.push(data.rows[i].createDate + 172800000); 
					//console.log(time)
					setInterval(function(){ShowCountDown(time);}, 1000);
				//} 
			}
			$('#order_list').append(html);
			
			for(var i=0;i<row.length;i++){
				if(data.rows[i].payStatus == '20' || data.rows[i].payStatus == '12'){
					$('.refund[id="'+ row[i].status +'"]').show();
				}
			}
			 //根据状态码判断上传、查看按钮显隐
			 for(var i=0;i<arr01.length;i++){
				 $('.upload[id='+ arr01[i] +']').hide();
			 };
			 for(var i=0;i<arr02.length;i++){
				 $('.check[id='+ arr02[i] +']').hide();
			 };
			 for(var i=0;i<arr03.length;i++){
				 $('.time[id='+ arr03[i] +']').hide();
			 };
			//点击上传凭证
				$('.upload').click(function(){
					code = $(this).parents('.list_info').find('li[name="code"]').html();
					type = $(this).parents('.list_info').find('span[name="state"]').html();
					//console.log(type)
					type = typeChange(type);
					//console.log(type)
					isok = true;
					uploadDepositFigure=new Array();
					//获取图片(查看已经上传的支付凭证)
		       		$.ajax({
		       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
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
				var imgArr01 = [];
				var imgArr02 = [];
				$('.check01').html('');
				$('.check02').html('');
				$('.check_pro').show();
				$('.boom').show();
				code = $(this).parents('.list_info').find('li[name="code"]').html();
				type = $(this).parents('.list_info').find('span[name="state"]').html();
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
		    					imgArr01.push(prefix + data[i].filePath);
		    					$('.check01').append('<li><a target="_blank" href="'+ g_requestContextPath+'/h/l/orders/picDetails?id=0"><img src="'+ prefix + data[i].filePath +'"></a></li>')
		    				}else if(data[i].type == '1'){
		    					imgArr02.push(prefix + data[i].filePath);
		    					$('.check02').append('<li><a target="_blank" href="'+ g_requestContextPath +'/h/l/orders/picDetails?id=1"><img src="'+ prefix + data[i].filePath +'"></a></li>')
		    				}
		    				 
		    			}	
		    			//console.log(imgArr02.length == '0');
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
			    		//console.log(imgArr01 +';'+imgArr02);
		    			localStorage.setItem('img01',imgArr01);
		    			localStorage.setItem('img02',imgArr02);
			    	},
			    	error:function(){
			    		//
			    	}
			    });
			})
			//点击查看退款凭证
			$('.refund').click(function(){
				$('.check_pro').css({'height':'160px'});
				$('.tk').css({'margin':'0'})
				$('.dj').hide();
				$('.wk').hide();
				$('.tk').show();
				var imgArr03 = [];
				$('.check03').html('');
				$('.check_pro').show();
				$('.boom').show();
				code = $(this).parents('.list_info').find('li[name="code"]').html();
				var parm = {
						"orderId":code,
						"type":'2'
				};
				$.ajax({ 
			    	url:g_requestContextPath+"/h/l/orders/getOrdersImages",
			    	dataType:"json",
			    	type:"post",
			    	async:true,
			    	data:parm,
			    	success:function(data){
		    			for(var i = 0;i < data.length;i++){
		    				imgArr03.push(prefix + data[i].filePath);
		    				$('.check03').append('<li><a target="_blank" href="'+ g_requestContextPath+'/h/l/orders/picDetails?id=2"><img src="'+ prefix + data[i].filePath +'"></a></li>') 
		    			}	
			    		//console.log(imgArr01 +';'+imgArr02);
		    			//console.log(imgArr03);
		    			localStorage.setItem('img03',imgArr03);
			    	},
			    	error:function(){
			    		//
			    	}
			    });
			})
			 
		},
		error:function(data){
			//报错信息
		}
	})
	//点击check框确定按钮
	$('.check_btn').click(function(){
		$('.boom').hide();
		$('.check_pro').hide();
	})
}

//车类型判断
function change(date){
    switch(date)
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

//状态判断
function State(date){
	switch(date)
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
	      return "取消待审核"
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
    }
}

//翻页获取数据函数
function getPageInfo(){
	//console.log(pageNumber)
	$.ajax({
		url: g_requestContextPath + "/h/l/orders/getOrders",
		type: "post",
		dateType: "json",
		async: true,
		data:{
			"page":pageNumber,
			"rows":pageSize,
			"userId":"",
			"state":state
		},
		success:function(data){
			//console.log(data)
			$('#order_list').html('');
			var row = data.rows;
			var html = '';
			time = [];
			for(var i=0;i<row.length;i++){
				html += '<ul class="list_info">';
				html += '<li><ul class="info_time">';
				html += '<li>'+ new Date(data.rows[i].createDate).format("yyyy-MM-dd hh:mm:ss")+'</li>';
				html += '<li>订单号码：</li><li name="code">'+ row[i].id +'</li>';
				html += '<li><span name="state">' + State(row[i].status) + '</span></li></ul>';
				html += '<ul class="info">';
				html += '<a href="'+ g_requestContextPath + '/h/l/orders/orderInfo/' + row[i].id + '?id=2">';
				html += '<li><img src="'+ prefix + row[i].productImage.filePath +'" alt=""></li>';
				html += '<li class="car_info"><span class="name">' + row[i].product.title +'</span>';
				html += '<span class="price">麦卡价：' + ("￥ "+thousandBitSeparator(parseInt(row[i].product.carPrice))) + '</span>';
				html += '<div><span class="addre add1"><h5>' + string(row[i].product.country) + '</h5></span>';
				html += '<span class="addre add2"><h5>' + change(row[i].product.position) + '</h5></span></div>';
				html += '<span class="Car">车身&nbsp;&nbsp;' + row[i].product.outColor  + '&nbsp;&nbsp;内饰&nbsp;&nbsp;' + row[i].product.inColor + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].deposit))) + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].amount))) + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].amount-row[i].deposit))) + '</span></li>';
				html += '<li class="inf"><span>' + row[i].customer + '</span></li>';
				html += '</a>'
				html += '<li class="now"><div>';
				if(operation){
					html += '<span class="lit upload" id="'+ row[i].status +'">上传凭证</span>';
					html += '<span class="lit check" id="'+ row[i].status +'">查看凭证</span>';
					html += '<span class="lit refund" id="'+ row[i].status +'">退款凭证</span>';
					//html += '<span class="time" id="'+ row[i].status +'"></span>';
				}
				html += '</div></li></ul></li></ul>';
				//if(state == '101'){
					time.push(data.rows[i].createDate + 172800000); 
					//console.log(time)
					setInterval(function(){ShowCountDown(time);}, 1000);
				//} 
			}
			//console.log(html);
			$('#order_list').append(html);
			 //根据状态码判断上传、查看按钮显隐
			for(var i=0;i<row.length;i++){
				if(data.rows[i].payStatus == '20' || data.rows[i].payStatus == '12'){
					$('.refund[id="'+ row[i].status +'"]').show();
				}
			}
			 for(var i=0;i<arr01.length;i++){
				 $('.upload[id='+ arr01[i] +']').hide();
			 };
			 for(var i=0;i<arr02.length;i++){
				 $('.check[id='+ arr02[i] +']').hide();
			 };
			 for(var i=0;i<arr03.length;i++){
				 $('.time[id='+ arr03[i] +']').hide();
			 };
			 
			//点击上传凭证
				$('.upload').click(function(){
					
					code = $(this).parents('.list_info').find('li[name="code"]').html();
					type = $(this).parents('.list_info').find('span[name="state"]').html();
					type = typeChange(type);
					isok = true;
					uploadDepositFigure=new Array();
					//获取图片(查看已经上传的支付凭证)
		       		$.ajax({
		       			url:g_requestContextPath + "/h/l/orders/getOrdersImages",
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
					
					if(type == '0'){
						$('.pro01').show();
					}else if(type == '1'){
						$('.pro02').show();
					}
				});
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
				type = $(this).parents('.list_info').find('span[name="state"]').html();
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
			//点击查看退款凭证
			$('.refund').click(function(){
				$('.check_pro').css({'height':'160px'});
				$('.tk').css({'margin':'0'})
				$('.dj').hide();
				$('.wk').hide();
				$('.tk').show();
				var imgArr03 = [];
				$('.check03').html('');
				$('.check_pro').show();
				$('.boom').show();
				code = $(this).parents('.list_info').find('li[name="code"]').html();
				var parm = {
						"orderId":code,
						"type":'2'
				};
				$.ajax({ 
			    	url:g_requestContextPath+"/h/l/orders/getOrdersImages",
			    	dataType:"json",
			    	type:"post",
			    	async:true,
			    	data:parm,
			    	success:function(data){
		    			for(var i = 0;i < data.length;i++){
		    				imgArr03.push(prefix + data[i].filePath);
		    				$('.check03').append('<li><a target="_blank" href="'+ g_requestContextPath+'/h/l/orders/picDetails?id=2"><img src="'+ prefix + data[i].filePath +'"></a></li>') 
		    			}	
			    		//console.log(imgArr01 +';'+imgArr02);
		    			console.log(imgArr03);
		    			localStorage.setItem('img03',imgArr03);
			    	},
			    	error:function(){
			    		//
			    	}
			    });
			})
		},
		error:function(data){
			//报错信息
		}
	})
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
    case "尾款审核未通过":
        return "1"
        break;
    case "待支付尾款":
      return "1"
      break;
    case "尾款待审核":
	    return "0"
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

function ShowCountDown(endDate) {
	for(var i = 0;i < endDate.length;i++){
		var now = new Date();  
		var leftTime=endDate[i]-now.getTime() - timeZ;  
		var leftsecond = parseInt(leftTime/1000);  
		var hour=Math.floor(leftsecond/3600); 
		var minute=Math.floor((leftsecond-hour*3600)/60); 
		var second=Math.floor(leftsecond-hour*3600-minute*60); 
		$('.time:eq('+ i +')').html(checkTime(hour)+":"+checkTime(minute)+":"+checkTime(second));
		if(endDate[i]-now.getTime()<0){
			$('.time:eq('+ i +')').hide();
		}
	}
} 

function checkTime(i){ //将0-9的数字前面加上0，例1变为01 
	 if(i<10) 
	 { 
	  i = "0" + i; 
	 } 
	 return i; 
	} 


function string(data){
	if(data.length < 7){
		return data;
	}else{
		return data.substr(0,6)
	}
}
 