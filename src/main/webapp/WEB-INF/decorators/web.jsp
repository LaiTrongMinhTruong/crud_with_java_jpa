<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title" default="VideoPortal" /></title>
    <!-- Bootstrap 5.3.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome 6 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <!-- Custom Web Template CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/templates/web/css/style.css">
    <sitemesh:write property="head" />
</head>
<body>
    <%@ include file="/common/web/header.jsp" %>

    <main class="main-content py-3">
        <div class="container">
            <%@ include file="/common/info.jsp" %>
        </div>
        <sitemesh:write property="body" />
    </main>

    <%@ include file="/common/web/footer.jsp" %>

    <!-- Bootstrap 5.3.3 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
