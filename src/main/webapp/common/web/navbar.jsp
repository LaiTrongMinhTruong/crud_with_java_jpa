<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
            <i class="fa-solid fa-clapperboard text-primary me-2"></i>VideoPortal
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#webNavbar"
                aria-controls="webNavbar" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="webNavbar">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">
                        <i class="fa-solid fa-house me-1"></i>Trang Chủ
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/video">
                        <i class="fa-solid fa-circle-play me-1"></i>Videos
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/video/list">
                        <i class="fa-solid fa-film me-1"></i>Quản lý Videos
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/category/list">
                        <i class="fa-solid fa-layer-group me-1"></i>Quản lý Danh mục
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
