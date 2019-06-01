$(function(){
	var id = GetQueryString('id');
	//console.log(id);
	var index = null;
	var arr = null;
	if(id == '0'){
		arr = localStorage.getItem('img01').split(',');
	}else if(id == '1'){
		arr = localStorage.getItem('img02').split(',');
	}else if(id == '2'){
		arr = localStorage.getItem('img03').split(',');
	}
	
	//console.log(localStorage.getItem('img'));
	 //console.log(arr);
	
	 var length = arr.length;
	 $('.big_img img').attr({'src':arr[0]});
	 for(var i = 0;i < length;i++){
	 	$('.small_box ul').append('<li><img src="'+ arr[i] +'"></li>')
	 }

	 $('.small_box ul li:eq(0)').addClass('ic');
	 //点击小图大图对应切换
	 $('.small_box ul li').click(function(){
	 	index = $(this).index();
	 	var Src = $(this).find('img').attr('src');
	 	$('.big_img img').attr({'src':Src});
	 	$(this).addClass('ic').siblings().removeClass('ic');
	 })
	 //点击左右按钮切换大图
	 $('.icon-left').click(function(){
	 	index--;
	 	if(index < 0){
	 		index = 0;
	 		return;
	 	}
	 	$('.big_img img').attr({'src':arr[index]});
	 	$('.small_box ul li:eq(' + index + ')').addClass('ic').siblings().removeClass('ic');
	 })

	 $('.icon-right').click(function(){
	 	index++;
	 	if(index > length - 1){
	 		index = length - 1
	 		return;
	 	}
	 	$('.big_img img').attr({'src':arr[index]});
	 	$('.small_box ul li:eq(' + index + ')').addClass('ic').siblings().removeClass('ic');
	 })
})

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return '';
}