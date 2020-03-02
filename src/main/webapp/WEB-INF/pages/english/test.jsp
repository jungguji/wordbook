<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Basic Window - jQuery EasyUI Demo</title>
    <link rel="stylesheet" type="text/css" href="/jquery-easyui-1.9.4/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/jquery-easyui-1.9.4/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/jquery-easyui-1.9.4/demo/demo.css">
    <script type="text/javascript" src="/jquery-easyui-1.9.4/jquery.min.js"></script>
    <script type="text/javascript" src="/jquery-easyui-1.9.4/jquery.easyui.min.js"></script>
</head>
<body>
    <h2>Basic Window</h2>
    <p>Window can be dragged freely on screen.</p>
    <div style="margin:20px 0;">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#w').window('open')">Open</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#w').window('close')">Close</a>
    </div>
    <div id="haha">
    </div>
    <div id="tq" class="easyui-panel" style="position:relative;width:500px;height:300px;overflow:auto;">
        <div id="w" class="easyui-window" data-options="title:'Inline Window',inline:true" style="width:100%;height:100%;padding:10px">
            This window stay inside its parent
        </div>
    </div>
    <div id="qt" class="easyui-panel" style="position:relative;width:500px;height:300px;overflow:auto;">
    <div id="w1" class="easyui-window" data-options="title:'Inline Window',inline:true" style="width:250px;height:150px;padding:10px">
            This window stay inside its parent
        </div>
    </div>
</body>
<script>
window.onload = () => {
	$("#tq").parent("div").css("width", "auto");
    $("#tq").parent("div").css("float", "left");
    $("#qt").parent("div").css("width", "auto");
}
</script>
</html>