/**与后台交互的方法***/
/**缓存保存信息**/
var examInfo = {
  "templateId":'',
  "examTemplate": {},
  "userId" : '',
  "userName": '',
  "organization": '',
  "doneQuestNum": 0,
  "totalQuestNum": 0,
  "score": {},
  "doingIndex": 0,
  "isPractice": '',
  "remark": ''
};

var numToOption = {
  0: 'A',
  1: 'B',
  2: 'C',
  3: 'D',
  4: 'E',
  5: 'F',
  6: 'G'
};

/**获取所有的考试模板**/
function findAllExam(){
    /**获取缓存数据**/
    examInfo = JSON.parse(window.localStorage.getItem("examInfo"));

    $.ajax({
        type: "GET",
        url:"getAllExam",//获取所有的考试模板
        contentType: "application/json;charset=UTF-8",
        data: {},
        dataType: "json",
        success:function(data){
            // location.href ="/admin/sysjgl/sysjck/index"
            var testHtml = '<div class="prospecl"><div class="title"><i class="iconfont" style="font-size:20px;color:#ffc231;">&#xe642;</i>&nbsp;&nbsp;正式考试</div><ul class="protips">';
            var practiceHtml = '<div class="prospecl">' +
                '<div class="title"><i class="iconfont" style="font-size:20px;color:#ffc231;">&#xe642;</i>&nbsp;&nbsp;模拟考试</div><ul class="protips">';
            if(data){
                $.each(data, function (index,element) {
                    if(element.isPractice == "1"){
                        practiceHtml += '<li style = "cursor:pointer;"><a  templateId ="' + element.id + '" ><span>' +
                            element.templateName + '</span></a></li>';
                    }else if(element.isPractice == '0'){
                        testHtml += '<li style = "cursor:pointer;"><a  templateId ="' + element.id + '" ><span>' +
                            element.templateName + '</span></a></li>';
                    }
                });

                practiceHtml += '</ul></div>';
                testHtml += '</ul></div>';

                $("#index-html .examList").append(testHtml);
                $("#index-html .examList").append(practiceHtml);

                // $("#index-html .prospecl").append(testHtml);
                // $("#index-html .prospecl").append(practiceHtml);
                // $("#index-html .prospecl .protips").append(testHtml);

                $("#index-html .prospecl ul").hide();
                $("#index-html").on('click', '.prospecl .title', function(){

                    if ($(this).next().css('display') == "none") {
                        //展开未展开
                        $('.prospecl').children('ul').slideUp(300);
                        $(this).next('ul').slideDown();
                        $(this).parent('.prospecl').children().children("i").html("&#xe607;").siblings('.prospecl').children().children("i").html("&#xe617;");
                    }else{
                        //收缩已展开
                        $(this).next('ul').slideUp(300);
                        $(this).children("i").html("&#xe617;");
                    }
                    return false;
                });

                // /**考试类别点击事件**/
                // $("#index-html .protips li a").on('click' ,function () {
                //     var templateId =  $(this).attr('templateId');
                //     /**跳转页面**/
                //     if(templateId){
                //         examInfo.templateId = templateId;
                //         window.localStorage.setItem("examInfo",JSON.stringify(examInfo));
                //         location.href = "exam.html";
                //     }
                // });

                /**考试类别点击事件**/
                $("#index-html").on('click', '.protips li', function () {
                    var templateId =  $(this).children('a').attr("templateId");
                    /**跳转页面**/
                    if(templateId){
                        examInfo.templateId = templateId;
                        window.localStorage.setItem("examInfo",JSON.stringify(examInfo));
                        location.href = "exam.html";
                    }
                });
            }else{
                alert("获取考试模板异常");
            }
        }
    });
};

