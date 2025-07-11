# UserServiceJDBC
### Общее:
Проект предназначен для освоения JDCB, путем создания CRUD операций для реляционной БД и логирования результатов автотестов в NoSQL БД.
***

### Использованные технологии
Maven, JDBC

Базы данных: <br> PostgreSQL, MongoDB
### DDL таблиц в PostgreSQL:
#### roles
CREATE TABLE public.roles (<br>
	id serial4 NOT NULL,<br>
	"name" varchar NOT NULL,<br>
	CONSTRAINT roles_name_key UNIQUE (name),<br>
	CONSTRAINT roles_pkey PRIMARY KEY (id)<br>
);<br>
#### users
CREATE TABLE public.users (<br>
	id serial4 NOT NULL,<br>
	"name" varchar NOT NULL,<br>
	CONSTRAINT users_pkey PRIMARY KEY (id)<br>
);<br>
#### user_roles
CREATE TABLE public.user_roles (<br>
	user_id int4 NOT NULL,<br>
	role_id int4 NOT NULL,<br>
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id)<br>
);
ALTER TABLE public.user_roles ADD CONSTRAINT user_roles_role_id_fkey <br>
FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;<br>
ALTER TABLE public.user_roles ADD CONSTRAINT user_roles_user_id_fkey <br>
FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;<br>

### Диаграмма БД:
<img width="1371" height="490" alt="image" src="https://github.com/user-attachments/assets/f41f4838-d38b-4d36-9f3d-9de961a66744" />

### Вид  лога в MongoDB Compass
<img width="528" height="188" alt="image" src="https://github.com/user-attachments/assets/aa081762-7932-4d47-bac1-265df7a4d3e0" />






