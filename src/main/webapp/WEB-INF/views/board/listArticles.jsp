<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!-- 페이징 관련 추가 -->
<c:set var="articlesList" value="${articlesMap.articlesList }"/>
<c:set var="totArticles" value="${articlesMap.totArticles }"/>
<c:set var="section" value="${articlesMap.section }"/>
<c:set var="pageNum" value="${articlesMap.pageNum }"/>

<%
  request.setCharacterEncoding("UTF-8");
%>  
<!DOCTYPE html>
<html>
<head>
 <style>
    <!-- 페이징 관련 설정 -->
    .no-uline{text-decoration:none;}
    .sel-page{text-decoration:none; color:red;}
    
   .cls1 {text-decoration:none;}
   .cls2{text-align:center; font-size:30px;}
  </style>
  <meta charset="UTF-8">
  <title>글목록창</title>
</head>
<script>
	function fn_articleForm(isLogOn,articleForm,loginForm){
	  if(isLogOn != '' && isLogOn != 'false'){
	    location.href=articleForm;
	  }else{
	    alert("로그인 후 글쓰기가 가능합니다.")
	    location.href=loginForm+'?action=/board/articleForm.do';
	  }
	}
</script>
<body>
<table align="center" border="1"  width="80%"  >
  <tr height="10" align="center"  bgcolor="lightgreen">
     <td >글번호</td>
     <td >작성자</td>              
     <td >제목</td>
     <td >작성일</td>
  </tr>
<c:choose>
  <c:when test="${empty articlesList }" >
    <tr  height="10">
      <td colspan="4">
         <p align="center">
            <b><span style="font-size:9pt;">등록된 글이 없습니다.</span></b>
        </p>
      </td>  
    </tr>
  </c:when>
  <c:when test="${!empty articlesList }" >
    <c:forEach  var="article" items="${articlesList }" varStatus="articleNum" >
     <tr align="center">
	<td width="5%">${articleNum.count}</td>
	<td width="10%">${article.id }</td>
	<td align='left'  width="35%">
	  <span style="padding-right:30px"></span>
	   <c:choose>
	      <c:when test='${article.level > 1 }'>  
	         <c:forEach begin="1" end="${article.level }" step="1">
	              <span style="padding-left:20px"></span>    
	         </c:forEach>
	         <span style="font-size:12px;">[답변]</span>
                   <a class='cls1' href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title}</a>
	          </c:when>
	          <c:otherwise>
	            <a class='cls1' href="${contextPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title }</a>
	          </c:otherwise>
	        </c:choose>
	  </td>
	  <td  width="10%">${article.writeDate}</td> 
	</tr>
    </c:forEach>
     </c:when>
    </c:choose>
</table>

<!--페이징 코드 추가 -->

