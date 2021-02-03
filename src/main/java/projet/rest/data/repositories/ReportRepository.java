package projet.rest.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.rest.data.models.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity,Long> {

}
