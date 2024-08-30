package app.financeapp.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DATA")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MOTHER_NAME", length = 128)
    private String motherName;
    @Column(name = "FATHER_NAME", length = 128)
    private String fatherName;
    @Column(name = "NATIONALITY", length = 128)
    private String nationality;
    @Column(name = "BIRTH_DATE", nullable = false)
    private LocalDate birthDate;

    @Column(name = "PHONE_NUMBER", length = 128)
    private String phoneNumber;

    @Column(name = "STREET", length = 128)
    private String street;
    @Column(name = "NUMBER", length = 128)
    private String number;
    @Column(name = "CITY", length = 128)
    private String city;
    @Column(name = "ZIP_CODE", length = 128)
    private String zipCode;
    @Column(name = "COUNTRY", length = 128)
    private String country;

    @Column(name = "MODIFICATION_DATE", nullable = false)
    private ZonedDateTime modificationDate;

    @PrePersist
    protected void onUpdate() {
        this.modificationDate = ZonedDateTime.now();
    }

}