/**获取试卷题目并动态生成试卷**/
function getPaperById() {

    examInfo = JSON.parse(window.localStorage.getItem("examInfo"));

    if(examInfo.templateId){
        $.ajax({
            type: "GET",
            url: "getPaper/"+examInfo.templateId,
            contentType: "application/json;charset=UTF-8",
            dataType: "json",
            async: false,
            success: function (res) {
                if(res){
                    /**处理所有题型---start**/
                    examInfo.examTemplate = res.examTemplate;//保存试卷模板
                    examInfo.isPractice = res.examTemplate.isPractice;//是否练习模式  0-否 1-是
                    /**处理单选题---start**/
                    var singleQuesHtml = '';
                    $.each(res.singleQuesList,function (index,element) {

                        var tempAns = '';//记录正确答案
                        singleQuesHtml += '<div class="test" style="display: table-cell; vertical-align: top; width: 414px;"> <div class="test_con"> <div class="item"><div class="quesTitle">单选题</div><div class="tit">' +
                            '<span>' + (index + 1) + '、' + '</span> <input type="hidden"  quesType="single" idunsed="' + element.id + '" value="" ></input>' + element.content + '</div>'+
                            '<div class="item_con"> <div class="answ"> <input type="hidden" name="' + element.id + '" class = "answ" id="' +  element.id + '" value="" /> <ul>';

                        /**循环处理选项**/
                        $.each(element.options,function (ind,ele) {
                            if(ele.optionNum == element.answer){
                                tempAns =  numToOption[ind];
                            }
                            singleQuesHtml += '<li class="single options" ids="' + ele.questionId + '" val="' + ele.optionNum + '"> <i>' + numToOption[ind] + '、' +  ele.content + '</i></li>';
                        });

                        /**练习模式添加答案在题目中，以便可以展示出来**/
                        if(examInfo.isPractice == '1'){
                            singleQuesHtml += '</ul></div></div><div class="rightAns"  id = "rightAns' + element.id + '"><span>正确答案:  </span><span>   ' + tempAns + '</span></div></div></div></div>';
                        }else{
                            singleQuesHtml += '</ul></div></div></div></div></div>';
                        }
                        // singleQuesHtml += '</ul></div></div></div></div></div>';
                    });
                    /**处理单选题---end**/

                    /**处理多选题---start**/
                    var mutiQuesHtml = '';
                    $.each(res.mutiQuesList,function (index,element) {

                        var tempAns = '';
                        mutiQuesHtml += '<div class="test" style="display: table-cell; vertical-align: top; width: 414px;"> <div class="test_con"> <div class="item"><div class="quesTitle">多选题</div><div class="tit">' +
                            '<span>' + (index + 1) + '、' + '</span> <input type="hidden" quesType="muti" idunsed="' + element.id + '" value="" ></input>' + element.content + '</div>'+
                            '<div class="item_con"> <div class="answ"> <input type="hidden" name="' + element.id + '" class = "answ" id="' +  element.id + '" value="" /> <ul>';

                        /**循环处理选项**/
                        $.each(element.options,function (ind,ele) {
                            if(element.answer.indexOf(ele.optionNum) > -1){
                                tempAns += numToOption[ind];
                            }
                            mutiQuesHtml += '<li class="muti options" ids="' + ele.questionId + '" val="' + ele.optionNum + '"> <i>' + numToOption[ind] + '、' +  ele.content + '</i></li>';
                        });

                        /**练习模式添加答案在题目中，以便可以展示出来**/
                        if(examInfo.isPractice == '1'){
                            mutiQuesHtml += '</ul></div></div><div class="rightAns"  id = "rightAns' + element.id + '"><span>正确答案:  </span><span>   ' + tempAns + '</span></div></div></div></div>';
                        }else{
                            mutiQuesHtml += '</ul></div></div></div></div></div>';
                        }
                    });
                    /**处理多选题---end**/

                    /**处理多选题---start**/
                    var judgeQuesHtml = '';
                    $.each(res.judgeQuesList,function (index,element) {
                        var tempAns = '';
                        judgeQuesHtml += '<div class="test" style="display: table-cell; vertical-align: top; width: 414px;"> <div class="test_con"> <div class="item"><div class="quesTitle">判断题</div><div class="tit">' +
                            '<span>' + (index + 1) + '、' + '</span> <input type="hidden" quesType="judge" idunsed="' + element.id + '" value="" ></input>' + element.content + '</div>'+
                            '<div class="item_con"> <div class="answ"> <input type="hidden" name="' + element.id + '" class = "answ" id="' +  element.id + '" value="" /> <ul>';

                        if(element.answer == '1'){
                            tempAns = '正确';
                        }else{
                            tempAns = '错误';
                        }

                        /**手工添加是和否**/
                        judgeQuesHtml += '<li class="judge options clickable" ids="' + element.id + '" val="1"> <i> 正确</i></li>';
                        judgeQuesHtml += '<li class="judge options clickable" ids="' + element.id + '" val="0"> <i> 错误</i></li>';


                        /**练习模式添加答案在题目中，以便可以展示出来**/
                        if(examInfo.isPractice == '1'){
                            judgeQuesHtml += '</ul></div></div><div class="rightAns"  id = "rightAns' + element.id + '"><span>正确答案:  </span><span>   ' + tempAns + '</span></div></div></div></div>';
                        }else{
                            judgeQuesHtml += '</ul></div></div></div></div></div>';
                        }
                        // judgeQuesHtml += '</ul></div></div></div></div></div>';
                    });
                    /**处理多选题---end**/

                    /**填充页面**/
                    $("#exam-html #bd12").append(singleQuesHtml);
                    $("#exam-html #bd12").append(mutiQuesHtml);
                    $("#exam-html #bd12").append(judgeQuesHtml);

                    /**根据是否练习模式的标志 决定是否展示答案的栏目**/
                    if(examInfo.isPractice == '1'){
                        $("#exam-html #AnsDis").show();
                    }else{
                        $("#exam-html #AnsDis").hide();
                    }


                    /**动态生成答题板内容**/
                    var singNum = Number(examInfo.examTemplate.singleNum);
                    var mutiNum = Number(examInfo.examTemplate.mutiNum);
                    var judgeNum = Number(examInfo.examTemplate.judgeNum);

                    var singleLayHtml = '';
                    if(singNum){
                        singleLayHtml += '<div class="singleLay"><span class="label-title">单选题</span>';

                        /**20200703 修改答题板上的内容**/
                        $.each(res.singleQuesList, function(index,element){
                            singleLayHtml += '<a href="#" class="noDo" questId="' + element.id +'" >' + (index + 1) + '</a>';
                        });

                        // for(var i=0; i < singNum; i ++){
                        //     singleLayHtml += '<a href="#" class="noDo">' + (i+1) +'</a>'
                        // }
                        singleLayHtml += '</div>';
                    }

                    var mutiLayHtml = '';
                    if(mutiNum){
                        mutiLayHtml += '<div class="mutiLay"><span class="label-title">多选题</span>';

                        /**20200703 修改答题板上的内容**/
                        $.each(res.mutiQuesList,function (index,element) {
                            mutiLayHtml += '<a href="#" class="noDo" questId="' + element.id + '" >' + ( index + 1) + '</a>';
                        });
                        // for(var i=0; i < mutiNum; i ++){
                        //     mutiLayHtml += '<a href="#" class="noDo">' + (i+1) +'</a>'
                        // }
                        mutiLayHtml += '</div>';
                    }

                    var judgeLayHtml = '';
                    if(judgeNum){
                        judgeLayHtml += '<div class="judgeLay"><span class="label-title">判断题</span>';
                        /**20200703 修改答题板上的内容**/
                        $.each(res.judgeQuesList,function (index,element) {
                            judgeLayHtml += '<a href="#" class="noDo" questId="' + element.id + '" >' + (index + 1) + '</a>';
                        })
                        /*for(var i=0; i < judgeNum; i ++){
                            judgeLayHtml += '<a href="#" class="noDo">' + (i+1) +'</a>'
                        }*/
                        judgeLayHtml += '</div>';
                    }

                    singleLayHtml&&$("#layAB .con").append(singleLayHtml);
                    mutiLayHtml&&$("#layAB .con").append(mutiLayHtml);
                    judgeLayHtml&&$("#layAB .con").append(judgeLayHtml);
                    var totalQuesNum = singNum + mutiNum + judgeNum;
                    examInfo.totalQuestNum = totalQuesNum;
                    $("#totalQuesNum").text(totalQuesNum);

                    window.localStorage.setItem("examInfo",JSON.stringify(examInfo));//保存缓存数据
                    examHtmlInit();//加載之前靜態頁面的一些方法

                }
            }
        });
    }

};

