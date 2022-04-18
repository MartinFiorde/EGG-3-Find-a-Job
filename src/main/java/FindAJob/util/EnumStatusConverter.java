package FindAJob.util;

import FindAJob.enums.Status;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//@Converter(autoApply = true)
//public class EnumStatusConverter implements AttributeConverter<Status, String> {
//
//    @Override
//    public String convertToDatabaseColumn(Status status) {
//        if (status == null) {
//            return null;
//        }
//        return status.toString();
//    }
//
//    @Override
//    public Status convertToEntityAttribute(String code) {
//        if (code == null) {
//            return null;
//        }
//
//        return Stream.of(Status.values())
//          .filter(m -> m.toString().equals(code))
//          .findFirst()
//          .orElseThrow(IllegalArgumentException::new);
//    }
//}