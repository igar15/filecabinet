package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.ExternalDispatch;
import com.igar15.filecabinet.entity.enums.Status;

import java.time.LocalDate;
import java.util.List;

public class ExternalDispatchTestData {

    public static final int EXTERNAL_DISPATCH1_ID = AbstractBaseEntity.START_SEQ + 23;
    public static final int NOT_FOUND = 10;

    public static final ExternalDispatch EXTERNAL_DISPATCH1 = new ExternalDispatch(EXTERNAL_DISPATCH1_ID, "wb-465", LocalDate.of(2018, 4, 20),
            Status.ACC_COPY, null, "49/SZ-123789", CompanyTestData.COMPANY2, null);
    public static final ExternalDispatch EXTERNAL_DISPATCH2 = new ExternalDispatch(EXTERNAL_DISPATCH1_ID + 1, "wb-467", LocalDate.of(2018, 4, 15),
            Status.DUPLICATE, "simple remark", "49/SZ-1229", CompanyTestData.COMPANY3, null);
    public static final ExternalDispatch EXTERNAL_DISPATCH3 = new ExternalDispatch(EXTERNAL_DISPATCH1_ID + 2, "wb-546", LocalDate.of(2019, 6, 20),
            Status.ACC_COPY, "true remark", "49/SZ-1289", CompanyTestData.COMPANY2, null);

    public static final List<ExternalDispatch> EXTERNAL_DISPATCHES = List.of(EXTERNAL_DISPATCH3, EXTERNAL_DISPATCH1, EXTERNAL_DISPATCH2);

    public static ExternalDispatch getNew() {
        return new ExternalDispatch(null, "wb-777", LocalDate.now(), Status.ACC_COPY, "test remark", "49/SZ-1289", CompanyTestData.COMPANY2, null);
    }

    public static ExternalDispatch getNewWithDuplicateWaybillAndDateAndOutgoingNumber() {
        return new ExternalDispatch(null, "wb-465", LocalDate.of(2018, 4, 20), Status.ACC_COPY, "test remark", "49/SZ-123789", CompanyTestData.COMPANY2, null);
    }

    public static ExternalDispatch getUpdated() {
        return new ExternalDispatch(EXTERNAL_DISPATCH1_ID, "wb-777", LocalDate.of(2020, 4, 15),
                Status.UNACC_COPY, null, "49/SZ-123789", CompanyTestData.COMPANY3, null);
    }

    public static List<ExternalDispatch> getNewsWithWrongValues() {
        return List.of(
                new ExternalDispatch(null, "", LocalDate.of(2018, 4, 20),
                        Status.ACC_COPY, null, "49/SZ-123789", CompanyTestData.COMPANY2, null),
                new ExternalDispatch(null, "wb-32", null,
                        Status.ACC_COPY, null, "49/SZ-123789", CompanyTestData.COMPANY2, null),
                new ExternalDispatch(null, "wb-45", LocalDate.of(2018, 4, 20),
                       null, null, "49/SZ-123789", CompanyTestData.COMPANY2, null),
                new ExternalDispatch(null, "wb-45", LocalDate.of(2018, 4, 20),
                        Status.ACC_COPY, null, "49/SZ-123789", null, null),
                new ExternalDispatch(null, "wb-45", LocalDate.of(2018, 4, 20),
                        Status.ACC_COPY, null, null, CompanyTestData.COMPANY2, null),
                new ExternalDispatch(null, "wb-45", LocalDate.of(2018, 4, 20),
                        Status.ACC_COPY, null, "", CompanyTestData.COMPANY2, null)
        );
    }


}
