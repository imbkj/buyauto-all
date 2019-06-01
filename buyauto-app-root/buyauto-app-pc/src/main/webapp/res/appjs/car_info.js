$(document).ready(function() {
	$(".prev_btn1,.next_btn1").hover(function() {
		$(".btn_lt,.btn_rt").show();
	}, function() {
		$(".btn_lt,.btn_rt").hide();
	});
	
   //头部菜单固定
	var navHeight = $("#navHeight").offset().top;
	var navFix = $("#nav-wrap");
	$(window).scroll(function() {
		if ($(this).scrollTop() > navHeight) {
			navFix.addClass("navFix");
			$(".nav_order").show();
		} else {
			navFix.removeClass("navFix");
			$(".nav_order").hide();
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
	});

})

$('#demo1').banqh({
	box : "#demo1",// 总框架
	pic : "#ban_pic1",// 大图框架
	pnum : "#ban_num1",// 小图框架
	prev_btn : "#prev_btn1",// 小图左箭头
	next_btn : "#next_btn1",// 小图右箭头
	pop_prev : "#prev2",// 弹出框左箭头
	pop_next : "#next2",// 弹出框右箭头
	prev : "#prev1",// 大图左箭头
	next : "#next1",// 大图右箭头
	pop_div : "#demo2",// 弹出框框架
	pop_pic : "#ban_pic2",// 弹出框图片框架
	pop_xx : ".pop_up_xx",// 关闭弹出框按钮
	mhc : ".mhc",// 朦灰层
	autoplay : true,// 是否自动播放
	interTime : 5000,// 图片自动切换间隔
	delayTime : 400,// 切换一张图片时间
	pop_delayTime : 400,// 弹出框切换一张图片时间
	order : 0,// 当前显示的图片（从0开始）
	picdire : true,// 大图滚动方向（true为水平方向滚动）
	mindire : true,// 小图滚动方向（true为水平方向滚动）
	min_picnum : 3,// 小图显示数量
	pop_up : true
// 大图是否有弹出框
})



// 内容信息导航锚点
$('.nav-wrap').navScroll({
	mobileDropdown : true,
	mobileBreakpoint : 800,
	scrollSpy : true
});

$('.click-me').navScroll({
	navHeight : 0
});

$('.nav-wrap').on('click', '.nav-mobile', function(e) {
	e.preventDefault();
	$('.nav-wrap ul').slideToggle('fast');
});


//提交
function lookinfo(id){
	//console.log(id)
	 $.ajax({
		 url:g_requestContextPath + "/h/l/supplier/subAudit",
		 type:'get',
		 data:{
			 "id":id,
		 },
		 async:false,
	     dataType:'json',
	     success:function(data){
	    	  //console.log(data);
	    	  alertcode(data);
	    	  return false;
	     },
	     error:function(data){
	    	 //console.log(2,data);
	    	  return false;
	     }
	 })
}



//提交成功返回提示
function alertcode(data){
	 //var code = data.code;
	 //if(code == "")return "";
	 if(data=="200"){
		 /*保存草稿成功*/
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的信息提交成功");
		 jump = true;	
	 };
	 if(data=="500"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的信息提交失败");
	 };
	 if(data=="601"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的标题为空");
	 };
	 if(data=="602"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的品牌ID为空");
	 };
	 if(data=="603"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的车辆为空");
	 };
	 if(data=="604"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的价格区间为空");
	 };
	 if(data=="605"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的车辆类型为空");
	 };
	 if(data=="606"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的定金比例为空");
	 };
	 if(data=="607"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的出厂日期为空");
	 };
	 if(data=="608"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的裸车价格为空");
	 };
	 if(data=="609"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的必须总价为空");
	 };
	 if(data=="610"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的国内指导价为空");
	 };
	 if(data=="611"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的库存为空");
	 };
	 if(data=="612"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的缩略图为空");
	 };
	 if(data=="613"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的封面图为空");
	 };
	 if(data=="614"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的详情图为空");
	 };
	 if(data=="615"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的推荐图为空");
	 };
	 if(data=="616"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的产品正文为空");
	 };
	 if(data=="617"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("您的车辆三证为空");
	 };	 
	 if(data=="618"){
		 $('.boom').show();
		 $('.Errors').show();
		 $('.Errors').find("span").html("未查到该产品");
	 };	 
	 return false;
}
