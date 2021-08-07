package com.mor.test.domain.base;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestTableRepository extends JpaRepository<TestTable, Integer> {
}
