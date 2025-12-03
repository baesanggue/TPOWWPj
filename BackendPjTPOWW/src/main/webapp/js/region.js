// 대한민국 행정구역별 격자 좌표 (기상청 API 기준)
// 2025년 최신 행정구역 반영 (부천시 구 부활, 전북특별자치도 등)

const sigunguData = {
  // 1. 서울특별시
  "Seoul": [
    { name: "종로구", nx: 60, ny: 127 },
    { name: "중구", nx: 60, ny: 127 },
    { name: "용산구", nx: 60, ny: 126 },
    { name: "성동구", nx: 61, ny: 127 },
    { name: "광진구", nx: 62, ny: 126 },
    { name: "동대문구", nx: 61, ny: 127 },
    { name: "중랑구", nx: 62, ny: 128 },
    { name: "성북구", nx: 61, ny: 127 },
    { name: "강북구", nx: 61, ny: 128 },
    { name: "도봉구", nx: 61, ny: 129 },
    { name: "노원구", nx: 61, ny: 129 },
    { name: "은평구", nx: 59, ny: 127 },
    { name: "서대문구", nx: 59, ny: 127 },
    { name: "마포구", nx: 59, ny: 127 },
    { name: "양천구", nx: 58, ny: 126 },
    { name: "강서구", nx: 58, ny: 126 },
    { name: "구로구", nx: 58, ny: 125 },
    { name: "금천구", nx: 59, ny: 124 },
    { name: "영등포구", nx: 58, ny: 126 },
    { name: "동작구", nx: 59, ny: 125 },
    { name: "관악구", nx: 59, ny: 125 },
    { name: "서초구", nx: 61, ny: 125 },
    { name: "강남구", nx: 61, ny: 126 },
    { name: "송파구", nx: 62, ny: 126 },
    { name: "강동구", nx: 62, ny: 126 }
  ],

  // 2. 부산광역시
  "Busan": [
    { name: "중구", nx: 97, ny: 74 },
    { name: "서구", nx: 97, ny: 74 },
    { name: "동구", nx: 98, ny: 75 },
    { name: "영도구", nx: 98, ny: 74 },
    { name: "부산진구", nx: 97, ny: 75 },
    { name: "동래구", nx: 98, ny: 76 },
    { name: "남구", nx: 98, ny: 75 },
    { name: "북구", nx: 96, ny: 76 },
    { name: "해운대구", nx: 99, ny: 75 },
    { name: "사하구", nx: 96, ny: 74 },
    { name: "금정구", nx: 98, ny: 77 },
    { name: "강서구", nx: 96, ny: 76 },
    { name: "연제구", nx: 98, ny: 76 },
    { name: "수영구", nx: 99, ny: 75 },
    { name: "사상구", nx: 96, ny: 75 },
    { name: "기장군", nx: 100, ny: 77 }
  ],

  // 3. 대구광역시 (군위군 포함)
  "Daegu": [
    { name: "중구", nx: 89, ny: 90 },
    { name: "동구", nx: 90, ny: 91 },
    { name: "서구", nx: 88, ny: 90 },
    { name: "남구", nx: 89, ny: 90 },
    { name: "북구", nx: 89, ny: 91 },
    { name: "수성구", nx: 89, ny: 90 },
    { name: "달서구", nx: 88, ny: 90 },
    { name: "달성군", nx: 86, ny: 88 },
    { name: "군위군", nx: 88, ny: 99 }
  ],

  // 4. 인천광역시
  "Incheon": [
    { name: "중구", nx: 54, ny: 125 },
    { name: "동구", nx: 54, ny: 125 },
    { name: "미추홀구", nx: 54, ny: 124 },
    { name: "연수구", nx: 55, ny: 123 },
    { name: "남동구", nx: 56, ny: 124 },
    { name: "부평구", nx: 55, ny: 125 },
    { name: "계양구", nx: 56, ny: 126 },
    { name: "서구", nx: 55, ny: 126 },
    { name: "강화군", nx: 51, ny: 130 },
    { name: "옹진군", nx: 54, ny: 124 }
  ],

  // 5. 광주광역시
  "Gwangju": [
    { name: "동구", nx: 60, ny: 74 },
    { name: "서구", nx: 59, ny: 74 },
    { name: "남구", nx: 59, ny: 73 },
    { name: "북구", nx: 59, ny: 75 },
    { name: "광산구", nx: 57, ny: 74 }
  ],

  // 6. 대전광역시
  "Daejeon": [
    { name: "동구", nx: 68, ny: 100 },
    { name: "중구", nx: 68, ny: 100 },
    { name: "서구", nx: 67, ny: 100 },
    { name: "유성구", nx: 67, ny: 101 },
    { name: "대덕구", nx: 68, ny: 100 }
  ],

  // 7. 울산광역시
  "Ulsan": [
    { name: "중구", nx: 102, ny: 84 },
    { name: "남구", nx: 102, ny: 84 },
    { name: "동구", nx: 104, ny: 83 },
    { name: "북구", nx: 103, ny: 85 },
    { name: "울주군", nx: 101, ny: 84 }
  ],

  // 8. 세종특별자치시
  "Sejong": [
    { name: "세종특별자치시", nx: 66, ny: 103 }
  ],

  // 9. 경기도
  "Gyeonggi": [
    { name: "수원시 장안구", nx: 60, ny: 121 },
    { name: "수원시 권선구", nx: 60, ny: 120 },
    { name: "수원시 팔달구", nx: 61, ny: 121 },
    { name: "수원시 영통구", nx: 61, ny: 120 },
    { name: "성남시 수정구", nx: 63, ny: 124 },
    { name: "성남시 중원구", nx: 63, ny: 124 },
    { name: "성남시 분당구", nx: 62, ny: 123 },
    { name: "의정부시", nx: 61, ny: 130 },
    { name: "안양시 만안구", nx: 59, ny: 123 },
    { name: "안양시 동안구", nx: 59, ny: 123 },
    { name: "부천시 원미구", nx: 57, ny: 125 },
    { name: "부천시 소사구", nx: 57, ny: 125 },
    { name: "부천시 오정구", nx: 57, ny: 126 },
    { name: "광명시", nx: 58, ny: 125 },
    { name: "평택시", nx: 62, ny: 114 },
    { name: "동두천시", nx: 61, ny: 134 },
    { name: "안산시 상록구", nx: 58, ny: 121 },
    { name: "안산시 단원구", nx: 57, ny: 121 },
    { name: "고양시 덕양구", nx: 57, ny: 128 },
    { name: "고양시 일산동구", nx: 56, ny: 129 },
    { name: "고양시 일산서구", nx: 56, ny: 129 },
    { name: "과천시", nx: 60, ny: 124 },
    { name: "구리시", nx: 62, ny: 127 },
    { name: "남양주시", nx: 64, ny: 128 },
    { name: "오산시", nx: 62, ny: 118 },
    { name: "시흥시", nx: 57, ny: 123 },
    { name: "군포시", nx: 59, ny: 122 },
    { name: "의왕시", nx: 60, ny: 122 },
    { name: "하남시", nx: 64, ny: 126 },
    { name: "용인시 처인구", nx: 64, ny: 119 },
    { name: "용인시 기흥구", nx: 62, ny: 120 },
    { name: "용인시 수지구", nx: 62, ny: 121 },
    { name: "파주시", nx: 56, ny: 131 },
    { name: "이천시", nx: 68, ny: 121 },
    { name: "안성시", nx: 65, ny: 115 },
    { name: "김포시", nx: 55, ny: 128 },
    { name: "화성시", nx: 57, ny: 119 },
    { name: "광주시", nx: 65, ny: 123 },
    { name: "양주시", nx: 61, ny: 131 },
    { name: "포천시", nx: 64, ny: 134 },
    { name: "여주시", nx: 71, ny: 121 },
    { name: "연천군", nx: 61, ny: 138 },
    { name: "가평군", nx: 69, ny: 133 },
    { name: "양평군", nx: 69, ny: 125 }
  ],

  // 10. 강원특별자치도
  "Gangwon": [
    { name: "춘천시", nx: 73, ny: 134 },
    { name: "원주시", nx: 76, ny: 122 },
    { name: "강릉시", nx: 92, ny: 131 },
    { name: "동해시", nx: 97, ny: 127 },
    { name: "태백시", nx: 95, ny: 119 },
    { name: "속초시", nx: 87, ny: 141 },
    { name: "삼척시", nx: 98, ny: 125 },
    { name: "홍천군", nx: 75, ny: 130 },
    { name: "횡성군", nx: 77, ny: 125 },
    { name: "영월군", nx: 86, ny: 119 },
    { name: "평창군", nx: 84, ny: 123 },
    { name: "정선군", nx: 89, ny: 123 },
    { name: "철원군", nx: 65, ny: 139 },
    { name: "화천군", nx: 72, ny: 139 },
    { name: "양구군", nx: 77, ny: 139 },
    { name: "인제군", nx: 80, ny: 138 },
    { name: "고성군", nx: 85, ny: 145 },
    { name: "양양군", nx: 88, ny: 138 }
  ],

  // 11. 충청북도
  "Chungbuk": [
    { name: "청주시 상당구", nx: 69, ny: 106 },
    { name: "청주시 서원구", nx: 69, ny: 107 },
    { name: "청주시 흥덕구", nx: 67, ny: 106 },
    { name: "청주시 청원구", nx: 69, ny: 107 },
    { name: "충주시", nx: 76, ny: 114 },
    { name: "제천시", nx: 81, ny: 118 },
    { name: "보은군", nx: 73, ny: 103 },
    { name: "옥천군", nx: 71, ny: 99 },
    { name: "영동군", nx: 74, ny: 97 },
    { name: "증평군", nx: 71, ny: 110 },
    { name: "진천군", nx: 68, ny: 111 },
    { name: "괴산군", nx: 74, ny: 111 },
    { name: "음성군", nx: 72, ny: 113 },
    { name: "단양군", nx: 84, ny: 115 }
  ],

  // 12. 충청남도
  "Chungnam": [
    { name: "천안시 동남구", nx: 63, ny: 110 },
    { name: "천안시 서북구", nx: 63, ny: 112 },
    { name: "공주시", nx: 63, ny: 102 },
    { name: "보령시", nx: 54, ny: 100 },
    { name: "아산시", nx: 60, ny: 110 },
    { name: "서산시", nx: 51, ny: 110 },
    { name: "논산시", nx: 62, ny: 97 },
    { name: "계룡시", nx: 65, ny: 99 },
    { name: "당진시", nx: 54, ny: 112 },
    { name: "금산군", nx: 69, ny: 95 },
    { name: "부여군", nx: 59, ny: 99 },
    { name: "서천군", nx: 55, ny: 94 },
    { name: "청양군", nx: 57, ny: 103 },
    { name: "홍성군", nx: 55, ny: 106 },
    { name: "예산군", nx: 58, ny: 107 },
    { name: "태안군", nx: 48, ny: 109 }
  ],

  // 13. 전북특별자치도
  "Jeonbuk": [
    { name: "전주시 완산구", nx: 63, ny: 89 },
    { name: "전주시 덕진구", nx: 63, ny: 89 },
    { name: "군산시", nx: 56, ny: 92 },
    { name: "익산시", nx: 60, ny: 91 },
    { name: "정읍시", nx: 58, ny: 83 },
    { name: "남원시", nx: 68, ny: 80 },
    { name: "김제시", nx: 59, ny: 88 },
    { name: "완주군", nx: 63, ny: 89 },
    { name: "진안군", nx: 68, ny: 88 },
    { name: "무주군", nx: 72, ny: 93 },
    { name: "장수군", nx: 70, ny: 85 },
    { name: "임실군", nx: 66, ny: 84 },
    { name: "순창군", nx: 63, ny: 79 },
    { name: "고창군", nx: 56, ny: 80 },
    { name: "부안군", nx: 56, ny: 87 }
  ],

  // 14. 전라남도
  "Jeonnam": [
    { name: "목포시", nx: 50, ny: 67 },
    { name: "여수시", nx: 73, ny: 66 },
    { name: "순천시", nx: 70, ny: 70 },
    { name: "나주시", nx: 56, ny: 71 },
    { name: "광양시", nx: 73, ny: 70 },
    { name: "담양군", nx: 61, ny: 78 },
    { name: "곡성군", nx: 66, ny: 77 },
    { name: "구례군", nx: 69, ny: 75 },
    { name: "고흥군", nx: 66, ny: 62 },
    { name: "보성군", nx: 62, ny: 66 },
    { name: "화순군", nx: 61, ny: 72 },
    { name: "장흥군", nx: 59, ny: 64 },
    { name: "강진군", nx: 57, ny: 63 },
    { name: "해남군", nx: 54, ny: 61 },
    { name: "영암군", nx: 56, ny: 66 },
    { name: "무안군", nx: 52, ny: 71 },
    { name: "함평군", nx: 52, ny: 72 },
    { name: "영광군", nx: 52, ny: 77 },
    { name: "장성군", nx: 57, ny: 77 },
    { name: "완도군", nx: 57, ny: 56 },
    { name: "진도군", nx: 48, ny: 59 },
    { name: "신안군", nx: 50, ny: 66 }
  ],

  // 15. 경상북도
  "Gyeongbuk": [
    { name: "포항시 남구", nx: 102, ny: 94 },
    { name: "포항시 북구", nx: 102, ny: 95 },
    { name: "경주시", nx: 100, ny: 91 },
    { name: "김천시", nx: 80, ny: 96 },
    { name: "안동시", nx: 91, ny: 106 },
    { name: "구미시", nx: 84, ny: 96 },
    { name: "영주시", nx: 89, ny: 111 },
    { name: "영천시", nx: 95, ny: 93 },
    { name: "상주시", nx: 81, ny: 102 },
    { name: "문경시", nx: 81, ny: 106 },
    { name: "경산시", nx: 91, ny: 90 },
    { name: "의성군", nx: 90, ny: 101 },
    { name: "청송군", nx: 96, ny: 103 },
    { name: "영양군", nx: 97, ny: 108 },
    { name: "영덕군", nx: 102, ny: 103 },
    { name: "청도군", nx: 91, ny: 86 },
    { name: "고령군", nx: 83, ny: 87 },
    { name: "성주군", nx: 83, ny: 91 },
    { name: "칠곡군", nx: 85, ny: 93 },
    { name: "예천군", nx: 86, ny: 107 },
    { name: "봉화군", nx: 90, ny: 113 },
    { name: "울진군", nx: 102, ny: 115 },
    { name: "울릉군", nx: 127, ny: 127 },
    { name: "독도", nx: 144, ny: 123 }
  ],

  // 16. 경상남도
  "Gyeongnam": [
    { name: "창원시 의창구", nx: 90, ny: 77 },
    { name: "창원시 성산구", nx: 91, ny: 76 },
    { name: "창원시 마산합포구", nx: 89, ny: 76 },
    { name: "창원시 마산회원구", nx: 89, ny: 76 },
    { name: "창원시 진해구", nx: 91, ny: 75 },
    { name: "진주시", nx: 81, ny: 75 },
    { name: "통영시", nx: 87, ny: 68 },
    { name: "사천시", nx: 80, ny: 71 },
    { name: "김해시", nx: 95, ny: 77 },
    { name: "밀양시", nx: 92, ny: 83 },
    { name: "거제시", nx: 90, ny: 69 },
    { name: "양산시", nx: 97, ny: 79 },
    { name: "의령군", nx: 83, ny: 78 },
    { name: "함안군", nx: 86, ny: 77 },
    { name: "창녕군", nx: 87, ny: 83 },
    { name: "고성군", nx: 85, ny: 71 },
    { name: "남해군", nx: 77, ny: 68 },
    { name: "하동군", nx: 74, ny: 73 },
    { name: "산청군", nx: 76, ny: 80 },
    { name: "함양군", nx: 74, ny: 82 },
    { name: "거창군", nx: 77, ny: 86 },
    { name: "합천군", nx: 81, ny: 84 }
  ],

  // 17. 제주특별자치도
  "Jeju": [
    { name: "제주시", nx: 53, ny: 38 },
    { name: "서귀포시", nx: 53, ny: 33 }
  ]
};

