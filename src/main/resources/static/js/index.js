function timer(time) {
    var btn=$("#codeButton");
    btn.attr("disabled",true);
    btn.html(time<=0?"发送验证码":(""+(time--)+"秒可发送"));
    var hander=setInterval(function () {
        if(time<=0){
            clearInterval(this);
            btn.html("发送验证码");
            btn.attr("disabled",false);
            return false;
        }else {
            btn.html(""+(time--)+"秒可发送");
        }
    },1000);
}
$(document).ready(function () {
    $("#codeButton").click(function () {
        $.ajax({
            async:true,
            type:"POST",
            utl:'/GetCode',
            dataType:"json",
            data:$("#f1").serialize(),
            success:function (data) {
                if(data.msg!=null){
                    alert(data.msg);
                }
                if(data.msg==='获取验证码成功'){
                    timer(120);
                    $("#i1").val(data.id);
                    $("#i2").val(data.mail);
                }
            },
            error:function () {
                alert("操作异常！");
                
            }
        })
    });
    $("#sub").click(function () {
        $.ajax({
            async:true,
            type:"POST",
            url:"/CheckCode",
            dataType:"json",
            data:$("#f2").serialize(),
            success:function (data) {
                if(data.msg!=null){
                    alert(data.msg);
                }
            },
            error:function () {
                alert("操作异常");
            }
        })
    })
});