INSERT INTO decision_probability (decision_type, probability) VALUES ('LAB', 0.33);
INSERT INTO decision_probability (decision_type, probability) VALUES ('XRAY', 0.33);
INSERT INTO decision_probability (decision_type, probability) VALUES ('TREATMENT', 0.33);

INSERT INTO average_time (event, average_time) VALUES ('check-in', 3);
INSERT INTO average_time (event, average_time) VALUES ('doctor', 5);
INSERT INTO average_time (event, average_time) VALUES ('lab', 10);
INSERT INTO average_time (event, average_time) VALUES ('xray', 8);
INSERT INTO average_time (event, average_time) VALUES ('treatment', 12);
INSERT INTO average_time (event, average_time) VALUES ('arrival', 15);
