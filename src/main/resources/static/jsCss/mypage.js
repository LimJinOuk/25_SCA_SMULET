document.addEventListener('DOMContentLoaded', function () {
    const loginLink = document.getElementById('loginLink');
    const logoutLink = document.getElementById('logoutLink');
    const registerLink = document.querySelector('nav a[href="/Register"]');
    const token = localStorage.getItem('jwtToken');
    console.log("📦 가져온 토큰:", token);

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
function loaduserinfo(){
    const token = localStorage.getItem('jwtToken');

    fetch('/userinfo',{
        method: 'GET',
        headers:{
            'Authorization':`Bearer ${token}`
        }
    })
        .then(res=>{
            if(!res.ok) throw new Error('사용자 정보 불러오기 실패');
            return res.json()
        })
        .then(data=>{
            document.getElementById('userName').textContent = data.name;
            document.getElementById('studentId').textContent = data.email;
        })
        .catch((err=>{
            console.log(err);
            alert('사용자 정보를 불러오는데 실패했습니다.')
        }));
}




function deleteAccount() {
    const token = localStorage.getItem('jwtToken');
    if (!confirm("정말로 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) {
        return;
    }

    fetch('/member/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ confirm: true })  // 선택적으로 confirm 값  전달
    })
        .then(res => {
            console.log("응답 상태코드:", res.status);
            if (!res.ok) {
                throw new Error('서버 응답 오류');
            }
            return res.json();
        })
        .then(data=> {
            if(data.deleteStatus === 'success'){
                alert('회원 탈퇴가 완료되었습니다.');
                localStorage.removeItem('jwtToken');
                window.location.href = '/';
            }
        })
        .catch(err => {
            alert('회원 탈퇴 중 오류가 발생했습니다.');
            console.error('탈퇴 에러:', err);
        });

}


function modifinguserinfo() {
    const popup = document.getElementById('popup');
    const closeBtn = document.getElementById('close-btn');
    const confirmBtn = document.getElementById('confirm');
    const token = localStorage.getItem('jwtToken');  // 꼭 필요!



    // 팝업 열기
    popup.style.display = 'block';

    document.addEventListener('click', function (e) {
        const popup = document.getElementById('popup');
        if (popup.style.display === 'block' && !popup.contains(e.target)) {
            e.stopPropagation();
            e.preventDefault();
        }
    }, true); // useCapture를 true로 설정해야 작동

    // 팝업 닫기
    closeBtn.onclick = () => {
        popup.style.display = 'none';
    };

    // 확인 버튼 클릭 시 fetch 실행
    confirmBtn.onclick = () => {
        const pw = document.getElementById('pw').value;

        fetch('/check_pw_button', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ pw: pw })
        })
            .then(res => {
                console.log('응답 상태:', res.status, res.statusText);
                if (!res.ok) {
                    throw new Error('서버 응답 오류');
                }
                return res.json();
            })
            .then(data => {
                if (data.Password === true) {
                    alert('비밀번호 확인 완료!');
                    popup.style.display = 'none';
                    window.location.href = '/';
                } else {
                    console.log('입력한 비밀번호:', pw);
                    alert('비밀번호가 일치하지 않습니다.');
                }
            })
            .catch(err => {
                console.error('에러:', err);
                alert('요청 중 문제가 발생했습니다: ' + err.message);
            });
    };
}


function goToTechTree() {
    const select = document.getElementById('techTreeSelect');
    const year = select.value;

    if (year) {
        window.location.href = `/year?year=${year}`;
    }
}