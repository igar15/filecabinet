package com.igar15.filecabinet;

import com.igar15.filecabinet.entity.abstracts.AbstractBaseEntity;
import com.igar15.filecabinet.entity.Address;
import com.igar15.filecabinet.entity.Company;

import java.util.List;

public class CompanyTestData {

    public static final int COMPANY1_ID = AbstractBaseEntity.START_SEQ + 6;
    public static final String COMPANY1_NAME = "NTC \"NIEMI\"";

    public static final int NOT_FOUND_ID = 10;
    public static final String NOT_FOUND_NAME = "ALMAZ";

    public static final Company COMPANY1 =
            new Company(COMPANY1_ID, "NTC \"NIEMI\"", new Address("Moscow", "Vereyskaya street", "41", "121357"), "Raev A.A.");
    public static final Company COMPANY2 =
            new Company(COMPANY1_ID + 1, "AO \"MMZ\"", new Address("Yoshkar-Ola", "Suvorov street", "15", "424003"), "Bozhko M.V.");
    public static final Company COMPANY3 =
            new Company(COMPANY1_ID + 2, "AO \"IEMZ\"", new Address("Izhevsk", "Pesochnaya street", "3", "426033"), "Visher S.K.");

    public static final List<Company> COMPANIES = List.of(COMPANY3, COMPANY2, COMPANY1);

    public static Company getNew() {
        return new Company(null, "Submicron", new Address("Zelenograd", "Zelenaya street", "20", "574569"), "Vasya");
    }

    public static Company getNewWithDuplicateNameAndAddress() {
        return new Company(null, "NTC \"NIEMI\"", new Address("Moscow", "Vereyskaya street", "41", "121357"), "Sanya");
    }

    public static Company getUpdated() {
        return new Company(COMPANY1_ID, "Almaz", new Address("Moscow", "Leningradskiy pr-t", "80k16", "121546"), "Benderskiy G.P");
    }

    public static List<Company> getNewsWithWrongValues() {
        return List.of(
                new Company(null, "   ", new Address("Zelenograd", "Zelenaya street", "20", "574569"), "Vasya"),
                new Company(null, null, new Address("Zelenograd", "Zelenaya street", "20", "574569"), "Vasya"),
                new Company(null, "Almaz", new Address("", "Zelenaya street", "20", "574569"), "Vasya"),
                new Company(null, "Almaz", new Address(null, "", "20", "574569"), "Vasya"),
                new Company(null, "Almaz", new Address(null, "Zelenaya street", "", "574569"), "Vasya"),
                new Company(null, "Almaz", new Address(null, "Zelenaya street", null, "574569"), "Vasya"),
                new Company(null, "Almaz", new Address(null, "Zelenaya street", "30", "!!!"), "Vasya"),
                new Company(null, "Almaz", new Address(null, "Zelenaya street", "30", null), "Vasya"),
                new Company(null, "Almaz", new Address(null, "Zelenaya street", "30", ""), "Vasya")
        );
    }

}