/**試題頁面獲取題目后初始化加載方法**/
function examHtmlInit() {

    $("#exam-html").on('click',' #AnsDis',function(){

        var questionid=$("#QuesDoing").val();
        if(questionid=="")
        {
            $("#QuesDoing").val($("#QuestionDoing1").val());
            questionid=$("#QuesDoing").val();
        }
        $("#rightAns"+questionid).css("display","block");
        $("#QuesAnsw"+questionid).css("display","block");
        if($("#TrueAns"+questionid).text()=="")
        {
            $("#TrueAns"+questionid).text("无");
        }
    });

    //备份
    // $("#exam-html").on('click',' #AnsDis',function(){
    //     alert('点击查看答案');
    //     var questionid=$("#QuesDoing").val();
    //     if(questionid=="")
    //     {
    //         $("#QuesDoing").val($("#QuestionDoing1").val());
    //         questionid=$("#QuesDoing").val();
    //     }
    //     $("#rightAns"+questionid).css("display","block");
    //     $("#QuesAnsw"+questionid).css("display","block");
    //     if($("#TrueAns"+questionid).text()=="")
    //     {
    //         $("#TrueAns"+questionid).text("无");
    //     }
    // });
    //答案点击
    // $(document).on('click','.options',function(){
    //     $("a.doing").addClass("done");   //答题板上的颜色
    //     var val=$(this).attr("val");     //题目答案
    //     var ids=$(this).attr("ids");	 //题目编号
    //     $("li[ids="+ids+"]").removeClass("click");
    //     $(this).addClass("click");
    //     $(".doneCount").text($("a.done").length);
    //     $("#QuestionA"+ids).val(val);
    //     $("#TrueAns"+ids).text(val);
    // });

    /**单选题选项选中事件**/
    $("#exam-html").on("click",".single.options",function(){
        $(".singleLay a.doing").addClass("done");   //答题板上的颜色
        var val=$(this).attr("val");     //题目答案
        var ids=$(this).attr("ids");	 //题目编号
        $("li[ids="+ids+"]").removeClass("click");//单选题去除其他选中的，只把本条记录加上选中效果
        $(this).addClass("click");
        $(".doneCount").text($("a.done").length);
        $("#"+ids).val(val);
        $("#TrueAns"+ids).text(val);
    });
    /**多选题选项点击事件**/
    $("#exam-html").on("click",".muti.options", function(){
        var val = '';
        var ids=$(this).attr("ids");
        /**当前选中就去除，当前没选中就添加选中**/
        if($(this).hasClass('click')){
            $(this).removeClass("click");
        }else{
            $(this).addClass("click");
        }
        $("li[ids="+ids+"].click").each(function (index,element) {
            val+=$(element).attr('val');
        });
        $("#"+ids).val(val);
        if(val){
            $(".mutiLay a.doing").addClass("done");
        }else{
            $(".mutiLay a.doing").removeClass("done");
        }
        $(".doneCount").text($("a.done").length);

    });
    /**判断题选项选中事件**/
    $("#exam-html").on("click",".judge.options", function(){
        $(".judgeLay a.doing").addClass("done");   //答题板上的颜色
        var val=$(this).attr("val");     //题目答案
        var ids=$(this).attr("ids");	 //题目编号
        $("li[ids="+ids+"]").removeClass("click");//单选题去除其他选中的，只把本条记录加上选中效果
        $(this).addClass("click");
        $(".doneCount").text($("a.done").length);
        $("#"+ids).val(val);
        $("#TrueAns"+ids).text(val);
    });

    /**点击答题卡的 返回**/
    $("#exam-html").on('click', '#closeLayAB',function(){
        $("#exam-html #layAB").hide();
    });
    $("#exam-html").on('click','#collection', function(){
        mui.alert("添加收藏成功!", "提示",function(){});
    });


    pushHistory();
    window.addEventListener("popstate", function(e) {
        pushHistory();
    }, false);
    function pushHistory() {
        var state = {
            title:"title",
            url:"#"
        };
        window.history.pushState(state, "title", "#");
    }
    //答题卡页面   题目栏展示页点击页签事件
    $("#exam-html ").on('click','.con a', function(){
        /**判断点击的是哪一个模块的题目，再根据题目的位置跳转到对应的宽度上**/
        if($(this).parent("div")){
            var quesType = $(this).parent("div").attr('class');
            if(quesType == 'singleLay'){
                index=$(this).text()-1;
            }else if(quesType == 'mutiLay'){
                index= $(".singleLay a").length + Number($(this).text()-1);
            }else if(quesType == 'judgeLay'){
                index= $(".singleLay a").length + $(".mutiLay a").length + Number($(this).text()-1);
            }
        }
        s=$(".test").css("width")
        temp=parseInt(s.substring(0,s.length-2));
        t="-"+temp*index+"px";
        document.getElementById("bd12").style.webkitTransform="translate("+t+",0px)  translateZ(0px)";
        /**新加的，尝试修复跳至当前题目。没有显示正在答题的问题 --修复成功 但是从跳转的这个问题滑动不是它的下一题**/
        $("a.doing").removeClass("doing");
        $(this).addClass("doing");
        examInfo.doingIndex = index;
        $("#QuesDoing").val($("a.doing").attr('questId'));//将当前所答题目的questid记录下来
        // $("#QuesDoing").val(index);

        $(".layAB").hide();
    });
    //答题卡 点击事件
    $("#exam-html ").on('click','#AnsSheet',function(){
        $(".layAB").show();
    });
    //动态规划  根据题目数量（test类的数目）和屏幕分辨率展示题目实现滑屏效果
    TouchSlide({slideCell:"#leftTabBox",startFun:function(i){ //已做题目标识
            $("a.doing").removeClass("doing");
            $(".con a").eq(i).addClass("doing");
            i++;
            // $("#QuesDoing").val($("#QuestionDoing"+i).val());
            $("#QuesDoing").val($("a.doing").attr('questId'));
            $(".itemansw").css("display","none");
        }});
    /*倒计时*/
    function fSubmit()
    {
        getAnswerAndSubmit();
    }

    if(examInfo.examTemplate.timeLimit){
        $("#exam-html #clock").show();
        var limitTime = Number(examInfo.examTemplate.timeLimit) * 60;//获取后台考试模板定义的倒计时--后台返回的是分钟
        var count=0;
        var CUR_TIME = 0;var END_TIME = limitTime;var LEFT_TIME;var M_LABEL;var S_LABEL;var ID;
        function timer() {
            if(ID) {window.clearTimeout(ID);}
            if(!S_LABEL || !M_LABEL) {M_LABEL = document.getElementById("m");S_LABEL = document.getElementById("s");}
            LEFT_TIME = END_TIME - CUR_TIME;
            if(LEFT_TIME > 0) {
                M_LABEL.innerHTML = format(parseInt(LEFT_TIME / 60));S_LABEL.innerHTML = format((LEFT_TIME % 60));CUR_TIME ++;
                $("#current_time").val(CUR_TIME);
                if(CUR_TIME ==END_TIME){
                    setTimeout("getAnswerAndSubmit()", 3000);
                }
            }else {return 0;}
            ID = window.setTimeout(timer, 1000);
        }

        function format(time) {if(time < 10) {return ("0"+ time);} else {return time;}}
        window.onload=function(){timer()}
    }else{
        $("#exam-html #clock").hide();
    }
    $("#exam-html").on('click','#SubmitExam', function(){
        var btnArray = ["否", "是"];
        if($("a.done").length<examInfo.totalQuestNum)
        {
            mui.confirm("还有未答完的题目,确认要提交吗？", "确认提交", btnArray, function(e) {
                if (e.index == 1) {
                    getAnswerAndSubmit();
                    // document.UsersDo.submit();
                } else {

                }
            })
        }else if(LEFT_TIME > 0){
            mui.confirm("题目已答完,尚有时间剩余,确认要提交吗？", "确认提交", btnArray, function(e) {
                if (e.index == 1) {
                    getAnswerAndSubmit();
                    // document.UsersDo.submit();
                } else {

                }
            })
        }else
        {
            getAnswerAndSubmit();
            // document.UsersDo.submit();
        }
    });

    /**点击返回提示 确认后再返回**/
    $("#exam-html").on('click','.back', function () {

        var btnArray = ["否", "是"];

        mui.confirm("返回到试卷选择页面，本次考试将不记录，是否确认返回？", "确认返回", btnArray, function(e) {
            if (e.index == 1) {
                // window.localStorage.setItem("examInfo",JSON.stringify(examInfo));
                location.href = 'index.html';
            } else {

            }
        })
    });
}

