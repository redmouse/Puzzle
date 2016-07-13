<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="../js/jquery-1.7.2.min.js" ></script>
<%@ page contentType="text/html; charset=UTF-8" %>
<style>
.number{
	width:18px;
}
.highlight{
	BACKGROUND-COLOR: Thistle;
}
.textCenter{
	text-align:center;
}
</style>
<html>
<head>
<script type="text/javascript">
var blockNum = '<s:property value="model.blockNum" />';
if(blockNum == undefined || blockNum == ''){
	blockNum = 3;
}
var lineNum = blockNum * blockNum;
var totalNum = lineNum * lineNum;


$(document).ready(function(){
	$("#txtArea").hide();
	$("#blockNum").change(function(){ 
		resetNumField();
	});
	resetNumField();
	resetNumFieldValue();
});

function resetNumField(){
	blockNum = $("#blockNum").find("option:selected").val();
	if(blockNum == undefined || blockNum == ''){
		blockNum = 3;
	}
	lineNum = blockNum * blockNum;
	totalNum = lineNum * lineNum;
	var itemHtml = '<input type="text" name="model.sdkList[itemIndex].val" class="number textCenter" maxlength="2"/>' + 
		'<input type="hidden" name="model.sdkList[itemIndex].row" value="rowIndex"/>' +
		'<input type="hidden" name="model.sdkList[itemIndex].col" value="colIndex"/>' +
		'<input type="hidden" name="model.sdkList[itemIndex].id" value="itemIndex"/>';
		
	var divHtml = '';
	for(var row=0; row<=lineNum; row++){
		divHtml += '<tr>';
		for(var col=0; col<=lineNum; col++){
			divHtml += '<td align="center">';
			if(row == 0 && col == 0){
			}else if(col == 0){
				divHtml += row;
			} else if(row==0){
				divHtml += col;
			} else {
				var idx = (row-1) * lineNum + (col-1);
				var appendHtml = itemHtml.replace(/itemIndex/g, idx);
				appendHtml = appendHtml.replace("rowIndex", row-1);
				appendHtml = appendHtml.replace("colIndex", col-1);
				divHtml += appendHtml;
			}
			divHtml += '</td>';
		}
		divHtml += '</tr>';
	}
	$("#numField").html(divHtml);
	resetNumFieldBGColor();
	resetNumFieldFontColor();
}
function resetNumFieldBGColor(){
	for (var idx = 1; idx <= totalNum; idx++) {
		var col = idx % lineNum;
		var row = Math.floor(idx/lineNum);
		var flg = false;
		if(Math.floor(row/blockNum)%2 == 1){
			flg = !flg;
		}
		if(Math.floor(col/blockNum)%2 == 1){
			flg = !flg;
		}
		if(flg){
			$("input[name='model.sdkList["+(idx)+"].val']").addClass("highlight");
		}
	}
}
function resetNumFieldFontColor(){
	var idx = 0;
	<s:iterator value="model.sdkList" id="cell">
		$("input[name='model.sdkList["+(idx++)+"].val']").attr("style","color:#"+'<s:property value="#cell.colorCode" />'+";");
	</s:iterator>
}
function resetNumFieldValue(){
	var idx = 0;
	<s:iterator value="model.sdkList" id="cell">
		$("input[name='model.sdkList["+(idx++)+"].val']").val('<s:property value="#cell.val" />');
	</s:iterator>
}

function doReduceOnce(){
	document.forms[0].action = "doReduceOnce";
	document.forms[0].submit();
}
function printArr(){
	var msg = '';
	for (var row = 0; row < lineNum; row++) {
		for(var col = 0; col < lineNum; col++){
			var idx = row * lineNum + col;
			var val = $("input[name='model.sdkList["+(idx)+"].val']").val();
			val = (val.length == 0) ? '  ' : val;
			val = (val.length == 1) ? ' ' + val : val;
			msg += val + ',';
		}
		msg += '\r\n';
	}
	$("#txtArea").show();
	$("#txtArea").val(msg);
}
</script>
<script type="text/javascript" src="./sudoku.js"></script>
<title>Sudoku</title>
</head>
<body>
<form id="form1" action="doCal" method="post">
    <s:select id="blockNum"
              name="model.blockNum"
              list="model.blockNumList"
              listKey="KEY"
              listValue="VALUE"
              value="model.blockNum"/>
	<input type="submit" value="計算" name="btnconfirm">
	<input type="button" value="printArr" name="btnPrintArr" onclick="printArr();">
	<input type="button" value="初級(9)" name="btnTestInit" onclick="initNumList(3,1);">
	<input type="button" value="中級(9)" name="btnTestInit" onclick="initNumList(3,2);">
	<input type="button" value="上級(9)" name="btnTestInit" onclick="initNumList(3,3);">
	<input type="button" value="難問(9)" name="btnTestInit" onclick="initNumList(3,4);">
	<input type="button" value="劇難問(9)" name="btnTestInit" onclick="initNumList(3,5);">
	&nbsp;&nbsp;
	<input type="button" value="初級(16)" name="btnTestInit" onclick="initNumList(4,1);">
	<input type="button" value="難問(16)" name="btnTestInit" onclick="initNumList(4,5);">
<!-- 
	<input type="button" value="一回計算" name="btnReduceOnce" onclick="doReduceOnce();">
 -->
	<hr/>
	<table>
	    <div id="numField">
	    </div>
    </table>
</form>
<hr/>
<p><s:property value="model.msg" escapeHtml="false"/></p>
<textarea id="txtArea" rows="25" cols="80">
</textarea>
</body>
</html>