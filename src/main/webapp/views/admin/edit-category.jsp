<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chỉnh sửa danh mục</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; background-color: #f4f6f9; }
    .container { max-width: 600px; margin: 40px auto; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); }
    h2 { margin-top: 0; margin-bottom: 25px; color: #333; border-bottom: 2px solid #0088cc; padding-bottom: 12px; }
    .form-group { margin-bottom: 20px; }
    label { display: block; font-weight: 600; margin-bottom: 8px; color: #444; }
    .form-control { width: 100%; padding: 10px 14px; border: 1px solid #ced4da; border-radius: 4px; font-size: 14px; outline: none; }
    .form-control:focus { border-color: #0088cc; }
    .current-img { width: 120px; height: 90px; object-fit: cover; border-radius: 4px; border: 1px solid #ddd; margin-bottom: 10px; display: block; }
    .btn-group { display: flex; gap: 10px; margin-top: 25px; }
    .btn { padding: 10px 20px; border: none; border-radius: 4px; font-size: 14px; font-weight: bold; cursor: pointer; }
    .btn-default { background: #0088cc; color: #fff; }
    .btn-default:hover { background: #0077b3; }
    .btn-primary { background: #6c757d; color: #fff; }
    .btn-primary:hover { background: #5a6268; }
    .back-link { display: inline-block; margin-top: 15px; color: #0088cc; text-decoration: none; font-size: 14px; }
    .back-link:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="container">
    <h2>Chỉnh sửa danh mục</h2>
    <c:if test="${alert != null}">
        <div class="alert alert-danger">${alert}</div>
    </c:if>
    <c:url value="/admin/category/edit?id=${category.cateid}" var="editUrl"></c:url>
    <form role="form" action="${editUrl}" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
        <input type="hidden" name="id" value="${category.cateid}">
        <input type="hidden" name="cateid" value="${category.cateid}">

        <div class="form-group">
            <label>Tên danh mục:</label>
            <input type="text" class="form-control" value="${category.name}" name="catename" required minlength="2" maxlength="100" />
            <div class="invalid-feedback">Tên danh mục phải từ 2 đến 100 ký tự.</div>
        </div>

        <div class="form-group">
            <label>Ảnh đại diện hiện tại:</label>
            <c:url value="/image?fname=${category.icon}" var="imgUrl"></c:url>
            <c:choose>
                <c:when test="${category.icon != null && category.icon.startsWith('http')}">
                    <img class="current-img" src="${category.icon}" alt="Ảnh danh mục" />
                </c:when>
                <c:otherwise>
                    <img class="current-img" src="${imgUrl}" alt="Ảnh danh mục" />
                </c:otherwise>
            </c:choose>
            <label>Chọn ảnh mới (nếu muốn thay đổi ảnh):</label>
            <input type="file" name="icon" accept="image/*" />
        </div>

        <div class="btn-group">
            <button type="submit" class="btn btn-default">Cập nhật (Edit)</button>
            <button type="reset" class="btn btn-primary">Khôi phục (Reset)</button>
        </div>
    </form>
    <a href="<c:url value='/admin/category/list'/>" class="back-link"><i class="fa fa-arrow-left"></i> Quay lại danh sách danh mục</a>
</div>
</body>
</html>