/**获取试题id和本人填写答案交卷**/
function getAnswerAndSubmit(resultHtml){
    /**答案列表**/
    var answerList = new Array();

/**在各种题型的点击事件中处理了选择答案后 把答案放在input class=answ 中，直接循环这个去获取**/
var doneQuestNum = 0;//统计已做题数

$("input.answ").each(function (index,element) {

    var answer ={
        "questionId": "",
        "answer": ""
    }
    answer.questionId = $(this).attr('id');
    answer.answer = $(this).val();
    if(answer.answer){
        doneQuestNum++;
    }

    answerList.push(answer);
});

examInfo.doneQuestNum = doneQuestNum;

/**提交答案到后台计算分数并跳转到 resultHtml**/
var  postPaperForm = {
    "templateId": examInfo.templateId,
    "userName": examInfo.userName,
    "userId": examInfo.userId,
    "organization": examInfo.organization,
    "remark": '',
    "remark1": '',
    "answerList": answerList

}
$.ajax({
    type: "POST",
    url:"postAnswer",//获取所有的考试模板
    contentType: "application/json;charset=UTF-8",
    data: JSON.stringify(postPaperForm),
    dataType: "json",
    success:function(data){
        examInfo.score = data;
        window.localStorage.setItem("examInfo",JSON.stringify(examInfo));
        location.href = 'exam_do.html';
    }
});


};

