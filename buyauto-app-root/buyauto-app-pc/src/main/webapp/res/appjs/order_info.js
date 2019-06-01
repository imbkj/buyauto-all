var W = '';
var H = '';
$(function(){
	 //console.log($('.order_price ul li').length);
	 if($('.order_price ul li').length == '1'){
		 $('.order_price ul li').css({
			 'color':'#212121',
			 'fontSize':'14px',
			 'fontWeight':'bold',
			 'lineHeight':'43px'
		 })
	 }else if($('.order_price ul li').length == '2'){
		 $('.order_price ul li:eq(0)').css({
			 'color':'#212121',
			 'fontSize':'14px',
			 'fontWeight':'bold',
			 'float':'left',
			 'width':'100%'
		 });
		 $('.order_price ul li:eq(1)').css({
			 'color':'#fda118',
			 'fontSize':'14px'
		 })
	 }else if($('.order_price ul li').length == '3'){
		 $('.order_price ul li:eq(0)').css({
			 'color':'#212121',
			 'fontSize':'14px',
			 'fontWeight':'bold',
			 'float':'left',
			 'width':'100%'
		 });
		 $('.order_price ul li:eq(1)').css({
			 'color':'#212121',
			 'fontSize':'14px',
			 'float':'left'
		 });
		 $('.order_price ul li:eq(2)').css({
			 'color':'#fda118',
			 'fontSize':'14px',
			 'float':'left',
			 'marginLeft':'20px'
		 });
	 }else if($('.order_price ul li').length == '4'){
		 $('.order_price ul li:eq(0)').css({
			 'color':'#212121',
			 'fontSize':'14px',
			 'fontWeight':'bold',
			 'float':'left',
			 'width':'100%'
		 });
		 $('.order_price ul li:eq(1)').css({
			 'color':'#212121',
			 'fontSize':'14px',
			 'float':'left'
		 });
		 $('.order_price ul li:eq(2)').css({
			 'color':'#212121',
			 'fontSize':'14px',
			 'float':'left',
			 'marginLeft':'20px'
		 });
		 $('.order_price ul li:eq(3)').css({
			 'color':'#fda118',
			 'fontSize':'14px',
			 'float':'left',
			 'marginLeft':'20px'
		 });
	 }
	 var id = GetQueryString('id');
	//console.log(id);
	 if(id=='1'){
		 $('.order_lt .icon4 a').css({
			 'background':'#f39800 url("'+ g_requestContextPath +'/res/images/order-icon04.png") no-repeat 20px center',
			 'color':'#ffffff'
		 })
	 }else if(id=='2'){
		 $('.order_lt .icon3 a').css({
			 'background':'#f39800 url("'+ g_requestContextPath +'/res/images/order-icon03.png") no-repeat 20px center',
			 'color':'#ffffff'
		 })
	 }
	//滚动条
	$('#logistics').niceScroll({
            cursorcolor: "#ccc", 
            cursoropacitymax: 0.8,  
            touchbehavior: false,  
            cursorwidth: "5px",  
            cursorborder: "1",  
            cursorborderradius: "5px", 
            autohidemode: false  
    });
	//遮罩层
	$('.boom').css({
		'width':$(window).width(),
		'height':$(window).height()
	})
	//三证图片 
//	$('.car_card ul li img').toggler(function(){
//		$('.boom').show();
//		$(this).css({
//			'position':'fixed',
//			'top':'0',
//			'left':'0',
//			'right':'0',
//			'bottom':'0',
//			'margin':'auto',
//			'zIndex':'10' 
//		});
//	},function(){
//		$('.boom').hide();
//		$(this).css({
//			'position':'static'
//		})
//	})
	
	$('.car_card ul li img').click(function(){
		if($('.boom').css('display') == 'none'){
			$('.boom').show();
			$(this).css({
				'position':'fixed',
				'top':'0',
				'left':'0',
				'right':'0',
				'bottom':'0',
				'margin':'auto',
				'zIndex':'10'
			});
			$(this).removeClass('big');
			return;
		}
		if($('.boom').css('display') == 'block'){
			$('.boom').hide();
			$(this).css({
				'position':'static'
			})
			$(this).addClass('big');
		}
		return;
	})
	
	$('.boom').click(function(){
		if($('.boom').css('display') == 'block'){
			$('.boom').hide();
			$('.car_card ul li img').css({
				'position':'static'
			})
			$('.car_card ul li img').addClass('big');
		}
	})
	
	 
	//遍历可选配置
	$.each(JSON.parse($("#carOptionalConfigure").val()),function(idx,optionalConfigures){
		var carOptionalHtml = "<li>";
		$.each(optionalConfigures,function(idx,configName){
			//当前格式标识{"name":"","number":""}
			if (idx=="name") {
				carOptionalHtml += configName + "</li>";
			}
		});
		$("#carOptionalConfigUl").append(carOptionalHtml);
	});
	//遍历物流信息
	var logisticsInfo = eval($("#logisticsInfoValue").val());
	var carOptionalHtml = "";
	if(logisticsInfo == null || logisticsInfo == ""){
		carOptionalHtml += "<li><span>当前时间："+new Date().format("yyyy-MM-dd hh:mm:ss")+"&nbsp;&nbsp;"+"物流信息暂无</span></li>";
	}else{
		$.each(logisticsInfo,function(idx,logs){
			carOptionalHtml += "<li><span>" + new Date(logs.modifiTime).format("yyyy-MM-dd hh:mm:ss") + "&nbsp;&nbsp;&nbsp;" + logs.logistStatus + "</span></li>";
		});
	}
	$("#logistics").append(carOptionalHtml);
	
	
})

 
 
 function GetQueryString(name)
 {
      var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if(r!=null)return  unescape(r[2]); return '';
 }