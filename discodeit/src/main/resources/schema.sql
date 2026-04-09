CREATE TABLE binary_contents
(
    id           uuid PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    file_name    varchar(255)             NOT NULL,
    size         bigint                   NOT NULL,
    content_type varchar(100)             NOT NULL
);

CREATE TABLE channels
(
    id          uuid PRIMARY KEY,
    created_at  timestamp with time zone NOT NULL,
    updated_at  timestamp with time zone,
    name        varchar(100),
    description varchar(500),
    type        varchar(10)              NOT NULL,
    CONSTRAINT check_channel_type CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE users
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username   varchar(50) UNIQUE       NOT NULL,
    email      varchar(100) UNIQUE      NOT NULL,
    password   varchar(60)              NOT NULL,
    profile_id uuid UNIQUE,
    CONSTRAINT fk_profile FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL
);

CREATE TABLE user_statuses
(
    id             uuid PRIMARY KEY,
    created_at     timestamp with time zone NOT NULL,
    updated_at     timestamp with time zone,
    user_id        uuid UNIQUE              NOT NULL,
    last_active_at timestamp with time zone NOT NULL,
    CONSTRAINT fk_user_status_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE read_statuses
(
    id           uuid PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    updated_at   timestamp with time zone,
    user_id      uuid                     NOT NULL,
    channel_id   uuid                     NOT NULL,
    last_read_at timestamp with time zone NOT NULL,
    CONSTRAINT fk_read_status_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_read_status_channel FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    CONSTRAINT uk_user_channel UNIQUE (user_id, channel_id)
);

CREATE TABLE messages
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content    text,
    channel_id uuid                     NOT NULL,
    author_id  uuid                     NOT NULL,
    CONSTRAINT fk_message_channel FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    CONSTRAINT fk_message_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE message_attachments
(
    message_id    uuid NOT NULL,
    attachment_id uuid NOT NULL,
    CONSTRAINT fk_message FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    CONSTRAINT fk_attachment FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);

