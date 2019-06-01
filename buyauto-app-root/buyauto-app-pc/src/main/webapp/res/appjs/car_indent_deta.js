window.onload = function(){
	//遍历可选配置
	if($("#carOptionalConfigure").val() != ""){
		$.each(JSON.parse($("#carOptionalConfigure").val()),function(idx,optionalConfigures){
			var carOptionalHtml = "<li class='fl'>";
			$.each(optionalConfigures,function(idx,configName){
				//当前格式标识{"name":"","number":""}
				if (idx=="name") {
					carOptionalHtml += configName + "</li>";
				}
			});
			$("#carOptionalConfigUl").append(carOptionalHtml);
		});
	}
	//遍历物流信息
	var logisticsInfo = eval($("#logisticsInfoValue").val());
	var logisticsHtml = "";
	if(logisticsInfo == null || logisticsInfo == "" || logisticsInfo == "[]"){
		logisticsHtml += "<li>当前时间："+new Date().format("yyyy-MM-dd hh:mm:ss")+"&nbsp;&nbsp;"+"物流信息暂无</li>";
	}else{
		$.each(logisticsInfo,function(idx,logs){
			logisticsHtml += "<li>" + new Date(logs.modifiTime).format("yyyy-MM-dd hh:mm:ss") + "&nbsp;&nbsp;&nbsp;" + logs.logistStatus + "</li>";
		});
	}
	$("#logisticsInfoUl").append(logisticsHtml);
	if($("#financeType").val() == 2){
		$("[name=otherPay]").html("￥ "+$.formatMoney(parseFloat($("#mustConfigurePrice").val()) + parseFloat($("#ConfigurePrice").val())));
		}
	/*待支付定金倒计时*/
//	var timer = null;
//	var time = document.getElementById("orderCreateTime");
//	var str = time.value;
//	var td = 2;
//	var Count = document.getElementById("count");
//	var yy = new Date(str).format("yyyy");
//	var MM = new Date(str).format("MM");
//	var dd = Number(new Date(str).format("dd")) + Number(td);
//	var hh = new Date(str).format("hh");
//	var mm = new Date(str).format("mm");
//	var ss = new Date(str).format("ss");
//	var d = new Date(serverCurrentTime);
//	var nowTime=d.getTime();	//获取当前时间戳
//	d.setMonth(d.getMonth()+1);
//	d.setDate(0);
//	var res = d.getDate()*24;
//	d.setFullYear(yy,MM,dd);
//	d.setHours(hh,mm,ss);
//	var overTime=d.getTime();	//结束时间戳
//	var mist = parseInt((overTime-nowTime)/1000);	//结束事件戳-当前时间戳 
//	
//	var hours=parseInt(mist/3600)-res;
//	
//	mist = mist%3600;	//去小时后的秒数
//	
//	var minutes=parseInt(mist/60)
//	
//	mist = mist%60;
//	Count.innerHTML='剩余'+hours+'小时'+minutes+'分'+mist+'秒';
//	timer = setInterval(function(){
//		mist--;
//		if(mist<=0){
//			mist = 59;
//			minutes--;
//		}
//		if(minutes<=0){
//			hours--;
//			minutes=59;
//		}
//		if(hours<=0){
//			hours=0;
//		}
//		if(hours<=0 && minutes<=0 && mist<=0){
//			hours=0;
//			minutes=0;
//			mist=0;
//		}
//		Count.innerHTML='剩余'+hours+'小时'+minutes+'分'+mist+'秒';
//	},1000)
};
