use chat;
SELECT count(m.id) AS ucount, m.user_id, u.name FROM messages m LEFT JOIN users u ON (u.id = m.user_id) GROUP BY m.user_id having ucount > 3;
