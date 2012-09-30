<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<img class="errorImg" src='<spring:url value="images/error/nosignal.gif" />'>
<div class="errorMessage"><spring:message code="error.uncaught"></spring:message> </div>
<a class="errorGotohome" href="<spring:url value="/" />"><spring:message code="error.gotohome"></spring:message></a>