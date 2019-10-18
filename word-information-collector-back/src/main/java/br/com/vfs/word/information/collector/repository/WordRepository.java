package br.com.vfs.word.information.collector.repository;

import br.com.vfs.word.information.collector.entity.Word;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository  extends ReactiveMongoRepository<Word, String> {

}
