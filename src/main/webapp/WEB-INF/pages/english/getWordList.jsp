<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body onload="printWord()">
    <span id="word"></span> <br />
    <input type="hidden" id="currentIndex" value="" />
    <input type="text" id="answer" /> <br />
    <input type="button" id="check" value="확인" onclick="check()"/>
    <input type="button" id="continue" value="계속하기" onclick="printWord()" style="display:none"/>
    <span id="result"></span>
</body>
<script src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script type="text/javascript">
    var wordList = ${wordList};
    var answerList = [];
    function printWord() {
        let currentIndex = Math.floor(Math.random() * wordList.length);
        
        document.querySelector('#word').innerHTML = wordList[currentIndex].word;
        document.querySelector('#word').style.color = "black";
        document.querySelector('#result').innerHTML = "";
        document.querySelector('#answer').value = "";
        document.querySelector('#currentIndex').value = currentIndex;
        document.querySelector('#answer').focus();
        
        document.querySelector('#continue').style.display = "none";
        document.querySelector('#check').style.display = "block";
    }
    
    function check() {
        const currentIndex = document.querySelector('#currentIndex').value;
        const answer = document.querySelector('#answer').value
        
        if (wordList[currentIndex].meaning.includes(answer)) {
            document.querySelector('#word').style.color = "green";
            document.querySelector('#result').innerHTML = "정답";
            
            answerList.push(wordList[currentIndex].id);
            
            wordList.splice(currentIndex, 1);
        } else {
        	document.querySelector('#word').style.color = "red";
        	document.querySelector('#result').innerHTML = "오답";
        }
        
        if (wordList.length == 0) {
            answersUpdate();
        }
        
        document.querySelector('#check').style.display = "none";
        document.querySelector('#continue').style.display = "block";
    }
    
    function answersUpdate() {
    	let dataa = JSON.stringify(answerList);
    	
    	alert("asd");
        $.ajax({
        	type:"POST"
        	, contentType: "application/json"
        	, url: "/english/answers"
        	, data: dataa
        	, dataType : 'json'
        	, cache: false
        	, success: function(data) {
        		alert(data);
        		alert("시험 끝");
        	}
        });
    }
</script>
</html>
