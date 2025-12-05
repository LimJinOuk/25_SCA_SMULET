/***********************
 * 모달 전용 상태
 ***********************/
let currentCourseList = [];
let currentSemesterCode = null;
let currentTimetableId = null;

const previewSession = { zeroAdded: false };
const FIXED_PREVIEW_COLOR = 'rgba(99, 179, 237, 0.25)';

/***********************
 * 유틸(모달 내부)Q
 ***********************/
function N(v,fb=null){ const n=Number(v); return Number.isFinite(n)?n:fb; }
function toPeriodNumber(v, fb=null){
    if (v==null) return fb;
    if (typeof v === 'number' && Number.isFinite(v)) return v;
    const m = String(v).match(/\d+/);
    if (!m) return fb;
    const n = parseInt(m[0],10);
    return Number.isFinite(n) ? n : fb;
}
function getTid(r){ const v = r.timeTableId ?? r.timetableId ?? r.time_table_id ?? r.timetable_id ?? r.parentTimetableId ?? r.timeTableIdOfParent ?? r.tid; return v!=null?Number(v):NaN; }
function getCourseId(r){ return (r.courseId ?? r.identifyNumberOfCourse ?? r.subjectCode ?? r.course_id ?? null); }

/***********************
 * 0교시 / 그리드 유틸
 ***********************/
function hasZeroPeriodGrid(grid){ return !!grid.querySelector('.cell-placeholder[data-period="0"]'); }
function gridRowForPeriod(grid, period){ return hasZeroPeriodGrid(grid) ? (period + 1) : period; }
function relabelTimeCells(grid){
    if (!grid) return;
    grid.querySelectorAll('.time-cell').forEach(tc=>{
        const rowCss = tc.style.gridRow || '';
        let row = rowCss.includes('/') ? N(rowCss.split('/')[0], 1) : N(rowCss, 1);
        const period = hasZeroPeriodGrid(grid) ? (row - 1) : row;
        const title = period===0 ? '0교시' : `${period}교시`;
        tc.innerHTML = `${title}<br/><span>${window.periodLabel(period)}</span>`;
    });
}
function prependZeroPeriodGrid(grid){
    if (!grid || hasZeroPeriodGrid(grid)) return;
    Array.from(grid.children).forEach(el=>{
        const row = (el.style.gridRow || '').trim();
        if (!row) return;
        if (row.includes('/')){
            const [s,e] = row.split('/').map(v=>parseInt(v.trim(),10));
            if (Number.isFinite(s) && Number.isFinite(e)) el.style.gridRow = `${s+1} / ${e+1}`;
        } else {
            const n = parseInt(row,10);
            if (Number.isFinite(n)) el.style.gridRow = String(n+1);
        }
    });

    const frag = document.createDocumentFragment();
    const t = document.createElement('div');
    t.className='time-cell'; t.style.gridColumn='1'; t.style.gridRow='1';
    t.innerHTML = `0교시<br/><span>8~9시</span>`;
    frag.appendChild(t);
    for (let day=0; day<5; day++){
        const cell = document.createElement('div');
        cell.className='cell-placeholder';
        cell.dataset.day=String(day);
        cell.dataset.period='0';
        cell.style.gridColumn=String(day+2);
        cell.style.gridRow='1';
        frag.appendChild(cell);
    }
    grid.prepend(frag);
    relabelTimeCells(grid);
}
function removeZeroPeriodGridIfEmpty(grid){
    if (!grid || !hasZeroPeriodGrid(grid)) return;
    const used = Array.from(grid.querySelectorAll('.cell-placeholder[data-period="0"]'))
        .some(ph => ph.getAttribute('data-course-id'));
    if (used) return;

    Array.from(grid.querySelectorAll('.time-cell, .cell-placeholder')).forEach(el=>{
        if (el.dataset && el.dataset.period === '0') el.remove();
    });
    grid.querySelectorAll('.preview-block').forEach(el=>el.remove());
    Array.from(grid.children).forEach(el=>{
        const row = (el.style.gridRow || '').trim();
        if (!row) return;
        if (row.includes('/')){
            const [s,e] = row.split('/').map(v=>parseInt(v.trim(),10));
            if (Number.isFinite(s) && Number.isFinite(e)) el.style.gridRow = `${s-1} / ${e-1}`;
        } else {
            const n = parseInt(row,10);
            if (Number.isFinite(n)) el.style.gridRow = String(n-1);
        }
    });
    relabelTimeCells(grid);
}

