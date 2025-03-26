UPDATE member
SET profile_img_key = 'images/admin/oing.png',
    profile_img_url = 'https://oing-bucket.kr.object.ncloudstorage.com/images/admin/oing.png'
WHERE member_id IN ('99999999999999999999999999', '99999999999999999999999998');
