const token = localStorage.getItem('jwtToken');
document.addEventListener('DOMContentLoaded', function () {
    const loginLink = document.getElementById('loginLink');
    const logoutLink = document.getElementById('logoutLink');
    const registerLink = document.querySelector('nav a[href="/Register"]');

    logoutLink.addEventListener('click', function (e) {
        e.preventDefault();
        localStorage.removeItem('jwtToken');
        alert('로그아웃 되었습니다.');
        window.location.href = '/';
    });

    if (token) {
        loginLink.style.display = 'none';
        logoutLink.style.display = 'inline';
        registerLink.style.display = 'none';
    } else {
        loginLink.style.display = 'inline';
        logoutLink.style.display = 'none';
        registerLink.style.display = 'inline';
    }

});

function deleteAccount() {

    if (!confirm("정말로 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) {
        return;
    }

    fetch('/member/delete', {
        method: 'POST',  // ✅ POST 방식
        headers: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ confirm: true })  // 선택적으로 confirm 값  전달
    })
        .then(res => res.json())
        .then(res => {
            console.log("응답 상태코드:", res.status);
            if (!res.ok) throw new Error('서버 응답 오류');
            return res.json();
        })
        .then(()=> {
            alert('회원 탈퇴가 완료되었습니다.');
            localStorage.removeItem('jwtToken');
            window.location.href = '/';
        })
        .catch(err => {
            alert('회원 탈퇴 중 오류가 발생했습니다.');
            console.error('탈퇴 에러:', err);
        });

}


function goToTechTree() {
    const select = document.getElementById('techTreeSelect');
    const year = select.value;

    if (year) {
        window.location.href = `/year?year=${year}`;
    }
}