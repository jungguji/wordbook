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
                
                <a href="javascript:check()" class="btn btn-lg btn-default" id="check"> 확인 </a>
                <a href="javascript:printWord()" class="btn btn-lg btn-default" id="continue" style="display:none"> 계속 </a>
                
                <br />
                <h1 class="cover-heading" id="result"> </h1> <br />
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
    
    function printWord() {
    	document.querySelector("#answer").removeAttribute("disabled");
        const currentIndex = Math.floor(Math.random() * wordList.length);
        
        document.querySelector('#word').innerHTML = wordList[currentIndex].word;
        document.querySelector('#word').style.color = "#fff"
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
        
        if(empty(answer)) {
            alert("값을 입력해주세요.");
            document.querySelector('#answer').focus();
            return;
        }
        
        document.querySelector("#answer").setAttribute("disabled","disabled");
        
        if (wordList[currentIndex].meaning.includes(answer)) {
            document.querySelector('#word').style.color = "green";
            document.querySelector('#result').innerHTML = "정답";
            
            answerList.push(wordList[currentIndex].id+"_" + 1);
            
            wordList.splice(currentIndex, 1);
        } else {
            document.querySelector('#word').style.color = "red";
            document.querySelector('#result').innerHTML = "오답";
            
            answerList.push(wordList[currentIndex].id+"_" + 0);
        }
        
        if (wordList.length == 0) {
            answersUpdate();
        }
        
        document.querySelector('#check').style.display = "none";
        document.querySelector('#continue').style.display = "block";
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
