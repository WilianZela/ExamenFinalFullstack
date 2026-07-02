/*
************************************************************************

Los UUIDs de usuarios y destinos son dinámicos, por lo tanto estos
inserts son solo de referencia. Primero registra usuarios y destinos
mediante sus respectivos servicios, luego crea reseñas via la API.

Yo lo puse como comentario para dejar la referencia de como sería pero no es necesario.
************************************************************************

INSERT INTO reviews (id, destination_id, user_id, username, place_name, rating, comment, created_at)
VALUES
(UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), 'juan.perez', 'Hotel Plaza', 4, 'Muy buena atención', NOW()),
(UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), UUID_TO_BIN(UUID()), 'maria.gonzalez', 'Cerro San Cristóbal', 5, 'Vista increíble', NOW());

INSERT INTO review_tags (id, review_id, tag_name)
SELECT UUID_TO_BIN(UUID()), id, 'limpio' FROM reviews WHERE username = 'juan.perez';

INSERT INTO review_tags (id, review_id, tag_name)
SELECT UUID_TO_BIN(UUID()), id, 'buen servicio' FROM reviews WHERE username = 'juan.perez';

*/