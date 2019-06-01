var page = 1;
var pageSize = 20;

$(function() {
	getPageInfo(0);

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
		url : g_requestContextPath + "/h/l/commission/rackBackList",
		type : "post",
		data : {
			"page" : page,
			"rows" : pageSize
		},
		success : function(data) {
			// console.log(data.total)
			if (type == 0) {
				var pageNum = Math.ceil(data.total / pageSize);
				pageChange(pageNum + 1);
			}
			if (data.total < 21) {
				$('.page').hide();
			}
			// console.log(data);
			if (data.rows.length == 0) {
				$("#recomm").hide();
			}
			var messageList = '';
			for (var i = 0; i < data.rows.length; i++) {
				messageList += '<ul class="list_nav list"><li>';
				messageList += data.rows[i].id;
				messageList += '</li><li>';
				messageList += data.rows[i].phone;
				messageList += '</li><li>';
				messageList += new Date(data.rows[i].createDate)
						.format("yyyy-MM-dd hh:mm:ss");
				messageList += '</li><li>';
				if (data.rows[i].ratio == null) {
					messageList += '0%</li><li>';
				} else {
					messageList += data.rows[i].ratio + '%</li><li>';
				}
				if (data.rows[i].commission == null) {
					messageList += "0.00";
				} else {
					messageList += data.rows[i].commission.toFixed(2);
				}
				messageList += '元</li></ul>';
				$("#recomm").html(messageList);
				$(".noData").hide();
			}
		},
		error : function() {
			// console.log(data);
			console.log("非法参数");
		}
	});
}