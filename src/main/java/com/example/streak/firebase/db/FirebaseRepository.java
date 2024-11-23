package com.example.streak.firebase.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FirebaseRepository extends JpaRepository<FirebaseEntity, Long> {
}
