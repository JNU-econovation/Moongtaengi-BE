-- 기존 데이터 삭제 (개발 환경)
DELETE FROM members WHERE kakao_id = 'admin-test';

-- 관리자 계정 INSERT
INSERT INTO members (id, created_at, updated_at, kakao_id, nickname, role, status, total_experience, profile_icon)
VALUES (1, NOW(), NOW(), 'admin-test', '관리자', 'ADMIN', 'ACTIVE', 0,'DEFAULT');

-- ✅ AUTO_INCREMENT를 2부터 시작
ALTER TABLE members ALTER COLUMN id RESTART WITH 2;
