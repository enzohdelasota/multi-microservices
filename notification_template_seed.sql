-- Script para poblar la tabla de templates de notificación para el microservicio

CREATE TABLE IF NOT EXISTS notification_templates (
    id SERIAL PRIMARY KEY,
    type VARCHAR(100) NOT NULL UNIQUE,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL
);

INSERT INTO notification_templates (type, subject, body) VALUES (
    'USER_CREATED',
    'Bienvenido, {{name}}',
    '<html>
<body style="font-family:sans-serif;">
<h2 style="color:#4CAF50;">¡Bienvenido, {{name}}!</h2>
<p>Tu usuario ha sido creado exitosamente.</p>
<p style="font-size:12px;color:#888;">Este es un mensaje automático.</p>
</body>
</html>'
) ON CONFLICT (type) DO NOTHING;
