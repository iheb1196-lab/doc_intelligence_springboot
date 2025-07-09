package com.labels.fileslabelling.repositories;

import com.labels.fileslabelling.models.FileDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de persistence pour les FileDocument.
 * 
 * En étendant MongoRepository<FileDocument, String>, Spring Data MongoDB
 * génère automatiquement toutes les opérations CRUD de base (save, findById,
 * findAll, delete, etc.), ainsi que la pagination et le tri. Vous pouvez
 * également ajouter des méthodes de requête personnalisées simplement
 * en déclarant leur signature (par exemple findByStatus(String status)).
 * 
 * L’annotation @Repository permet à Spring de reconnaître cette interface
 * comme un composant de persistence et d’appliquer la traduction des
 * exceptions spécifiques à MongoDB en exceptions Spring Data plus génériques.
 */
@Repository
public interface FileDocumentRepository extends MongoRepository<FileDocument, String> {
    // ex. List<FileDocument> findByStatus(String status);
}
