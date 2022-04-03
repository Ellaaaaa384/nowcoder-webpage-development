$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	if($(btn).hasClass("btn-info")) {
		// 关注TA
		$.post(
		    CONTEXT_PATH + "/follow",
		    {"entityType":3,"entityId":$(btn).prev().val()}, // html页面的关注按钮前加了一个隐藏框（th:value="${user.id}"），获取当前按钮的上一节点
		    function(data) {
		        data = $.parseJSON(data); // 返回结果转成js对象
		        if(data.code==0) {
		            window.location.reload();
		        } else {
		            alert(data.msg);
		        }
		    }
		);
		// $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary"); // 改样式的代码
	} else {
		// 取消关注
		$.post(
		    CONTEXT_PATH + "/unfollow",
		    {"entityType":3,"entityId":$(btn).prev().val()}, // html页面的关注按钮前加了一个隐藏框（th:value="${user.id}"），获取当前按钮的上一节点
		    function(data) {
		        data = $.parseJSON(data); // 返回结果转成js对象
		        if(data.code==0) {
		            window.location.reload();
		        } else {
		            alert(data.msg);
		        }
		    }
		);
//		$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info"); // 改样式的代码
	}
}