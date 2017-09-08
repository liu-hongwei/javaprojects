<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%-- 
    Document   : index
    Created on : Aug 3, 2017, 2:28:35 PM
    Author     : eybcegc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%--<sql:query var="categories" dataSource="jdbc/affablebean">
    select * from category
</sql:query>--%>
<div id="indexLeftColumn">
    <div id="welcomeText">
        <p>[ welcome text ]</p>
    </div>
</div>
<div id="indexRightColumn">
    <c:forEach var="category" items="${categories}">
    <div class="categoryBox">
        <a href="category?${category.id}">
            <span class="categoryLabelText">${category.name}</span>
            <img src="${initParam.categoryImagePath}${category.name}.jpg" alt="${category.name}">
        </a>
    </div>
    </c:forEach>

</div>