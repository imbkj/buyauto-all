$(function(){
	var nav_tree = $.ajax({
		url : g_requestContextPath+"/res/json/nav2.json",
		type : "get",
		async : false,
		dataType : "json"
	});
	
	nav_tree.done(function(d) {
		if(d){
			var nav_str = "";
			$(d).each(function(i, v){
				var li_str = "<li>";
				li_str += '<a><i class="'+v.iconCls+'"></i> '+ v.text +' <span class="fa fa-chevron-down"></span></a>';
				
				if(v.children.length > 0){
					li_str += '<ul class="nav child_menu">';
					$(v.children).each(function(i, v){
						if(location.href.indexOf(v.url) > -1){
							li_str +=  "<li class='current-page'>";
						}else{
							li_str += "<li>";
						}
						
						if(v.target != undefined)
							li_str += '<a href="'+g_requestContextPath+v.url+'" target="'+v.target+'">'+ v.text +'</a>';
						else
							li_str += '<a href="'+g_requestContextPath+v.url+'">'+ v.text +'</a>';
						
						li_str += "</li>";
					});
					li_str += "</ul>";
				}
				
				li_str += "</li>";
				
				nav_str += li_str;
			});
		
			$("#navTree").append(nav_str);
		}
	});
});