/**结果展示页面初始化限时分数及答题统计**/
function  initShowScore() {
    /**获取缓存数据**/
    examInfo = JSON.parse(window.localStorage.getItem("examInfo"));
    var correctSingleNum = Number(examInfo.score.correctSingleNum);
    var correctMutiNum = Number(examInfo.score.correctMutiNum);
    var correctJudgeNum = Number(examInfo.score.correctJudgeNum);

    /**总题数与答题数**/
    $("#totalQuestNum").text(examInfo.totalQuestNum + "道");
    $("#doneQuesNum").text(examInfo.doneQuestNum + "道");


    /**结果详情展示**/
    var detailHtml = '<div class="detail-title" style="text-align:left;">&nbsp;&nbsp;做对试题' + (correctSingleNum + correctMutiNum + correctJudgeNum) + '道</div>';
    if(correctSingleNum){
        detailHtml += '<div class="detail-wrap" style="width:80px;height:80px;float:left;text-align:center;margin-top:10px;"><div style="heigh:40px;line-height:40px;">单选题</div>' +
            '<div style="heigh:40px;line-height:40px;">' + correctSingleNum + '</div></div>';
    }
    if(correctMutiNum){
        detailHtml += '<div class="detail-wrap" style="width:80px;height:80px;float:left;text-align:center;margin-top:10px;"><div style="heigh:40px;line-height:40px;">多选题</div>' +
            '<div style="heigh:40px;line-height:40px;">' + correctMutiNum + '</div></div>';
    }
    if(correctJudgeNum){
        detailHtml += '<div class="detail-wrap" style="width:80px;height:80px;float:left;text-align:center;margin-top:10px;"><div style="heigh:40px;line-height:40px;">判断题</div>' +
            '<div style="heigh:40px;line-height:40px;">' + correctJudgeNum + '</div></div>';
    }
    detailHtml += '<div style="clear:both"></div>';

    /**将结果填充到页面中**/
    $(".detail-box:eq(0)").append(detailHtml);

    /**加上边界效果**/
    if($(".detail-box:eq(0) .detail-wrap").length == 3){
        $(".detail-box:eq(0) .detail-wrap:eq(0)").addClass("border-line");
        $(".detail-box:eq(0) .detail-wrap:eq(1)").addClass("border-line");
    }else if($(".detail-box:eq(0) .detail-wrap").length == 2){
        $(".detail-box:eq(0) .detail-wrap:eq(0)").addClass("border-line");
    }

    /**题型分数展示**/
    var ruleHtml = '<div class="detail-title">总得分</div><div class="detail-wrap" style="text-align:center;padding-top:25px;padding-bottom:25px;"><font style="font-size:46px;color:red;font-weight:bold;">' +
        examInfo.score.score + '</font></div><div class="detail-wrap" style="border-top:1px solid #d9d9d9;height:120px;">';

    if(examInfo.examTemplate.singleNum){
        ruleHtml+='<span class="left" style="margin-top:15px;clear:both;">单选题(' + examInfo.examTemplate.singleNum + '题每题' + examInfo.examTemplate.singleScore + '分）</span>';
    }
    if(examInfo.examTemplate.mutiNum){
        ruleHtml+='<span class="left" style="margin-top:15px;clear:both;">多选题(' + examInfo.examTemplate.mutiNum + '题每题' + examInfo.examTemplate.mutiScore + '分）</span>';
    }
    if(examInfo.examTemplate.judgeNum){
        ruleHtml+='<span class="left" style="margin-top:15px;clear:both;">判断题(' + examInfo.examTemplate.judgeNum + '题每题' + examInfo.examTemplate.judgeScore + '分）</span>';
    }

    ruleHtml += '</div>';
    $('.detail-box:eq(1)').append(ruleHtml);
}

