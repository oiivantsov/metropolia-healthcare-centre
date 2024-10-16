INSERT INTO decision_probability (decision_type, probability) VALUES ('NO_TREATMENT', 0.10);
INSERT INTO decision_probability (decision_type, probability) VALUES ('LAB', 0.40);
INSERT INTO decision_probability (decision_type, probability) VALUES ('XRAY', 0.40);
INSERT INTO decision_probability (decision_type, probability) VALUES ('TREATMENT', 0.10);

INSERT INTO distribution (event, distribution, average_time) VALUES ('check-in', 'negexp', 3);
INSERT INTO distribution (event, distribution, average_time) VALUES ('doctor', 'negexp', 5);
INSERT INTO distribution (event, distribution, average_time) VALUES ('lab', 'negexp', 10);
INSERT INTO distribution (event, distribution, average_time) VALUES ('xray', 'negexp', 8);
INSERT INTO distribution (event, distribution, average_time) VALUES ('treatment', 'negexp', 12);
INSERT INTO distribution (event, distribution, average_time) VALUES ('arrival', 'negexp', 15);
