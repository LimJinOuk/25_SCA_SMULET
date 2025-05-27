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

    //사용자 정보(이름, 이메일)
    function loaduserinfo(){
        const token = localStorage.getItem('jwtToken');

        fetch('/userinfo',{
            method: 'GET',
            headers:{
                'Authorization':`Bearer ${token}`
            }
        })
            .then(res=>{
                if(!res.ok)
                    throw new Error('사용자 정보 불러오기 실패');
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


    //회원 탈퇴
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
            //body: JSON.stringify({ confirm: true })
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

    //사용자 정보 수정
    function modifinguserinfo() {
        const modalOverlay = document.getElementById('modalOverlay');
        const closeBtn = document.getElementById('close-btn');
        const confirmBtn = document.getElementById('confirm');
        const token = localStorage.getItem('jwtToken');  // 꼭 필요!



        // 팝업 열기
        modalOverlay.classList.remove('hidden');
        document.body.classList.add("modal-open");


        // 팝업 닫기
        closeBtn.onclick = () => {
            document.getElementById("modalOverlay").classList.add("hidden");
            document.body.classList.remove("modal-open");
        };

        // 확인 버튼 클릭 시 fetch 실행
        confirmBtn.onclick = () => {
            const pw = document.getElementById('pw').value;

            //비밀번호 확인
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
                        modalOverlay.style.display = 'none';
                        document.getElementById("modalOverlay").classList.add("hidden"); // 인증 모달 숨기기
                        document.getElementById("changePwModal").classList.remove("hidden"); // 변경 모달 표시
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

    //비밀번호 재설정
    document.getElementById("changePwBtn").onclick = function () {
        const newPw = document.getElementById("newPassword").value;
        const confirmPw = document.getElementById("confirmPassword").value;
        const token = localStorage.getItem("jwtToken");

        if (!newPw || !confirmPw || newPw !== confirmPw) {
            alert("비밀번호를 정확히 입력해주세요.");
            return;
        }

        fetch('/PWupdate_button', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ pw: newPw })
        })
            .then(async res => {
                if (!res.ok) {
                    const errorText = await res.text();
                    alert(`서버 오류 ${res.status}: ${errorText}`);
                    throw new Error("서버 오류: " + res.status);
                }
                return res.json();
            })
            .then(data => {
                if (data.updateStatus === true) {
                    alert("비밀번호가 성공적으로 변경되었습니다.");
                    document.getElementById("changePwModal").classList.add("hidden");
                } else {
                    alert("비밀번호 변경에 실패했습니다.");
                }
            })
            .catch(err => {
                alert("비밀번호 변경 중 오류 발생: " + err.message);
            });
    };
    //모달 열기
    document.getElementById("openPwModalBtn").onclick = function () {
        document.getElementById("changePwModal").classList.remove("hidden");
        document.getElementById("newPassword").value = "";
        document.getElementById("confirmPassword").value = "";
    };


    //모달 닫기
    document.getElementById("close-btn").onclick = function () {
        document.getElementById("changePwModal").classList.add("hidden");
    };
    document.getElementById("cancelChange").onclick = function () {
        document.getElementById("changePwModal").classList.add("hidden");
    };


    //테크트리 이동
    function goToTechTree() {
        const select = document.getElementById('techTreeSelect');
        const year = select.value;

        if (year) {
            window.location.href = `/year?year=${year}`;
        }
    }