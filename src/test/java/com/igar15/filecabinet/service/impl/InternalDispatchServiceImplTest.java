package com.igar15.filecabinet.service.impl;

import com.igar15.filecabinet.DocumentTestData;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.service.DocumentService;
import com.igar15.filecabinet.service.InternalDispatchService;
import com.igar15.filecabinet.util.exception.NotFoundException;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;

import static com.igar15.filecabinet.InternalDispatchTestData.*;

class InternalDispatchServiceImplTest extends AbstractServiceTest {

    @Autowired
    private InternalDispatchService internalDispatchService;

    @Autowired
    private DocumentService documentService;


    @Test
    void create() {
        InternalDispatch created = internalDispatchService.create(getNew());
        int newId = created.id();
        InternalDispatch newInternalDispatch = getNew();
        newInternalDispatch.setId(newId);
        Assertions.assertEquals(newInternalDispatch, created);
        Assertions.assertEquals(internalDispatchService.findById(newId), newInternalDispatch);
    }

    @Test
    void createWithWrongValues() {
        getNewsWithWrongValues().forEach(internalDispatch -> {
            validateRootCause(PSQLException.class, () -> internalDispatchService.create(internalDispatch));
        });
    }

    @Test
    void createDuplicateStampAndDocument() {
        Assertions.assertThrows(DataAccessException.class, () -> internalDispatchService.create(getNewWithDuplicateStampAndAlbumName()));
    }

    @Test
    void createDuplicateWaybillAndDispatchDate() {
        Assertions.assertThrows(DataAccessException.class, () -> internalDispatchService.create(getNewWithDuplicateWaybillAndDispatchDate()));
    }

    @Test
    void findById() {
        InternalDispatch found = internalDispatchService.findById(INTERNAL_DISPATCH1_ID);
        Assertions.assertEquals(INTERNAL_DISPATCH1, found);
        Assertions.assertThrows(LazyInitializationException.class, () -> found.getDocuments().size());
    }