/**登录页面操作**/
function checklogin()
{
    $("#errmsg").html("");
    if($("#username").val()==null||$("#username").val()=="")
    {
        //mui.alert("请输入用户名！", "提示",function(){});
        $("#errmsg").html("请输入姓名！");
        return false;
    }else
    if($("#userId").val()==null||$("#userId").val()=="")
    {
        //mui.alert("请输入密码！", "提示",function(){});
        $("#errmsg").html("请输入员工编号");
        return false;
    }else
    if($("#orgSelect").val()==null||$("#orgSelect").val()=="")
    {
        $("#errmsg").html("请选择机构！");
        return false;
    }else
    {
        // findAllExam();
        /**保存用户信息**/
        examInfo.userId = $("#userId").val().trim();
        examInfo.userName = $("#username").val().trim();
        examInfo.organization = $("#orgSelect").val();
        window.localStorage.setItem("examInfo",JSON.stringify(examInfo));//保存缓存数据
        location.href="index.html";

    }
}

/**登录页请求获取所有机构信息**/
function loginInit() {
    $.ajax({
        type: "GET",
        url:"getAllOrgs",//获取所有的机构信息
        contentType: "application/json;charset=UTF-8",
        data: {},
        dataType: "json",
        success:function(data){

            var optionsHtml = '<option value="">请选择机构</option>';
            if(data){
                $.each(data, function (index,element) {
                    optionsHtml += '<option value ="' + element.code + '" >' + element.name + '</option>>';
                });
                $("#loginHtml #orgSelect").append(optionsHtml);
                /**机构下拉列表的选择事件**/
                $("#loginHtml").on('change', '#orgSelect', function () {
                    var org = $("#orgSelect").val();
                    if(org){
                        $("#orgSelect").css('color','#1e1e21');
                    }else{
                        $("#orgSelect").css('color','#a1a1b1')
                    }
                });


            }else{
                alert("获取机构信息异常");
            }
        }
    });

}