<div class="cls2">
    <c:if test="${totArticles != null }">
        <c:choose>
             <c:when test="${totArticles > 100}">                                 
               <!-- / 로 나눴을 때 정수 값만 나오게 하기 위하여 fmt 태그 이용  -->
               <!-- 아래는 next가 전체 글 수에 상응되는 만큼만 표시되도록하고, 마지막 부분(section)의 페이지도 전체 글 수만큼만 표기되도록 하는 코드-->
               <c:choose>           
                    
                   <c:when test="${(totArticles%100)>0 }">
                        <fmt:parseNumber var="test" value="${totArticles/100 }" integerOnly="true"/>  
                        <c:set var="lastSec" value="${test +1}"/>
                   </c:when>                                    
                   <c:otherwise>
                       <fmt:parseNumber var="test" value="${totArticles/100 }" integerOnly="true"/>  
                      <c:set var="lastSec" value="${test }"/>
                   </c:otherwise>
               </c:choose> 
                             
               <!-- 추가 - 마지막 section값일 경우 forEach의 end 변수 구하기 -->
               <!-- / 기호를 사용해서 나눌 때 소수점 이하를 제거시켜주어야 하므로 fmt 사용  -->
                <fmt:parseNumber var="test1" value="${(totArticles%100)/10 }" integerOnly="true"/>
               <c:choose>             
                   <c:when test="${test1>0}">                   
                       <c:set var="end" value="${test1+1 }"/>
                   </c:when>
                   <c:otherwise>
                       <c:set var="end" value="${test1}"/>
                   </c:otherwise>
               </c:choose>
             
               <!-- 마지막 section인 경우와 아닌 경우로 나눠서 -->
             <c:choose>  
                  <c:when test="${section==lastSec }"> 
		                <c:forEach var="page" begin="1" end="${end }" step="1">
		                    <c:if test="${section > 1 && page == 1 }">  
		                       <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section-1}&pageNum=1">pre</a>              
		                    </c:if>		                   
		                    <c:choose>
		                       <c:when test="${page==pageNum }">
		                          <a class="sel-page" href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
		                       </c:when>
		                       <c:otherwise>
		                          <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
		                       </c:otherwise>
		                    </c:choose>
		                    
		                    <c:if test="${page==10 }">
		                       <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section+1}&pageNum=1">next</a>
		                    </c:if>
		                </c:forEach>
		             </c:when>
		             
		             
		             <c:otherwise>
		                 <c:forEach var="page" begin="1" end="10" step="1">
		                    <c:if test="${section > 1 && page == 1 }">   
		                       <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section-1}&pageNum=1">pre</a>              
		                    </c:if>	                  
		                    <c:choose>
		                       <c:when test="${page==pageNum }">
		                          <a class="sel-page" href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
		                       </c:when>
		                       <c:otherwise>
		                          <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
		                       </c:otherwise>
		                    </c:choose>
		                    
		                    <c:if test="${page==10 }">
		                       <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section+1}&pageNum=1">next</a>
		                    </c:if>
		                </c:forEach>
		             </c:otherwise>
		       </c:choose>
             </c:when>
             
             <c:when test="${totArticles == 100 }">
                <c:forEach var="page" begin="1" end="10" step="1">              
                  <c:choose>
                     <c:when test="${page==pageNum }">
                         <a class="sel-page" href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${page }</a>
                     </c:when>
                     <c:otherwise>
                         <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${page }</a>
                     </c:otherwise>
                  </c:choose>                 
                </c:forEach>                
             </c:when>
             
             <c:when test="${totArticles<100 }">
             
	              <!-- 마지막 section에서  x0(예 - 50)개인지 xx(예- 53) 개인지에 따라 유동적으로 페이지 조절해주기 위해 변수 추가-->
	              <c:choose>
	                  <c:when test="${totArticles }==0">
	                      <c:set var="end" value="${totArticles/10 }"/>
	                  </c:when>
	                  <c:otherwise>
	                      <c:set var="end" value="${totArticles/10 + 1 }"/>
	                  </c:otherwise>
	              </c:choose>  
                  
                  <c:forEach var="page" begin="1" end="${end }" step="1">
                      <c:choose>
                         <c:when test="${page==pageNum }">  
                             <a class="sel-page" href="${contextPath }/board/listArticles.do?section=${section}&pageNum=${page}">${page }</a>
                         </c:when>
                         <c:otherwise>
                             <a class="no-uline" href="${contextPath }/board/listArticles.do?section=${section }&pageNum=${page }">${page }</a>
                         </c:otherwise>
                      </c:choose>
                  </c:forEach>
            
             </c:when>
        </c:choose>
    </c:if>
</div>



<a  class="cls1"  href="javascript:fn_articleForm('${isLogOn}','${contextPath}/board/articleForm.do', 
                                                    '${contextPath}/member/loginForm.do')"><p class="cls2">글쓰기</p></a>
 <!-- 임의의 테스트 글 추가 버튼 -->
<form method="post"  action="${contextPath}/board/addNewTestArticle.do" >                                                    
    <input type="number"  name="number"/>
    <input type="submit" value="테스트 글 추가"/>
</form> 
 <!-- 임의의 테스트 글 삭제 버튼 -->
<form method="post" action="${contextPath }/board/deleteTestArticle.do">
    <input type="number"  name="deleteNumber"/>
    <input type="submit" value="테스트 글 삭제" />
</form>
</body>
</html>