package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;
import com.igar15.filecabinet.entity.enums.Form;
import com.igar15.filecabinet.entity.enums.Stage;
import com.igar15.filecabinet.entity.enums.Status;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static com.igar15.filecabinet.ChangeNoticeTestData.*;

public class DocumentTestData {

    public static final int DOCUMENT1_ID = AbstractBaseEntity.START_SEQ + 9;
    public static final String DOCUMENT1_DECIMAL_NUMBER = "БА4.151.128";

    public static final int NOT_FOUND_ID = 10;
    public static final String NOT_FOUND_DECIMAL_NUMBER = "ЮПИЯ.456789.454СБ";

    public static final String PART_OF_DECIMAL_NUMBER = "БА4";

    public static final String PART_OF_NAME = "СТОЙ";

    public static final String FULL_DECIMAL_NUMBER = "БА4.151.128СБ";



    public static final Document DOCUMENT1 = new Document(DOCUMENT1_ID, "Стойка", "БА4.151.128", 880572,
            LocalDate.of(2003, 1, 30), Status.ORIGINAL, null, Form.ELECTRONIC, Stage.O1,
            null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1,  null, null, null);

    public static final Document DOCUMENT2 = new Document(DOCUMENT1_ID + 1, "Стойка Сборочный чертеж", "БА4.151.128СБ", 63140,
            LocalDate.of(2003, 1, 30), Status.ORIGINAL, null, Form.PAPER, Stage.O1,
            28, "A4", 28, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);

    public static final Document DOCUMENT3 = new Document(DOCUMENT1_ID + 2, "Крышка", "БА5.666.777", 884986,
            LocalDate.of(2004, 6, 30), Status.ORIGINAL, Set.of(DOCUMENT1, DOCUMENT2), Form.ELECTRONIC, Stage.O1,
            null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);

    public static final Document DOCUMENT4 = new Document(DOCUMENT1_ID + 3, "Крышка Сборочный чертеж", "БА5.666.777СБ", 939986,
            LocalDate.of(2004, 6, 30), Status.ORIGINAL, Set.of(DOCUMENT3), Form.PAPER, Stage.O1,
            1, "A1", 8, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);

    public static final Document DOCUMENT5 = new Document(DOCUMENT1_ID + 4, "Вал", "ВУИА.758341.198", 68199,
            LocalDate.of(2018, 9, 15), Status.ORIGINAL, null, Form.ELECTRONIC, null,
            null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);

    public static final Document DOCUMENT6 = new Document(DOCUMENT1_ID + 5, "Панель ПСЭ25-22", "ЮПИЯ.468151.031", 929213,
            LocalDate.of(2012, 11, 18), Status.ACC_COPY, null, Form.ELECTRONIC, null,
            null, null, null, null, CompanyTestData.COMPANY3, null, null, null);

    public static final List<Document> DOCUMENTS = List.of(DOCUMENT1, DOCUMENT2, DOCUMENT5, DOCUMENT3, DOCUMENT4, DOCUMENT6);

    public static final Pageable PAGEABLE = PageRequest.of(0, 3, Sort.by("decimalNumber"));

    public static final Page<Document> PAGE_FOR_FULL_DECIMAL_NUMBER = new PageImpl<>(List.of(DOCUMENT2), PAGEABLE, 1);

    public static final Page<Document> PAGE_FOR_FULL_NAME = new PageImpl<>(List.of(DOCUMENT2), PAGEABLE, 1);

