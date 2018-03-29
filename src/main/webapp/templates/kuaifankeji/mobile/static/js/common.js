//地区
function areaSelect(parentId,element,selectValue){
	//ajax
	$.axse("/common/area",{"parentId":parentId},
        function(data){
			if(data==""){
				return false;
			}
			var html='<option value="">请选择</option>';
			for(var d in data){
				var area = data[d];
				html+='<option value="'+area.value+'">'+area.name+'</option>';
			}
			element.html(html);
			if(selectValue!=null){
				element.find("option[value='"+selectValue+"']").attr("selected","selected");
			}
        },
        function(data){
    });
}
//城市
$(document).ready(function(){
	$("select.province").on("change",function(){
		var city = $(this).parent().parent().find(".city");
		var district = $(this).parent().parent().find(".district");
		city.html('<option value="">请选择</option>');
		district.html('<option value="">请选择</option>');
		areaSelect($(this).val(),city,null);
	});
	
	$("select.city").on("change",function(){
		var district = $(this).parent().parent().find(".district");
		district.html('<option value="">请选择</option>');
		areaSelect($(this).val(),district,null);
	});
	
	$.each($(".province"),function(index,item){
		var city = $(item).parent().parent().find(".city");
		var district = $(item).parent().parent().find(".district");
		var dataTreePath = $(item).attr("data-treePath");
		var dataValue = $(item).attr("data-value");
		if(typeof(dataTreePath)!="undefined" && dataTreePath!=""){
			var dataProvinceId = dataTreePath.split(",")[1];
			var dataCityId = dataTreePath.split(",")[2];
			areaSelect(null,$(item),dataProvinceId);
			areaSelect(dataProvinceId,city,dataCityId);
			areaSelect(dataCityId,district,dataValue);
		}else{
			areaSelect(null,$(item),null);
		}
	});
	
});