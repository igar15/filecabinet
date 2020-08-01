package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.entity.enums.Stamp;
import com.igar15.filecabinet.entity.enums.Status;

import java.time.LocalDate;
import java.util.List;

public class InternalDispatchTestData {

    public static final int INTERNAL_DISPATCH1_ID = AbstractBaseEntity.START_SEQ + 26;
    public static final int NOT_FOUND = 10;

    public static final InternalDispatch INTERNAL_DISPATCH1 = new InternalDispatch(INTERNAL_DISPATCH1_ID, "wb-531", LocalDate.of(2017, 4, 15),
            Status.ACC_COPY, null, Stamp.I, null, null, LocalDate.of(2019, 2, 15), "Naumkin", "1-31-65", "ЮПИЯ.301265.026");
    public static final InternalDispatch INTERNAL_DISPATCH2 = new InternalDispatch(INTERNAL_DISPATCH1_ID + 1, "wb-532", LocalDate.of(2018, 4, 20),
            Status.ACC_COPY, "simple remark", Stamp.V, DeveloperTestData.DEVELOPER2, null, LocalDate.of(2020, 1, 15), "Naumkin", "1-31-65", "Singly");
    public static final InternalDispatch INTERNAL_DISPATCH3 = new InternalDispatch(INTERNAL_DISPATCH1_ID + 2, "wb-556", LocalDate.of(2019, 6, 20),
            Status.ACC_COPY, "true remark", null, DeveloperTestData.DEVELOPER2, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", "Singly");

    public static final List<InternalDispatch> INTERNAL_DISPATCHES = List.of(
            INTERNAL_DISPATCH3, INTERNAL_DISPATCH2, INTERNAL_DISPATCH1
    );

    public static InternalDispatch getNew() {
        return new InternalDispatch(null, "wb-54", LocalDate.of(2017, 4, 15),
                Status.ACC_COPY, null, Stamp.II, DeveloperTestData.DEVELOPER1, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", "Singly");
    }

    public static InternalDispatch getNewWithDuplicateStampAndDocument() {
        return new InternalDispatch(null, "wb-54", LocalDate.of(2017, 4, 15),
                Status.ACC_COPY, null, Stamp.V, DeveloperTestData.DEVELOPER1, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", "Singly");

    }

    public static List<InternalDispatch> getNewsWithWrongValues() {
        return List.of(
                new InternalDispatch(null, "", LocalDate.of(2017, 4, 15),
                        Status.ACC_COPY, null, Stamp.I, null, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", "Singly"),
                new InternalDispatch(null, null, LocalDate.of(2017, 4, 15),
                        Status.ACC_COPY, null, Stamp.I, null, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", "Singly"),
                new InternalDispatch(null, "wb-22", null,
                        Status.ACC_COPY, null, Stamp.I, null, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", "Singly"),
                new InternalDispatch(null, "wb-22", LocalDate.of(2017, 4, 15),
                        null, null, Stamp.I, null, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", "Singly")
        );
    }

    public static InternalDispatch getUpdated() {
        return new InternalDispatch(INTERNAL_DISPATCH1_ID, "wb-545", LocalDate.of(2014, 4, 15),
                Status.ACC_COPY, null, Stamp.I, DeveloperTestData.DEVELOPER2, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", "Singly");
    }

}
