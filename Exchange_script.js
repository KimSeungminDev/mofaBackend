// script.js

const BACKEND_API = 'http://localhost:8080/api/exchange';
const exchangeRateTableBody = document.getElementById('exchangeRateTableBody');
const paginationControls = document.getElementById('paginationControls');
const sortOptionSelect = document.getElementById('sortOption');
const searchFilterInput = document.getElementById('searchFilter');

let allExchangeData = []; // 현재 필터링 및 정렬된 데이터 (렌더링에 사용)
let rawExchangeData = []; // API에서 처음 불러온 원본 데이터 (필터링의 기준)
let previousDayExchangeData = {}; // 전 영업일 데이터
const pageSize = 15;
let currentPage = 1;
let currentSortBy = 'name'; // 현재 정렬 기준 (초기값: 이름순)

/**
 * 오늘을 기준으로 가장 최근의 평일 날짜를 YYYYMMDD 형식으로 반환합니다.
 */
function getLatestWeekday() {
    const today = new Date();
    const day = today.getDay();
    let daysToSubtract = 0;
    if (day === 0) daysToSubtract = 2; // Sunday, go back to Friday
    else if (day === 6) daysToSubtract = 1; // Saturday, go back to Friday
    today.setDate(today.getDate() - daysToSubtract);

    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const date = String(today.getDate()).padStart(2, '0');
    return `${year}${month}${date}`;
}

/**
 * 주어진 날짜 문자열(YYYYMMDD)을 기준으로 전 영업일 날짜를 YYYYMMDD 형식으로 반환합니다.
 */
function getPreviousWeekday(currentDateStr) {
    const year = parseInt(currentDateStr.substring(0, 4));
    const month = parseInt(currentDateStr.substring(4, 6)) - 1;
    const day = parseInt(currentDateStr.substring(6, 8));

    const previousDay = new Date(year, month, day);
    previousDay.setDate(previousDay.getDate() - 1);

    let dayOfWeek = previousDay.getDay();
    while (dayOfWeek === 0 || dayOfWeek === 6) {
        previousDay.setDate(previousDay.getDate() - 1);
        dayOfWeek = previousDay.getDay();
    }

    const prevYear = previousDay.getFullYear();
    const prevMonth = String(previousDay.getMonth() + 1).padStart(2, '0');
    const prevDate = String(previousDay.getDate()).padStart(2, '0');
    return `${prevYear}${prevMonth}${prevDate}`;
}

/**
 * 환율 데이터를 테이블에 렌더링합니다.
 * 전일대비 값을 계산하여 함께 표시합니다.
 */
