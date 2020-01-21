<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body onload="printWord()">
     <div class="site-wrapper">
      <div class="site-wrapper-inner">
        <div class="cover-container">
          <div class="masthead clearfix">
            <div class="inner">
              <h3 class="masthead-brand">Word Test</h3>
              <nav>
                <ul class="nav masthead-nav">
                  <li class="active"><a href="#">Home</a></li>
                </ul>
              </nav>
            </div>
          </div>

          <div class="inner cover">
           <div class="form-group">
                <h1 class="cover-heading" id="word"> </h1>
                <p class="lead">위 단어에 해당하는 뜻을 한글로 적어주세요.</p>
                
                <input type="hidden" id="currentIndex" value="" />
                <input type="text" class="form-control" id="answer" placeholder="뜻을 입력하세요"/> <br />
                
                <button onclick="button(this)" class="btn btn-lg btn-default" value="확인"> 확인 </button>
                
                <br />
                    <h1 class="cover-heading" id="result"> </h1> 
                <p class="lead" id="different"> </p>
                
            </div>
          </div>

          <div class="mastfoot">
            <div class="inner">
              <p>Cover template for <a href="http://getbootstrap.com">Bootstrap</a>, by <a href="https://twitter.com/mdo">@mdo</a>.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    
   
</body>
<script src="/webjars/jquery/2.1.3/dist/jquery.min.js"></script>
<script src="/webjars/bootstrap/4.4.1/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="/webjars/bootstrap/4.4.1/css/bootstrap.min.css"></link>
<link href="/css/cover.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
    var wordList = ${wordList};
    const answerList = [];
    
    document.querySelector("#answer").addEventListener("keydown", key => {
        if (key.keyCode == 13) {
            button(document.querySelector("button"));
        }
    });

    const button = (element) => {
        const answerTag = document.querySelector("#answer");
        
        if(empty(answerTag.value)) {
            alert("값을 입력해주세요.");
            answerTag.focus();
            return;
        }
        
        if (element.value === "확인") {
            check();
            element.value = "계속하기";
            element.innerHTML = "계속하기";
            
            answerTag.focus();
        } else {
            printWord();
            
            element.value = "확인";
            element.innerHTML = "확인";
        }
    }
    
    function printWord() {
    	const currentIndex = Math.floor(Math.random() * wordList.length);
    	clear(currentIndex);
    	
        const wordTag = document.querySelector('#word');
        
        wordTag.innerHTML = wordList[currentIndex].word;
        wordTag.style.color = "#fff"
    }
    
    const clear = (currentIndex) => {
        document.querySelector('#currentIndex').value = currentIndex;
        document.querySelector('#result').innerHTML = "";
        document.querySelector("#different").innerHTML = "";
        
        answerTagNotDisabled();
    }
    
    function check() {
        answerTagDisabled();
        
        const answerTag = document.querySelector('#answer')
        const wordTag = document.querySelector('#word');
        const resultTag = document.querySelector('#result');
        const currentIndex = document.querySelector('#currentIndex').value;
        
        const meanArray = stringToArray(wordList[currentIndex].meaning);
        if (meanArray.includes(answerTag.value)) {
            wordTag.style.color = "green";
            resultTag.innerHTML = "정답";
            
            answerList.push(wordList[currentIndex].id+"_" + 1);
            document.querySelector("#different").innerHTML = "(" + wordList[currentIndex].meaning + ")";
            
            wordList.splice(currentIndex, 1);
        } else {
            wordTag.style.color = "red";
            resultTag.innerHTML = "오답";
            
            answerList.push(wordList[currentIndex].id+"_" + 0);
        }
        
        if (wordList.length == 0) {
            answersUpdate();
        }
    }
    
    const answerTagDisabled = () => {
        const answerTag = document.querySelector('#answer');
        answerTag.setAttribute("readonly","readonly");
        answerTag.style.color = "#495057";
    }
    
    const answerTagNotDisabled = () => {
        const answerTag = document.querySelector("#answer");
        
        answerTag.removeAttribute("readonly");
        answerTag.value = "";
        answerTag.focus();
    }
    
    const stringToArray = (str) => {
    	const array = str.split(",");
        return arrayElementTrim(array);
    }
    
    const arrayElementTrim = (array) => {
    	for (i = 0; i < array.length; i++) {
            array[i] = array[i].trim();
        }
    	
    	return array;
    } 
    
    const empty = (value) => { 
        if (value === null) return true 
        if (typeof value === 'undefined') return true 
        if (typeof value === 'string' && value === '') return true 
        if (Array.isArray(value) && value.length < 1) return true 
        if (typeof value === 'object' && value.constructor.name === 'Object' && Object.keys(value).length < 1 && Object.getOwnPropertyNames(value) < 1) return true 
        if (typeof value === 'object' && value.constructor.name === 'String' && Object.keys(value).length < 1) return true // new String() return false }
    }

    function answersUpdate() {
        let dataa = JSON.stringify(answerList);
        
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
