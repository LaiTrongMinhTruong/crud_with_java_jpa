<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<aside class="admin-sidebar py-3">
    <div class="sidebar-heading">Quản lý nội dung</div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/category/list">
                <i class="fa-solid fa-layer-group me-2"></i>Quản lý Danh mục
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/admin/video/list">
                <i class="fa-solid fa-film me-2"></i>Quản lý Videos
            </a>
        </li>
    </ul>

    <div class="sidebar-heading">Khác</div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/home">
                <i class="fa-solid fa-globe me-2"></i>Về trang Web
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/video">
                <i class="fa-solid fa-circle-play me-2"></i>Xem Videos
            </a>
        </li>
    </ul>
</aside>
