<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm Video Mới - Tải lên Cloudinary</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; margin: 0; background-color: #f4f6f9; color: #333; }
    .container { max-width: 680px; margin: 35px auto; background: #fff; padding: 30px 35px; border-radius: 8px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
    h2 { margin-top: 0; margin-bottom: 20px; color: #333; border-bottom: 2px solid #0088cc; padding-bottom: 12px; font-size: 22px; display: flex; align-items: center; gap: 10px; }
    .form-group { margin-bottom: 20px; }
    label { display: block; font-weight: 600; margin-bottom: 8px; color: #333; font-size: 14px; }
    .form-control { width: 100%; padding: 10px 14px; border: 1px solid #ced4da; border-radius: 5px; font-size: 14px; outline: none; }
    .form-control:focus { border-color: #0088cc; box-shadow: 0 0 0 3px rgba(0,136,204,0.15); }
    textarea.form-control { resize: vertical; min-height: 90px; }
    .upload-card { background: #f8f9fa; border: 1px dashed #0088cc; padding: 15px; border-radius: 6px; margin-top: 6px; }
    .upload-note { font-size: 12px; color: #666; margin-top: 6px; }
    .cloud-badge { display: inline-block; background: #e0f2fe; color: #0284c7; padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; margin-left: 6px; }
    .btn-group { display: flex; gap: 12px; margin-top: 25px; }
    .btn { padding: 11px 22px; border: none; border-radius: 5px; font-size: 14px; font-weight: bold; cursor: pointer; transition: background .2s; }
    .btn-submit { background: #0088cc; color: #fff; }
    .btn-submit:hover { background: #0073ab; }
    .btn-reset { background: #6c757d; color: #fff; }
    .btn-reset:hover { background: #5a6268; }
    .back-link { display: inline-flex; align-items: center; gap: 6px; margin-top: 20px; color: #0088cc; text-decoration: none; font-size: 14px; font-weight: 600; }
    .back-link:hover { text-decoration: underline; }
</style>
</head>
<body>

<div class="container">
    <h2><i class="fa fa-cloud-upload" style="color: #0088cc;"></i> Thêm Video Mới</h2>
    <c:if test="${alert != null}">
        <div class="alert alert-danger">${alert}</div>
    </c:if>

    <form action="<c:url value='/admin/video/add'/>" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
        <div class="form-group">
            <label>Tiêu đề Video: <span style="color: red;">*</span></label>
            <input type="text" name="title" value="${title}" class="form-control" placeholder="Nhập tiêu đề video..." required minlength="3" maxlength="200" autofocus />
            <div class="invalid-feedback">Tiêu đề video phải từ 3 đến 200 ký tự.</div>
        </div>

        <div class="form-group">
            <label>Mô tả Video:</label>
            <textarea name="description" class="form-control" placeholder="Nhập mô tả chi tiết về video..."></textarea>
        </div>

        <!-- UPLOAD ẢNH POSTER QUA MULTIPART / CLOUDINARY -->
        <div class="form-group">
            <label>
                Ảnh đại diện (Poster / Thumbnail):
                <span class="cloud-badge"><i class="fa fa-cloud"></i> Hỗ trợ Cloudinary</span>
            </label>
            <div class="upload-card">
                <input type="file" name="posterFile" accept="image/*" class="form-control" style="background: #fff;" />
                <div class="upload-note">
                    <i class="fa fa-info-circle"></i> Chọn file ảnh từ máy tính (JPEG, PNG, WebP) để tự động đẩy lên <strong>Cloudinary</strong>.
                </div>
            </div>
            <div style="margin-top: 10px;">
                <label style="font-weight: normal; font-size: 13px; color: #666;">Hoặc dán URL ảnh trực tiếp nếu không upload file:</label>
                <input type="url" name="posterUrl" class="form-control" placeholder="https://res.cloudinary.com/... hoặc link ảnh online" />
            </div>
        </div>

        <!-- UPLOAD FILE VIDEO QUA MULTIPART / CLOUDINARY HOẶC YOUTUBE -->
        <div class="form-group">
            <label>
                Nguồn phát Video:
                <span class="cloud-badge"><i class="fa fa-cloud"></i> Hỗ trợ Cloudinary Video</span>
            </label>
            <div class="upload-card">
                <input type="file" name="videoFile" accept="video/mp4,video/webm,video/ogg" class="form-control" style="background: #fff;" />
                <div class="upload-note">
                    <i class="fa fa-info-circle"></i> Chọn file video MP4 từ máy tính để tải trực tiếp lên <strong>Cloudinary</strong>.
                </div>
            </div>
            <div style="margin-top: 10px;">
                <label style="font-weight: normal; font-size: 13px; color: #666;">Hoặc nhập URL video online / Link YouTube:</label>
                <input type="text" name="videoUrl" class="form-control" placeholder="Ví dụ: https://www.youtube.com/watch?v=... hoặc link mp4 trực tiếp" />
            </div>
        </div>

        <div class="form-group" style="margin-top: 15px;">
            <label style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
                <input type="checkbox" name="active" checked style="width: 18px; height: 18px;" />
                <span>Kích hoạt hiển thị công khai ngay sau khi tạo</span>
            </label>
        </div>

        <div class="btn-group">
            <button type="submit" class="btn btn-submit">
                <i class="fa fa-check"></i> Lưu &amp; Đăng Tải Video
            </button>
            <button type="reset" class="btn btn-reset">
                <i class="fa fa-refresh"></i> Làm lại
            </button>
        </div>
    </form>

    <a href="<c:url value='/admin/video/list'/>" class="back-link">
        <i class="fa fa-arrow-left"></i> Quay lại danh sách quản lý Videos
    </a>
</div>
</body>
</html>
