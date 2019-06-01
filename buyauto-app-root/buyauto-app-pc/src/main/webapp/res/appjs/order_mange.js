var pageNumber = 1;
var pageSize = 5;
var state = "";
var userId = '';
var prefix = null;
var arr03 = [100,102,103,104,105,106,107,108,110,200,401,402,403,404,405];
var time = [];
$(function(){
	 //图片前缀
	$.getJSON(g_requestContextPath + "/getShowAddress",function(data){
		prefix = data;
	})
	//初始化获取销售人员ID
    getId();
    //初始化订单列表
    getInfo();
    //点击获取不同状态的列表
    $('#indent_mn li').click(function(){
    	$(this).addClass('active').siblings().removeClass('active');
    	//console.log($(this).attr('state'));
    	state = $(this).attr('state');
    	pageNumber = 1;
    	getInfo();
    })
    //根据员工ID获取列表
    $('.Btn').click(function(){
    	var option=$("#test option:selected");
    	//console.log(option.attr('id'));
    	userId = option.attr('id');
    	getInfo();
    })
	 //翻页
	$('.page').click(function(e){
		 var e = e || window.event;
         var target = e.target || e.srcElement;
         //console.log(target.id)
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

//获取销售人员ID
function getId(){
	$.ajax({
		url: g_requestContextPath + "/h/l/orders/getUsersByCompanyId",
		type: "get",
		dateType: "json",
		async: true,
		success:function(data){
			//console.log(data)
			for(var i = 0;i < data.length;i++){
				$('.sel select').append('<option id="'+ data[i].id +'">'+ data[i].name +'</option>');
			}
		},
		error:function(data){
			//报错信息
		}
	})
}
//初始化获取数据函数
function getInfo(){
	//console.log(pageNumber)
	$.ajax({
		url: g_requestContextPath + "/h/l/orders/getOrders",
		type: "post",
		dateType: "json",
		async: true,
		data:{
			"page":pageNumber,
			"rows":pageSize,
			"userId":userId,
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
				html += '<a href="'+ g_requestContextPath + '/h/l/orders/orderInfo/' + row[i].id + '?id=1">';
				html += '<ul class="list_info" id="'+ row[i].id +'">';
				html += '<li><ul class="info_time">';
				html += '<li>'+ new Date(data.rows[i].createDate).format("yyyy-MM-dd hh:mm:ss") +'</li>';
				html += '<li>订单号码：'+ row[i].id +'</li></ul>';
				html += '<ul class="info">';
				html += '<li><img src="'+ prefix + row[i].productImage.filePath +'" alt=""></li>';
				html += '<li><span class="name">' + row[i].product.title +'</span>';
				html += '<span class="price">麦卡价：' + ("￥ "+thousandBitSeparator(parseInt(row[i].product.carPrice))) + '</span>';
				html += '<div><span class="addre add1"><h5>' + string(row[i].product.country) + '</h5></span>';
				html += '<span class="addre add2"><h5>' + change(row[i].product.position) + '</h5></span></div>';
				html += '<span class="Car">车身&nbsp;&nbsp;' + row[i].product.outColor  + '&nbsp;&nbsp;内饰&nbsp;&nbsp;' + row[i].product.inColor + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].deposit))) + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].amount))) + '</span></li>';
				html += '<li class="inf"><span>' + row[i].customer + '</span></li>';
				html += '<li class="now"><div><span>' + State(row[i].status) + '</span>';
				html += '<span class="time" id="'+ row[i].status  +'"></span>';
				html += '</div></li></ul></li></ul>';
				html += '</a>';
				//if(state == '101'){
					time.push(data.rows[i].createDate + 172800000); 
					//console.log(time)
					setInterval(function(){ShowCountDown(time);}, 1000);
				//} 
			}
			//console.log(html);
			$('#order_list').append(html);
			 for(var i=0;i<arr03.length;i++){
				 $('.time[id='+ arr03[i] +']').hide();
			 };
		},
		error:function(data){
			//报错信息
		}
	})
}

//判断车的状态
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
//判断订单的状态
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
    }
}

//翻页获取数据函数
function getPageInfo(){
	//console.log(pageNumber)
	$.ajax({
		url: g_requestContextPath + "/h/l/orders/getOrders",
		type: "post",
		dateType: "json",
		async: false,
		data:{
			"page":pageNumber,
			"rows":pageSize,
			"userId":userId,
			"state":state
		},
		success:function(data){
			//console.log(data)
			$('#order_list').html('');
			var row = data.rows;
			//var pageNum = Math.ceil(row.length/pageSize);
			//pageChange(pageNum+1);
			var html = '';
			time = [];
			for(var i=0;i<row.length;i++){
				html += '<a href="'+ g_requestContextPath + '/h/l/orders/orderInfo/' + row[i].id + '?id=1">';
				html += '<ul class="list_info">';
				html += '<li><ul class="info_time">';
				html += '<li>'+ new Date(data.rows[i].createDate).format("yyyy-MM-dd hh:mm:ss") +'</li>';
				html += '<li>订单号码：'+ row[i].id +'</li></ul>';
				html += '<ul class="info">';
				html += '<li><img src="'+ prefix + row[i].productImage.filePath +'" alt=""></li>';
				html += '<li><span class="name">'+ row[i].product.title +'</span>';
				html += '<span class="price">麦卡价：' + ("￥ "+thousandBitSeparator(parseInt(row[i].product.carPrice))) + '</span>';
				html += '<div><span class="addre add1"><h5>' + string(row[i].product.country) + '</h5></span>';
				html += '<span class="addre add2"><h5>' + change(row[i].product.position) + '</h5></span></div>';
				html += '<span class="Car">车身&nbsp;&nbsp;' + row[i].product.outColor  + '&nbsp;&nbsp;内饰&nbsp;&nbsp;' + row[i].product.inColor + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].deposit))) + '</span></li>';
				html += '<li class="inf"><span>' + ("￥ "+thousandBitSeparator(parseInt(row[i].amount))) + '</span></li>';
				html += '<li class="inf"><span>' + row[i].customer + '</span></li>';
				html += '<li class="now"><div><span>' + State(row[i].status) + '</span>';
				html += '<span class="time" id="'+ row[i].status  +'"></span>';
				html += '</div></li></ul></li></ul>';
				html += '</a>';
				//if(state == '101'){
					time.push(data.rows[i].createDate + 172800000); 
					//console.log(time);
					setInterval(function(){ShowCountDown(time);}, 1000);
				//} 
			}
			$('#order_list').append(html);
			 for(var i=0;i<arr03.length;i++){
				 $('.time[id='+ arr03[i] +']').hide();
			 };
		},
		error:function(data){
			//报错信息
		}
	})
}

function ShowCountDown(endDate) {
	for(var i = 0;i < endDate.length;i++){
		var now = new Date();  
		var leftTime=endDate[i]-now.getTime(); 
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