function renderTable(pageNumber) {
    exchangeRateTableBody.innerHTML = '';
    const startIndex = (pageNumber - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    const itemsToDisplay = allExchangeData.slice(startIndex, endIndex);

    if (itemsToDisplay.length === 0) {
        exchangeRateTableBody.innerHTML = `<tr><td colspan="5" style="text-align: center; padding: 20px;">표시할 데이터가 없습니다.</td></tr>`;
        return;
    }

    itemsToDisplay.forEach(item => {
        const curCode = item.currencyCode || '-';
        const curName = item.currencyName || '-';
        const baseRate = item.baseRate !== undefined ? item.baseRate : '-';
        const flagUrl = item.flagUrl || '';
        const countryName = item.countryName || '-';

        if (countryName === '알 수 없음' || countryName === '-') return;

        let changeDisplay = '-';
        let changeClass = '';
        // item.calculatedChangeRate는 filterData 또는 sortData에서 이미 계산되어 있을 수 있습니다.
        const changeValue = item.calculatedChangeRate !== undefined ? item.calculatedChangeRate : 0;

        if (typeof baseRate === 'number' && previousDayExchangeData[curCode] !== undefined) {
            if (changeValue > 0) {
                changeDisplay = `▲ ${changeValue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
                changeClass = 'increase';
            } else if (changeValue < 0) {
                changeDisplay = `▼ ${Math.abs(changeValue).toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
                changeClass = 'decrease';
            } else {
                changeDisplay = '-';
            }
        }

        const html = `
            <tr>
                <td><img src="${flagUrl}" alt="${countryName} 국기" /></td>
                <td>${countryName}</td>
                <td>${curName} (${curCode})</td>
                <td>${typeof baseRate === 'number' ? baseRate.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 }) : baseRate}</td>
                <td class="${changeClass}">${changeDisplay}</td>
            </tr>
        `;
        exchangeRateTableBody.insertAdjacentHTML('beforeend', html);
    });
}

/**
 * 페이지네이션 컨트롤을 렌더링합니다.
 */
function renderPagination() {
    paginationControls.innerHTML = '';
    const totalPages = Math.ceil(allExchangeData.length / pageSize);
    if (totalPages <= 1) return;

    for (let i = 1; i <= totalPages; i++) {
        const pageSpan = document.createElement('span');
        pageSpan.textContent = i;
        pageSpan.classList.add('page-number');
        if (i === currentPage) pageSpan.classList.add('active');
        pageSpan.addEventListener('click', () => goToPage(i));
        paginationControls.appendChild(pageSpan);
    }
}

/**
 * 특정 페이지로 이동합니다.
 */
function goToPage(pageNumber) {
    if (pageNumber < 1 || pageNumber > Math.ceil(allExchangeData.length / pageSize)) return;
    currentPage = pageNumber;
    renderTable(currentPage);
    renderPagination();
}

/**
 * API에서 환율 데이터를 비동기로 불러옵니다.
 */
async function loadData() {
    try {
        const searchDate = getLatestWeekday();
        const prevSearchDate = getPreviousWeekday(searchDate);

        // 1. 현재 날짜의 환율 데이터 불러오기
        const currentResponse = await fetch(`${BACKEND_API}?searchDate=${searchDate}`);
        if (!currentResponse.ok) {
            const errorText = await currentResponse.text();
            throw new Error(`현재 환율 API 호출 실패: ${currentResponse.status} - ${errorText}`);
        }
        rawExchangeData = await currentResponse.json();
        if (!Array.isArray(rawExchangeData)) {
            throw new Error('현재 환율 데이터 형식이 배열이 아닙니다.');
        }

        // 2. 전 영업일의 환율 데이터 불러오기
        const previousResponse = await fetch(`${BACKEND_API}?searchDate=${prevSearchDate}`);
        if (!previousResponse.ok) {
            const errorText = await previousResponse.text();
            console.warn(`전 영업일 환율 API 호출 실패 (${prevSearchDate}): ${previousResponse.status} - ${errorText}. 전일대비 정보가 제한될 수 있습니다.`);
            previousDayExchangeData = {};
        } else {
            const rawPreviousData = await previousResponse.json();
            if (!Array.isArray(rawPreviousData)) {
                console.warn('전 영업일 환율 데이터 형식이 배열이 아닙니다. 전일대비 정보가 제한될 수 있습니다.');
                previousDayExchangeData = {};
            } else {
                previousDayExchangeData = rawPreviousData.reduce((acc, item) => {
                    if (item.currencyCode && item.baseRate !== undefined) {
                        acc[item.currencyCode] = item.baseRate;
                    }
                    return acc;
                }, {});
            }
        }
        
        // 데이터 로드 후 전일대비 값 미리 계산하여 rawExchangeData에 추가
        rawExchangeData.forEach(item => {
            const curCode = item.currencyCode;
            const baseRate = item.baseRate;
            let changeValue = 0;
            if (typeof baseRate === 'number' && previousDayExchangeData[curCode] !== undefined) {
                changeValue = baseRate - previousDayExchangeData[curCode];
            }
            item.calculatedChangeRate = changeValue;
        });

        // 초기 필터링 및 정렬 적용
        applyFiltersAndSort();

    } catch (error) {
        console.error('loadData() 오류:', error);
        exchangeRateTableBody.innerHTML = `<tr><td colspan="5" style="color:red; text-align:center;">데이터를 불러오지 못했습니다: ${error.message}</td></tr>`;
        paginationControls.innerHTML = '';
    }
}

/**
 * 검색어에 따라 데이터를 필터링합니다.
 */
function filterData(searchTerm) {
    const lowerCaseSearchTerm = searchTerm.toLowerCase().trim();
    if (!lowerCaseSearchTerm) {
        allExchangeData = [...rawExchangeData]; // 검색어가 없으면 원본 데이터를 사용
    } else {
        allExchangeData = rawExchangeData.filter(item => {
            const countryName = (item.countryName || '').toLowerCase();
            const currencyName = (item.currencyName || '').toLowerCase();
            const currencyCode = (item.currencyCode || '').toLowerCase();
            
            return countryName.includes(lowerCaseSearchTerm) ||
                   currencyName.includes(lowerCaseSearchTerm) ||
                   currencyCode.includes(lowerCaseSearchTerm);
        });
    }
    // 필터링 후 현재 정렬 기준을 다시 적용
    sortData(currentSortBy);
}


/**
 * 데이터를 주어진 옵션에 따라 정렬합니다.
 * 'name': 국가명(이름) 기준 오름차순
 * 'change': 전일대비 변화량 기준 내림차순 (절대값이 큰 것부터)
 */
function sortData(sortBy) {
    currentSortBy = sortBy; // 현재 정렬 기준 업데이트

    if (sortBy === 'name') {
        allExchangeData.sort((a, b) => {
            const nameA = a.countryName || '';
            const nameB = b.countryName || '';
            return nameA.localeCompare(nameB, 'ko-KR');
        });
    } else if (sortBy === 'change') {
        allExchangeData.sort((a, b) => {
            const changeA = a.calculatedChangeRate !== undefined ? Math.abs(a.calculatedChangeRate) : 0;
            const changeB = b.calculatedChangeRate !== undefined ? Math.abs(b.calculatedChangeRate) : 0;
            return changeB - changeA; // 내림차순 (변동 폭이 큰 순)
        });
    }
    currentPage = 1; // 정렬 후 첫 페이지로 이동
    renderTable(currentPage);
    renderPagination();
}

/**
 * 필터링과 정렬을 한 번에 적용하고 테이블을 갱신합니다.
 */
function applyFiltersAndSort() {
    const searchTerm = searchFilterInput.value;
    filterData(searchTerm); // 필터링이 먼저 적용되고, 그 결과에 정렬이 적용됨
}


/**
 * 테이블 로우 클릭 시 Tableau 시각화를 업데이트합니다.
 */
function updateTableauViz(currencyCode) {
    const container = document.querySelector('#vizUSD');
    container.innerHTML = ''; // 기존 내용 지우기
    const objectHTML = `
        <object class='tableauViz' style="width: 100%; height: 500px;">
            <param name='host_url' value='https%3A%2F%2Fpublic.tableau.com%2F' />
            <param name='embed_code_version' value='3' />
            <param name='site_root' value='' />
            <param name='name' value='_17515184080120/${currencyCode}' />
            <param name='tabs' value='no' />
            <param name='toolbar' value='yes' />
            <param name='static_image' value='https://public.tableau.com/static/images/_1/_17515184080120/${currencyCode}/1.png' />
            <param name='language' value='ko-KR' />
        </object>
    `;
    container.insertAdjacentHTML('beforeend', objectHTML);
    const script = document.createElement('script');
    script.src = 'https://public.tableau.com/javascripts/api/viz_v1.js';
    document.body.appendChild(script);
}

// --- 이벤트 리스너 ---

// 테이블 로우 클릭 이벤트 리스너
document.addEventListener('click', (e) => {
    const row = e.target.closest('tr');
    if (!row || !row.parentElement || row.parentElement.tagName !== 'TBODY') return;
    const cells = row.getElementsByTagName('td');
    if (cells.length >= 3) {
        const currencyText = cells[2].innerText;
        const codeMatch = currencyText.match(/\(([^)]+)\)/);
        if (codeMatch) {
            const currencyCode = codeMatch[1];
            updateTableauViz(currencyCode);
        }
    }
});

// 정렬 옵션(select) 변경 이벤트 리스너
sortOptionSelect.addEventListener('change', (event) => {
    sortData(event.target.value);
});

// 검색 입력란(input) 키업(keyup) 이벤트 리스너
searchFilterInput.addEventListener('keyup', () => {
    applyFiltersAndSort(); // 검색어 변경 시 필터링 및 정렬 다시 적용
});

// 페이지 로드 시 데이터 불러오기
loadData();