    public static final Page<Document> PAGE_FOR_PART_OF_DECIMAL_NUMBER = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2), PAGEABLE, 2);

    public static final Page<Document> PAGE_FOR_PART_OF_NAME = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2), PAGEABLE, 2);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2, DOCUMENT3), PAGEABLE, 6);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_AFTER_AND_BEFORE = new PageImpl<>(List.of(DOCUMENT5, DOCUMENT6), PAGEABLE, 2);

    public static final Page<Document> PAGE_FOR_DEFINE_DATE = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2), PAGEABLE, 2);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_ORIGINAL_HOLDER = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2, DOCUMENT3), PAGEABLE, 5);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_DEPARTMENT = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2, DOCUMENT3), PAGEABLE, 5);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_DEPARTMENT_AND_ORIGINAL_HOLDER = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2, DOCUMENT3), PAGEABLE, 5);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_DEPARTMENT_AND_ORIGINAL_HOLDER_AND_AFTER_AND_BEFORE = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2), PAGEABLE, 2);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_DEPARTMENT_AND_AFTER_AND_BEFORE = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2, DOCUMENT3), PAGEABLE, 4);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_ORIGINAL_HOLDER_AND_AFTER_AND_BEFORE = new PageImpl<>(List.of(DOCUMENT6), PAGEABLE, 1);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_INVENTORY_NUMBER = new PageImpl<>(List.of(DOCUMENT1), PAGEABLE, 1);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_STATUS = new PageImpl<>(List.of(DOCUMENT6), PAGEABLE, 1);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_STAGE = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT2, DOCUMENT3), PAGEABLE, 4);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NULL_EXCEPT_FORM = new PageImpl<>(List.of(DOCUMENT1, DOCUMENT3, DOCUMENT5), PAGEABLE, 4);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NOT_NULL_EXCEPT_AFTER_AND_BEFORE = new PageImpl<>(List.of(DOCUMENT1), PAGEABLE, 1);

    public static final Page<Document> PAGE_FOR_ALL_PARAMS_NOT_NULL = new PageImpl<>(List.of(DOCUMENT1), PAGEABLE, 1);



    public static Document getNew() {
        return new Document(null, "Стойка 777", "БА6.151.999", 341572,
                LocalDate.of(2017, 1, 14), Status.ORIGINAL, null, Form.ELECTRONIC, Stage.O1,
                null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);
    }

    public static Document getNewWithDuplicateDecimalNumber() {
        return new Document(null, "Стойка 777", "БА4.151.128", 341572,
                LocalDate.of(2017, 1, 14), Status.ORIGINAL, null, Form.ELECTRONIC, Stage.O1,
                null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);
    }

    public static Document getNewWithDuplicateInventoryNumber() {
        return new Document(null, "Стойка 777", "БА6.151.444", 880572,
                LocalDate.of(2017, 1, 14), Status.ORIGINAL, null, Form.ELECTRONIC, Stage.O1,
                null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);
    }

    public static Document getUpdated() {
        Document document = new Document(DOCUMENT1_ID, "Стойка 999", "БА6.151.999-05", 777555,
                LocalDate.of(2020, 1, 14), Status.DUPLICATE, null, Form.ELECTRONIC, Stage.O1,
                null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);
        Map<Integer, ChangeNotice> changeNotices = new HashMap<>();
        changeNotices.put(1, CHANGE_NOTICE1);
        changeNotices.put(2, CHANGE_NOTICE2);
        document.setChangeNotices(changeNotices);
        Map<ExternalDispatch, Boolean> externalDispatches = new HashMap<>();
        externalDispatches.put(ExternalDispatchTestData.EXTERNAL_DISPATCH1, true);
        externalDispatches.put(ExternalDispatchTestData.EXTERNAL_DISPATCH2, true);
        externalDispatches.put(ExternalDispatchTestData.EXTERNAL_DISPATCH3, true);
        document.setExternalDispatches(externalDispatches);
        Map<InternalDispatch, Boolean> internalDispatches = new HashMap<>();
        internalDispatches.put(InternalDispatchTestData.INTERNAL_DISPATCH1, true);
        internalDispatches.put(InternalDispatchTestData.INTERNAL_DISPATCH2, true);
        document.setInternalDispatches(internalDispatches);
        Set<Document> applicabilities = new HashSet<>();
        applicabilities.add(DOCUMENT3);
        applicabilities.add(DOCUMENT6);
        document.setApplicabilities(applicabilities);
        return document;
    }

    public static Document getWithChangeNotices() {
        Document document = new Document(DOCUMENT1_ID, "Стойка", "БА4.151.128", 880572,
                LocalDate.of(2003, 1, 30), Status.ORIGINAL, null, Form.ELECTRONIC, Stage.O1,
                null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1,  null, null, null);
        Map<Integer, ChangeNotice> changeNotices = new HashMap<>();
        changeNotices.put(1, CHANGE_NOTICE1);
        changeNotices.put(2, CHANGE_NOTICE2);
        document.setChangeNotices(changeNotices);
        return document;
    }

    public static Document getWithExternalDispatches() {
        Document document = new Document(DOCUMENT1_ID, "Стойка", "БА4.151.128", 880572,
                LocalDate.of(2003, 1, 30), Status.ORIGINAL, null, Form.ELECTRONIC, Stage.O1,
                null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1,  null, null, null);
        Map<ExternalDispatch, Boolean> externalDispatches = new HashMap<>();
        externalDispatches.put(ExternalDispatchTestData.EXTERNAL_DISPATCH1, true);
        externalDispatches.put(ExternalDispatchTestData.EXTERNAL_DISPATCH2, true);
        externalDispatches.put(ExternalDispatchTestData.EXTERNAL_DISPATCH3, true);
        document.setExternalDispatches(externalDispatches);
        return document;
    }

    public static Document getWithInternalDispatches() {
        Document document = new Document(DOCUMENT1_ID, "Стойка", "БА4.151.128", 880572,
                LocalDate.of(2003, 1, 30), Status.ORIGINAL, null, Form.ELECTRONIC, Stage.O1,
                null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1,  null, null, null);
        Map<InternalDispatch, Boolean> internalDispatches = new HashMap<>();
        internalDispatches.put(InternalDispatchTestData.INTERNAL_DISPATCH1, true);
        internalDispatches.put(InternalDispatchTestData.INTERNAL_DISPATCH2, true);
        document.setInternalDispatches(internalDispatches);
        return document;
    }

    public static Document getWithApplicabilities() {
        Document document = new Document(DOCUMENT1_ID + 4, "Вал", "ВУИА.758341.198", 68199,
                LocalDate.of(2018, 9, 15), Status.ACC_COPY, null, Form.ELECTRONIC, null,
                null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null);
        Set<Document> applicabilities = new HashSet<>();
        applicabilities.add(DOCUMENT3);
        applicabilities.add(DOCUMENT6);
        document.setApplicabilities(applicabilities);
        return document;
    }

    public static List<Document> getNewsWithWrongValues() {
        return List.of(
                new Document(null, null, "БА6.151.999-05", 777555,
                        LocalDate.of(2020, 1, 14), Status.DUPLICATE, null, Form.ELECTRONIC, Stage.O1,
                        null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null),

                new Document(null, "Stoika", null, 777555,
                        LocalDate.of(2020, 1, 14), Status.DUPLICATE, null, Form.ELECTRONIC, Stage.O1,
                        null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null),

                new Document(null, "Stoika", "БА6.151.999-05", null,
                        LocalDate.of(2020, 1, 14), Status.DUPLICATE, null, Form.ELECTRONIC, Stage.O1,
                        null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null),

                new Document(null, "Stoika", "БА6.151.999-05", 12313,
                        null, Status.DUPLICATE, null, Form.ELECTRONIC, Stage.O1,
                        null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null),

                new Document(null, "Stoika", "БА6.151.999-05", 1123,
                        LocalDate.of(2020, 1, 14), null, null, Form.ELECTRONIC, Stage.O1,
                        null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null),

                new Document(null, "Stoika", "БА6.151.999-05", 23213,
                        LocalDate.of(2020, 1, 14), Status.DUPLICATE, null, null, Stage.O1,
                        null, null, null, DepartmentTestData.DEPARTMENT_1, CompanyTestData.COMPANY1, null, null, null),

                new Document(null, "Stoika", "БА6.151.999-05", 23213,
                        LocalDate.of(2020, 1, 14), Status.DUPLICATE, null, Form.ELECTRONIC, Stage.O1,
                        null, null, null, null, CompanyTestData.COMPANY1, null, null, null),

        new Document(null, "Stoika", "БА6.151.999-05", 23213,
                LocalDate.of(2020, 1, 14), Status.DUPLICATE, null, Form.ELECTRONIC, Stage.O1,
                null, null, null, DepartmentTestData.DEPARTMENT_1, null, null, null, null)

        );
    }
}
