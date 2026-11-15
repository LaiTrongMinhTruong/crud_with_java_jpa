<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản lý danh mục</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; background-color: #f4f6f9; }
    .container { max-width: 1000px; margin: 30px auto; background: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); }
    .header-box { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #0088cc; padding-bottom: 15px; margin-bottom: 20px; }
    h2 { margin: 0; color: #333; font-size: 22px; }
    .btn-add { background: #28a745; color: #fff; padding: 10px 18px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px; }
    .btn-add:hover { background: #218838; }
    table { width: 100%; border-collapse: collapse; margin-top: 15px; }
    th, td { border: 1px solid #dee2e6; padding: 12px; text-align: center; }
    th { background: #0088cc; color: #fff; font-weight: 600; }
    tr:nth-child(even) { background: #f8f9fa; }
    tr:hover { background: #f1f3f5; }
    .cate-img { width: 120px; height: 90px; object-fit: cover; border-radius: 4px; border: 1px solid #ddd; }
    .action-links a { text-decoration: none; font-weight: 500; margin: 0 5px; }
    .action-edit { color: #007bff; }
    .action-delete { color: #dc3545; }
    .action-links a:hover { text-decoration: underline; }
</style>
</head>
<body>
<jsp:include page="/views/topbar.jsp" />
<div class="container">
    <div class="header-box">
        <h2>Quản lý danh mục</h2>
        <a href="<c:url value='/admin/category/add'/>" class="btn-add"><i class="fa fa-plus"></i> Thêm danh mục mới</a>
    </div>

    <table>
        <thead>
            <tr>
                <th style="width: 60px;">STT</th>
                <th style="width: 160px;">Hình ảnh</th>
                <th>Tên danh mục</th>
                <th style="width: 150px;">Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${cateList}" var="cate" varStatus="STT">
                <tr class="odd gradeX">
                    <td>${STT.index + 1}</td>
                    <c:url value="/image?fname=${cate.icon}" var="imgUrl"></c:url>
                    <td>
                        <c:choose>
                            <c:when test="${cate.icon != null && cate.icon.startsWith('http')}">
                                <img class="cate-img" src="${cate.icon}" />
                            </c:when>
                            <c:otherwise>
                                <img class="cate-img" src="${imgUrl}" />
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td><strong>${cate.catename}</strong></td>
                    <td class="action-links">
                        <a href="<c:url value='/admin/category/edit?id=${cate.cateid}'/>" class="action-edit"><i class="fa fa-edit"></i> Sửa</a>
                        |
                        <a href="<c:url value='/admin/category/delete?id=${cate.cateid}'/>" class="action-delete" onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục này?');"><i class="fa fa-trash"></i> Xóa</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
