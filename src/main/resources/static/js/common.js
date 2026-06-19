// 공통 스크립트: Spring Security CSRF 토큰을 모든 AJAX 요청에 자동 첨부
$(function () {
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  if (token && header) {
    $(document).ajaxSend(function (e, xhr) {
      xhr.setRequestHeader(header, token);
    });
  }
});

// 에러/성공 메시지 표시 헬퍼
function showMsg(selector, text, ok) {
  $(selector).removeClass("error ok").addClass(ok ? "ok" : "error").text(text).show();
}

// 서버 에러 응답에서 메시지 추출
function errText(xhr) {
  try {
    var r = xhr.responseJSON;
    if (r && r.error) return r.error;
    if (Array.isArray(r) && r.length && r[0].defaultMessage) return r[0].defaultMessage;
  } catch (e) {}
  return "요청 처리 중 오류가 발생했습니다";
}

// 로그아웃
function logout() {
  $.ajax({ url: "/api/user/logout", type: "POST" })
    .always(function () { location.href = "/"; });
}
