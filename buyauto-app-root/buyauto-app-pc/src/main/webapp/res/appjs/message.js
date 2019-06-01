var page = 1;
var pageSize = 10;
var jump = false;
$(function() {
	getPageInfo(0);
	//遮罩层
	$('.boom').css({
		'width':$(window).width(),
		'height':$(window).height()
	})
	
	//点击确定(报错提示)
	$('.delBtn').click(function(){
		if(jump){
			jump=false;
			window.location.reload();
		}
		$('.Errors').hide();
		$('.boom').hide();
	})
	// 翻页
	$('.page').click(
			function(e) {
				var e = e || window.event;
				var target = e.target || e.srcElement;
				if (target.id == "preBtn") {
					var id = $('.current').attr('id');
					id = id - 1;
					if (id < 1) {
						id = 1;
						return;
					}
					page = id;
					// console.log(pageNumber)
					$('.ctPage ul li[id=' + id + ']').addClass('current')
							.siblings().removeClass('current');
					if (length > 5 && id > 2 && id < length - 1) {
						$('.ctPage ul').css({
							'left' : -39 * (id - 3) + 'px'
						})
					}
					getPageInfo(1);
				} else if (target.id == "nextBtn") {
					var id = $('.current').attr('id');
					id = parseInt(id) + 1;
					if (id > length) {
						id = length;
						return;
					}
					page = id;
					// console.log(pageNumber)
					$('.ctPage ul li[id=' + id + ']').addClass('current')
							.siblings().removeClass('current');
					if (length > 5 && id > 3 && id < length - 1) {
						$('.ctPage ul').css({
							'left' : -39 * (id - 3) + 'px'
						})
					}

					getPageInfo(1);
				} else if (target.id == '') {
					return;
				} else {
					var id = target.id;
					page = id;
					// console.log(pageNumber)
					$('.page ul li[id="' + id + '"]').addClass('current')
							.siblings().removeClass('current');
					if (length > 5 && id > 3 && id < length - 2) {
						$('.ctPage ul').css({
							'left' : -39 * (id - 3) + 'px'
						})
					} else if (length > 5 && id < 4) {
						$('.ctPage ul').css({
							'left' : 0
						})
					} else if (length > 5 && id > length - 3) {
						$('.ctPage ul').css({
							"left" : -39 * (length - 5) + 'px'
						})
					}
					getPageInfo(1);
				}
			});
	$(".all").click(function() {
		$.ajax({
			url : g_requestContextPath + "/h/l/msg/allRead",
			data : {},
			type : "POST",
			success : function(data) {
				window.location.reload();
			}
		});
	});

})

function pageChange(page) {
	$('.ctPage ul').html('');
	$('.ctPage ul').css({
		'left' : "0"
	})
	for (var i = 1; i < page; i++) {
		$('.ctPage ul').append('<li id="' + i + '">' + i + '</li>')
	}
	$('.ctPage ul li:eq(0)').addClass('current');
	length = $('.ctPage ul li').length;
	var wid = length * 40 - 10;
	$('.ctPage ul').css({
		'width' : wid + 'px'
	})
}

// 翻页获取数据函数
function getPageInfo(type) {
	// console.log(pageNumber)
			$.ajax({
				url : g_requestContextPath + "/h/l/msg/getMegInfo",
				type : "post",
				data : {
					"page" : page,
					"rows" : pageSize
				},
				success : function(data) {
					if (type == 0) {
						var pageNum = Math.ceil(data.total / pageSize);
						pageChange(pageNum + 1);
					}
					if(data.total < 11){
						$('.page').hide();
					}
					// console.log(data);
					var messageList = '';
					for (var i = 0; i < data.rows.length; i++) {
						messageList += '<li class="lt">';
						if (data.rows[i].status == 0) {
							messageList += '<div class="message">';
						} else {
							messageList += '<div class="message changeColor">';
						}
						messageList += '<span>'
								+ new Date(data.rows[i].createDate).format("yyyy-MM-dd hh:mm:ss")
								
								+ '</span><span>' + data.rows[i].msgInfo
								+ '</span></div><div class="made">';

						if (data.rows[i].status == 0) {
							messageList += '<span data-id="' + data.rows[i].id
									+ '" onclick="changeRead(this)">已读</span>';
							if (data.rows[i].type == 1 || data.rows[i].type == 2) {
								messageList += '<input type="button" id="btn_levelUp" value="接受邀请" data-id="'
										+ data.rows[i].id
										+ '" onclick="checkUpLevel(this)"/>';
							}
						}
						messageList += '</div></li>';
						$(".messase_list").html(messageList);
					}
				},
				error : function() {
					// console.log(data);
					console.log("非法参数");
				}
			});
}

function checkUpLevel(span) {
	
	var id = $(span).data("id");
	//console.log(id)
	$.ajax({
		url : g_requestContextPath + "/h/l/msg/checkUpLevel",
		data : {
			"id" : id
		},
		type : "POST",
		success : function(data) {
			//console.log(data)
			if (data == "200") {
				window.location.href = g_requestContextPath + '/h/l/msg/personalInfo?id='+id;
			} else {
				alertCode(data);// 根据code提示
				$(span).parent().hide();
				$(span).parent().prev().addClass('changeColor');
			}
		}
	});
}

function changeRead(span) {
	var id = $(span).data("id");
	$.ajax({
		url : g_requestContextPath + "/h/l/msg/hasRead",
		data : {
			"id" : id
		},
		type : "POST",
		success : function(data) {
			if (data == "200") {
				$(span).parent().hide();
				$(span).parent().prev().addClass('changeColor');
			} else if (data == "500") {
				window.location.reload();
			}
		}
	});
}

/**
 * 根据对应的验证码提示信息
 */
function alertCode(code) {
	if (code == "")
		return "";
if (code == "500") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("您已提交个人信息，请耐心等待审核。");
	}  else if (code == "501") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("抱歉，您目前级别未满足升级要求");
	} else if (code == "0") {
		$('.boom').show();
		$('.Errors').show();
		$('.Errors').find("span").html("恭喜您成功升级为个人经销商！");
		jump = true;
	}
}