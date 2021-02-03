package projet.rest.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.rest.data.models.DislikeEntity;

public interface DislikeRepository extends JpaRepository<DislikeEntity, Long> {

}
