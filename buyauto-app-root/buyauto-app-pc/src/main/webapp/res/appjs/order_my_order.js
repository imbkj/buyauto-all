var pageNumber = 1;
var pageSize = 5;
var state = "";
var stateAfter = "";
var searchTitle = '';
var prefix = null;

$(function(){
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
				carIndent+=				'<div class="code"><span>'+ new Date(data.rows[i].createDate).format("yyyy-MM-dd hh:mm:ss") +'</span>订单号码&nbsp;<span>'+data.rows[i].id+'</span></div>';
				carIndent+=				'<ul class="cl">';
				carIndent+=					'<li class="fl cl">';
				carIndent+=						'<div class="fl img"><img src="'+prefix+''+data.rows[i].productImage.filePath+'" width="170" height="119"/></div>';
				carIndent+=						'<div class="fl mat">';
				carIndent+=							'<div>'+ data.rows[i].product.title +'</div>';
				carIndent+=							'<div>麦卡价：￥'+thousandBitSeparator(parseInt(data.rows[i].carPrice))+'</div>';
				carIndent+=							'<div class="cl car_site">';
				carIndent+=								'<span class="fl site">'+data.rows[i].product.country+'</span>';
				carIndent+=								'<span class="fl spot">' + change(data.rows[i].product.position) + '</span>';
				carIndent+=							'</div>';
				carIndent+=							'<div class="cft">车身 '+data.rows[i].product.outColor+'  内饰 '+data.rows[i].product.inColor+'</div>';
				carIndent+=						'</div>';
				carIndent+=					'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].product.mustConfigurePrice))+'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].configurePrice))+'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].amount))+'</li>';
				carIndent+=					'<li class="fl status">'+getOrderState(data.rows[i].status)+'</li>';
				carIndent+=					'<li class="fl">';
				if(data.rows[i].status == 107){
				carIndent+=						'<a href="javascript:void(0);" class="take_car" data-id="'+data.rows[i].id+'">确认收车</a>';
				}else if(data.rows[i].status >= 100 && data.rows[i].status <= 103){
				carIndent+=						'<a href="javascript:void(0);" class="take_car" data-id="'+data.rows[i].id+'" data-oldstatus="'+data.rows[i].status+'">取消订单</a>';
				}
				carIndent+=					'</li>';
				carIndent+=				'</ul>';
				carIndent+=			'</div>';
				carIndent+=       '</a>';
				
				$(".car_indent_deta").html(carIndent);
				
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
				
			}
			
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
				carIndent+=							'<div>'+ data.rows[i].product.title + '</div>';
				carIndent+=							'<div>麦卡价：￥'+thousandBitSeparator(parseInt(data.rows[i].carPrice))+'</div>';
				carIndent+=							'<div class="cl car_site">';
				carIndent+=								'<span class="fl site">'+data.rows[i].product.country+'</span>';
				carIndent+=								'<span class="fl spot">' + change(data.rows[i].product.position) + '</span>';
				carIndent+=							'</div>';
				carIndent+=							'<div class="cft">车身 '+data.rows[i].product.inColor+'  内饰 '+data.rows[i].product.outColor+'</div>';
				carIndent+=						'</div>';
				carIndent+=					'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].product.mustConfigurePrice))+'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].configurePrice))+'</li>';
				carIndent+=					'<li class="fl">￥'+thousandBitSeparator(parseInt(data.rows[i].amount))+'</li>';
				carIndent+=					'<li class="fl status">'+getOrderState(data.rows[i].status)+'</li>';
				carIndent+=					'<li class="fl">';
				if(data.rows[i].status == 107){
				carIndent+=						'<a href="javascript:void(0);" class="take_car" data-id="'+data.rows[i].id+'">确认收车</a>';
				}else if(data.rows[i].status >= 100 && data.rows[i].status <= 103){
				carIndent+=						'<a href="javascript:void(0);" class="take_car" data-id="'+data.rows[i].id+'" data-oldstatus="'+data.rows[i].status+'">取消订单</a>';
				}
				carIndent+=					'</li>';
				carIndent+=				'</ul>';
				carIndent+=			'</div>';
				carIndent+=       '</a>';
				
				$(".car_indent_deta").html(carIndent);
				
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
			};
			
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