/***********************
 * 모달 진입/그리드 생성
 ***********************/
function generateModalGridSpanTable(maxPeriod=9){
    const days = ['월','화','수','목','금'];
    const labels = ['9~10시','10~11시','11~12시','12~1시','1~2시','2~3시','3~4시','4~5시','5~6시'];
    let grid = `
      <div class="schedule-card" id="scheduleTable">
        <div class="schedule-header">
          <div class="time-col"></div>
          ${days.map(d=>`<div>${d}</div>`).join('')}
        </div>
        <div class="schedule-body">
          <div class="schedule-grid" id="modalGridSpanBody">`;
    for (let p=1; p<=maxPeriod; p++){
        grid += `
          <div class="time-cell" style="grid-column:1;grid-row:${p}">
            ${p}교시<br/><span>${labels[p-1] || ''}</span>
          </div>`;
        for (let day=0; day<5; day++){
            grid += `
            <div class="cell-placeholder" data-day="${day}" data-period="${p}"
                 style="grid-column:${day+2};grid-row:${p}"></div>`;
        }
    }
    grid += `</div></div></div>`;
    return grid;
}
function ensureModalGridSpanMax(periodMax){
    const grid = document.getElementById('modalGridSpanBody'); if (!grid) return;
    const curMax = Array.from(grid.querySelectorAll('.cell-placeholder'))
        .reduce((m,el)=>Math.max(m, Number(el.dataset.period)||1), 0);
    if (periodMax <= curMax) return;

    for (let p=curMax+1; p<=periodMax; p++){
        const t=document.createElement('div'); t.className='time-cell';
        t.style.gridColumn='1'; t.style.gridRow=String(gridRowForPeriod(grid, p));
        t.innerHTML = `${p}교시<br/><span>${window.periodLabel(p)}</span>`;
        grid.appendChild(t);
        for (let day=0; day<5; day++){
            const cell=document.createElement('div'); cell.className='cell-placeholder';
            cell.dataset.day=String(day); cell.dataset.period=String(p);
            cell.style.gridColumn=String(day+2); cell.style.gridRow=String(gridRowForPeriod(grid, p));
            grid.appendChild(cell);
        }
    }
}

/***********************
 * 리스트/툴팁
 ***********************/
function groupByCourse(data){
    const map = {};
    data.forEach(e=>{ const key = e.courseName; (map[key] ||= []).push(e); });
    return map;
}

/***********************
 * 수업 리스트 & 프리뷰/추가
 ***********************/
