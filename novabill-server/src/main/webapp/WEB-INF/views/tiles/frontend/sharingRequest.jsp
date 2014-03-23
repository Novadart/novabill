<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>Issue sharing request here!</h3>
	<form:form modelAttribute="sharingRequest">
		<table>
          <tr>
              <td>Email:</td>
              <td><form:input path="email" /></td>
              <td><form:errors path="email" /></td>
          </tr>
          <tr>
              <td>VatID or SSN:</td>
              <td><form:input path="vatID" /></td>
              <td><form:errors path="vatID" /></td>
          </tr>
          <tr>
              <td colspan="3">
                  <input type="submit" value="Ask" />
              </td>
          </tr>
      </table>
	</form:form>
</body>
</html>