INSERT INTO family(family_id, score, created_at)
VALUES ('11111111111111111111111111', 0, '2018-01-01T00:00:00.000Z');

INSERT INTO member(member_id, family_id, family_join_at, day_of_birth, name, profile_img_url, profile_img_key, created_at, updated_at, deleted_at)
VALUES (1, '11111111111111111111111111', '2024-01-31T13:00:00.000Z', '1990-01-01', '김철수', 'https://s3.ap-northeast-2.amazonaws.com/our-family/profile/1.jpg', 'profile/1.jpg', '2018-01-01T00:00:00.000Z', '2018-01-01T00:00:00.000Z', null);