function renderCourseList(classList){
    const listContainer = document.getElementById('classInfoList');
    if (!listContainer) return;
    listContainer.innerHTML = '';

    const searchInput = document.getElementById('searchInput');
    const searchTerm = searchInput?.value?.toLowerCase() || '';

    if (!Array.isArray(classList) || classList.length === 0){
        listContainer.innerHTML = '<p>수업 정보가 없습니다.</p>'; return;
    }
    const grouped = groupByCourse(classList);
    Object.entries(grouped).forEach(([courseName, allEntries])=>{
        if (searchTerm && !courseName.toLowerCase().includes(searchTerm)) return;

        const wrapper = document.createElement('div');
        wrapper.style.marginBottom='12px';
        wrapper.style.border='1px solid #ccc';
        wrapper.style.borderRadius='6px';
        wrapper.style.background='#f9f9f9';
        wrapper.style.padding='10px';

        const header = document.createElement('div');
        header.style.cursor='pointer';
        header.style.fontWeight='bold';
        header.style.color='#2563eb';
        header.textContent = courseName;

        const subList = document.createElement('div');
        subList.style.display='none';
        subList.style.marginTop='8px';

        const groupedByIdentify = {};
        allEntries.forEach(e=>{ const key = e.identifyNumberOfCourse; (groupedByIdentify[key] ||= []).push(e); });

        Object.values(groupedByIdentify).forEach(entries=>{
            const detail = document.createElement('div');
            detail.style.padding='6px 10px';
            detail.style.borderTop='1px solid #ddd';
            detail.style.cursor='pointer';
            detail.style.background='#fff';

            const professor = entries[0].professorName || '-';
            const code = entries[0]?.identifyNumberOfCourse ?? '';
            const division = (typeof code === 'string' && code.includes('-')) ? code.split('-')[1] : '?';
            const weekdayNames = ['월','화','수','목','금'];
            const timeText = entries.map(e=>{
                const day = weekdayNames[e.scheduleDay - 1] || `요일 ${e.scheduleDay}`;
                return e.timeStart === e.timeEnd ? `${day} ${e.timeStart}교시` : `${day} ${e.timeStart}~${e.timeEnd}교시`;
            }).join(', ');

            detail.innerHTML = `분반: ${division}<br>교수: ${professor}<br>시간: ${timeText}`;

            detail.addEventListener('mouseover', ()=>{ if (!isCoursePermanentlyAddedGrid(entries)) previewCourseOnGrid(entries); });
            detail.addEventListener('mouseout',  ()=>{ if (!isCoursePermanentlyAddedGrid(entries)) removePreviewFromSchedule(); });
            detail.addEventListener('click',    ()=>{ addCourseToScheduleTableGrid(entries); });

            subList.appendChild(detail);
        });

        header.addEventListener('click', ()=>{ subList.style.display = (subList.style.display==='none') ? 'block' : 'none'; });
        wrapper.appendChild(header); wrapper.appendChild(subList);
        listContainer.appendChild(wrapper);
    });
}

function isCoursePermanentlyAddedGrid(entries){
    const grid = document.getElementById('modalGridSpanBody');
    if (!grid || !entries?.length) return false;
    const code = entries[0].identifyNumberOfCourse;
    return !!grid.querySelector(`.course-block[data-course-id="${code}"]`);
}

function previewCourseOnGrid(entries){
    const grid = document.getElementById('modalGridSpanBody');
    if (!grid || !entries?.length) return;

    grid.querySelectorAll('.preview-block').forEach(b=>b.remove());

    const needsZero = entries.some(e => Number(e.timeStart)===0 || Number(e.timeEnd)===0);
    if (needsZero) prependZeroPeriodGrid(grid);

    let needMax = 9;
    entries.forEach(e=>{ const end = toPeriodNumber(e.timeEnd ?? e.periodEnd ?? e.timeStart, null); if (end != null) needMax = Math.max(needMax, end); });
    ensureModalGridSpanMax(needMax);

    const byDay = {};
    entries.forEach(e=>{
        const d = N(e.scheduleDay,0)-1;
        const s = N(e.timeStart ?? e.periodStart, null);
        const t = N(e.timeEnd   ?? e.periodEnd   ?? e.timeStart, null);
        if (!(d>=0 && d<5) || !Number.isFinite(s) || !Number.isFinite(t)) return;
        (byDay[d] ||= []).push([s,t]);
    });

    Object.entries(byDay).forEach(([dStr, ranges])=>{
        const d = Number(dStr);
        ranges.sort((a,b)=>a[0]-b[0]);
        const merged = [];
        let cur=null;
        for (const [s,t] of ranges){
            if (!cur) cur=[s,t];
            else if (s <= cur[1]+1) cur[1] = Math.max(cur[1], t);
            else { merged.push(cur); cur=[s,t]; }
        }
        if (cur) merged.push(cur);

        merged.forEach(([s,t])=>{
            const block = document.createElement('div');
            block.className='course-block preview-block';
            block.style.gridColumn = String(d+2);
            const startRow = gridRowForPeriod(grid, s);
            const endRow   = gridRowForPeriod(grid, t) + 1;
            block.style.gridRow = `${startRow} / ${endRow}`;
            const base = entries[0];
            block.innerHTML = window.renderSlotHTML(base.courseName, base.classroom, base.professorName || '');
            block.style.background = FIXED_PREVIEW_COLOR;
            grid.appendChild(block);
        });
    });

    previewSession.zeroAdded ||= needsZero;
}
function removePreviewFromSchedule(){
    const grid = document.getElementById('modalGridSpanBody');
    if (grid){
        grid.querySelectorAll('.preview-block').forEach(b=>b.remove());
        if (previewSession.zeroAdded){
            removeZeroPeriodGridIfEmpty(grid);
            previewSession.zeroAdded = false;
        }
    }
}

