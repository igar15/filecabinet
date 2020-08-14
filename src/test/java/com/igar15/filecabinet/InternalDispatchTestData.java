package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.InternalDispatch;
import com.igar15.filecabinet.entity.enums.Stamp;
import com.igar15.filecabinet.entity.enums.Status;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.igar15.filecabinet.DocumentTestData.*;

public class InternalDispatchTestData {

    public static final int INTERNAL_DISPATCH1_ID = AbstractBaseEntity.START_SEQ + 26;
    public static final int NOT_FOUND = 10;

    public static final String ALBUM_NAME_EXAMPLE = "А4.151";

    public static final InternalDispatch INTERNAL_DISPATCH1 = new InternalDispatch(INTERNAL_DISPATCH1_ID, "wb-531", LocalDate.of(2017, 4, 15),
            Status.ACC_COPY, null, Stamp.I, DepartmentTestData.DEPARTMENT_1, Map.of(DOCUMENT1, true), LocalDate.of(2019, 2, 15), "Naumkin", "1-31-65", false, null, true);

    public static final InternalDispatch INTERNAL_DISPATCH2 = new InternalDispatch(INTERNAL_DISPATCH1_ID + 1, "wb-532", LocalDate.of(2018, 4, 20),
            Status.ACC_COPY, "simple remark", Stamp.V, DepartmentTestData.DEPARTMENT_1, Map.of(DOCUMENT1, true, DOCUMENT2, true, DOCUMENT3, true, DOCUMENT4, true, DOCUMENT5, true), LocalDate.of(2020, 1, 15), "Naumkin", "1-31-65", true, "БА4.151.128", true);

    public static final InternalDispatch INTERNAL_DISPATCH3 = new InternalDispatch(INTERNAL_DISPATCH1_ID + 2, "wb-556", LocalDate.of(2019, 6, 20),
            Status.ACC_COPY, "true remark", Stamp.I, DepartmentTestData.DEPARTMENT_1, Map.of(DOCUMENT6, true), LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "ЮПИЯ.468151.031", true);

    public static final List<InternalDispatch> INTERNAL_DISPATCHES = List.of(
            INTERNAL_DISPATCH3, INTERNAL_DISPATCH2, INTERNAL_DISPATCH1
    );

