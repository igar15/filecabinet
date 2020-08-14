package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.igar15.filecabinet.DocumentTestData.*;

public class ChangeNoticeTestData {

    public static final int CHANGE_NOTICE1_ID = AbstractBaseEntity.START_SEQ + 15;
    public static final int CHANGE_NOTICE_NEW_ID = AbstractBaseEntity.START_SEQ + 29;
    public static final String CHANGE_NOTICE1_NAME = "ВУИА.СЭ.739";

    public static final int NOT_FOUND_ID = 10;
    public static final String NOT_FOUND_NAME = "ВУИА.ТК.777";

    public static final String NAME_EXAMPLE = "ТК";

    public static final String CHANGE_CODE_EXAMPLE = "7";

    public static final String AFTER_EXAMPLE = "2018-05-27";

    public static final String BEFORE_EXAMPLE = "2018-06-17";

    public static final String DEFINE_DATE = "2018-05-24";

    public static final ChangeNotice CHANGE_NOTICE1 = new ChangeNotice(CHANGE_NOTICE1_ID, "ВУИА.СЭ.739", 5,
            LocalDate.of(2018, 5, 24), DepartmentTestData.DEPARTMENT_1, Map.of(DOCUMENT1, 1));

    public static final ChangeNotice CHANGE_NOTICE2 = new ChangeNotice(CHANGE_NOTICE1_ID + 1, "ВУИА.СЭ.744", 8,
            LocalDate.of(2018, 6, 17), DepartmentTestData.DEPARTMENT_1, Map.of(DOCUMENT1, 2));

    public static final ChangeNotice CHANGE_NOTICE3 = new ChangeNotice(CHANGE_NOTICE1_ID + 2, "ВУИА.ТК.133", 4,
            LocalDate.of(2018, 8, 30), DepartmentTestData.DEPARTMENT_3, Map.of(DOCUMENT2, 1, DOCUMENT6, 1));

    public static final ChangeNotice CHANGE_NOTICE4 = new ChangeNotice(CHANGE_NOTICE1_ID + 3, "ВУИА.ТК.153", 2,
            LocalDate.of(2019, 4, 24), DepartmentTestData.DEPARTMENT_3, Map.of(DOCUMENT2, 2, DOCUMENT6, 2));

    public static final ChangeNotice CHANGE_NOTICE5 = new ChangeNotice(CHANGE_NOTICE1_ID + 4, "ВУИА.ТК.156", 7,
            LocalDate.of(2020, 5, 18), DepartmentTestData.DEPARTMENT_3, Map.of(DOCUMENT6, 3));

    public static final ChangeNotice CHANGE_NOTICE6 = new ChangeNotice(CHANGE_NOTICE1_ID + 5, "ВУИА.СО.134", 6,
            LocalDate.of(2016, 12, 19), DepartmentTestData.DEPARTMENT_4, Map.of(DOCUMENT3, 1));

    public static final ChangeNotice CHANGE_NOTICE7 = new ChangeNotice(CHANGE_NOTICE1_ID + 6, "ВУИА.СО.135", 6,
            LocalDate.of(2018, 5, 24), DepartmentTestData.DEPARTMENT_4, Map.of(DOCUMENT3, 2));

    public static final ChangeNotice CHANGE_NOTICE8 = new ChangeNotice(CHANGE_NOTICE1_ID + 7, "ВУИА.СО.136", 7,
            LocalDate.of(2018, 5, 27), DepartmentTestData.DEPARTMENT_4, Map.of(DOCUMENT3, 3));

    public static final List<ChangeNotice> CHANGE_NOTICES = List.of(
            CHANGE_NOTICE6, CHANGE_NOTICE7, CHANGE_NOTICE8, CHANGE_NOTICE1, CHANGE_NOTICE2, CHANGE_NOTICE3, CHANGE_NOTICE4, CHANGE_NOTICE5
    );

    public static final Pageable PAGEABLE = PageRequest.of(0, 10, Sort.by("name"));

