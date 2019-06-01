$(function(){

	var url = g_requestContextPath + "/h/n/news/getInfoById";
	//console.log(url);
	
	var prefix = null;
	$.getJSON(g_requestContextPath + "/getShowAddress",function(data){
		prefix = data;
	})
	
	var id = GetQueryString("id");
	
	$.ajax({
		url : url,
		type : 'post',
		data:{
			"id":id
		},
		success : function(data){
			//console.log(data);
			var infott = '';
			infott+='<div class="info_tt">';
		    infott+=	'<h5>'+data.title+'</h5>';
		    infott+=	'<i>'+new Date(data.onlinetime).format("yyyy-MM-dd hh:mm:ss")+'</i>';
	    	infott+='</div>';
	    	infott+='<div class="info_deta">';
	    	infott+=	'<p>'+data.content+'</p>';
	    	infott+='</div>';
	    	
			$(".info_detas").html(infott);
		},
		error : function() {
			// console.log(data);
			console.log("非法参数");
		}
		
	});
	
});

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return '';
}