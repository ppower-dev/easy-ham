INSERT INTO campus (name) VALUES
                              ('서울'),
                              ('대전'),
                              ('구미'),
                              ('부울경'),
                              ('광주');

INSERT INTO maincode (main_code, main_code_name, main_code_description, is_used)
VALUES
    ('EDU', '학사', '학사 관련 메인코드', 1),
    ('JOB', '취업', '취업 관련 메인코드', 1);

INSERT INTO subcode (upper_code_id, sub_code, sub_code_name, sub_code_description, is_used)
SELECT upper_code_id, 'TODO', '할일', '학사 - 할일', 1 FROM maincode WHERE TRIM(main_code) = 'EDU';
INSERT INTO subcode (upper_code_id, sub_code, sub_code_name, sub_code_description, is_used)
SELECT upper_code_id, 'LECT', '특강', '학사 - 특강', 1 FROM maincode WHERE TRIM(main_code) = 'EDU';
INSERT INTO subcode (upper_code_id, sub_code, sub_code_name, sub_code_description, is_used)
SELECT upper_code_id, 'INFO', '정보', '학사 - 정보', 1 FROM maincode WHERE TRIM(main_code) = 'EDU';
INSERT INTO subcode (upper_code_id, sub_code, sub_code_name, sub_code_description, is_used)
SELECT upper_code_id, 'EVT', '이벤트', '학사 - 이벤트', 1 FROM maincode WHERE TRIM(main_code) = 'EDU';

INSERT INTO subcode (upper_code_id, sub_code, sub_code_name, sub_code_description, is_used)
SELECT upper_code_id, 'TODO', '할일', '취업 - 할일', 1 FROM maincode WHERE TRIM(main_code) = 'JOB';
INSERT INTO subcode (upper_code_id, sub_code, sub_code_name, sub_code_description, is_used)
SELECT upper_code_id, 'LECT', '특강', '취업 - 특강', 1 FROM maincode WHERE TRIM(main_code) = 'JOB';
INSERT INTO subcode (upper_code_id, sub_code, sub_code_name, sub_code_description, is_used)
SELECT upper_code_id, 'INFO', '정보', '취업 - 정보', 1 FROM maincode WHERE TRIM(main_code) = 'JOB';
INSERT INTO subcode (upper_code_id, sub_code, sub_code_name, sub_code_description, is_used)
SELECT upper_code_id, 'EVT', '이벤트', '취업 - 이벤트', 1 FROM maincode WHERE TRIM(main_code) = 'JOB';