    public static final Page<ChangeNotice> PAGE_FOR_NAME_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE3, CHANGE_NOTICE4, CHANGE_NOTICE5), PAGEABLE, 3);

    public static final Page<ChangeNotice> PAGE_FOR_DEPARTMENT_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE1, CHANGE_NOTICE2), PAGEABLE, 2);

    public static final Page<ChangeNotice> PAGE_FOR_CHANGE_CODE_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE8, CHANGE_NOTICE5), PAGEABLE, 2);

    public static final Page<ChangeNotice> PAGE_FOR_AFTER_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE8, CHANGE_NOTICE2, CHANGE_NOTICE3, CHANGE_NOTICE4, CHANGE_NOTICE5), PAGEABLE, 5);

    public static final Page<ChangeNotice> PAGE_FOR_BEFORE_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE6, CHANGE_NOTICE7, CHANGE_NOTICE8, CHANGE_NOTICE1, CHANGE_NOTICE2), PAGEABLE, 5);

    public static final Page<ChangeNotice> PAGE_FOR_DEFINE_DATE_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE7, CHANGE_NOTICE1), PAGEABLE, 2);

    public static final Page<ChangeNotice> PAGE_FOR_NAME_AND_DEPARTMENT_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE3, CHANGE_NOTICE4, CHANGE_NOTICE5), PAGEABLE, 3);

    public static final Page<ChangeNotice> PAGE_FOR_NAME_AND_AFTER_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE3, CHANGE_NOTICE4, CHANGE_NOTICE5), PAGEABLE, 3);

    public static final Page<ChangeNotice> PAGE_FOR_NAME_AND_DEPARTMENT_AND_CHANGE_CODE_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE5), PAGEABLE, 1);

    public static final Page<ChangeNotice> PAGE_FOR_AFTER_AND_BEFORE_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE8, CHANGE_NOTICE2), PAGEABLE, 2);

    public static final Page<ChangeNotice> PAGE_FOR_NAME_AND_DEPARTMENT_AND_CHANGE_CODE_AND_AFTER_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE5), PAGEABLE, 1);

    public static final Page<ChangeNotice> PAGE_FOR_NAME_AND_DEPARTMENT_AND_CHANGE_CODE_AND_AFTER_AND_BEFORE_EXAMPLE = new PageImpl<>(List.of(), PAGEABLE, 0);

    public static final Page<ChangeNotice> PAGE_FOR_NAME_AND_AFTER_AND_BEFORE_EXAMPLE = new PageImpl<>(List.of(), PAGEABLE, 0);

    public static final Page<ChangeNotice> PAGE_FOR_CHANGE_CODE_AND_AFTER_AND_BEFORE_EXAMPLE = new PageImpl<>(List.of(CHANGE_NOTICE8), PAGEABLE, 1);

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_NULL_DECIMAL_NUMBER = {"Decimal number must not be empty", null};

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_NULL_CHANGE_NUMBER = {null, "Change number must not be empty"};

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_NULL_DECIMAL_AND_CHANGE_NUMBERS = {"Decimal number must not be empty", "Change number must not be empty"};

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_WRONG_DECIMAL_NUMBER = {"Document does not exist", null};

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_DUPLICATE_CHANGE_NUMBER = {null, "Document already has this change number"};

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_DUPLICATE_DECIMAL_NUMBER = {"This change notice already has this document", null};

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_WRONG_CHANGE_NUMBER = {null, "Change number must be greater than 0"};

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_INVALID_CHANGE_NUMBER = {null, "Invalid change number"};

    public static final Object[] ADD_DOCUMENT_RESULTS_FOR_OK_DECIMAL_AND_CHANGE_NUMBERS = {null, null};

    public static final String REMOVE_DOCUMENT_ERROR_FOR_SIZE_EQUALS_1 = "The change notice can not exist without any documents!";


    public static ChangeNotice getNew() {
        return new ChangeNotice(null, "ВУИА.СЭ.999", 5,
                LocalDate.of(2018, 5, 24), DepartmentTestData.DEPARTMENT_1, null);
    }

    public static ChangeNotice getNewWithDuplicateName() {
        return new ChangeNotice(null, "ВУИА.СЭ.739", 5,
                LocalDate.of(2018, 5, 24), DepartmentTestData.DEPARTMENT_1, null);
    }

    public static ChangeNotice getNewWithAddedDocument() {
        ChangeNotice changeNotice = getNew();
        changeNotice.setId(CHANGE_NOTICE_NEW_ID);
        changeNotice.setDocuments(Map.of(DOCUMENT1, 3));
        return changeNotice;
    }

    public static ChangeNotice getNotNewWithAddedDocument() {
        CHANGE_NOTICE3.setDocuments(new HashMap<>(CHANGE_NOTICE3.getDocuments()));
        CHANGE_NOTICE3.getDocuments().put(DOCUMENT1, 3);
        return CHANGE_NOTICE3;
    }

    public static ChangeNotice getWithRemovedDocument() {
        CHANGE_NOTICE4.setDocuments(Map.of(DOCUMENT6, 2));
        return CHANGE_NOTICE4;
    }

    public static ChangeNotice getUpdated() {
        return new ChangeNotice(CHANGE_NOTICE1_ID, "ВУИА.СЭ.999", 9,
                LocalDate.of(2019, 5, 24), DepartmentTestData.DEPARTMENT_1, Map.of(DOCUMENT1, 1));
    }

    public static List<ChangeNotice> getNewsWithWrongValues() {
        return List.of(
                new ChangeNotice(null, null, 5,
                        LocalDate.of(2018, 5, 24), DepartmentTestData.DEPARTMENT_1, null),
                new ChangeNotice(null, "  ", 5,
                        LocalDate.of(2018, 5, 24), null, null),
                new ChangeNotice(null, "ВУИА.СЭ.999", null,
                        LocalDate.of(2018, 5, 24), DepartmentTestData.DEPARTMENT_1, null),
                new ChangeNotice(null, "ВУИА.СЭ.999", 3,
                        null, DepartmentTestData.DEPARTMENT_1, null)
        );
    }




}
