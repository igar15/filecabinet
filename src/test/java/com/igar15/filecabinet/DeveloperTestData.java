package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.Developer;

import java.util.List;

public class DeveloperTestData {

    public static final int DEVELOPER1_ID = AbstractBaseEntity.START_SEQ;
    public static final String DEVELOPER1_NAME = "KTK-40";

    public static final int NOT_FOUND_ID = 10;
    public static final String NOT_FOUND_NAME = "OKB-4";

    public static final Developer DEVELOPER1 = new Developer(DEVELOPER1_ID, "KTK-40", "KTK-40 chief name", null, null);
    public static final Developer DEVELOPER2 = new Developer(DEVELOPER1_ID + 1, "NIO-6", "NIO-6 chief name", "Developers from NIO-6 are good folks", 24);
    public static final Developer DEVELOPER3 = new Developer(DEVELOPER1_ID + 2, "NIO-8", "NIO-8 chief name", null, 15);
    public static final Developer DEVELOPER4 = new Developer(DEVELOPER1_ID + 3, "SKB-3", "SKB-3 chief name", "SKB-3 is a very good department. There are many talent people work here.", 43);
    public static final Developer DEVELOPER5 = new Developer(DEVELOPER1_ID + 4, "OTD 25", "OTD 25 chief name", null, 11);
    public static final Developer DEVELOPER6 = new Developer(DEVELOPER1_ID + 5, "OTD 33", "OTD 33 chief name", "OTD 33 makes great job for the organization!", 22);

    public static final List<Developer> DEVELOPERS = List.of(DEVELOPER1, DEVELOPER2, DEVELOPER3, DEVELOPER5, DEVELOPER6, DEVELOPER4);

    public static Developer getNew() {
        return new Developer(null, "SCB-1", "SCB-1 chief name", "test description", 10);
    }

    public static Developer getUpdated() {
        return new Developer(DEVELOPER1_ID, "updated name", "updated chief name", "updated description", 15);
    }

    public static Developer getNewWithDuplicateName() {
        return new Developer(null, "KTK-40", "new chief name", "new description", 5);
    }

    public static List<Developer> getNewsWithWrongValues() {
        return List.of(
                new Developer(null, null, "chief", "description", 3),
                new Developer(null, "name", null, "description", 3),
                new Developer(null, "  ", null, "description", 3)
        );
    }
}
