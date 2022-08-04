package com.zzw.repository;

import com.zzw.mo.messageMO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface messageRepository extends MongoRepository<messageMO,String> {

public List<messageMO> findAllByToUserIdOrderByCreateDateDesc(String userId, Pageable pageable);

}
