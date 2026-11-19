<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<div class="card shadow-sm mb-4">
    <div class="card-header bg-primary text-white fw-semibold">
        <i class="fa-solid fa-bars me-2"></i>Điều hướng nhanh
    </div>
    <div class="list-group list-group-flush">
        <a href="${pageContext.request.contextPath}/home" class="list-group-item list-group-item-action">
            <i class="fa-solid fa-house me-2 text-primary"></i>Trang chủ
        </a>
        <a href="${pageContext.request.contextPath}/video?scope=all" class="list-group-item list-group-item-action">
            <i class="fa-solid fa-fire me-2 text-danger"></i>Tất cả Videos
        </a>
        <a href="${pageContext.request.contextPath}/video" class="list-group-item list-group-item-action">
            <i class="fa-solid fa-user-check me-2 text-success"></i>Video của tôi
        </a>
    </div>
</div>
