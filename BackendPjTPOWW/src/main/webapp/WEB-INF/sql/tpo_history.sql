CREATE TABLE IF NOT EXISTS tpo_history (
    h_id INT AUTO_INCREMENT PRIMARY KEY,
    un INT NOT NULL,
    request_date VARCHAR(20),  -- TPO 요청 날짜 (예: 2025-12-05)
    request_time VARCHAR(10),  -- TPO 요청 시간 (예: 14:00)
    what VARCHAR(50),          -- 상황 (예: 데이트)
    weather_summary TEXT,      -- 당시 날씨 요약
    ai_recommend TEXT,         -- AI 추천 결과
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (un) REFERENCES user(un) ON DELETE CASCADE
);