function addCourseToScheduleTableGrid(entries){
    const grid = document.getElementById('modalGridSpanBody');
    if (!grid || !entries?.length) return;

    removePreviewFromSchedule();

    // 0교시 포함 여부 확인
    const needsZero = entries.some(e => Number(e.timeStart) === 0 || Number(e.timeEnd) === 0);
    if (needsZero) prependZeroPeriodGrid(grid);

    const base = entries[0];  // ★ 원본 정보 보관
    const normalizedBase = normalizeCourseInfo(base);
    const courseName = base.courseName;
    const classroom  = base.classroom;
    const professor  = base.professorName || '';
    const courseCode = String(base.identifyNumberOfCourse ?? '');
    const coursePk   = base.courseId != null ? String(base.courseId) : null;

    // 이미 추가된 동일 과목이면 삭제 처리
    const selectorByCode = `.course-block[data-course-id="${courseCode}"]`;
    const existingExact = coursePk != null
        ? grid.querySelectorAll(`${selectorByCode}[data-course-pk="${coursePk}"], ${selectorByCode}:not([data-course-pk])`)
        : grid.querySelectorAll(selectorByCode);

    if (existingExact.length){
        existingExact.forEach(b => b.remove());
        const phSelBase = `.cell-placeholder[data-course-id="${courseCode}"]`;
        const phSel = coursePk != null
            ? `${phSelBase}[data-course-pk="${coursePk}"], ${phSelBase}:not([data-course-pk])`
            : phSelBase;
        grid.querySelectorAll(phSel).forEach(ph=>{
            ph.removeAttribute('data-course-id');
            ph.removeAttribute('data-course-pk');
            ph.style.borderBottom = '';
        });
        window.ColorManager.free(courseCode || courseName);
        removeZeroPeriodGridIfEmpty(grid);
        return;
    }

    // 요일·시간대 그룹
    const byDay = {};
    entries.forEach(e=>{
        const d = toPeriodNumber(e.scheduleDay,0)-1;
        const s = toPeriodNumber(e.timeStart ?? e.periodStart, null);
        const t = toPeriodNumber(e.timeEnd   ?? e.periodEnd   ?? e.timeStart, null);
        if (d<0 || s==null || t==null) return;
        (byDay[d] ||= []).push([s,t]);
    });

    // 연속 교시 병합
    const mergedByDay = {};
    Object.entries(byDay).forEach(([dStr, ranges])=>{
        const d = Number(dStr);
        ranges.sort((a,b)=>a[0]-b[0]);
        let cur=null, merged=[];
        for (const [s,t] of ranges){
            if (!cur) cur=[s,t];
            else if (s <= cur[1]+1) cur[1] = Math.max(cur[1], t);
            else { merged.push(cur); cur=[s,t]; }
        }
        if (cur) merged.push(cur);
        mergedByDay[d] = merged;
    });

    // 충돌 검사
    for (const [dStr, merged] of Object.entries(mergedByDay)){
        const d = Number(dStr);
        for (const [s, t] of merged){
            for (let p=s; p<=t; p++){
                const ph = grid.querySelector(`.cell-placeholder[data-day="${d}"][data-period="${p}"]`);
                if (!ph) continue;
                const occId = ph.getAttribute('data-course-id');
                const occPk = ph.getAttribute('data-course-pk');
                const sameId = occId && occId === courseCode;
                const samePk = (coursePk == null) ? true : (occPk === coursePk);
                const occupiedByOthers = occId && !(sameId && samePk);

                if (occupiedByOthers){
                    alert('이미 배치된 수업 시간과 겹쳐 추가할 수 없습니다.');
                    return;
                }
            }
        }
    }

    // 색상 관리
    const color = window.ColorManager.get(courseCode || courseName);

    // ★★★ 블록 생성 + 툴팁 시 원본 entries[0] 전체를 넘김 ★★★
    Object.entries(mergedByDay).forEach(([dStr, merged])=>{
        const d = Number(dStr);
        merged.forEach(([s,t])=>{
            const block = document.createElement('div');
            block.className = 'course-block busy';
            block.style.gridColumn = String(d+2);

            const startRow = gridRowForPeriod(grid, s);
            const endRow   = gridRowForPeriod(grid, t) + 1;
            block.style.gridRow = `${startRow} / ${endRow}`;
            block.style.setProperty('--course-color', color);

            block.dataset.courseId = courseCode;
            if (coursePk != null) block.dataset.coursePk = coursePk;

            block.innerHTML = window.renderSlotHTML(courseName, classroom, professor);

            // ⭐⭐⭐ 여기서 원본 데이터를 spread로 통째로 넘김 ⭐⭐⭐
            block.onmouseenter = () =>
                showCourseTooltip(block, {
                    ...normalizedBase,        // ← 과목 전체 정보 포함(bsm, 전선전심, credit 등)
                    scheduleDay: d+1,
                    timeStart: s,
                    timeEnd: t,
                });

            block.onmouseleave = hideCourseTooltip;

            grid.appendChild(block);

            // placeholder 정보 기입
            for (let p=s; p<=t; p++){
                const ph = grid.querySelector(`.cell-placeholder[data-day="${d}"][data-period="${p}"]`);
                if (ph){
                    ph.setAttribute('data-course-id', courseCode);
                    if (coursePk != null) ph.setAttribute('data-course-pk', coursePk);
                    ph.style.borderBottom = 'none';
                }
            }
        });
    });
}


