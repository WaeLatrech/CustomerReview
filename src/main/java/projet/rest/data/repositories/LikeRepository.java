package projet.rest.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import projet.rest.data.models.LikeEntity;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

}
