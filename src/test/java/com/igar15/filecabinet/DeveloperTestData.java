package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.Department;
import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;

import java.util.List;

public class DeveloperTestData {

    public static final int DEVELOPER1_ID = AbstractBaseEntity.START_SEQ;
    public static final String DEVELOPER1_NAME = "KTK-40";

    public static final int NOT_FOUND_ID = 10;
    public static final String NOT_FOUND_NAME = "OKB-4";

    public static final Department DEPARTMENT_1 = new Department(DEVELOPER1_ID, "KTK-40", "KTK-40 chief name", "KTK-40 description", 30, true, true);
    public static final Department DEPARTMENT_2 = new Department(DEVELOPER1_ID + 1, "NIO-6", "NIO-6 chief name", "NIO-6 description", 24, true, true);
    public static final Department DEPARTMENT_3 = new Department(DEVELOPER1_ID + 2, "NIO-8", "NIO-8 chief name", "NIO-8 description", 15, true, true);
    public static final Department DEPARTMENT_4 = new Department(DEVELOPER1_ID + 3, "SKB-3", "SKB-3 chief name", "SKB-3 description", 43, true, true);
    public static final Department DEPARTMENT_5 = new Department(DEVELOPER1_ID + 4, "OTD 49/3", "OTD 49/3 chief name", "OTD 49/3 description", 21, false, true);
    public static final Department DEPARTMENT_6 = new Department(DEVELOPER1_ID + 5, "OTD 33", "OTD 33 chief name", "OTD 33 description", 22, true, true);

    public static final List<Department> DEPARTMENTS = List.of(DEPARTMENT_1, DEPARTMENT_2, DEPARTMENT_3, DEPARTMENT_6, DEPARTMENT_5, DEPARTMENT_4);

    public static Department getNew() {
        return new Department(null, "SCB-1", "SCB-1 chief name", "SCB-1 description", 10, true, true);
    }

    public static Department getUpdated() {
        return new Department(DEVELOPER1_ID, "updated name", "updated chief name", "updated description", 15, true, true);
    }

    public static Department getNewWithDuplicateName() {
        return new Department(null, "KTK-40", "new chief name", "new description", 5, true, true);
    }

    public static List<Department> getNewsWithWrongValues() {
        return List.of(
                new Department(null, null, "chief", "description", 3, true, true),
                new Department(null, "name", null, "description", 3, true, true),
                new Department(null, "  ", null, "description", 3, true, true),
                new Department(null, "  ", null, "description", 3, null, true),
                new Department(null, "  ", null, "description", 3, true, null)
        );
    }
}