function attachCellEvents(){
    const grid = document.getElementById('modalGridSpanBody');
    if (!grid) return;

    grid.querySelectorAll('.cell-placeholder[data-period]').forEach(cell=>{
        cell.onmouseover = () => {
            const courseId = cell.getAttribute('data-course-id');
            if (!courseId) return;
            const raw = currentCourseList.find(c => String(c.identifyNumberOfCourse) === String(courseId));
            if (!raw) return;
            const info = normalizeCourseInfo(raw);   // ✅ 정규화
            showCourseTooltip(cell, info);
        };
        cell.onmouseout = hideCourseTooltip;
    });

    grid.querySelectorAll('.course-block').forEach(block=>{
        const courseId = block.getAttribute('data-course-id');
        const raw = currentCourseList.find(c => String(c.identifyNumberOfCourse) === String(courseId));
        if (raw){
            const info = normalizeCourseInfo(raw);   // ✅ 정규화
            block.onmouseenter = () => showCourseTooltip(block, info);
            block.onmouseleave = hideCourseTooltip;
        }
    });
}


/***********************
 * 저장 로직
 ***********************/
function collectSelectedCourseCodes(){
    const root = document.getElementById('modalGridSpanBody');
    if (!root) return [];
    const codes = new Set();
    root.querySelectorAll('.course-block[data-course-id], .cell-placeholder[data-course-id]')
        .forEach(el=>{ const code = el.getAttribute('data-course-id'); if (code) codes.add(code); });
    return Array.from(codes);
}
function pickCoursePk(row){
    const cand = [row.courseId, row.course_id, row.timeTableId, row.timetableId, row.id];
    for (const v of cand){ const n = Number(v); if (Number.isInteger(n) && n>0) return n; }
    return null;
}
function mapCodesToCourseIds(codes){
    const result = new Set();
    codes.forEach(code=>{
        (currentCourseList || []).forEach(e=>{
            if (String(e.identifyNumberOfCourse) === String(code)){
                const pk = pickCoursePk(e);
                if (pk != null) result.add(pk);
            }
        });
    });
    return Array.from(result);
}

