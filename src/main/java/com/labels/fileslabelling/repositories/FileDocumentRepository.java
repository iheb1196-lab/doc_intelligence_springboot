
package com.labels.fileslabelling.repositories;

import com.labels.fileslabelling.models.FileDocument;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDocumentRepository extends MongoRepository<FileDocument, String> {
}
