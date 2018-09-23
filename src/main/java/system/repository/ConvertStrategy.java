package system.repository;

/**
 * Общий интерфейс всех стратегий конвертации java-объектов в различные форматы.
 */
public interface ConvertStrategy<T> {
    String convert(T object, Class tClass) throws Exception;
}
