document.addEventListener('DOMContentLoaded', () => {
    const dialog = document.getElementById('addIconDialog');
    const addBtn = document.getElementById('add-icon-btn');
    const customContainer = document.getElementById('custom-icons');

    // "+" 버튼 클릭하면 모달 열기
    addBtn.addEventListener('click', () => {
        dialog.showModal();
    });

    // 모달 닫힐 때 (취소 또는 확인)
    dialog.addEventListener('close', () => {
        if (dialog.returnValue === 'confirm') {
            const form = dialog.querySelector('form');
            const name = form.name.value.trim();
            const url = form.url.value.trim();
            if (!name || !url) return;
            addCustomIcon(name, url);
            form.reset();
        }
    });
});

/**
 * 사용자 아이콘을 로고 목록에 추가합니다.
 */
function addCustomIcon(name, url) {
    const container = document.createElement('div');
    container.className = 'logo-container';

    // 삭제 버튼 생성
    const removeBtn = document.createElement('button');
    removeBtn.className = 'remove-btn';
    removeBtn.textContent = '×';
    removeBtn.addEventListener('click', () => {
        container.remove();
    });
    container.appendChild(removeBtn);

    // 아이콘 링크 생성
    const link = document.createElement('a');
    link.href = url;
    link.className = 'logo-box';
    link.target = '_blank';

    // favicon 경로 자동 생성
    const img = document.createElement('img');
    img.alt = name;
    try {
        const { origin } = new URL(url);
        img.src = `${origin}/favicon.ico`;
    } catch {
        img.src = '';
    }
    link.appendChild(img);
    container.appendChild(link);

    // 캡션 추가
    const caption = document.createElement('div');
    caption.className = 'logo-caption';
    caption.textContent = name;
    container.appendChild(caption);

    // 목록에 삽입
    document.getElementById('custom-icons').appendChild(container);
}
