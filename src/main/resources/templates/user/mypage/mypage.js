// 사용자  정보 불러오기
window.onload = function () {
    fetch("https://example.com/userinfo")
        .then(res => res.json())
        .then(userInfo => {
            document.getElementById("studentId").textContent = userInfo.studentId;
            document.getElementById("userName").textContent = userInfo.name;
            document.getElementById("userMajor").textContent = userInfo.major;
        })
        .catch(error => {
            document.getElementById("errorMessage").textContent =
                "❌ 서버에서 정보를 불러오지 못했습니다.";
            console.error("에러 발생:", error);
        });
};
//사용자 정보 바꾸기
function openEditPopup() {
    // 새 창 열기 (popup.html 이라는 새 창을 엽니다)
    window.open('popup.html', 'EditPopup', 'width=400,height=400');
}
//사용자 비밀번호 바꾸기
function openPasswordPopup() {
    window.open('popup_password.html', 'PasswordPopup', 'width=400,height=300');
}
//사용자 없애기
function openDeletePopup() {
    window.open('popup_delete.html', 'DeletePopup', 'width=400,height=250');
}
//테크트리 추가하기
function openSchedulePopup() {
    window.open('popup_schedule.html', 'SchedulePopup', 'width=400,height=300');
}

function addScheduleToMain(year, term) {
    const scheduleList = document.getElementById('scheduleList');
    const item = document.createElement('div');
    item.className = 'schedule-item';

    const link = document.createElement('a');
    link.textContent = `${year} ${term} 시간표`;
    link.href = `/schedule?year=${year}&term=${term}`;
    link.style.marginRight = '10px';

    item.appendChild(link);
    scheduleList.appendChild(item);
}
//테크트리 삭제하기
function deleteScheduleToggle() {
    deleteMode = !deleteMode;
    const items = document.querySelectorAll('#scheduleList .schedule-item');
    items.forEach(item => {
        if (deleteMode && !item.querySelector('.del-btn')) {
            const delBtn = document.createElement('button');
            delBtn.textContent = '❌';
            delBtn.className = 'del-btn';
            delBtn.onclick = function () {
                const text = item.querySelector('a').textContent;
                fetch('/delete_schedule', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ title: text })
                })
                    .then(res => {
                        if (res.ok) item.remove();
                        else alert('삭제 실패');
                    })
                    .catch(err => alert('삭제 오류: ' + err));
            };
            item.appendChild(delBtn);
        } else if (!deleteMode && item.querySelector('.del-btn')) {
            item.querySelector('.del-btn').remove();
        }
    });
}
