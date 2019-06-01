var pageNumber = 1;
var pageSize = 10;

$(function(){
	accountState();
	dataList();
	
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
});

//加载员工列表
function dataList(){
	//获取员工列表
	var url2 = g_requestContextPath + "/h/l/user/getUserByPage";
	$.ajax({
		url : url2,
		type : 'post',
		data:{
			"page":pageNumber,
			"rows":pageSize
		},
		success : function(data){
			//console.log(data)
			var htmls = '';
			var rows = data.rows;
			var len = rows.length;
			var pageNum = Math.ceil(data.total/len);
			pageChange(pageNum+1);
			//console.log(len);
			console.log(data.total);
			if(data.total > 10){
				$('.page').show();
			}else{
				$('.page').hide();
			}
			for(var i = 0; i < len; i++){
				var companyName = rows[i].companyName;//公司名
				var account = rows[i].account;//账号名
				var name = rows[i].name;//姓名
				var position;//职位
				if(rows[i].position==1){
					position = "销售";
				}else if(rows[i].position==2){
					position = "财务";
				}
				var status;//状态
				var sta = rows[i].status;
				if( sta == 1){
					status = "可用";
				}else if(sta == 3){
					status = "冻结";
				}
				var id = rows[i].id;
				htmls+='<tr>';
				htmls+=	'<td class="account_01">'+ companyName +':'+ account +'</td>';
				htmls+=	'<td class="account_02">'+ name +'</td>';
				htmls+=	'<td class="account_03">'+ position +'</td>';
				htmls+=	'<td class="account_04">' +status+ '</td>';
				htmls+=	'<td class="cl account_05">';
				htmls+=		'<a class="fl alter" href="'+ g_requestContextPath +'/h/l/user/editUser?id='+id+'">修改</a>';
				if( sta == 1){
					htmls+=		'<a class="fl frost" href="javascript:void(0);" onclick="eidtUserState(this)"  data-id="'+id+'" data-sta="3" >冻结</a>';
				}else if(sta = 3){
					htmls+=		'<a class="fl start" href="javascript:void(0);" onclick="eidtUserState(this)" data-id="'+id+'" data-sta="1" >启用</a>';
				}
				htmls+=	'</td>';
				htmls+='</tr>';
				$(".car_management").html(htmls);
			};
		},
		error : function() {
			// console.log(data);
			console.log("非法参数");
		}
	});
}
//获取账号概况
function accountState(){
	var url = g_requestContextPath + "/h/l/user/getAccountState";
	$.ajax({
		url : url,
		type : 'post',
		success : function(data) {
			$(".account1").html(data.code_status_success);//可使用账号
			$(".account2").html(data.code_account_count);//可新建账号
			$(".account3").html(data.code_status_frozen);//冻结账号
		},
		error : function() {
			console.log("非法参数");
		}
	});
}

//修改员工状态
function eidtUserState(obj){
	var id = $(obj).data("id");
	var sta = $(obj).data("sta");
	$.ajax({
		url:g_requestContextPath+"/h/l/user/updateState",
		data:{
			"id":id,
			"state":sta
		},
		dataType:"json",
		type:"post",
		success:function(data){
			if(data == "200"){
				$(".toolTip").html("操作成功");
				$(".toolTip").show(1).delay(1000).hide(1);
				dataList();
				accountState();
			}else{
				$(".toolTip").html("操作失败");
				$(".toolTip").show(1).delay(1000).hide(1);
			}
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

//翻页获取数据函数
function getPageInfo(){
	//console.log(pageNumber)
	$.ajax({
		url: g_requestContextPath + "/h/l/user/getUserByPage",
		type: "post",
		data:{
			"page":pageNumber,
			"rows":pageSize
		},
		success : function(data){
			//console.log(data)
			var htmls = '';
			var rows = data.rows;
			var len = rows.length;
			//console.log(len);
			for(var i = 0; i < len; i++){
				var companyName = rows[i].companyName;//公司名
				var account = rows[i].account;//账号名
				var name = rows[i].name;//姓名
				var position;//职位
				if(rows[i].position==1){
					position = "销售";
				}else if(rows[i].position==2){
					position = "财务";
				}
				var status;//状态
				var sta = rows[i].status;
				if( sta == 1){
					status = "可用";
				}else if(sta == 3){
					status = "冻结";
				}
				var id = rows[i].id;
				htmls+='<tr>';
				htmls+=	'<td class="account_01">'+ companyName +':'+ account +'</td>';
				htmls+=	'<td class="account_02">'+ name +'</td>';
				htmls+=	'<td class="account_03">'+ position +'</td>';
				htmls+=	'<td class="account_04">' +status+ '</td>';
				htmls+=	'<td class="cl account_05">';
				htmls+=		'<a class="fl alter" href="'+ g_requestContextPath +'/h/l/user/editUser?id='+id+'">修改</a>';
				if( sta == 1){
					htmls+=		'<a class="fl frost" href="javascript:void(0);" onclick="eidtUserState(this)"  data-id="'+id+'" data-sta="3" >冻结</a>';
				}else if(sta = 3){
					htmls+=		'<a class="fl start" href="javascript:void(0);" onclick="eidtUserState(this)" data-id="'+id+'" data-sta="1" >启用</a>';
				}
				htmls+=	'</td>';
				htmls+='</tr>';
				$(".car_management").html(htmls);
			};
		},
		error : function() {
			// console.log(data);
			console.log("非法参数");
		}
	})
}
