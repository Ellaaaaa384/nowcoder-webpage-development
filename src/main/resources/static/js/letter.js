$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");

//	给服务端发送数据
    var toName = $("#recipient-name").val();
    var content = $("#message-text").val();
    $.post(
        CONTEXT_PATH + "/letter/send",
        {"toName":toName,"content":content},
        function(data) {
            data = $.parseJSON(data); // 转化为JS对象
            if(data.code == 0) {
                $("#hintBody").text("发送成功！");
            } else {
                $("#hintBody").text(data.msg);
            }
            // 刷新页面
            $("#hintModal").modal("show");
            setTimeout(function(){
                $("#hintModal").modal("hide");
                location.reload();
            }, 2000);
        }
    );
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}