package ua.nure.sagaresearch.common.util;

import io.eventuate.Aggregate;
import io.eventuate.EntityWithIdAndVersion;
import io.eventuate.EntityWithMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityNotFoundException;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public final class ConverterUtil {

    private ConverterUtil() {
    }

    public static <T extends Aggregate<T>, R> ResponseEntity<R> supplyAndConvertToResponseEntity(Supplier<EntityWithIdAndVersion<T>> entitySupplier,
                                                                                                 BiFunction<String, T, R> responseConverter) {
        EntityWithIdAndVersion<T> entityWithIdAndVersion;
        try {
            entityWithIdAndVersion = entitySupplier.get();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String entityId = entityWithIdAndVersion.getEntityIdAndVersion().getEntityId();
        return new ResponseEntity<>(responseConverter.apply(entityId, entityWithIdAndVersion.getAggregate()), HttpStatus.OK);
    }

    public static <T extends Aggregate<T>> EntityWithIdAndVersion<T> toEntityWithIdAndVersion(EntityWithMetadata<T> entityWithMetadata) {
        return new EntityWithIdAndVersion<>(entityWithMetadata.getEntityIdAndVersion(), entityWithMetadata.getEntity());
    }
}
