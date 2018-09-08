package system.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class CustomDateSerializer extends StdSerializer<Instant> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDateSerializer.class);

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public CustomDateSerializer(Class t) {
        super(t);
    }

    public CustomDateSerializer() {
        this(null);
    }

    @Override
    public void serialize(Instant date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            LOGGER.info("serialize  {}", date);
            jsonGenerator.writeString(formatter.format(Date.from(date)));
        } catch (IOException e) {
           throw new RuntimeException("Exception during convertation date to json date",e);
        }
    }
}