    public static final Pageable PAGEABLE_SORT_BY_DISPATCH_DATE = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "dispatchDate"));

    public static final Pageable PAGEABLE_SORT_BY_ALBUM_NAME = PageRequest.of(0, 5, Sort.by("albumName"));

    public static final Page<InternalDispatch> PAGE_FIND_ALL = new PageImpl<>(List.of(INTERNAL_DISPATCH3, INTERNAL_DISPATCH2, INTERNAL_DISPATCH1), PAGEABLE_SORT_BY_DISPATCH_DATE, 3);

    public static final Page<InternalDispatch> PAGE_FIND_ALL_BY_IS_ALBUM_AND_IS_ACTIVE = new PageImpl<>(List.of(INTERNAL_DISPATCH3, INTERNAL_DISPATCH2), PAGEABLE_SORT_BY_DISPATCH_DATE, 2);

    public static final Page<InternalDispatch> PAGE_FIND_ALL_BY_NOT_ALBUM_AND_IS_ACTIVE = new PageImpl<>(List.of(INTERNAL_DISPATCH1), PAGEABLE_SORT_BY_DISPATCH_DATE, 1);

    public static final Page<InternalDispatch> PAGE_FIND_ALL_BY_IS_ALBUM_AND_NOT_ACTIVE = new PageImpl<>(List.of(), PAGEABLE_SORT_BY_DISPATCH_DATE, 0);

    public static final Page<InternalDispatch> PAGE_FIND_ALL_BY_NOT_ALBUM_AND_NOT_ACTIVE = new PageImpl<>(List.of(), PAGEABLE_SORT_BY_DISPATCH_DATE, 0);

    public static final Page<InternalDispatch> PAGE_FIND_ALL_BY_ALBUM_NAME_AND_IS_ACTIVE = new PageImpl<>(List.of(INTERNAL_DISPATCH2), PAGEABLE_SORT_BY_ALBUM_NAME, 0);

    public static final Page<InternalDispatch> PAGE_FIND_ALL_BY_ALBUM_NAME_AND_NOT_ACTIVE = new PageImpl<>(List.of(), PAGEABLE_SORT_BY_ALBUM_NAME, 0);

    public static final Page<InternalDispatch> PAGE_FIND_ALL_BY_ALBUM_NAME_EMPTY_AND_IS_ACTIVE = new PageImpl<>(List.of(INTERNAL_DISPATCH2, INTERNAL_DISPATCH3), PAGEABLE_SORT_BY_ALBUM_NAME, 0);

    public static final String NOT_EMPTY_ERROR_MESSAGE = "Decimal number must not be empty";

    public static final String ALREADY_ADDED_ERROR_MESSAGE = "Document already added";

    public static final String NOT_FOUND_ERROR_MESSAGE = "Document not found";

    public static final String ERROR_DELETE_DOCUMENT_MESSAGE = "Internal dispatch wb-531 can not exist without any documents!";

    public static InternalDispatch getNew() {
        return new InternalDispatch(null, "wb-54", LocalDate.of(2017, 4, 15),
                Status.ACC_COPY, null, Stamp.II, DepartmentTestData.DEPARTMENT_1, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true);
    }

    public static InternalDispatch getNewWithDuplicateStampAndAlbumName() {
        return new InternalDispatch(null, "wb-54", LocalDate.of(2017, 4, 15),
                Status.ACC_COPY, null, Stamp.V, DepartmentTestData.DEPARTMENT_1, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true);
    }

    public static InternalDispatch getNewWithDuplicateWaybillAndDispatchDate() {
        return new InternalDispatch(null, "wb-531", LocalDate.of(2017, 4, 15),
                Status.ACC_COPY, null, Stamp.XII, DepartmentTestData.DEPARTMENT_1, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true);
    }

    public static InternalDispatch getForAddOrRemoveDocument() {
        return new InternalDispatch(INTERNAL_DISPATCH1_ID + 1, "wb-532", LocalDate.of(2018, 4, 20),
                Status.ACC_COPY, "simple remark", Stamp.V, DepartmentTestData.DEPARTMENT_1, new HashMap<>(Map.of(DOCUMENT1, true, DOCUMENT2, true, DOCUMENT3, true, DOCUMENT4, true, DOCUMENT5, true)), LocalDate.of(2020, 1, 15), "Naumkin", "1-31-65", true, "БА4.151.128", true);
    }

    public static InternalDispatch getForRemoveDocumentSizeEqualsOne() {
        return new InternalDispatch(INTERNAL_DISPATCH1_ID, "wb-531", LocalDate.of(2017, 4, 15),
                Status.ACC_COPY, null, Stamp.I, DepartmentTestData.DEPARTMENT_1, new HashMap<>(Map.of(DOCUMENT1, true)), LocalDate.of(2019, 2, 15), "Naumkin", "1-31-65", false, null, true);
    }





    public static List<InternalDispatch> getNewsWithWrongValues() {
        return List.of(
                new InternalDispatch(null, null, LocalDate.of(2017, 4, 15),
                        Status.ACC_COPY, null, Stamp.XII, DepartmentTestData.DEPARTMENT_1, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true),

                new InternalDispatch(null, "wb-22", null,
                        Status.ACC_COPY, null, Stamp.XII, DepartmentTestData.DEPARTMENT_1, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true),

                new InternalDispatch(null, "wb-22", LocalDate.of(2017, 4, 15),
                        null, null, Stamp.XII, DepartmentTestData.DEPARTMENT_1, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true),

                new InternalDispatch(null, "wb-22", LocalDate.of(2017, 4, 15),
                        Status.ACC_COPY, null, Stamp.XII, null, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true),

                new InternalDispatch(null, "wb-22", LocalDate.of(2017, 4, 15),
                        Status.ACC_COPY, null, Stamp.XII, DepartmentTestData.DEPARTMENT_1, null, null, "Fatelnikova", "1-34-68", true, "БА4.151.128", true),

                new InternalDispatch(null, "wb-22", LocalDate.of(2017, 4, 15),
                        null, null, Stamp.XII, null, null, LocalDate.of(2020, 4, 18), null, "1-34-68", true, "БА4.151.128", true),

                new InternalDispatch(null, "wb-22", LocalDate.of(2017, 4, 15),
                        null, null, Stamp.XII, null, null, LocalDate.of(2020, 4, 18), "Fatelnikova", null, true, "БА4.151.128", true),

                new InternalDispatch(null, "wb-22", LocalDate.of(2017, 4, 15),
                        null, null, Stamp.XII, null, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true),

                new InternalDispatch(null, "wb-22", LocalDate.of(2017, 4, 15),
                        null, null, Stamp.XII, null, null, LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true)
        );
    }

    public static InternalDispatch getUpdated() {
        return new InternalDispatch(INTERNAL_DISPATCH1_ID + 1, "wb-545", LocalDate.of(2014, 4, 15),
                Status.ACC_COPY, null, Stamp.I, DepartmentTestData.DEPARTMENT_2, Map.of(DOCUMENT1, true, DOCUMENT2, true, DOCUMENT3, true, DOCUMENT4, true), LocalDate.of(2020, 4, 18), "Fatelnikova", "1-34-68", true, "БА4.151.128", true);
    }

}