async function onClickSaveSchedule() {
    try {
        if (!currentTimetableId) {
            currentTimetableId = window.timetableIdsBySemester[currentSemesterCode] ?? null;
        }
        if (!currentTimetableId) throw new Error('timetableId가 없습니다.');

        const selectedCodes = collectSelectedCourseCodes();
        if (!selectedCodes.length) { alert('저장할 수업이 없습니다.'); return; }

        const allCourseIds = mapCodesToCourseIds(selectedCodes);
        if (!allCourseIds.length) { alert('화면의 과목들에서 courseId를 찾지 못했습니다.'); return; }

        const payload = { timetableId: Number(currentTimetableId), courseIds: allCourseIds };
        const res = await window.apiFetch('/addTC', {
            method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(payload),
        });

        if (!res.ok) { const txt = await res.text(); throw new Error(`저장 실패: ${txt || ('HTTP ' + res.status)}`); }

        alert('저장 완료!');
        await window.ensureAllCourseListLoaded?.();
        await window.hydrateSemestersFromServer?.();
        await window.loaduserinfo?.();
        await window.loadCredits?.();
    } catch (err) {
        console.error('[addTC] Exception:', err);
        alert(`저장 중 오류: ${err.message}`);
    }
}

/***********************
 * 외부 진입: 모달 열기
 ***********************/
async function openScheduleModal(semester){
    const modal = document.getElementById('scheduleModal');
    const title = document.getElementById('modalSemesterTitle');
    const container = document.getElementById('scheduleTableContainer');
    const listContainer = document.getElementById('classInfoList');

    currentSemesterCode = window.toSemesterCode(semester);
    currentTimetableId = window.timetableIdsBySemester[currentSemesterCode] ?? null;
    if (!currentTimetableId){
        alert('이 학기의 시간표 ID가 없습니다. 먼저 "시간표 추가"에서 학기를 추가하세요.');
        return;
    }

    // 닫기 버튼
    const closeBtn = document.getElementById('closeScheduleModal');
    closeBtn.onclick = async function(){
        modal.classList.add('hidden');
        document.body.classList.remove('modal-open');
        hideCourseTooltip?.();
        try {
            await window.renderRepresentativeFromServer?.();
            await window.hydrateSemestersFromServer?.();
        } catch(e) { console.warn('대표 시간표 동기화 실패:', e); }
    };

    // 오버레이 클릭으로 닫기
    modal.addEventListener('click', async (e) => {
        if (e.target === modal) {
            modal.classList.add('hidden');
            document.body.classList.remove('modal-open');
            hideCourseTooltip?.();
            try {
                await window.renderRepresentativeFromServer?.();
                await window.hydrateSemestersFromServer?.();
            } catch(e) { console.warn('대표 시간표 동기화 실패:', e); }
        }
    }, { once:true });

    title.textContent = semester;
    container.innerHTML = generateModalGridSpanTable();

    const modalGrid = document.getElementById('modalGridSpanBody');
    if (modalGrid){
        modalGrid.style.display='grid';
        modalGrid.style.gridAutoFlow='row dense';
        modalGrid.style.alignContent='start';
        modalGrid.style.justifyContent='stretch';
        modalGrid.style.gridAutoRows='52px';
        if (!modalGrid.__previewClearBound){
            modalGrid.addEventListener('click', ()=>{ removePreviewFromSchedule(); });
            modalGrid.__previewClearBound = true;
        }
    }

    listContainer.innerHTML = '<p>수업 정보를 불러오는 중입니다...</p>';
    modal.classList.remove('hidden');
    document.body.classList.add('modal-open');

    // 1) 현재 시간표 → 슬롯 배치
    const userId = await window.getUserId?.();
    window.apiFetch('/getTC', {
        method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(userId),
    })
        .then(res=>{ if(!res.ok) throw new Error('시간표 조회 실패'); return res.json(); })
        .then(async (allRows)=>{
            const filtered = (allRows||[]).filter(r => {
                const tid = getTid(r);
                return Number.isFinite(tid) && tid === Number(currentTimetableId);
            });
            const courseIds = Array.from(new Set(
                filtered.map(getCourseId).filter(v=>v!=null && String(v).trim()!=='')
            ));
            if (courseIds.length === 0) return;
            const slotRes = await window.apiFetch('/getTS2TE', {
                method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify({ getScheduleIds: courseIds }),
            });
            if (!slotRes.ok) throw new Error('슬롯 조회 실패');
            const slotData = await slotRes.json();
            renderScheduleFromTS2TE(slotData);
        })
        .catch(err=>console.error('getTC/슬롯 로드 오류:', err));

    // 2) 수업 목록
// 2) 수업 목록
    const semesterCode = currentSemesterCode;



    console.log(semesterCode);
    window.apiFetch(`/a?semester=${encodeURIComponent(semesterCode)}`)
        .then(res => res.json())
        .then(data => {
            currentCourseList = data || [];
            renderCourseList(currentCourseList);
            attachCellEvents();
        })
        .catch(err => {
            listContainer.innerHTML = '<p style="color:red;">수업 목록을 불러오지 못했습니다.</p>';
            console.error(err);
        });

    // 저장 버튼(중복 제거 후 바인딩)
    const saveBtn = document.getElementById('saveSchedule');
    saveBtn.replaceWith(saveBtn.cloneNode(true));
    document.getElementById('saveSchedule').addEventListener('click', onClickSaveSchedule);
}

