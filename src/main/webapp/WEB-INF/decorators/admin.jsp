<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title" default="Admin Dashboard" /></title>
    <!-- Bootstrap 5.3.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome 6 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <!-- Custom Admin Template CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/templates/admin/css/admin.css">
    <sitemesh:write property="head" />
</head>
<body>
    <%@ include file="/common/admin/header.jsp" %>

    <div class="admin-wrapper">
        <%@ include file="/common/admin/left.jsp" %>

        <div class="admin-content">
            <main class="p-4 flex-grow-1">
                <%@ include file="/common/info.jsp" %>
                <sitemesh:write property="body" />
            </main>
            <%@ include file="/common/admin/footer.jsp" %>
        </div>
    </div>

    <!-- Bootstrap 5.3.3 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        (() => {
            'use strict';
            const forms = document.querySelectorAll('.needs-validation');
            Array.from(forms).forEach(form => {
                form.addEventListener('submit', event => {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>
</body>
</html>
