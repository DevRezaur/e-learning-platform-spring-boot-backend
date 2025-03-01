#!/bin/bash

psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'keycloak'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE keycloak"
psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'user_service_db'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE user_service_db"
psql -U postgres -tc "SELECT 1 FROM pg_database WHERE datname = 'course_management_service_db'" | grep -q 1 || psql -U postgres -c "CREATE DATABASE course_management_service_db"