/***********************
 * 기존 슬롯 배치
 ***********************/
function renderScheduleFromTS2TE(scheduleData){
    const grid = document.getElementById('modalGridSpanBody');
    if (!grid || !Array.isArray(scheduleData)) return;

    const hasZero = scheduleData.some(it => {
        const s = Number(it.timeStart ?? it.time_start);
        const e = Number(it.timeEnd   ?? it.time_end   ?? s);
        return s === 0 || e === 0;
    });
    if (hasZero) prependZeroPeriodGrid(grid);

    let needMax = 9;
    scheduleData.forEach(it => {
        const info = normalizeCourseInfo(it);
        const e = toPeriodNumber(it.timeEnd ?? it.timeStart, null);
        if (e != null) needMax = Math.max(needMax, e);
    });
    ensureModalGridSpanMax(needMax);

    scheduleData.forEach(item=>{
        const info = window.normalizeCourseInfo(item);
        const subject    = item.courseName || '';
        const professor  = item.professorName || '';
        const classroom  = item.classroom || '';
        const courseCode = item.identify_number_of_course ?? item.identifyNumberOfCourse ?? item.courseCode ?? subject;

        const dayIdx = toPeriodNumber(item.day ?? item.scheduleDay, 0) - 1;
        const pStart = toPeriodNumber(item.time_start ?? item.timeStart ?? item.periodStart, null);
        const pEnd   = toPeriodNumber(item.time_end   ?? item.timeEnd   ?? item.periodEnd ?? pStart, null);
        if (dayIdx < 0 || pStart == null || pEnd == null) return;

        const color = window.ColorManager.get(courseCode);
        const block = document.createElement('div');
        block.className = 'course-block busy';
        block.style.gridColumn = String(dayIdx + 2);
        const startRow = gridRowForPeriod(grid, pStart);
        const endRow   = gridRowForPeriod(grid, pEnd) + 1;
        block.style.gridRow = `${startRow} / ${endRow}`;
        block.style.setProperty('--course-color', color);

        block.dataset.courseId = courseCode;
        block.innerHTML = window.renderSlotHTML(subject, classroom, professor);

        block.onmouseenter = () => showCourseTooltip(block, info);
        block.onmouseleave = hideCourseTooltip;

        grid.appendChild(block);

        for (let p = pStart; p <= pEnd; p++) {
            const ph = grid.querySelector(`.cell-placeholder[data-day="${dayIdx}"][data-period="${p}"]`);
            if (ph){
                ph.setAttribute('data-course-id', courseCode);
                ph.style.borderBottom = 'none';
            }
        }
    });

    attachCellEvents();
}

/***********************
 * 전역 공개
 ***********************/
window.openScheduleModal = openScheduleModal;
window.renderCourseList = renderCourseList; // schedule-modal.html에서 oninput 호출