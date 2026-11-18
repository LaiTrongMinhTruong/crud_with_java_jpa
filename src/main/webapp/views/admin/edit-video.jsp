<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chỉnh Sửa Video - ID: ${video.videoId}</title>
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
    .preview-box { display: flex; align-items: center; gap: 15px; margin-bottom: 10px; background: #f1f3f5; padding: 10px; border-radius: 6px; }
    .thumb-preview { width: 100px; height: 60px; object-fit: cover; border-radius: 4px; border: 1px solid #ccc; }
    .cloud-badge { display: inline-block; background: #e0f2fe; color: #0284c7; padding: 2px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; margin-left: 6px; }
    .btn-group { display: flex; gap: 12px; margin-top: 25px; }
    .btn { padding: 11px 22px; border: none; border-radius: 5px; font-size: 14px; font-weight: bold; cursor: pointer; transition: background .2s; }
    .btn-submit { background: #0088cc; color: #fff; }
    .btn-submit:hover { background: #0073ab; }
    .btn-reset { background: #6c757d; color: #fff; text-decoration: none; display: inline-flex; align-items: center; justify-content: center; }
    .btn-reset:hover { background: #5a6268; }
    .back-link { display: inline-flex; align-items: center; gap: 6px; margin-top: 20px; color: #0088cc; text-decoration: none; font-size: 14px; font-weight: 600; }
    .back-link:hover { text-decoration: underline; }
</style>
</head>
<body>
<jsp:include page="/views/topbar.jsp" />

<div class="container">
    <h2><i class="fa fa-pencil-square-o" style="color: #0088cc;"></i> Chỉnh Sửa Video</h2>

    <form action="<c:url value='/admin/video/edit'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${video.videoId}" />

        <div class="form-group">
            <label>Tiêu đề Video: <span style="color: red;">*</span></label>
            <input type="text" name="title" value="${video.title}" class="form-control" required autofocus />
        </div>

        <div class="form-group">
            <label>Mô tả Video:</label>
            <textarea name="description" class="form-control">${video.description}</textarea>
        </div>

        <!-- POSTER HIỆN TẠI VÀ CẬP NHẬT MỚI -->
        <div class="form-group">
            <label>
                Ảnh đại diện (Poster / Thumbnail):
                <span class="cloud-badge"><i class="fa fa-cloud"></i> Hỗ trợ Cloudinary</span>
            </label>
            <c:if test="${not empty video.poster}">
                <div class="preview-box">
                    <img src="${video.poster}" alt="Poster hiện tại" class="thumb-preview" onerror="this.src='https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600&auto=format&fit=crop&q=80';">
                    <div style="font-size: 12px; color: #555; word-break: break-all;">
                        <strong>Poster hiện tại:</strong> ${video.poster}
                    </div>
                </div>
            </c:if>

            <div class="upload-card">
                <input type="file" name="posterFile" accept="image/*" class="form-control" style="background: #fff;" />
                <div class="upload-note">
                    <i class="fa fa-info-circle"></i> Chọn file ảnh mới nếu muốn thay đổi (sẽ tự động đẩy lên <strong>Cloudinary</strong>).
                </div>
            </div>
            <div style="margin-top: 10px;">
                <label style="font-weight: normal; font-size: 13px; color: #666;">Hoặc sửa trực tiếp link URL ảnh:</label>
                <input type="url" name="posterUrl" value="${video.poster}" class="form-control" />
            </div>
        </div>

        <!-- VIDEO FILE HIỆN TẠI VÀ CẬP NHẬT MỚI -->
        <div class="form-group">
            <label>
                Nguồn phát Video:
                <span class="cloud-badge"><i class="fa fa-cloud"></i> Hỗ trợ Cloudinary Video</span>
            </label>
            <div class="preview-box">
                <div style="font-size: 12px; color: #555; word-break: break-all;">
                    <i class="fa fa-play-circle" style="color: #0088cc;"></i> <strong>URL hiện tại:</strong> ${video.url}
                </div>
            </div>

            <div class="upload-card">
                <input type="file" name="videoFile" accept="video/mp4,video/webm,video/ogg" class="form-control" style="background: #fff;" />
                <div class="upload-note">
                    <i class="fa fa-info-circle"></i> Chọn file video mới để tải lên <strong>Cloudinary</strong> (để trống nếu giữ nguyên).
                </div>
            </div>
            <div style="margin-top: 10px;">
                <label style="font-weight: normal; font-size: 13px; color: #666;">Hoặc sửa trực tiếp link URL video (YouTube / MP4):</label>
                <input type="text" name="videoUrl" value="${video.url}" class="form-control" />
            </div>
        </div>

        <div class="form-group" style="margin-top: 15px;">
            <label style="display: flex; align-items: center; gap: 8px; cursor: pointer;">
                <input type="checkbox" name="active" ${video.active ? 'checked' : ''} style="width: 18px; height: 18px;" />
                <span>Kích hoạt hiển thị công khai</span>
            </label>
        </div>

        <div class="btn-group">
            <button type="submit" class="btn btn-submit">
                <i class="fa fa-save"></i> Cập Nhật Video
            </button>
            <a href="<c:url value='/admin/video/list'/>" class="btn btn-reset">
                Hủy bỏ
            </a>
        </div>
    </form>

    <a href="<c:url value='/admin/video/list'/>" class="back-link">
        <i class="fa fa-arrow-left"></i> Quay lại danh sách quản lý Videos
    </a>
</div>
</body>
</html>
