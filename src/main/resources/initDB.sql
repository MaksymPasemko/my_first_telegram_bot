CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    chat_id    BIGINT UNIQUE    NOT NULL,
    name       VARCHAR           NOT NULL,
    score      INTEGER DEFAULT 0 NOT NULL,
    high_score INTEGER DEFAULT 0 NOT NULL,
    bot_state  VARCHAR           NOT NULL
);

CREATE TABLE IF NOT EXISTS java_quiz
(
    id             BIGSERIAL PRIMARY KEY,
    question       VARCHAR NOT NULL,
    correct_answer VARCHAR NOT NULL,
    option1        VARCHAR NOT NULL,
    option2        VARCHAR NOT NULL,
    option3        VARCHAR NOT NULL
);

