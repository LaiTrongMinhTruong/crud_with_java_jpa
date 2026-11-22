<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ cá nhân - ${user.fullName}</title>
    <style>
        .avatar-preview {
            width: 150px;
            height: 150px;
            object-fit: cover;
            border-radius: 50%;
            border: 4px solid #e9ecef;
            box-shadow: 0 4px 12px rgba(0,0,0,0.08);
        }
    </style>
</head>
<body>
<div class="container py-4">
    <div class="row g-4">
        <!-- Cột trái: Thông tin tóm tắt & Avatar -->
        <div class="col-lg-4">
            <div class="card shadow-sm border-0">
                <div class="card-body text-center p-4">
                    <c:choose>
                        <c:when test="${not empty user.avatar && user.avatar.startsWith('http')}">
                            <img id="avatarPreview" src="${user.avatar}" alt="Avatar" class="avatar-preview mb-3"
                                 onerror="this.src='https://ui-avatars.com/api/?name=${user.userName}&background=0d6efd&color=fff&size=150';">
                        </c:when>
                        <c:when test="${not empty user.avatar}">
                            <img id="avatarPreview" src="${pageContext.request.contextPath}/image?fname=${user.avatar}" alt="Avatar" class="avatar-preview mb-3"
                                 onerror="this.src='https://ui-avatars.com/api/?name=${user.userName}&background=0d6efd&color=fff&size=150';">
                        </c:when>
                        <c:otherwise>
                            <img id="avatarPreview" src="https://ui-avatars.com/api/?name=${user.userName}&background=0d6efd&color=fff&size=150" alt="Avatar" class="avatar-preview mb-3">
                        </c:otherwise>
                    </c:choose>

                    <h5 class="fw-bold mb-1">${user.fullName}</h5>
                    <p class="text-muted mb-2">@${user.userName}</p>
                    <span class="badge bg-primary mb-3">
                        <c:choose>
                            <c:when test="${user.roleid == 1}">Quản trị viên (Admin)</c:when>
                            <c:when test="${user.roleid == 2}">Quản lý (Manager)</c:when>
                            <c:otherwise>Người dùng (User)</c:otherwise>
                        </c:choose>
                    </span>

                    <hr>
                    <div class="text-start small text-muted">
                        <div class="mb-2">
                            <i class="fa-solid fa-envelope me-2 text-primary"></i>
                            <strong>Email:</strong> ${user.email}
                        </div>
                        <div class="mb-2">
                            <i class="fa-solid fa-phone me-2 text-success"></i>
                            <strong>SĐT:</strong> ${not empty user.phone ? user.phone : 'Chưa cập nhật'}
                        </div>
                        <c:if test="${user.createdDate != null}">
                            <div>
                                <i class="fa-solid fa-calendar-days me-2 text-secondary"></i>
                                <strong>Ngày tham gia:</strong> ${user.createdDate}
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Cột phải: Xác nhận OTP đổi Email (nếu có) & Form cập nhật Profile -->
        <div class="col-lg-8">
            <c:if test="${not empty sessionScope.pendingEmail}">
                <div class="card border-warning shadow-sm mb-4">
                    <div class="card-header bg-warning bg-opacity-25 fw-bold text-dark">
                        <i class="fa-solid fa-shield-halved me-2 text-warning"></i>Xác nhận OTP đổi Email mới
                    </div>
                    <div class="card-body">
                        <p class="mb-3">
                            Mã xác thực 6 chữ số đã được gửi tới email mới:
                            <strong class="text-primary">${sessionScope.pendingEmail}</strong>.
                            Vui lòng nhập mã OTP để hoàn tất cập nhật email.
                        </p>
                        <form action="${pageContext.request.contextPath}/profile/verify-email" method="post" class="needs-validation" novalidate>
                            <div class="row g-2 align-items-start">
                                <div class="col-sm-6">
                                    <input type="text" name="otp" class="form-control" placeholder="Nhập mã OTP 6 số"
                                           required pattern="^[0-9]{6}$" maxlength="6">
                                    <div class="invalid-feedback">Mã OTP phải gồm đúng 6 chữ số.</div>
                                </div>
                                <div class="col-sm-auto">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fa-solid fa-check me-1"></i>Xác nhận OTP
                                    </button>
                                </div>
                            </div>
                        </form>
                        <div class="d-flex gap-3 mt-3 align-items-center">
                            <form action="${pageContext.request.contextPath}/profile/resend-email-otp" method="post" class="d-inline">
                                <button type="submit" class="btn btn-link btn-sm p-0 text-decoration-none">
                                    <i class="fa-solid fa-rotate-right me-1"></i>Gửi lại mã OTP
                                </button>
                            </form>
                            <span class="text-muted">|</span>
                            <a href="${pageContext.request.contextPath}/profile?cancelEmail=true" class="text-danger small text-decoration-none">
                                <i class="fa-solid fa-xmark me-1"></i>Hủy đổi email
                            </a>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="card shadow-sm border-0">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0 fw-bold text-primary">
                        <i class="fa-solid fa-user-pen me-2"></i>Cập nhật thông tin cá nhân
                    </h5>
                </div>
                <div class="card-body p-4">
                    <form action="${pageContext.request.contextPath}/profile" method="post" enctype="multipart/form-data"
                          class="needs-validation" novalidate>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Tên đăng nhập</label>
                            <input type="text" class="form-control bg-light" value="${user.userName}" readonly>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Họ và tên <span class="text-danger">*</span></label>
                            <input type="text" name="fullname" value="${user.fullName}" class="form-control"
                                   placeholder="Nhập họ và tên" required minlength="2" maxlength="100">
                            <div class="invalid-feedback">Họ và tên phải từ 2 đến 100 ký tự.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">Số điện thoại <span class="text-danger">*</span></label>
                            <input type="text" name="phone" value="${user.phone}" class="form-control"
                                   placeholder="Ví dụ: 0912345678" required pattern="^0[0-9]{9,10}$">
                            <div class="invalid-feedback">Số điện thoại phải bắt đầu bằng 0 và gồm 10–11 chữ số.</div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-semibold">
                                Email <span class="text-danger">*</span>
                                <small class="text-muted fw-normal">(Nếu đổi email mới, hệ thống sẽ gửi OTP xác nhận)</small>
                            </label>
                            <input type="email" name="email"
                                   value="${not empty sessionScope.pendingEmail ? sessionScope.pendingEmail : user.email}"
                                   class="form-control" placeholder="example@gmail.com" required>
                            <div class="invalid-feedback">Vui lòng nhập đúng định dạng email.</div>
                        </div>

                        <div class="mb-4">
                            <label class="form-label fw-semibold">
                                Ảnh đại diện (Images)
                                <span class="badge bg-info text-dark ms-1">
                                    <i class="fa-solid fa-cloud-arrow-up me-1"></i>Cloudinary
                                </span>
                            </label>
                            <input type="file" name="avatarFile" id="avatarInput" accept="image/*" class="form-control">
                            <div class="form-text">Chọn ảnh JPG, PNG, WebP hoặc GIF để tải lên Cloudinary (để trống nếu giữ ảnh hiện tại).</div>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary px-4">
                                <i class="fa-solid fa-floppy-disk me-1"></i>Lưu thay đổi
                            </button>
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">
                                Quay lại trang chủ
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('avatarInput')?.addEventListener('change', function (e) {
        const file = e.target.files && e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (ev) {
                const img = document.getElementById('avatarPreview');
                if (img) img.src = ev.target.result;
            };
            reader.readAsDataURL(file);
        }
    });
</script>
</body>
</html>
