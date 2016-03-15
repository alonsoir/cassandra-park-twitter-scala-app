-- description: create things table
-- authoredAt: 1418718253000
-- up:

CREATE TABLE things (
  id int,
  name text,
  PRIMARY KEY (id)
);

-- down:

DROP TABLE things;