document.addEventListener("DOMContentLoaded", function () {
  const regionSelect = document.getElementById("region");
  const sigunguSelect = document.getElementById("sigungu");
  const nxInput = document.getElementById("nx");
  const nyInput = document.getElementById("ny");

  if (!regionSelect || !sigunguSelect) return;

  // 1. 시/도 선택 시 시/군/구 목록 업데이트
  regionSelect.addEventListener("change", function () {
    const selectedRegion = regionSelect.value;
    sigunguSelect.innerHTML = "<option value=''>-- 시·군·구 선택 --</option>";
    
    // 좌표 초기화 (input 태그가 있을 때만)
    if(nxInput) nxInput.value = "";
    if(nyInput) nyInput.value = "";

    const list = sigunguData[selectedRegion];
    if (!list) return;

    list.forEach(function (item) {
      const opt = document.createElement("option");
      opt.value = item.name; 
      opt.textContent = item.name;
      opt.dataset.nx = item.nx;
      opt.dataset.ny = item.ny;
      sigunguSelect.appendChild(opt);
    });
    
    // 세종특별자치시 예외 처리 (하위 행정구역이 1개뿐임)
    if(list.length === 1) {
        sigunguSelect.selectedIndex = 1; 
        sigunguSelect.dispatchEvent(new Event('change'));
    }
  });

  // 2. 시/군/구 선택 시 좌표 업데이트
  sigunguSelect.addEventListener("change", function () {
    const selectedOption = sigunguSelect.options[sigunguSelect.selectedIndex];
    
    if (selectedOption && selectedOption.value) {
        // input 태그가 존재할 때만 값 설정 (회원가입 페이지 오류 방지)
        if(nxInput) nxInput.value = selectedOption.dataset.nx;
        if(nyInput) nyInput.value = selectedOption.dataset.ny;
        
        console.log("Selected:", selectedOption.value, "NX:", selectedOption.dataset.nx, "NY:", selectedOption.dataset.ny);
    } else {
        if(nxInput) nxInput.value = "";
        if(nyInput) nyInput.value = "";
    }
  });
});