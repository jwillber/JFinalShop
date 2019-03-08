//添加购物车
function addToCart(productId,type){
	var quantity=1;
	//不带数量
	if(type==2){
		quantity=$("#number").val();
	}
	//ajax
	$.axse("/cart_item/save",
            {"productId":productId,"quantity":quantity}, 
            function(data){
                showMsg(data);
                showCart(data);
            },
            function(data){
        });
}
//删除购物项
function deleteCartItem(cartItemId,type){
	//ajax
	$.axse("/cart_item/delete",
            {"id":cartItemId}, 
            function(data){
            	if(type=="ajax"){
            		loadCart();
            	}else if(type="redirect"){
            		location.href="/cart/list"
            	}
            },
            function(data){
        });
	
}
//更新购物车数量
function updateChangeQuantity(productId,quantity){
	$.axse("/cart_item/update",
            {"productId":productId,"quantity":quantity,"type":2}, 
            function(data){
            	$("#cartItem_subtotal_"+productId).html("￥"+data.subtotal);
            	$("#totalPrice").html("￥"+data.totalPrice);
            },
            function(data){
        });
}
//显示购物车
function showCart(data){
	var html = '';
	 html += '<div class="cart_shoping_wrap">';
	 html += '<div id="J_listBox" class="cart_shoping">';
	 html += '<ul class="cs_list">';
	 //遍历
	 for(var d in data.cartItems){
		 var cartItem=data.cartItems[d];
		 html += '<li class="cs_list_item J_cart_item_dd">';
		 html += '<dl>';
		 html += '<dt class="cs_list_img"><a target="_blank" href="'+cartItem.product.path+'"><img width="47" height="60" alt="'+cartItem.product.name+'" src="'+cartItem.product.image+'"></a></dt>';
		 html += '<dd class="cs_list_title">';
		 html += '<p class="cs_list_name"><a target="_blank" href="'+cartItem.product.path+'">'+cartItem.product.name+'</a></p>';
		 html += '<p></p>';
		 html += '</dd>';
		 html += '<dd class="cs_list_price"><span>￥'+cartItem.product.price+'</span>Χ<em>'+cartItem.quantity+'</em><i class="del-goods" title="删除" alt="删除" onclick="javascript:deleteCartItem('+cartItem.id+',\'ajax\');">删除</i></dd>';
		 html += '</dl>';
		 html += '</li>';
	 }
	 //遍历
	 html += '</ul>';
	 html += '<div class="cs_checkout">';
	 html += '<p><span class="cs_checkout_num">共<i>'+data.quantity+'</i>件商品</span><span id="J_cart_total" class="cs_checkout_price">共计：¥<i>'+data.totalPrice+'</i></span></p>';
	 html += '<p><a class="cs_checkout_btn" href="/cart/list">去购物袋结算</a></p>';
	 html += '</div>';
	 html += '</div>';
	 html += '</div>';
	 $("#J_header_cart_list").html(html);
	 $("#J_header_cart_quantity").html(data.quantity);
}
//加载购物车
function loadCart(){
	//ajax
	$.axse("/cart/view",{},
            function(data){
				if(data.cartItems.length>0){
					showCart(data);
				}else{
					var html='';
					html+='<div class="header_cart_empty fwr gray_header">';
					html+='<p class="hce_title">您的购物袋还没有商品，再去逛逛吧~</p> ';
					html+='</div>';
					$("#J_header_cart_list").html(html);
					 $("#J_header_cart_quantity").html('0');
				}
            },
            function(data){
        });
}
//点击购物车后弹出层
function showMsg(data) {
  var html = '';
  html += '<div id="shopbox" class="shopbox" style="display: block;">';
  html += '<div class="shopboxcon">';
  html += '<div id="shoploading" style="display: none;">';
  html += '<img alt="loading" src="'+staticPath+'/images/loading.gif">';
  html += '</div>';
  html += '<div style="">';
  html += '<h2><a class="track close" name="item-close-cart" href="javascript:;" onclick="$(\'#shopbox\').remove();"><span>关闭</span></a></h2>';
  html += '<div class="spboxcontent">';
  html += '<div class="shopboxdetail">';
  html += '<div class="spboxleft"><img src="'+staticPath+'/images/DPshopcarIco.gif"></div>';
  html += '<div class="spboxright">';
  html += '<span class="spboxtitle">该商品已成功放入购物车</span> <span class="blank5"></span>';
  html += '<p class="goods-num">购物车共 <span id="shopboxcount" class="numstyle">'+data.quantity+'</span> 件商品 合计：<span id="shopboxprice" class="numstyle2">'+data.totalPrice+'</span> 元</p>';
  html += '<span class="blank5"></span>';
  html += '<p class="spbbtndiv">';
  html += '<a href="javascript:void(0)" name="item-continueshop" class="jxgwbtn track" onclick="$(\'#shopbox\').remove();" style="color:#666666">&lt;&lt;继续购物</a>';
  html += '<input type="button" onclick="location.href=\'/cart/list\'" value="去结算>>" style="border:1px solid #fff; background-color:#FF6600; padding:5px 25px; color:#FFFFFF; font-size:14px">';
  html += '</p>';
  html += '</div>';
  html += '</div>';
  html += '</div>';
  html += '</div>';
  html += '</div>';
  html += '</div>';
  $(document.body).append(html);
}

//数量增加减
function changeQuantity(productId,operate){
	var cartItemNum = $("#cartItem_quantity_"+productId);;
	if("+"==operate){
		cartItemNum.val(Number(cartItemNum.val())+1);
		cartItemNum.attr("data",Number(cartItemNum.val())+1)
	}else if("-"==operate){
		cartItemNum.val(Number(cartItemNum.val())-1);
		cartItemNum.attr("data",Number(Number(cartItemNum.val())-1));
	}
	updateChangeQuantity(productId,cartItemNum.val());
}

function changePrice(productId){
	var cartItemNum = $("#cartItem_quantity_"+productId);
	var quantity = cartItemNum.val();
	var reg = /^\+?[1-9][0-9]*$/;　　//正整数 
    if(!reg.test(quantity)){
    	alert("请输入数字!")
    	cartItemNum.val(cartItemNum.attr("data"));
    	return;
    }
	updateChangeQuantity(productId,quantity);
}
//加载数据
$(document).ready(function(){
	loadCart();
});

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