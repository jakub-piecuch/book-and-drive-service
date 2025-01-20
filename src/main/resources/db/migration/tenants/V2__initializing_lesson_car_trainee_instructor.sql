CREATE TABLE car (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    make VARCHAR NOT NULL,
    model VARCHAR NOT NULL,
    registration_number VARCHAR UNIQUE NOT NULL
);

CREATE TABLE instructor (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    sure_name VARCHAR NOT NULL,
    email VARCHAR UNIQUE NOT NULL
);

CREATE TABLE trainee (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    sure_name VARCHAR NOT NULL,
    email VARCHAR UNIQUE NOT NULL
);

CREATE TABLE lesson (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    instructor_id UUID NOT NULL,
    trainee_id UUID NOT NULL,
    car_id UUID,

    CONSTRAINT fk_instructor FOREIGN KEY (instructor_id) REFERENCES instructor (id) ON DELETE CASCADE,
    CONSTRAINT fk_trainee FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    CONSTRAINT fk_car FOREIGN KEY (car_id) REFERENCES car (id) ON DELETE SET NULL
);
