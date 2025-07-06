const semesterOptions = [
    "2025년 2학기", "2025년 1학기",
    "2024년 2학기", "2024년 1학기",
    "2023년 2학기", "2023년 1학기",
    "2022년 2학기", "2022년 1학기",
    "2021년 2학기", "2021년 1학기"
];

let selectedSemesters = [];


document.addEventListener('DOMContentLoaded', function () {
    const loginLink = document.getElementById('loginLink');
    const logoutLink = document.getElementById('logoutLink');
    const registerLink = document.querySelector('nav a[href="/Register"]');
    const token = localStorage.getItem('jwtToken');

    // 로그인/로그아웃 링크 토글
    if (token) {
        loginLink.style.display = 'none';
        logoutLink.style.display = 'inline';
        registerLink.style.display = 'none';
    } else {
        loginLink.style.display = 'inline';
        logoutLink.style.display = 'none';
        registerLink.style.display = 'inline';
    }

    // 로그아웃
    logoutLink.addEventListener('click', function (e) {
        e.preventDefault();
        localStorage.removeItem('jwtToken');
        alert('로그아웃 되었습니다.');
        window.location.href = '/';
    });

    // 시간표 추가 버튼 연결
    const showSemesterBtn = document.getElementById('showSemesterBtn');
    if (showSemesterBtn) {
        showSemesterBtn.addEventListener('click', showAddSemester);
    }
});

// 사용자 정보 로드
function loaduserinfo() {
    const token = localStorage.getItem('jwtToken');
    fetch('/userinfo', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
        .then(res => {
            if (!res.ok) throw new Error('사용자 정보 불러오기 실패');
            return res.json();
        })
        .then(data => {
            document.getElementById('userName').textContent = data.name;
            document.getElementById('studentId').textContent = data.email;
        })
        .catch(err => {
            console.error(err);
            alert('사용자 정보를 불러오는데 실패했습니다.');
        });
}

// 회원 탈퇴
function deleteAccount() {
    const token = localStorage.getItem('jwtToken');
    if (!confirm("정말로 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) return;

    fetch('/member/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
        .then(res => {
            if (!res.ok) throw new Error('서버 응답 오류');
            return res.json();
        })
        .then(data => {
            if (data.deleteStatus === 'success') {
                alert('회원 탈퇴가 완료되었습니다.');
                localStorage.removeItem('jwtToken');
                window.location.href = '/';
            }
        })
        .catch(err => {
            alert('회원 탈퇴 중 오류가 발생했습니다.');
            console.error(err);
        });
}

// 사용자 정보 수정 - 비밀번호 확인
function modifinguserinfo() {
    const modalOverlay = document.getElementById('modalOverlay');
    const closeBtn = document.getElementById('close-btn');
    const confirmBtn = document.getElementById('confirm');
    const token = localStorage.getItem('jwtToken');

    modalOverlay.classList.remove('hidden');
    document.body.classList.add("modal-open");

    closeBtn.onclick = () => {
        modalOverlay.classList.add("hidden");
        document.body.classList.remove("modal-open");
    };

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
                if (!res.ok) throw new Error('서버 응답 오류');
                return res.json();
            })
            .then(data => {
                if (data.Password === true) {
                    alert('비밀번호 확인 완료!');
                    modalOverlay.classList.add("hidden");
                    document.getElementById("changePwModal").classList.remove("hidden");
                } else {
                    alert('비밀번호가 일치하지 않습니다.');
                }
            })
            .catch(err => {
                alert('요청 중 문제가 발생했습니다: ' + err.message);
                console.error(err);
            });
    };
}

// 비밀번호 재설정
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

// 모달 열기/닫기
document.getElementById("openPwModalBtn").onclick = function () {
    document.getElementById("changePwModal").classList.remove("hidden");
    document.getElementById("newPassword").value = "";
    document.getElementById("confirmPassword").value = "";
};
document.getElementById("close-btn").onclick = function () {
    document.getElementById("modalOverlay").classList.add("hidden");
};
document.getElementById("cancelChange").onclick = function () {
    document.getElementById("changePwModal").classList.add("hidden");
};

function goToTechTree() {
    const select = document.getElementById('techTreeSelect');
    const year = select.value;
    const token = localStorage.getItem('jwtToken');

    if (!token) {
        alert("로그인 토큰이 없습니다.");
        return;
    }
    if (!year) {
        alert("학번을 선택하세요.");
        return;
    }

    fetch(`/tech_tree?year=${year}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
        .then(res => res.text())
        .then(path => {
            window.location.href = path;
        })
}


// 학기 추가 관련

function showAddSemester() {
    const addList = document.getElementById('addSemesterList');
    const select = document.getElementById('semesterSelect');

    select.innerHTML = '';
    semesterOptions.forEach(sem => {
        if (!selectedSemesters.includes(sem)) {
            const option = document.createElement('option');
            option.value = sem;
            option.textContent = sem;
            select.appendChild(option);
        }
    });

    addList.classList.toggle('hidden');
}

function addSemester() {
    const select = document.getElementById('semesterSelect');
    const selected = select.value;
    if (!selected) return;

    selectedSemesters.push(selected);
    renderSemesterList();
    document.getElementById('addSemesterList').classList.add('hidden');
}

function renderSemesterList() {
    const list = document.getElementById('semesterList');
    list.innerHTML = '';  // ← 이 줄은 먼저 리스트 비우기용으로 제대로 빼주고

    selectedSemesters.forEach((sem, index) => {
        const li = document.createElement('li');
        li.innerHTML = `
            <span>${sem}</span>
            <button onclick="removeSemester(${index})" style = "color: white; background-color: red">삭제</button>
        `;
        list.appendChild(li);
    });
}


function removeSemester(index) {
    selectedSemesters.splice(index, 1);
    renderSemesterList();
}