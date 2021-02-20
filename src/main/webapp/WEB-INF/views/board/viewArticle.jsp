<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<c:set var="article"  value="${articleMap.article}"  />
<c:set var="imageFileList"  value="${articleMap.imageFileList}"  />


<%
  request.setCharacterEncoding("UTF-8");
%> 

<head>
   <meta charset="UTF-8">
   <title>글보기</title>
   <style>
     #tr_file_upload{
       display:none;
     }
     #tr_btn_modify{
       display:none;
     }
   
   </style>
   <script  src="http://code.jquery.com/jquery-latest.min.js"></script> 
   <script type="text/javascript" >
     function backToList(obj){
	    obj.action="${contextPath}/board/listArticles.do";
	    obj.submit();
     }
 // 굳이 this.form 안 넣어도 된다?   obj 
		 /*
	 function fn_enable(obj){
		
		 document.getElementById("i_title").disabled=false;
		 document.getElementById("i_content").disabled=false;
		 document.getElementById("tr_btn_modify").style.display="block";
		 document.getElementById("tr_btn").style.display="none";
		// document.getElementById("i_imageFileName").disabled=false;  단일파일 업로드 
		// document.getElementById("tr_file_upload").style.display="block";   단일파일업로드
		
		 var size = document.getElementsByName("imageFileName").length;
	     for(var i=0;i<size;i++){
	    	 document.getElementsByName("imageFileName")[i].disabled = false;	    	 
	     }	
	 }
 */
 //테스트용 
     function fn_enable(obj){
				
		document.getElementById("i_title").disabled=false;
		document.getElementById("i_content").disabled=false;
		document.getElementById("tr_btn_modify").style.display="block";
		document.getElementById("tr_btn").style.display="none";
		// document.getElementById("i_imageFileName").disabled=false;  단일파일 업로드 
		// document.getElementById("tr_file_upload").style.display="block";   단일파일업로드
			
		//getElementsByClassName으로 class=imageFileName인 input을 가져와 길이만큼 disabled 해제 
		var size = document.getElementsByClassName("imageFileName").length;
		for(var i=0;i<size;i++){
		      document.getElementsByClassName("imageFileName")[i].disabled = false;	 	 
		}		
	 }
	 
	 function fn_modify_article(obj){
		 //----------------------추가
		 //수정반영하기 버튼을 누를 때, <input id="i_imagefileName"+i>에 값이 있다면, 
		 //거기에 대응되는 imageFileNO 태그와 originalFileName 태그의 disabled를 해제한다. 
		 
		 //이건 사실 imageFileName 값으로 구해도 된다. 기존에 올라온 이미지 파일의 수를 구하는 것. 
		 var size = document.getElementsByClassName("imageFileNO").length;
		 for(var i=0;i<size;i++){
			 //i로 imageFileName과 imageFileNO, originalFileName을 엮어서,  imageFileName +i 값에 대응하는 imageFileNO와 originalFileName 태그의 disabled를 해제한다.
		    var imageFileName = document.getElementById("i_imageFileName"+i).value;
		    //originalFileName 관련 추가 - 어차피 imageFileName(새로 파일을 추가하는 input)에 대응되는 originalFileName만 담으면 되니까 if는 imageFileName으로 체크하면 됨.(동일하게)
			if(imageFileName) { 									
				 document.getElementById("i_imageFileNO"+i).disabled = false;
				 //originalFileName 추가
				 document.getElementById("i_originalFileName"+i).disabled = false;
			 }
		 }		 		 
		 //----------------------추가		 		 
		 obj.action="${contextPath}/board/modArticle.do";
		 obj.submit();
	 }
	 
	 function fn_remove_article(url,articleNO){
		 var form = document.createElement("form");
		 form.setAttribute("method", "post");
		 form.setAttribute("action", url);
	     var articleNOInput = document.createElement("input");
	     articleNOInput.setAttribute("type","hidden");
	     articleNOInput.setAttribute("name","articleNO");
	     articleNOInput.setAttribute("value", articleNO);
		 
	     form.appendChild(articleNOInput);
	     document.body.appendChild(form);
	     form.submit();
	 
	 }
	 <!-- 답글 기능 (직접구현해보라함 -->
	 function fn_reply_form(url, parentNO){
		 var form = document.createElement("form");
		 form.setAttribute("method", "post");
		 form.setAttribute("action", url);
	     var parentNOInput = document.createElement("input");
	     parentNOInput.setAttribute("type","hidden");
	     parentNOInput.setAttribute("name","parentNO");
	     parentNOInput.setAttribute("value", parentNO);
		 
	     form.appendChild(parentNOInput);
	     document.body.appendChild(form);
		 form.submit();
	 }
	 /* 원래꺼
	 function readURL(input) {
	     if (input.files && input.files[0]) {
	         var reader = new FileReader();
	         reader.onload = function (e) {
	        	 //각각 아이디에 할당되게
	             $('#preview').attr('src', e.target.result);
	         }	         
	           reader.readAsDataURL(input.files[0]); 
	     }
	 }  
	 */
	 //테스트용
	 //count도 인자로 넣어서 
	 function readURL(input, count) {
	     if (input.files && input.files[0]) {
	         var reader = new FileReader();	         
	         reader.onload = function (e) {
	        	 //각각 아이디에 할당되게
	             $('#'+count).attr('src', e.target.result);
	         }	         
	           reader.readAsDataURL(input.files[0]); 
	     }
	 }  
	 

 </script>
</head>
<body>
  <form name="frmArticle" method="post"  action="${contextPath}"  enctype="multipart/form-data">
  <table  border=0  align="center">
  <tr>
   <td width=150 align="center" bgcolor=#FF9933>
      글번호
   </td>
   <td >
    <input type="text"  value="${article.articleNO }"  disabled />
    <input type="hidden" name="articleNO" value="${article.articleNO}"  />
   </td>
  </tr>
  <tr>
    <td width="150" align="center" bgcolor="#FF9933">
      작성자 아이디
   </td>
   <td >
    <input type=text value="${article.id }" name="writer"  disabled />
   </td>
  </tr>
  <tr>
    <td width="150" align="center" bgcolor="#FF9933">
      제목 
   </td>
   <td>
    <input type=text value="${article.title }"  name="title"  id="i_title" disabled />
   </td>   
  </tr>
  <tr>
    <td width="150" align="center" bgcolor="#FF9933">
      내용
   </td>
   <td>
    <textarea rows="20" cols="60"  name="content"  id="i_content"  disabled />${article.content }</textarea>
   </td>  
  </tr>
<!-- 다중 파일 이미지 -->
<!--  (원래)
 <c:if test="${not empty imageFileList && imageFileList!='null' }">
	  <c:forEach var="item" items="${imageFileList}" varStatus="status" >
		    <tr>
			    <td width="150" align="center" bgcolor="#FF9933"  rowspan="2">
			   </td>
			   <td>
			     <input  type= "hidden"   name="originalFileName" value="${item.imageFileName }" />			     
			    <img src="${contextPath}/download.do?articleNO=${article.articleNO}&imageFileName=${item.imageFileName}" id="preview" /><br>
			   </td>   
			  </tr>  
			  <tr>
			    <td> 
			       <input  type="file"  name="imageFileName" id="i_imageFileName" disabled   onchange="readURL(this);"   />
			    </td>
			 </tr>
		</c:forEach>
 </c:if>
 -->
 <!--  테스트용  -->	   
 <c:if test="${not empty imageFileList && imageFileList!='null' }">					   
	  <c:forEach var="item" items="${imageFileList}" varStatus="status" >
		    <tr>
			    <td width="150" align="center" bgcolor="#FF9933"  rowspan="2">
			   </td>
			   <td>     
			   <!--추가. originalFileName에서 originalFileName${status.index}로  바꾼다. -->     
			   <!--수정 . originalFileName에 class 요소 추가. disabled 추가해서 수정반영하기 버튼 누르면, 해당하는 input 값이 수정하는 경우에 거기에 상응하는 번호의 originalFileName만 보내기  -->             
			     <input  type= "hidden"   name="originalFileName${status.index }" id="i_originalFileName${status.index }" class="originalFileName" value="${item.imageFileName }" disabled />	

			    <c:set var="previewId" value="preview${status.index }"/>			   
 	
			     <!-- id="preview"에서 ${previewId}로 바꿈 -->				     	     
			    <img src="${contextPath}/download.do?articleNO=${article.articleNO}&imageFileName=${item.imageFileName}" id="${previewId}" width="300" height="300" /><br>
			    
			   </td>   
			  </tr>  
			  <tr>
			    <td>  
			    <!--  인자로  img 태그 id값도 보낸다. -->
			    <!-- 1. id를 i_imageFileName에서 i_imageFileName${status.index} 로 바꾼다 -->
			    <!-- 2. name을 imageFileName에서 imageFileName${status.index}로 바꾼다. -->
			    <!-- 3.input에  class를 추가한다.  -->
			    <!-- 4. id값을 imageFileNO로 바꾼다.  -->
			       <input  type="file"  name="imageFileName${status.index }" id="i_imageFileName${status.index }" class="imageFileName" disabled   onchange="readURL(this, '${previewId}');"   />
			       <!-- hidden 태그 -->
			       <input type="hidden" name="imageFileNO${status.index }" id="i_imageFileNO${status.index }" class="imageFileNO" value="${item.imageFileNO }" disabled />
			    </td>
			 </tr>	
		</c:forEach>		
		
		<!--  여기다  imageFileList의 size  -->
		<!--  imageFileList의 길이를 알려주기 위해  -->
		<c:set var="listSize" value="${imageFileList.size() }" />	
		<input type="hidden" name="listSize" value="${listSize }" />   		
 </c:if>
 
 
   
  
 <!-- 단일파일 업로드    -->
 <!--  
  <c:choose> 
	  <c:when test="${not empty article.imageFileName && article.imageFileName!='null' }">
	   	<tr>
		    <td width="150" align="center" bgcolor="#FF9933"  rowspan="2">
		      이미지
		   </td>
		   <td>
		     <input  type= "hidden"   name="originalFileName" value="${article.imageFileName }" />
		    <img src="${contextPath}/download.do?articleNO=${article.articleNO}&imageFileName=${article.imageFileName}" id="preview"  /><br>
		   </td>   
		  </tr>  
		  <tr>
		    <td ></td>
		    <td>
		       <input  type="file"  name="imageFileName " id="i_imageFileName"   disabled   onchange="readURL(this);"   />
		    </td>
		  </tr> 
		 </c:when>
		 <c:otherwise>
		    <tr  id="tr_file_upload" >
				    <td width="150" align="center" bgcolor="#FF9933"  rowspan="2">
				      이미지
				    </td>
				    <td>
				      <input  type= "hidden"   name="originalFileName" value="${article.imageFileName }" />
				    </td>
			    </tr>
			    <tr>
				    <td ></td>
				    <td>
				       <img id="preview"  /><br>
				       <input  type="file"  name="imageFileName " id="i_imageFileName"   disabled   onchange="readURL(this);"   />
				    </td>
			  </tr>
		 </c:otherwise>
	 </c:choose> -->
  <tr>
	   <td width="150" align="center" bgcolor="#FF9933">
	      등록일자
	   </td>
	   <td>
	    <input type=text value="<fmt:formatDate value="${article.writeDate}" />" disabled />
	   </td>   
  </tr>
  <tr   id="tr_btn_modify"  align="center"  >
	   <td colspan="2"   >
	       <input type=button value="수정반영하기"   onClick="fn_modify_article(frmArticle)"  >
           <input type=button value="취소"  onClick="backToList(frmArticle)">
	   </td>   
  </tr>
    
  <tr  id="tr_btn"    >
   <td colspan="2" align="center">
       <c:if test="${member.id == article.id }">
	      <input type=button value="수정하기" onClick="fn_enable(this.form)">
	      <input type=button value="삭제하기" onClick="fn_remove_article('${contextPath}/board/removeArticle.do', ${article.articleNO})">
	    </c:if>
	    <input type=button value="리스트로 돌아가기"  onClick="backToList(this.form)">
	     <input type=button value="답글쓰기"  onClick="fn_reply_form('${contextPath}/board/replyForm.do', ${article.articleNO})">
   </td>
  </tr>
 </table>
 </form>
</body>
</html>