    @Test
    void findByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> internalDispatchService.findById(NOT_FOUND));
    }

    @Test
    void findByIdWithDocuments() {
        InternalDispatch found = internalDispatchService.findByIdWithDocuments(INTERNAL_DISPATCH1_ID);
        Assertions.assertEquals(INTERNAL_DISPATCH1, found);
        Assertions.assertEquals(INTERNAL_DISPATCH1.getDocuments(), found.getDocuments());
    }

    @Test
    void findByIdAndIsAlbumWithDocuments() {
        InternalDispatch found = internalDispatchService.findByIdAndIsAlbumWithDocuments(INTERNAL_DISPATCH2.getId(), true);
        Assertions.assertEquals(INTERNAL_DISPATCH2, found);
        Assertions.assertEquals(INTERNAL_DISPATCH2.getDocuments(), found.getDocuments());
        found = internalDispatchService.findByIdAndIsAlbumWithDocuments(INTERNAL_DISPATCH1_ID, false);
        Assertions.assertEquals(INTERNAL_DISPATCH1, found);
        Assertions.assertEquals(INTERNAL_DISPATCH1.getDocuments(), found.getDocuments());
    }

    @Test
    void findAll() {
        Page<InternalDispatch> page = internalDispatchService.findAll(PAGEABLE_SORT_BY_DISPATCH_DATE);
        Assertions.assertEquals(PAGE_FIND_ALL, page);
    }

    @Test
    void findAllByIsAlbumAndIsActive() {
        Page<InternalDispatch> page = internalDispatchService.findAllByIsAlbumAndIsActive(true, true, PAGEABLE_SORT_BY_DISPATCH_DATE);
        Assertions.assertEquals(PAGE_FIND_ALL_BY_IS_ALBUM_AND_IS_ACTIVE, page);
    }

    @Test
    void findAllByNotAlbumAndIsActive() {
        Page<InternalDispatch> page = internalDispatchService.findAllByIsAlbumAndIsActive(false, true, PAGEABLE_SORT_BY_DISPATCH_DATE);
        Assertions.assertEquals(PAGE_FIND_ALL_BY_NOT_ALBUM_AND_IS_ACTIVE, page);
    }

    @Test
    void findAllByIsAlbumAndNotActive() {
        Page<InternalDispatch> page = internalDispatchService.findAllByIsAlbumAndIsActive(true, false, PAGEABLE_SORT_BY_DISPATCH_DATE);
        Assertions.assertEquals(PAGE_FIND_ALL_BY_IS_ALBUM_AND_NOT_ACTIVE, page);
    }

    @Test
    void findAllByNotAlbumAndNotActive() {
        Page<InternalDispatch> page = internalDispatchService.findAllByIsAlbumAndIsActive(false, false, PAGEABLE_SORT_BY_DISPATCH_DATE);
        Assertions.assertEquals(PAGE_FIND_ALL_BY_NOT_ALBUM_AND_NOT_ACTIVE, page);
    }

    @Test
    void findAllByAlbumNameContainsIgnoreCaseAndIsActive() {
        Page<InternalDispatch> page = internalDispatchService.findAllByAlbumNameContainsIgnoreCaseAndIsActive(ALBUM_NAME_EXAMPLE, true, PAGEABLE_SORT_BY_ALBUM_NAME);
        Assertions.assertEquals(PAGE_FIND_ALL_BY_ALBUM_NAME_AND_IS_ACTIVE, page);
    }

    @Test
    void findAllByAlbumNameContainsIgnoreCaseAndNotActive() {
        Page<InternalDispatch> page = internalDispatchService.findAllByAlbumNameContainsIgnoreCaseAndIsActive(ALBUM_NAME_EXAMPLE, false, PAGEABLE_SORT_BY_ALBUM_NAME);
        Assertions.assertEquals(PAGE_FIND_ALL_BY_ALBUM_NAME_AND_NOT_ACTIVE, page);
    }

    @Test
    void findAllByAlbumNameEmptyAndIsActive() {
        Page<InternalDispatch> page = internalDispatchService.findAllByAlbumNameContainsIgnoreCaseAndIsActive("", true, PAGEABLE_SORT_BY_ALBUM_NAME);
        Assertions.assertEquals(PAGE_FIND_ALL_BY_ALBUM_NAME_EMPTY_AND_IS_ACTIVE, page);
    }

    @Test
    void update() {
        InternalDispatch updated = getUpdated();
        internalDispatchService.update(updated);
        InternalDispatch found = internalDispatchService.findByIdWithDocuments(INTERNAL_DISPATCH2.getId());
        Assertions.assertEquals(updated, found);
        Assertions.assertEquals(updated.getDocuments(), found.getDocuments());
    }

    @Test
    void updateWithoutChildren() {
        InternalDispatch updated = getUpdated();
        updated.setDocuments(null);
        internalDispatchService.updateWithoutChildren(updated);
        InternalDispatch found = internalDispatchService.findByIdWithDocuments(INTERNAL_DISPATCH2.getId());
        Assertions.assertEquals(getUpdated(), found);
        Assertions.assertNotNull(found.getDocuments());
    }


    @Test
    void addDocumentWithNullDecimalNumber() {
        String errorMessage = internalDispatchService.addDocument(getForAddOrRemoveDocument(), null);
        Assertions.assertEquals(NOT_EMPTY_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addDocumentWithAlreadyAddedDecimalNumber() {
        String errorMessage = internalDispatchService.addDocument(getForAddOrRemoveDocument(), DocumentTestData.DOCUMENT1.getDecimalNumber());
        Assertions.assertEquals(ALREADY_ADDED_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addDocumentWithNotExistDecimalNumber() {
        String errorMessage = internalDispatchService.addDocument(getForAddOrRemoveDocument(), DocumentTestData.NOT_FOUND_DECIMAL_NUMBER);
        Assertions.assertEquals(NOT_FOUND_ERROR_MESSAGE, errorMessage);
    }

    @Test
    void addDocument() {
        InternalDispatch internalDispatch = getForAddOrRemoveDocument();
        internalDispatchService.addDocument(internalDispatch, DocumentTestData.DOCUMENT6.getDecimalNumber());
        internalDispatch.getDocuments().put(DocumentTestData.DOCUMENT6, true);
        InternalDispatch found = internalDispatchService.findByIdWithDocuments(INTERNAL_DISPATCH2.getId());
        Assertions.assertEquals(internalDispatch, found);
        Assertions.assertEquals(internalDispatch.getDocuments(), found.getDocuments());
        Assertions.assertTrue(found.getDocuments().get(DocumentTestData.DOCUMENT6));
    }

    @Test
    void removeDocumentSizeEqualsOne() {
        InternalDispatch internalDispatch = getForRemoveDocumentSizeEqualsOne();
        String errorDeleteDocumentMessage = internalDispatchService.removeDocument(internalDispatch, DocumentTestData.DOCUMENT1.getId());
        Assertions.assertEquals(ERROR_DELETE_DOCUMENT_MESSAGE, errorDeleteDocumentMessage);
    }

    @Test
    void removeDocument() {
        InternalDispatch internalDispatch = getForAddOrRemoveDocument();
        internalDispatchService.removeDocument(internalDispatch, DocumentTestData.DOCUMENT1_ID);
        internalDispatch.getDocuments().remove(DocumentTestData.DOCUMENT1);
        InternalDispatch found = internalDispatchService.findByIdWithDocuments(INTERNAL_DISPATCH2.getId());
        Assertions.assertEquals(internalDispatch, found);
        Assertions.assertEquals(internalDispatch.getDocuments(), found.getDocuments());
        Assertions.assertFalse(found.getDocuments().containsKey(DocumentTestData.DOCUMENT1));
    }

    @Test
    void deleteById() {
        internalDispatchService.deleteById(INTERNAL_DISPATCH1_ID);
        Assertions.assertThrows(NotFoundException.class, () -> internalDispatchService.findById(INTERNAL_DISPATCH1_ID));
        Assertions.assertEquals(DocumentTestData.DOCUMENT1, documentService.findById(DocumentTestData.DOCUMENT1_ID));
    }

    @Test
    void deleteByIdNotFound() {
        Assertions.assertThrows(NotFoundException.class, () -> internalDispatchService.deleteById(NOT_FOUND));
    }
}