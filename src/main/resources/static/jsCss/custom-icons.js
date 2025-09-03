// jsCss/custom-icons.js
(() => {
    const MAX = 3;
    const STORAGE_KEY = 'userIcons';
    // 1) 기존 저장 데이터 불러오기 (없으면 빈 배열)
    let icons = JSON.parse(localStorage.getItem(STORAGE_KEY) || '[]');

    // 2) DOM 요소 참조
    const customArea = document.getElementById('custom-icons');
    const addBtn     = document.getElementById('add-icon-btn');
    const dialog     = document.getElementById('addIconDialog');
    const form       = dialog.querySelector('form');
    const confirmBtn = document.getElementById('confirmAdd');

    // 3) 화면 렌더 함수
    function render() {
        customArea.innerHTML = '';
        icons.forEach(({ name, url, icon }, i) => {
            const container = document.createElement('div');
            container.className = 'logo-container';

            // 삭제 버튼
            const removeBtn = document.createElement('button');
            removeBtn.setAttribute('type', 'button');
            removeBtn.className = 'remove-btn';
            removeBtn.textContent = '×';
            removeBtn.title = '삭제';
            removeBtn.style.zIndex = '10';
            removeBtn.addEventListener('click', () => {
                icons.splice(i, 1);
                localStorage.setItem(STORAGE_KEY, JSON.stringify(icons));
                render();
            });
            container.appendChild(removeBtn);

            // 아이콘 링크
            const a = document.createElement('a');
            a.href   = url;
            a.target = '_blank';
            a.rel    = 'noopener';
            a.className = 'logo-box';
            const img = document.createElement('img');
            img.src = icon;      // 고해상도 파비콘을 이미 계산해서 넣습니다
            img.alt = name;
            a.appendChild(img);
            container.appendChild(a);

            // 캡션
            const cap = document.createElement('div');
            cap.className = 'logo-caption';
            cap.textContent = name;
            container.appendChild(cap);

            customArea.appendChild(container);
        });

        // 최대치 도달 시 + 버튼 숨김
        addBtn.style.display = icons.length >= MAX ? 'none' : 'flex';
    }

    // 4) + 버튼 클릭 → dialog 띄우기
    addBtn.addEventListener('click', () => {
        form.name.value = '';
        form.url.value  = '';
        dialog.showModal();
    });

    // 5) ESC나 바깥 클릭 → dialog 자동 닫힘
    dialog.addEventListener('cancel', () => dialog.close());

    // 6) 추가 버튼 클릭 → 저장·렌더·닫기
    confirmBtn.addEventListener('click', () => {
        const name = form.name.value.trim();
        const url  = form.url.value.trim();
        if (!name || !url) return;  // 빈 값이면 무시

        if (icons.length >= MAX) {
            alert(`최대 ${MAX}개까지 등록 가능합니다.`);
            dialog.close();
            return;
        }

        // Google Favicon API로 128×128px 아이콘 가져오기
        const iconUrl = `https://www.google.com/s2/favicons?sz=128&domain_url=${encodeURIComponent(url)}`;

        icons.push({ name, url, icon: iconUrl });
        localStorage.setItem(STORAGE_KEY, JSON.stringify(icons));
        render();
        dialog.close();
    });

    // 7) 페이지 로드 시 최초 렌더
    document.addEventListener('DOMContentLoaded', render);
})();