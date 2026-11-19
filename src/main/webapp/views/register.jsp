<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tạo tài khoản mới</title>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    * { box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; background-color: #f4f6f9; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; padding: 20px 0; }
    .register-box { background: #fff; padding: 35px 30px; border-radius: 8px; box-shadow: 0 4px 16px rgba(0,0,0,0.08); width: 100%; max-width: 420px; }
    h2 { text-align: center; color: #555; margin-top: 0; margin-bottom: 25px; font-size: 22px; font-weight: 500; }
    .alert-danger { color: #a94442; background-color: #f2dede; border: 1px solid #ebccd1; padding: 12px 15px; border-radius: 4px; margin-bottom: 20px; font-size: 14px; text-align: center; }
    section { margin-bottom: 10px; }
    .input-group { display: flex; align-items: center; margin-bottom: 15px; border: 1px solid #d2d6de; border-radius: 4px; overflow: hidden; background: #fff; transition: border-color .2s; }
    .input-group:focus-within { border-color: #0088cc; }
    .input-group-addon { padding: 10px 14px; background: #f8f9fa; color: #888; border-right: 1px solid #d2d6de; width: 42px; text-align: center; }
    .form-control { flex: 1; border: none; padding: 12px 14px; outline: none; font-size: 14px; color: #333; }
    .btn-submit { width: 100%; background: #0088cc; color: #fff; border: none; padding: 12px; border-radius: 4px; font-size: 15px; cursor: pointer; font-weight: 600; margin-top: 10px; transition: background .2s; }
    .btn-submit:hover { background: #0073ab; }
    .bottom-note { text-align: center; margin-top: 25px; font-size: 13px; color: #777; }
    .bottom-note a { color: #0088cc; text-decoration: none; }
    .bottom-note a:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="register-box">
    <h2>Tạo tài khoản mới</h2>
    
    <c:if test="${alert != null}">
        <h3 class="alert alert-danger">${alert}</h3>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post" class="needs-validation" novalidate>
        <section>
            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text"><i class="fa fa-user"></i></span>
                    <input type="text" placeholder="Tài khoản" name="username" value="${username}" class="form-control" required minlength="3" maxlength="50" pattern="^[a-zA-Z0-9_]+$">
                    <div class="invalid-feedback">Tài khoản từ 3–50 ký tự (chỉ chữ, số, dấu _).</div>
                </div>
            </div>

            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text"><i class="fa fa-user-circle"></i></span>
                    <input type="text" placeholder="Họ tên" name="fullname" value="${fullname}" class="form-control" required minlength="2" maxlength="100">
                    <div class="invalid-feedback">Vui lòng nhập họ tên (tối thiểu 2 ký tự).</div>
                </div>
            </div>

            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text"><i class="fa fa-envelope"></i></span>
                    <input type="email" placeholder="Nhập Email" name="email" value="${email}" class="form-control" required>
                    <div class="invalid-feedback">Vui lòng nhập đúng định dạng email.</div>
                </div>
            </div>

            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text"><i class="fa fa-phone"></i></span>
                    <input type="text" placeholder="Số điện thoại" name="phone" value="${phone}" class="form-control" required pattern="^0[0-9]{9,10}$">
                    <div class="invalid-feedback">Số điện thoại phải bắt đầu bằng 0 và gồm 10–11 chữ số.</div>
                </div>
            </div>

            <div class="mb-3">
                <div class="input-group">
                    <span class="input-group-text"><i class="fa fa-lock"></i></span>
                    <input type="password" placeholder="Mật khẩu (tối thiểu 6 ký tự)" name="password" class="form-control" required minlength="6">
                    <div class="invalid-feedback">Mật khẩu phải có ít nhất 6 ký tự.</div>
                </div>
            </div>

            <button type="submit" class="btn-submit">Tạo tài khoản</button>
        </section>
    </form>

    <div class="bottom-note">
        Nếu bạn đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
    </div>
</div>
</body>
</html>
