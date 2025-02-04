-- 알림 이력 테이블
CREATE TABLE member_notification_history (
   member_notification_history_id CHAR(26) NOT NULL,
   title VARCHAR(255) NOT NULL,
   content VARCHAR(255) NOT NULL,
   aos_deep_link VARCHAR(255),
   ios_deep_link VARCHAR(255),
   sender_member_id CHAR(26) NOT NULL,
   receiver_member_id CHAR(26) NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Assuming BaseEntity adds created_at
   PRIMARY KEY (member_notification_history_id),
   INDEX member_notification_history_idx1 (sender_member_id),
   INDEX member_notification_history_idx2 (receiver_member_id)
);

-- 시스템용 계정 생성
insert into member (member_id,
                    family_id,
                    day_of_birth,
                    name,
                    profile_img_url,
                    created_at,
                    updated_at,
                    deleted_at,
                    profile_img_key,
                    family_join_at)
values (
        '99999999999999999999999999',
        null,
        '9999-12-31',
        'SYSTEM',
        'https://oing-bucket.kr.object.ncloudstorage.com/images/profile/system.jpg',
        now(),
        now(),
        null,
        'images/profile/system.jpg',
        null
       ),
       (
        '99999999999999999999999998',
        null,
        '9999-12-31',
        'NOTICE',
        'https://oing-bucket.kr.object.ncloudstorage.com/images/profile/notice.jpg',
        now(),
        now(),
        null,
        'images/profile/notice.jpg',
        null
       );
