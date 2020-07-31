package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.ChangeNotice;
import com.igar15.filecabinet.entity.Document;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeNoticeTestData {

    public static final int CHANGE_NOTICE1_ID = AbstractBaseEntity.START_SEQ + 15;
    public static final String CHANGE_NOTICE1_NAME = "ВУИА.СЭ.739";

    public static final int NOT_FOUND_ID = 10;
    public static final String NOT_FOUND_NAME = "ВУИА.ТК.777";

    public static final ChangeNotice CHANGE_NOTICE1 = new ChangeNotice(CHANGE_NOTICE1_ID, "ВУИА.СЭ.739", 5,
            LocalDate.of(2018, 5, 24), DeveloperTestData.DEVELOPER1);
    public static final ChangeNotice CHANGE_NOTICE2 = new ChangeNotice(CHANGE_NOTICE1_ID + 1, "ВУИА.СЭ.744", 8,
            LocalDate.of(2018, 6, 17), DeveloperTestData.DEVELOPER1);
    public static final ChangeNotice CHANGE_NOTICE3 = new ChangeNotice(CHANGE_NOTICE1_ID + 2, "ВУИА.ТК.133", 4,
            LocalDate.of(2018, 8, 30), DeveloperTestData.DEVELOPER3);
    public static final ChangeNotice CHANGE_NOTICE4 = new ChangeNotice(CHANGE_NOTICE1_ID + 3, "ВУИА.ТК.153", 2,
            LocalDate.of(2019, 4, 24), DeveloperTestData.DEVELOPER3);
    public static final ChangeNotice CHANGE_NOTICE5 = new ChangeNotice(CHANGE_NOTICE1_ID + 4, "ВУИА.ТК.156", 7,
            LocalDate.of(2020, 5, 18), DeveloperTestData.DEVELOPER3);
    public static final ChangeNotice CHANGE_NOTICE6 = new ChangeNotice(CHANGE_NOTICE1_ID + 5, "ВУИА.СО.134", 6,
            LocalDate.of(2016, 12, 19), DeveloperTestData.DEVELOPER4);
    public static final ChangeNotice CHANGE_NOTICE7 = new ChangeNotice(CHANGE_NOTICE1_ID + 6, "ВУИА.СО.135", 6,
            LocalDate.of(2018, 5, 24), DeveloperTestData.DEVELOPER4);
    public static final ChangeNotice CHANGE_NOTICE8 = new ChangeNotice(CHANGE_NOTICE1_ID + 7, "ВУИА.СО.136", 7,
            LocalDate.of(2018, 5, 27), DeveloperTestData.DEVELOPER4);

    public static final List<ChangeNotice> CHANGE_NOTICES = List.of(
            CHANGE_NOTICE6, CHANGE_NOTICE7, CHANGE_NOTICE8, CHANGE_NOTICE1, CHANGE_NOTICE2, CHANGE_NOTICE3, CHANGE_NOTICE4, CHANGE_NOTICE5
    );

    public static ChangeNotice getNew() {
        return new ChangeNotice(null, "ВУИА.СЭ.999", 5,
                LocalDate.of(2018, 5, 24), DeveloperTestData.DEVELOPER1);
    }

    public static ChangeNotice getNewWithDuplicateName() {
        return new ChangeNotice(null, "ВУИА.СЭ.739", 5,
                LocalDate.of(2018, 5, 24), DeveloperTestData.DEVELOPER1);
    }

    public static ChangeNotice getUpdated() {
        return new ChangeNotice(CHANGE_NOTICE1_ID, "ВУИА.СЭ.999", 9,
                LocalDate.of(2019, 5, 24), DeveloperTestData.DEVELOPER1);
    }

    public static ChangeNotice getWithDocuments() {
        Map<Document, Integer> documents = new HashMap<>();
        documents.put(DocumentTestData.DOCUMENT2, 1);
        documents.put(DocumentTestData.DOCUMENT6, 1);
        CHANGE_NOTICE3.setDocuments(documents);
        return CHANGE_NOTICE3;
    }

    public static List<ChangeNotice> getNewsWithWrongValues() {
        return List.of(
                new ChangeNotice(null, null, 5,
                        LocalDate.of(2018, 5, 24), DeveloperTestData.DEVELOPER1),
                new ChangeNotice(null, "  ", 5,
                        LocalDate.of(2018, 5, 24), DeveloperTestData.DEVELOPER1),
                new ChangeNotice(null, "ВУИА.СЭ.999", null,
                        LocalDate.of(2018, 5, 24), DeveloperTestData.DEVELOPER1),
                new ChangeNotice(null, "ВУИА.СЭ.999", 3,
                        null, DeveloperTestData.DEVELOPER1)
        );
    }




}
