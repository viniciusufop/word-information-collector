package br.com.vfs.word.information.collector.repository;

import br.com.vfs.word.information.collector.entity.Word;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository  extends MongoRepository<Word, String> {

}
