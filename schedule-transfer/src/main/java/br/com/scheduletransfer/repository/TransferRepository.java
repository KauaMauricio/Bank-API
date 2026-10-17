package br.com.scheduletransfer.repository;

import br.com.scheduletransfer.domain.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findAllByOrderByCreatedAtDesc();
}
