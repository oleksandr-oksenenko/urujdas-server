package ru.uruydas.config.dao.jooq.mappers;

import org.jooq.Record;
import org.jooq.RecordMapper;
import ru.uruydas.images.model.Image;

import static ru.uruydas.tables.ImagesTable.IMAGES;

public class ImageRecordMapper implements RecordMapper<Record, Image> {
    @Override
    public Image map(Record record) {
        return Image.builder()
                .withId(record.getValue(IMAGES.ID))
                .withContentType(record.getValue(IMAGES.CONTENT_TYPE))
                .build();
    }
}
