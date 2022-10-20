CREATE TABLE IF NOT EXISTS users
(
    id    INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(128)                         NOT NULL,
    name  VARCHAR(255)                         NOT NULL,
    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                          NOT NULL,
    CONSTRAINT pk_category_id PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                        NOT NULL,
    category_id        INT                                  NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    description        VARCHAR(7000)                        NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    initiator_id       INT                                  NOT NULL,
    paid               BOOLEAN                              NOT NULL,
    participant_limit  INT DEFAULT 0                        NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN DEFAULT FALSE                NOT NULL,
    state              VARCHAR(50)                          NOT NULL,
    title              VARCHAR(120)                         NOT NULL,
    latitude           DECIMAL                              NOT NULL,
    longitude          DECIMAL                              NOT NULL,
    CONSTRAINT pk_event_id PRIMARY KEY (id),
    CONSTRAINT fk_event_category_id FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_event_initiator_id FOREIGN KEY (initiator_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    requester_id INT                                  NOT NULL,
    event_id     INT                                  NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    status       VARCHAR(20)                          NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_request_requester_id FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_request_event_id FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(255)                         NOT NULL,
    pinned BOOLEAN DEFAULT FALSE                NOT NULL,
    CONSTRAINT pk_compilation_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id INT NOT NULL,
    event_id       INT NOT NULL,
    CONSTRAINT pk_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations(id),
    CONSTRAINT pk_event_id FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id  INT                                  NOT NULL,
    author_id INT                                  NOT NULL,
    text      VARCHAR(10000)                        NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comment_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_comment_author_id FOREIGN KEY (author_id) REFERENCES users (id)
);